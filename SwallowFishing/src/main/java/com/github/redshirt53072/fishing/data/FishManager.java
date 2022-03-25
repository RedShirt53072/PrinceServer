package com.github.redshirt53072.fishing.data;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.github.redshirt53072.api.message.MessageManager;
import com.github.redshirt53072.fishing.config.FishConfig;
import com.github.redshirt53072.fishing.config.RodConfig;
import com.github.redshirt53072.fishing.data.FishData.Time;

public class FishManager {
	static private ArrayList<RarityLootData> fishData = FishConfig.getAllData();
	static private ArrayList<RodLootData> rodData = RodConfig.getAllData();
	
	static public void reload() {
		fishData = FishConfig.getAllData();
		rodData = RodConfig.getAllData();
	}
	
	static public List<String> getRodList() {
		ArrayList<String> result = new ArrayList<String>();
		for(RodLootData rd : rodData) {
			result.add(rd.getId());
		}
		
		return result;
	}
	static public List<RarityLootData> getAllFish() {
		return fishData;
	}
	
	static public ItemStack getRodItem(String rodID) {
		for(RodLootData rd : rodData) {
			if(rd.getId().equals(rodID)){
				return rd.getRodItem();
			}
		}
		return null;
	}
	
	static public List<ItemStack> getAllRodItem() {
		List<ItemStack> list = new ArrayList<ItemStack>();
		for(RodLootData rd : rodData) {
			list.add(rd.getRodItem());
		}
		return list;
	}
	
	static public ItemStack lootNewFish(Player p,String rodID) {
		Biome biome = p.getLocation().getBlock().getBiome();
		Time time = Time.getTime(p.getWorld().getTime());
		
		int rarity = 0;
		for(RodLootData rd : rodData) {
			if(rd.getId().equals(rodID)){
				rarity = rd.getNewRarity();
				break;
			}
		}
		if(rarity == 0) {
			MessageManager.sendImportant("魚がうまく釣れませんでした。", p);
			MessageManager.sendImportant("アップデートによりこの釣り竿は現在使えなくなっているようです。", p);
			return null;
		}
		
		for(RarityLootData rd : fishData) {
			if(rd.getRarity() == rarity){
				FishData fd = rd.lootFish(time,biome);
				if(fd == null) {
					MessageManager.sendImportant("魚がうまく釣れませんでした。", p);
					MessageManager.sendImportant("釣り場と釣り竿の相性が悪いのかもしれません。", p);
					return null;
				}
				return fd.getNewItem(p,time,biome);
			}
		}
		MessageManager.sendImportant("魚がうまく釣れませんでした。", p);
		MessageManager.sendImportant("何か不具合が起きている可能性があります。運営にご報告ください。", p);
		return null;
	}
}
