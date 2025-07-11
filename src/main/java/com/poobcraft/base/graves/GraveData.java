package com.poobcraft.base.graves;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public record GraveData(UUID uuid, Location location, ItemStack[] items) {
}
