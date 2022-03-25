package com.github.redshirt53072.survival.ench;

import java.util.List;

import org.bukkit.inventory.ItemStack;

public interface EnchantBuilder {
	public ItemStack build();
	
	public int removeEnchant(EnchData enchant);

	public void removeAll();
	
	public int addEnchant(Enchant enchant);
	
	public List<Enchant> getEnchantList();
	
	public ItemStack getItem();
	
	public boolean isLocked();
}
