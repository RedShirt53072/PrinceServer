package com.github.redshirt53072.survival.config;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;

import com.github.redshirt53072.api.config.ConfigManager;
import com.github.redshirt53072.api.message.TextBuilder;
import com.github.redshirt53072.survival.GrowthSurvival;
import com.github.redshirt53072.survival.ench.EnchData.ScrapData;

public final class ScrapConfig {
	/*
	item1:
	  id: 
	  name: 
	  amount:
     */
	public static List<ScrapData> getAllData() {
		ConfigManager manager = new ConfigManager(GrowthSurvival.getInstance(),"ench","scrap.yml");
		manager.configInit();
		
		List<ScrapData> scrapData = new ArrayList<ScrapData>();
		
		for(String key : manager.getKeys("", "item")) {
			scrapData.add(readItem(key,manager));
		}
		manager.logConfig("", "を読み込みました。");
		return scrapData;
	}
	private static ScrapData readItem(String path,ConfigManager manager) {
		boolean cancel = false;
		//string
		String rawMate = manager.getString(TextBuilder.plus(path,".id"));
		Material mate = null;
		try {
			mate = Material.valueOf(rawMate);
		}catch(Exception ex) {
			cancel = true;
			manager.logWarning(TextBuilder.plus(path,".id"), "の値が不正です");
		}

		String name = manager.getString(TextBuilder.plus(path,".name"));
		if(name == null) {
			cancel = true;
			manager.logWarning(TextBuilder.plus(path,".name"), "の値が不正です");
		}
		
		//int
		Integer amount = manager.getInt(TextBuilder.plus(path,".amount"));
		if(amount == null || amount < -64 || amount > 64) {
			cancel = true;
			manager.logWarning(TextBuilder.plus(path,".amount"), "の値が不正です");
		}
		
		if(cancel) {
			return null;
		}
		return new ScrapData(amount,name,mate);
	}
}