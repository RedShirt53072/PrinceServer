package com.github.redshirt53072.economyapi.item;

import java.util.List;

import org.bukkit.inventory.ItemStack;

import com.github.redshirt53072.economyapi.general.EconomyApi;




public class ItemManager {
	ItemStack item;
	ItemNBTLoader loader;
	
	public ItemManager(ItemStack item) {
		this.item = item;
		loader = new ItemNBTLoader(item,EconomyApi.getInstance());
	}
	
	public void getCustomType() {
		loader.readString("customType");
	}
	/*
	public void addLore(String key,String value) {
		loader.writeString("itemLore_" + key, value);
	}
	public boolean removeLore(String key) {
		if(!loader.hasString("itemLore_" + key)) {
			return false;
		}
		loader.remove(key);
		updateLore();
		return true;
	}
	private void updateLore() {
		List<String> keys = loader.getAllKey();
		keys.forEach(key ->{if(key.matches("itemLore_.*")) {
			
		}});
		
		
		
	}*/
}
