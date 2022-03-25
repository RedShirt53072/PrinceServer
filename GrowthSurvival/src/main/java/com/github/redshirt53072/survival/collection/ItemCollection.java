package com.github.redshirt53072.survival.collection;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class ItemCollection {
	
	abstract public ItemStack getIconItem();
	
	abstract public String getName();
	
	abstract public void openGui(Player player);
}