package com.poobcraft.base.commands;

import com.poobcraft.base.graves.GraveData;
import com.poobcraft.base.graves.GraveManager;
import com.poobcraft.base.util.PBCommand;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class GraveCommand extends PBCommand {

    private static final HashMap<UUID, Long> lastUsed = new HashMap<>();
    private static final long COOLDOWN = 24 * 60 * 60 * 1000L; // 24 hours in ms

    public GraveCommand() {
        super("grave", "poobcraft.grave");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cOnly players can use this command.");
            return;
        }

        UUID uuid = player.getUniqueId();
        long now = System.currentTimeMillis();

        if (lastUsed.containsKey(uuid)) {
            long elapsed = now - lastUsed.get(uuid);
            if (elapsed < COOLDOWN) {
                long remaining = (COOLDOWN - elapsed) / 1000;
                long hours = remaining / 3600;
                long minutes = (remaining % 3600) / 60;
                sender.sendMessage("§cYou must wait " + hours + "h " + minutes + "m before using /grave again.");
                return;
            }
        }

        GraveData grave = GraveManager.getGrave(uuid);
        if (grave == null || grave.isExpired()) {
            sender.sendMessage("§7No active grave found.");
            return;
        }

        Location loc = grave.getLocation();
        sender.sendMessage("§7☠ Your grave is located at §e" +
                loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ() +
                " §7in world §e" + loc.getWorld().getName());

        lastUsed.put(uuid, now);
    }

    public static void register() {
        new GraveCommand().registerCommand();
    }
}
