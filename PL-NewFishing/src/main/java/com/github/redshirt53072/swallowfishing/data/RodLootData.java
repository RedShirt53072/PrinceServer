package com.github.redshirt53072.swallowfishing.data;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.github.redshirt53072.growthapi.util.LootRoller;
import com.github.redshirt53072.swallowfishing.nbt.FishingRodNBT;

public class RodLootData {
	private String id;
	private String name;
	private ArrayList<String> lore;
	private ArrayList<Integer> roll;
	private int texture;
	
	public RodLootData(ArrayList<String> lore,ArrayList<Integer> roll,String id,String name,int texture) {
		this.roll = roll;
		this.id = id;
		this.lore = lore;
		this.name = name;
		this.texture = texture;
    }
	
	public String getId() {
    	return id;
	}
	public String getName() {
    	return name;
	}
	public int getTexture() {
    	return texture;
	}
	public ArrayList<String> getLore() {
    	return lore;
    }
	
	public ItemStack getRodItem() {
		ItemStack item = new ItemStack(Material.FISHING_ROD);
		
		ItemMeta meta = item.getItemMeta();
		
		meta.setCustomModelData(texture);
		meta.setLore(lore);
		meta.setDisplayName(name);
		
		item.setItemMeta(meta);
		new FishingRodNBT(item).init(id);
		return item;
	}
	
	public int getNewRarity() {
		LootRoller<Integer> roller = new LootRoller<Integer>();
		for(int i = 0;i < 5;i++) {
			roller.addData(i + 1, roll.get(i));
		}
		Integer result = roller.getRandom();
		if(result == null) {
			return 1;
		}
		return result;
	}
}
