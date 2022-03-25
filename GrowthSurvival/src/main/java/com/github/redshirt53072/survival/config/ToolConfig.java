package com.github.redshirt53072.survival.config;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;

import com.github.redshirt53072.api.config.ConfigManager;
import com.github.redshirt53072.api.message.TextBuilder;
import com.github.redshirt53072.survival.GrowthSurvival;
import com.github.redshirt53072.survival.ench.ToolData;
import com.github.redshirt53072.survival.ench.EnchData.Ench;
import com.github.redshirt53072.survival.ench.ToolData.ToolGroup;
import com.github.redshirt53072.survival.ench.ToolData.ToolGroupData;

public final class ToolConfig {
	/*
	group1:
	  id:
	  ench: 
	    ench1: 
	    ench2: 
	    ench3: 
	  tool1:
	    id: 
	    scrap: 
	
	*/
	public static ArrayList<ToolGroupData> getToolGroup() {
		ConfigManager manager = new ConfigManager(GrowthSurvival.getInstance(),"ench","tool.yml");
		manager.configInit();
		
		ArrayList<ToolGroupData> listData = new ArrayList<ToolGroupData>();
		for(String key : manager.getKeys("", "group")) {
			ToolGroupData tgd = readGroup(key,manager);
			if(tgd == null) {
				continue;
			}
			listData.add(tgd);
		}
		manager.logConfig("", "を読み込みました。");
		return listData;
	}
	private static ToolGroupData readGroup(String path,ConfigManager manager) {
		boolean cancel = false;
		String rawID = manager.getString(TextBuilder.plus(path,".id"));
		ToolGroup group = null;
		try {
			group = ToolGroup.valueOf(rawID);
		}catch(Exception ex) {
			cancel = true;
			manager.logWarning(TextBuilder.plus(path,".id"), "の値が不正です");
		}
		List<Ench> enchList = new ArrayList<Ench>();
		for(String key : manager.getKeys(TextBuilder.plus(path,".ench"), "ench")) {
			String rawEnch = manager.getString(TextBuilder.plus(path,".ench.",key));
			Ench en = Ench.getEnch(rawEnch);
			if(en == null) {
				manager.logWarning(TextBuilder.plus(path,".ench.",key), "の値が不正です");
				continue;
			}
			enchList.add(en);
		}
		if(enchList.isEmpty()) {
			cancel = true;
			manager.logWarning(TextBuilder.plus(path,".ench"), "に有効な値が1つも入っていません");
		}
		if(cancel) {
			return null;
		}
		return new ToolGroupData(group, enchList);
	}
	
	public static ArrayList<ToolData> getToolList() {
		ConfigManager manager = new ConfigManager(GrowthSurvival.getInstance(),"ench","tool.yml");
		manager.configInit();
		
		ArrayList<ToolData> listData = new ArrayList<ToolData>();
		for(String key : manager.getKeys("", "group")) {
			String rawID = manager.getString(TextBuilder.plus(key,".id"));
			ToolGroup group = null;
			try {
				group = ToolGroup.valueOf(rawID);
			}catch(Exception ex) {
				manager.logWarning(TextBuilder.plus(key,".id"), "の値が不正です");
				continue;
			}
			for(String key2 : manager.getKeys(TextBuilder.plus(key,"."), "tool")) {
				ToolData td = readTool(TextBuilder.plus(key,".",key2),group,manager);
				if(td == null) {
					continue;
				}
				listData.add(td);
			}
		}
		manager.logConfig("", "を読み込みました。");
		return listData;
	}
	private static ToolData readTool(String path,ToolGroup tg,ConfigManager manager) {
		boolean cancel = false;
		//string
		String rawMate = manager.getString(TextBuilder.plus(path,".id"));
		Material mate = Material.getMaterial(rawMate);
		if(mate == null) {
			cancel = true;
			manager.logWarning(TextBuilder.plus(path,".id"), "の値が不正です");
		}
		
		//int
		Integer scrap = manager.getInt(TextBuilder.plus(path,".scrap"));
		if(scrap == null || scrap < 1 || scrap > 1000) {
			cancel = true;
			manager.logWarning(TextBuilder.plus(path,".scrap"), "の値が不正です");
		}
		
		if(cancel) {
			return null;
		}
		return new ToolData(mate,tg,scrap);
	}
}