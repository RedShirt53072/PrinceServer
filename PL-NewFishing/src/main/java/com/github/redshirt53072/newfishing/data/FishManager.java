package com.github.redshirt53072.newfishing.data;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.github.redshirt53072.growthapi.message.MessageManager;
import com.github.redshirt53072.newfishing.config.FishConfig;
import com.github.redshirt53072.newfishing.config.RodConfig;

public class FishManager {
	static private ArrayList<RarityLootData> fishData = FishConfig.getAllData();
	static private ArrayList<RodLootData> rodData = RodConfig.getAllData();
	
	static public void reload() {
		fishData = FishConfig.getAllData();
		rodData = RodConfig.getAllData();
	}
	
	
	static public ItemStack lootNewFish(Player p,String rodID) {
		Biome biome = p.getLocation().getBlock().getBiome();
		boolean isDay = p.getWorld().getTime() < 12000;
		
		int rarity = 0;
		for(RodLootData rd : rodData) {
			if(rd.getId().equals(rodID)){
				rarity = rd.getNewRarity();
				break;
			}
		}
		if(rarity == 0) {
			MessageManager.sendImportant("魚がうまく釣れなかった...", p);
			MessageManager.sendImportant(ChatColor.YELLOW + "[ヒント]" + ChatColor.WHITE + "アップデートによりこの釣り竿は現在使えなくなっているようです。", p);
			return null;
		}
		
		for(RarityLootData fd : fishData) {
			if(fd.getRarity() == rarity){
				return fd.lootFish(isDay,biome).getNewItem(p);
			}
		}
		MessageManager.sendImportant("魚がうまく釣れなかった...", p);
		MessageManager.sendImportant(ChatColor.YELLOW + "[ヒント]" + ChatColor.WHITE + "釣り場と釣り竿の相性が悪いのかもしれません。", p);
		return null;
	}
	
}
