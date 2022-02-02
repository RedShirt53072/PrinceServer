package com.github.redshirt53072.newfishing.config;

import java.util.ArrayList;

import org.bukkit.ChatColor;

import com.github.redshirt53072.growthapi.config.ConfigManager;
import com.github.redshirt53072.newfishing.NewFishing;
import com.github.redshirt53072.newfishing.data.RodLootData;


public final class RodConfig {
	public static ArrayList<RodLootData> getAllData() {
		//overworld
		ConfigManager manager = new ConfigManager(NewFishing.getInstance(),"rod","rod.yml");
		manager.configInit();
		
		ArrayList<RodLootData> listData = new ArrayList<RodLootData>();
		for(String key : manager.getKeys("", "rod")) {
			listData.add(readRod(key,manager));
		}
		return listData;
	}
	private static RodLootData readRod(String path,ConfigManager manager) {
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
		Integer texture = manager.getInt(path + ".texture");
		if(texture == null || texture > 10000) {
			cancel = true;
			manager.logWarning(path + ".texture", "の値が不正です");
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
		//rarity1-5
		ArrayList<Integer> rarity = new ArrayList<Integer>();
		for(int i = 1;i < 6;i++) {
			Integer in = manager.getInt(path + ".rare" + i);
			if(in == null || in < 0 || in > 100000000) {
				cancel = true;
				manager.logWarning(path + ".rare" + i, "の値が不正です");
			}
			rarity.add(in);
		}
		
		if(cancel) {
			return null;
		}
		return new RodLootData(lore,rarity,id,name,texture);
	}
	
}