package com.github.redshirt53072.growthapi.item;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemLibrary {
	public static ItemStack getLiquidEmerald() {
		ItemStack le = new ItemStack(Material.COMMAND_BLOCK);
		ItemMeta leMeta = le.getItemMeta();
		leMeta.setCustomModelData(1000);
		leMeta.setDisplayName(ChatColor.WHITE + "リキッドエメラルド");
		le.setItemMeta(leMeta);
		return le;
	}
}
