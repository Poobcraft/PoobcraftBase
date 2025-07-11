package com.poobcraft.base.util;

import com.poobcraft.base.PoobcraftBase;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TeleportRequestManager {
    private static final Map<UUID, UUID> requests = new HashMap<>();
    private static final Map<UUID, Long> cooldowns = new HashMap<>();
    private static final long COOLDOWN_MS = 5 * 60 * 1000;
    private static final long TIMEOUT_MS = 60 * 1000;

    public static boolean sendRequest(Player sender, Player target) {
        if (cooldowns.containsKey(sender.getUniqueId())) {
            long timeLeft = (cooldowns.get(sender.getUniqueId()) + COOLDOWN_MS) - System.currentTimeMillis();
            if (timeLeft > 0) return false;
        }

        requests.put(target.getUniqueId(), sender.getUniqueId());
        cooldowns.put(sender.getUniqueId(), System.currentTimeMillis());

        Bukkit.getScheduler().runTaskLater(PoobcraftBase.getInstance(), () -> {
            if (requests.containsKey(target.getUniqueId())
                    && requests.get(target.getUniqueId()).equals(sender.getUniqueId())) {
                requests.remove(target.getUniqueId());
                sender.sendMessage("§cYour teleport request to " + target.getName() + " has expired.");
                target.sendMessage("§7Teleport request from " + sender.getName() + " has expired.");
            }
        }, TIMEOUT_MS / 50); // convert ms to ticks

        return true;
    }

    public static boolean hasRequest(Player player) {
        return requests.containsKey(player.getUniqueId());
    }

    public static Player getRequester(Player player) {
        UUID senderId = requests.get(player.getUniqueId());
        return senderId != null ? Bukkit.getPlayer(senderId) : null;
    }

    public static void accept(Player target) {
        Player sender = getRequester(target);
        if (sender != null) {
            sender.teleport(target.getLocation());
            sender.sendMessage("§aTeleporting to " + target.getName() + "...");
            target.sendMessage("§aYou accepted the teleport request.");
            requests.remove(target.getUniqueId());
        }
    }

    public static void deny(Player target) {
        Player sender = getRequester(target);
        if (sender != null) {
            sender.sendMessage("§cYour teleport request was denied.");
            target.sendMessage("§7You denied the teleport request.");
            requests.remove(target.getUniqueId());
        }
    }
}
