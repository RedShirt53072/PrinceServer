package com.github.redshirt53072.newfishing.config;

import java.util.ArrayList;

import com.github.redshirt53072.growthapi.config.ConfigManager;
import com.github.redshirt53072.newfishing.NewFishing;
import com.github.redshirt53072.newfishing.data.FishData;
import com.github.redshirt53072.newfishing.data.RarityLootData;

public final class FishConfig {
	
	public ArrayList<RarityLootData> getAllData() {
		ConfigManager manager = new ConfigManager(NewFishing.getInstance(),"fish","fish.yml");
		ArrayList<RarityLootData> listData = new ArrayList<RarityLootData>();
		for(int i = 1;i < 6;i ++) {
			readFish("rarity" + i,manager);
			
		}
		return listData;
	}
	private RarityLootData readRarity(String path,ConfigManager manager) {
		
	}
	private FishData readFish(String path,ConfigManager manager) {
		String id = manager.getString(path + ".id");
		
		if(containData(path + ".id")) {
			id = getStringData(path + ".id");
		}else {
			plugin.getLogger().warning("[error]fish.yml内、fish" + loop + "にid(識別名)がありません");
			plugin.stopFishing();
		}
		String name = "temp";
		if(containData(path + ".name")) {
			name = getStringData(path + ".name");
		}else {
			plugin.getLogger().warning("[error]fish.yml内、fish" + loop + "にname(表示名)がありません");
			plugin.stopFishing();
		}
		
		ArrayList<String> lore = new ArrayList<String>();
		
		if(containData(path + ".lore")) {
			for(int i = 1;i < 7;i++) {
				if(containData(path + ".lore.lore" + i)) {
					lore.add(getStringData(path + ".lore.lore" + i));
				}
			}
		}else {
			plugin.getLogger().warning("[error]fish.yml内、fish" + loop + "にloreのリストがありません");
			plugin.stopFishing();
		}
		int size = 0;
		
		if(containData(path + ".size")) {
			size = getIntData(path + ".size");
		}else {
			plugin.getLogger().warning("[error]fish.yml内、fish" + loop + "にsize(大きさ係数)がありません");
		}
		int texture = -1;
		
		int rarity = 1;
		if(containData(path + ".rarity")) {
			rarity = getIntData(path + ".rarity");
		}else {
			plugin.getLogger().warning("[error]fish.yml内、fish" + loop + "にrarity(レア度)がありません");
		}
		if(rarity < 1 || rarity > 7){
			plugin.getLogger().warning("[error]fish.yml内、fish" + loop + "のrarity(レア度)の値が異常です");
			rarity = 1;
		}
		
		if(containData(path + ".texture")) {
			texture = getIntData(path + ".texture");	
		}
		
		
		
		FishData data = new FishData(id,name,lore,texture,size,rarity);
		return data;
	}
}