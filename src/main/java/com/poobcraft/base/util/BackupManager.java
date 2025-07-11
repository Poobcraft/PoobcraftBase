package com.poobcraft.base.util;

import com.poobcraft.base.PoobcraftBase;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class BackupManager {

    private static final File BACKUP_DIR = new File(PoobcraftBase.getInstance().getDataFolder(), "backups");
    private static final int MAX_BACKUPS = 7;

    public static void createBackup(CommandSender sender, boolean manual) {
        if (!BACKUP_DIR.exists()) BACKUP_DIR.mkdirs();

        String timestamp = getFormattedTimestamp();
        File zipFile = new File(BACKUP_DIR, "Backup-" + timestamp + ".zip");

        if (manual) sender.sendMessage("§7Creating backup...");
        PoobcraftBase.getInstance().getLogger().info("⏳ Starting backup...");

        new BukkitRunnable() {
            @Override
            public void run() {
                try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile))) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "save-all");

                    zipFolder(zos, new File("world"), "world");
                    zipFolder(zos, new File("world_nether"), "world_nether");
                    zipFolder(zos, new File("world_the_end"), "world_the_end");

                    zipFile(zos, new File(PoobcraftBase.getInstance().getDataFolder(), "config.yml"), "config.yml");
                    zipFile(zos, new File(PoobcraftBase.getInstance().getDataFolder(), "permissions.yml"), "permissions.yml");

                    deleteOldBackups();

                    PoobcraftBase.getInstance().getLogger().info("✅ Backup complete: " + zipFile.getName());
                    if (manual) sender.sendMessage("§aBackup completed: " + zipFile.getName());
                } catch (IOException e) {
                    PoobcraftBase.getInstance().getLogger().warning("❌ Backup failed: " + e.getMessage());
                    if (manual) sender.sendMessage("§cBackup failed. Check console.");
                }
            }
        }.runTaskAsynchronously(PoobcraftBase.getInstance());
    }

    private static String getFormattedTimestamp() {
        Calendar cal = Calendar.getInstance();

        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int hour = cal.get(Calendar.HOUR);
        if (hour == 0) hour = 12;

        String amPm = (cal.get(Calendar.AM_PM) == Calendar.AM) ? "AM" : "PM";

        return month + "_" + day + "-" + hour + amPm;
    }

    private static void zipFolder(ZipOutputStream zos, File folder, String base) throws IOException {
        if (!folder.exists()) return;

        Path basePath = folder.toPath();
        Files.walk(basePath).forEach(path -> {
            try {
                if (Files.isDirectory(path)) return;
                String relativePath = base + "/" + basePath.relativize(path).toString().replace("\\", "/");
                zos.putNextEntry(new ZipEntry(relativePath));
                Files.copy(path, zos);
                zos.closeEntry();
            } catch (IOException e) {
                PoobcraftBase.getInstance().getLogger().warning("Failed to zip: " + path);
            }
        });
    }

    private static void zipFile(ZipOutputStream zos, File file, String entryName) throws IOException {
        if (!file.exists()) return;
        zos.putNextEntry(new ZipEntry(entryName));
        Files.copy(file.toPath(), zos);
        zos.closeEntry();
    }

    private static void deleteOldBackups() {
        File[] backups = BACKUP_DIR.listFiles((dir, name) -> name.endsWith(".zip"));
        if (backups == null || backups.length <= MAX_BACKUPS) return;

        Arrays.sort(backups, Comparator.comparingLong(File::lastModified));
        for (int i = 0; i < backups.length - MAX_BACKUPS; i++) {
            backups[i].delete();
        }
    }

    public static void scheduleDailyBackup() {
        long ticksPerDay = 24L * 60 * 60 * 20; // 24 hours
        long ticksUntil3AM = getTicksUntil3AM();

        new BukkitRunnable() {
            @Override
            public void run() {
                createBackup(Bukkit.getConsoleSender(), false);
            }
        }.runTaskTimerAsynchronously(PoobcraftBase.getInstance(), ticksUntil3AM, ticksPerDay);
    }

    private static long getTicksUntil3AM() {
        Calendar now = Calendar.getInstance();
        Calendar next3am = (Calendar) now.clone();
        next3am.set(Calendar.HOUR_OF_DAY, 3);
        next3am.set(Calendar.MINUTE, 0);
        next3am.set(Calendar.SECOND, 0);
        next3am.set(Calendar.MILLISECOND, 0);

        if (now.after(next3am)) {
            next3am.add(Calendar.DAY_OF_MONTH, 1);
        }

        long delayMillis = next3am.getTimeInMillis() - now.getTimeInMillis();
        return delayMillis / 50;
    }
}
