package com.poobcraft.base.graves;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class GraveListener implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();

        // Drop XP like normal but not items
        event.setKeepInventory(true);
        event.getDrops().clear();

        GraveManager.spawnGrave(player);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Block block = event.getClickedBlock();
        if (block == null || block.getType() != Material.BLACK_SHULKER_BOX) return;

        Player player = event.getPlayer();
        if (!GraveManager.isPlayerGrave(player, block.getLocation())) {
            player.sendMessage("§cYou cannot loot another player's grave.");
            event.setCancelled(true);
        }
    }
}
