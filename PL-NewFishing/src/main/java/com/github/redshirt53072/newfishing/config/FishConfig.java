package com.github.redshirt53072.newfishing.config;

import java.util.ArrayList;

import org.bukkit.ChatColor;

import com.github.redshirt53072.growthapi.config.ConfigManager;
import com.github.redshirt53072.newfishing.NewFishing;
import com.github.redshirt53072.newfishing.data.FishData;
import com.github.redshirt53072.newfishing.data.FishData.BiomeGroup;
import com.github.redshirt53072.newfishing.data.FishData.Time;
import com.github.redshirt53072.newfishing.data.RarityLootData;

public final class FishConfig {
	/*
	rarity1: 
  		fish1:
    		id: "mura_sake"
    		name: "村上の鮭"
    		size: 100
    		texture: 1
    		price: 10
    		time: DAY
    		biome:
      			biome1: FROZEN_OCEAN
      			biome2: DEEP_OCEAN
    		lore: 
      			lore1: "aあ"
      			lore2: "bい"
     */
	public static ArrayList<RarityLootData> getAllData() {
		ConfigManager manager = new ConfigManager(NewFishing.getInstance(),"fish","fish.yml");
		manager.configInit();
		
		ArrayList<RarityLootData> listData = new ArrayList<RarityLootData>();
		for(int i = 1;i < 6;i ++) {
			listData.add(readRarity(i,manager));
		}
		return listData;
	}
	private static RarityLootData readRarity(int rarity,ConfigManager manager) {
		ArrayList<FishData> dayData = new ArrayList<FishData>();
		ArrayList<FishData> nightData = new ArrayList<FishData>();
		ArrayList<FishData> allData = new ArrayList<FishData>();
		
		String path = "rarity" + rarity;
		for(String key : manager.getKeys(path, "fish")){
			FishData fd = readFish(path + "." + key,manager,rarity);
			if(fd == null) {
				continue;
			}
			allData.add(fd);
			
			Time time = fd.getTime();
			switch (time){
			case ALL:
				nightData.add(fd);	
			case DAY:
				dayData.add(fd);
				break;
			case NIGHT:
				nightData.add(fd);
				break;
			}
		}
		return new RarityLootData(rarity,dayData,nightData,allData);
	}
	private static FishData readFish(String path,ConfigManager manager,int rarity) {
		boolean cancel = false;
		//string
		String id = manager.getString(path + ".id");
		if(id == null) {
			cancel = true;
			manager.logWarning(path + ".id", "の値が不正です");
		}
		String name = manager.getString(path + ".name");
		if(name == null) {
			cancel = true;
			manager.logWarning(path + ".name", "の値が不正です");
		}else {
			name = ChatColor.WHITE + name;
		}
		//int
		Integer size = manager.getInt(path + ".size");
		if(size == null || size < 1 || size > 5000) {
			cancel = true;
			manager.logWarning(path + ".size", "の値が不正です");
		}
		Integer texture = manager.getInt(path + ".texture");
		if(texture == null || texture < 1 || texture > 1000) {
			cancel = true;
			manager.logWarning(path + ".texture", "の値が不正です");
		}
		Integer price = manager.getInt(path + ".price");
		if(price == null || price < 0 || price > 1000000) {
			cancel = true;
			manager.logWarning(path + ".price", "の値が不正です");
		}
		//enum
		String rawTime = manager.getString(path + ".time");
		Time time = null;
		try{
			time = Time.valueOf(rawTime);
		}catch(Exception ex) {
			cancel = true;
			manager.logWarning(path + ".time", "の値が不正です");
		}
		//list
		ArrayList<String> lore = new ArrayList<String>();
		for(String key : manager.getKeys(path + ".lore", "lore")) {
			String lo = manager.getString(path + ".lore." + key);
			if(lo == null) {
				manager.logWarning(path + ".lore." + key, "の値が不正です");
			}
			lore.add(ChatColor.WHITE + lo);
		}
		if(lore.isEmpty()) {
			manager.logWarning(path + ".lore", "に有効な値が1つも入っていません");
		}
		//enum list
		ArrayList<BiomeGroup> biomeList = new ArrayList<BiomeGroup>();
		for(String key : manager.getKeys(path + ".biome", "biome")) {
			String rawBiome = manager.getString(path + ".biome." + key);
			try{
				BiomeGroup biome = BiomeGroup.valueOf(rawBiome);
				biomeList.add(biome);
			}catch(Exception ex) {
				manager.logWarning(path + ".biome." + key, "の値が不正です");
			}
		}
		if(biomeList.isEmpty()) {
			cancel = true;
			manager.logWarning(path + ".biome", "に有効な値が1つも入っていません");
		}
		if(cancel) {
			return null;
		}
		return new FishData(id,name,lore,biomeList, time, texture,size,rarity, price);
	}
}