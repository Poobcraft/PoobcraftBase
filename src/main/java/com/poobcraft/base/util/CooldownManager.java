package com.poobcraft.base.util;

import java.util.HashMap;
import java.util.UUID;

public class CooldownManager {
    private static final long COOLDOWN_MILLIS = 5 * 60 * 1000; // 5 minutes
    private static final HashMap<UUID, Long> cooldowns = new HashMap<>();

    public static boolean isOnCooldown(UUID uuid) {
        return cooldowns.containsKey(uuid) && System.currentTimeMillis() < cooldowns.get(uuid);
    }

    public static long getRemainingSeconds(UUID uuid) {
        if (!cooldowns.containsKey(uuid)) return 0;
        long remaining = cooldowns.get(uuid) - System.currentTimeMillis();
        return Math.max(0, remaining / 1000);
    }

    public static void setCooldown(UUID uuid) {
        cooldowns.put(uuid, System.currentTimeMillis() + COOLDOWN_MILLIS);
    }
}
