package com.poobcraft.base.permissions;

import com.poobcraft.base.PoobcraftBase;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class PoobPerms {

    private static final Map<String, Set<String>> rankPerms = new HashMap<>();
    private static final Map<String, String> rankInherits = new HashMap<>();
    private static final Map<String, Set<String>> userPerms = new HashMap<>();
    private static final Map<String, String> userRanks = new HashMap<>();

    private static File configFile;
    private static FileConfiguration config;

    public static void reload() {
        rankPerms.clear();
        rankInherits.clear();
        userPerms.clear();
        userRanks.clear();

        configFile = new File(PoobcraftBase.getInstance().getDataFolder(), "permissions.yml");
        if (!configFile.exists()) {
            PoobcraftBase.getInstance().saveResource("permissions.yml", false);
        }

        config = YamlConfiguration.loadConfiguration(configFile);

        // Load rank permissions and inheritance
        ConfigurationSection ranksSection = config.getConfigurationSection("ranks");
        if (ranksSection != null) {
            for (String rank : ranksSection.getKeys(false)) {
                List<String> perms = config.getStringList("ranks." + rank + ".permissions");
                rankPerms.put(rank, new HashSet<>(perms));

                if (config.contains("ranks." + rank + ".inherits")) {
                    rankInherits.put(rank, config.getString("ranks." + rank + ".inherits"));
                }
            }
        }

        // Load user ranks and permissions
        ConfigurationSection usersSection = config.getConfigurationSection("users");
        if (usersSection != null) {
            for (String player : usersSection.getKeys(false)) {
                String rank = config.getString("users." + player + ".rank", "default");
                userRanks.put(player.toLowerCase(), rank);

                List<String> perms = config.getStringList("users." + player + ".permissions");
                userPerms.put(player.toLowerCase(), new HashSet<>(perms));
            }
        }

        PoobcraftBase.getInstance().getLogger().info("✅ permissions.yml loaded.");
    }

    public static boolean has(CommandSender sender, String permission) {
        if (sender instanceof Player player) {
            return has(player, permission);
        }
        return true; // Console always allowed
    }

    public static boolean has(Player player, String permission) {
        String name = player.getName().toLowerCase();

        PoobcraftBase.getInstance().getLogger().info("[PoobPerms] Checking permission '" + permission + "' for player: " + name);

        Set<String> allPerms = new HashSet<>(userPerms.getOrDefault(name, Collections.emptySet()));

        String rank = userRanks.getOrDefault(name, "default");
        allPerms.addAll(resolveRankPermissions(rank, new HashSet<>()));

        PoobcraftBase.getInstance().getLogger().info("Resolved permissions: " + allPerms);

        return allPerms.contains(permission);
    }

    private static Set<String> resolveRankPermissions(String rank, Set<String> visited) {
        if (visited.contains(rank)) return Collections.emptySet();
        visited.add(rank);

        Set<String> perms = new HashSet<>(rankPerms.getOrDefault(rank, Collections.emptySet()));

        String parent = rankInherits.get(rank);
        if (parent != null) {
            perms.addAll(resolveRankPermissions(parent, visited));
        }

        return perms;
    }

    public static void save() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            PoobcraftBase.getInstance().getLogger().warning("Failed to save permissions.yml: " + e.getMessage());
        }
    }
}
