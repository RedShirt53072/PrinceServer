package com.github.redshirt53072.api.item;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.github.redshirt53072.api.message.TextBuilder;

public class ItemLibrary {
	public static ItemStack getLiquidEmerald() {
		ItemStack le = new ItemStack(Material.REPEATING_COMMAND_BLOCK);
		ItemMeta leMeta = le.getItemMeta();
		leMeta.setCustomModelData(1000);
		leMeta.setDisplayName(TextBuilder.quickBuild(ChatColor.WHITE, "リキッドエメラルド"));
		le.setItemMeta(leMeta);
		return le;
	}
	public static ItemStack getScrap() {
		ItemStack le = new ItemStack(Material.COMMAND_BLOCK);
		ItemMeta leMeta = le.getItemMeta();
		leMeta.setCustomModelData(1);
		leMeta.setDisplayName(TextBuilder.quickBuild(ChatColor.WHITE, "スクラップ"));
		le.setItemMeta(leMeta);
		return le;
	}
	
	
}
