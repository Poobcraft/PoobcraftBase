package com.poobcraft.base.graves;

import com.poobcraft.base.PoobcraftBase;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.ShulkerBox;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class GraveManager {

    private static final Map<UUID, GraveData> graves = new HashMap<>();
    private static File file;
    private static FileConfiguration gravesConfig;

    public static void init() {
        loadGraves();
    }

    public static void spawnGrave(Player player) {
        UUID uuid = player.getUniqueId();
        Location location = player.getLocation().getBlock().getLocation();
        World world = location.getWorld();
        if (world == null) return;

        Block block = world.getBlockAt(location);
        block.setType(Material.BLACK_SHULKER_BOX);

        ShulkerBox shulker = (ShulkerBox) block.getState();
        ItemStack[] contents = player.getInventory().getContents();

        for (int i = 0; i < contents.length && i < 27; i++) {
            shulker.getInventory().setItem(i, contents[i]);
        }

        shulker.update();
        world.playSound(location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.2f);

        graves.put(uuid, new GraveData(uuid, location, contents));
        saveGraves();
    }

    public static boolean isPlayerGrave(Player player, Location location) {
        GraveData data = graves.get(player.getUniqueId());
        return data != null && data.location().equals(location.getBlock().getLocation());
    }

    public static void removeGrave(UUID uuid) {
        graves.remove(uuid);
        saveGraves();
    }

    public static Location getGraveLocation(UUID uuid) {
        GraveData data = graves.get(uuid);
        return data != null ? data.location() : null;
    }

    private static void loadGraves() {
        file = new File(PoobcraftBase.getInstance().getDataFolder(), "graves.yml");
        if (!file.exists()) {
            try {
                boolean created = file.createNewFile();
                if (!created) {
                    PoobcraftBase.getInstance().getLogger().warning("graves.yml could not be created!");
                }
            } catch (IOException e) {
                PoobcraftBase.getInstance().getLogger().severe("Could not create graves.yml: " + e.getMessage());
                return;
            }
        }

        graves.clear();
        gravesConfig = YamlConfiguration.loadConfiguration(file);

        if (!gravesConfig.contains("graves")) return;
        ConfigurationSection section = gravesConfig.getConfigurationSection("graves");
        if (section == null) return;

        for (String uuidString : section.getKeys(false)) {
            try {
                UUID uuid = UUID.fromString(uuidString);
                String base = "graves." + uuidString;

                String worldName = gravesConfig.getString(base + ".world");
                if (worldName == null) continue;

                World world = Bukkit.getWorld(worldName);
                if (world == null) continue;

                double x = gravesConfig.getDouble(base + ".x");
                double y = gravesConfig.getDouble(base + ".y");
                double z = gravesConfig.getDouble(base + ".z");

                Location loc = new Location(world, x, y, z);
                List<?> rawList = gravesConfig.getList(base + ".items");
                if (rawList == null) continue;

                List<ItemStack> items = new ArrayList<>();
                for (Object obj : rawList) {
                    if (obj instanceof ItemStack stack) {
                        items.add(stack);
                    }
                }

                graves.put(uuid, new GraveData(uuid, loc, items.toArray(new ItemStack[0])));
            } catch (Exception e) {
                PoobcraftBase.getInstance().getLogger().warning("Failed to load grave for " + uuidString);
            }
        }
    }

    private static void saveGraves() {
        if (gravesConfig == null) gravesConfig = new YamlConfiguration();
        gravesConfig.set("graves", null); // Reset section

        for (Map.Entry<UUID, GraveData> entry : graves.entrySet()) {
            UUID uuid = entry.getKey();
            GraveData data = entry.getValue();
            String base = "graves." + uuid.toString();

            Location loc = data.location();
            gravesConfig.set(base + ".world", loc.getWorld().getName());
            gravesConfig.set(base + ".x", loc.getX());
            gravesConfig.set(base + ".y", loc.getY());
            gravesConfig.set(base + ".z", loc.getZ());
            gravesConfig.set(base + ".items", Arrays.asList(data.items()));
        }

        try {
            gravesConfig.save(file);
        } catch (IOException e) {
            PoobcraftBase.getInstance().getLogger().warning("Failed to save graves.yml: " + e.getMessage());
        }
    }
}
