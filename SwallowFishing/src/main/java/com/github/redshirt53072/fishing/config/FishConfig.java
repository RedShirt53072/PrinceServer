package com.github.redshirt53072.fishing.config;

import java.util.ArrayList;

import org.bukkit.ChatColor;

import com.github.redshirt53072.api.config.ConfigManager;
import com.github.redshirt53072.fishing.SwallowFishing;
import com.github.redshirt53072.fishing.data.FishData;
import com.github.redshirt53072.fishing.data.RarityLootData;
import com.github.redshirt53072.fishing.data.FishData.BiomeGroup;
import com.github.redshirt53072.fishing.data.FishData.Time;

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
		ConfigManager manager = new ConfigManager(SwallowFishing.getInstance(),"fish","fish.yml");
		manager.configInit();
		
		ArrayList<RarityLootData> listData = new ArrayList<RarityLootData>();
		for(int i = 1;i < 6;i ++) {
			listData.add(readRarity(i,manager));
		}
		manager.logConfig("", "を読み込みました。");
		return listData;
	}
	private static RarityLootData readRarity(int rarity,ConfigManager manager) {
		ArrayList<FishData> dayData = new ArrayList<FishData>();
		ArrayList<FishData> nightData = new ArrayList<FishData>();
		ArrayList<FishData> morningData = new ArrayList<FishData>();
		ArrayList<FishData> midnightData = new ArrayList<FishData>();
		ArrayList<FishData> allData = new ArrayList<FishData>();
		
		String path = "rarity" + rarity;
		for(String key : manager.getKeys(path, "fish")){
			FishData fd = readFish(path + "." + key,manager,rarity);
			if(fd == null) {
				continue;
			}
			allData.add(fd);
			
			ArrayList<Time> time = fd.getTime();
			for(Time t : time) {
				switch (t){
				case DAY:
					dayData.add(fd);
					break;
				case NIGHT:
					nightData.add(fd);
					break;
				case MORNING:
					morningData.add(fd);
					break;
				case MIDNIGHT:
					midnightData.add(fd);
					break;
				}	
			}
		}
		return new RarityLootData(rarity,dayData,nightData,midnightData,morningData,allData);
	}
	private static FishData readFish(String path,ConfigManager manager,int rarity) {
		boolean cancel = false;
		//string
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
		
		ArrayList<Time> timeList = new ArrayList<Time>();
		for(String key : manager.getKeys(path + ".time", "time")) {
			String rawTime = manager.getString(path + ".time." + key);
			try{
				Time time = Time.valueOf(rawTime);
				timeList.add(time);
			}catch(Exception ex) {
				manager.logWarning(path + ".time." + key, "の値が不正です");
			}
		}
		if(timeList.isEmpty()) {
			cancel = true;
			manager.logWarning(path + ".time", "に有効な値が1つも入っていません");
		}
		
		if(cancel) {
			return null;
		}
		return new FishData(texture,name,lore,biomeList, timeList, texture,size,rarity, price);
	}
}