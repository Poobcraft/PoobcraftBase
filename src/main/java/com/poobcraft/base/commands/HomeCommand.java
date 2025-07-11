package com.poobcraft.base.commands;

import com.poobcraft.base.util.PBCommand;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HomeCommand extends PBCommand {

    private static final Map<UUID, Long> cooldowns = new HashMap<>();
    private static final long COOLDOWN_MILLIS = 5 * 60 * 1000; // 5 minutes

    public HomeCommand() {
        super("home", "poobcraft.home");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cOnly players can use /home.");
            return;
        }

        UUID uuid = player.getUniqueId();
        long now = System.currentTimeMillis();
        long lastUsed = cooldowns.getOrDefault(uuid, 0L);

        if (now - lastUsed < COOLDOWN_MILLIS) {
            long remaining = (COOLDOWN_MILLIS - (now - lastUsed)) / 1000;
            sender.sendMessage("§cYou must wait " + remaining + " more seconds to use /home again.");
            return;
        }

        Location spawn = player.getWorld().getSpawnLocation().clone();
        spawn.setYaw(player.getLocation().getYaw());
        spawn.setPitch(player.getLocation().getPitch());
        player.teleportAsync(spawn); // ✅ Async & non-deprecated

        cooldowns.put(uuid, now);
        sender.sendMessage("§aTeleported to your home.");
    }

    public static void register() {
        new HomeCommand().registerCommand();
    }
}
