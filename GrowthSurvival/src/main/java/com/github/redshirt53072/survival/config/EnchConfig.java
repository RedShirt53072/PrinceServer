package com.github.redshirt53072.survival.config;

import java.util.ArrayList;

import org.bukkit.ChatColor;

import com.github.redshirt53072.api.config.ConfigManager;
import com.github.redshirt53072.api.message.TextBuilder;
import com.github.redshirt53072.survival.GrowthSurvival;
import com.github.redshirt53072.survival.ench.EnchData;
import com.github.redshirt53072.survival.ench.EnchData.Ench;

public final class EnchConfig {
	/*
	vanilla:
	  ench1:
	    name:
	    id: 
	    maxlevel: 
	    exp:
	    exclusive:
	      ench1:
	      ench2:
	      ench3:
	    lore:
	      lore1:
	      lore2:
     */
	public static ArrayList<EnchData> getAllData() {
		ConfigManager manager = new ConfigManager(GrowthSurvival.getInstance(),"ench","ench.yml");
		manager.configInit();
		
		ArrayList<EnchData> listData = new ArrayList<EnchData>();
		for(String key : manager.getKeys("vanilla", "ench")) {
			EnchData ed = readVanilla(TextBuilder.plus("vanilla.",key),manager);
			if(ed == null) {
				continue;
			}
			listData.add(ed);
		}
		manager.logConfig("", "を読み込みました。");
		return listData;
	}
	private static EnchData readVanilla(String path,ConfigManager manager) {
		boolean cancel = false;
		//string
		String rawID = manager.getString(TextBuilder.plus(path,".id"));
		Ench ench = Ench.getEnch(rawID);
		if(ench == null) {
			cancel = true;
			manager.logWarning(TextBuilder.plus(path,".id"), "の値が不正です");
		}
		
		String name = manager.getString(TextBuilder.plus(path,".name"));
		if(name == null) {
			cancel = true;
			manager.logWarning(TextBuilder.plus(path,".name"), "の値が不正です");
		}

		//int
		Integer maxLevel = manager.getInt(TextBuilder.plus(path,".maxlevel"));
		if(maxLevel == null || maxLevel < 1 || maxLevel > 100) {
			cancel = true;
			manager.logWarning(TextBuilder.plus(path,".maxlevel"), "の値が不正です");
		}
		Integer exp = manager.getInt(TextBuilder.plus(path,".exp"));
		if(exp == null || exp < 0 || exp > 1000) {
			cancel = true;
			manager.logWarning(TextBuilder.plus(path,".exp"), "の値が不正です");
		}
		//list
		ArrayList<String> lore = new ArrayList<String>();
		for(String key : manager.getKeys(TextBuilder.plus(path,".lore"), "lore")) {
			String lo = manager.getString(TextBuilder.plus(path,".lore.",key));
			if(lo == null) {
				manager.logWarning(TextBuilder.plus(path,".lore.",key), "の値が不正です");
			}
			lore.add(TextBuilder.quickBuild(ChatColor.WHITE,lo));
		}
		if(lore.isEmpty()) {
			cancel = true;
			manager.logWarning(TextBuilder.plus(path ,".lore"), "に有効な値が1つも入っていません");
		}
		//enum list
		ArrayList<Ench> cantList = new ArrayList<Ench>();
		for(String key : manager.getKeys(TextBuilder.plus(path,".exclusive"), "ench")) {
			String rawEnch = manager.getString(TextBuilder.plus(path,".exclusive.",key));
			Ench en = Ench.getEnch(rawEnch);
			if(en == null) {
				manager.logWarning(TextBuilder.plus(path,".exclusive.",key), "の値が不正です");
				continue;
			}
			cantList.add(en);
		}
		
		if(cancel) {
			return null;
		}
		return new EnchData(ench,cantList,name,lore,maxLevel, exp);
	}
}