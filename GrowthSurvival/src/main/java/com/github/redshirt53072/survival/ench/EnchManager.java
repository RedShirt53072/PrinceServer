package com.github.redshirt53072.survival.ench;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;

import com.github.redshirt53072.survival.config.EnchConfig;
import com.github.redshirt53072.survival.config.ScrapConfig;
import com.github.redshirt53072.survival.config.ToolConfig;
import com.github.redshirt53072.survival.ench.EnchData.Ench;
import com.github.redshirt53072.survival.ench.EnchData.ScrapData;
import com.github.redshirt53072.survival.ench.ToolData.ToolGroup;
import com.github.redshirt53072.survival.ench.ToolData.ToolGroupData;

public class EnchManager{
	private static List<EnchData> enchData = new ArrayList<EnchData>();
	private static List<ToolGroupData> toolGroup = new ArrayList<ToolGroupData>();
	private static List<ToolData> toolList = new ArrayList<ToolData>();
	
	private static List<ScrapData> scrapData = new ArrayList<ScrapData>();
	
	private static ToolGroupData brokenToolGroup;
	private static ToolData brokenTool;
	
	
	public static void reload() {
		enchData = EnchConfig.getAllData();
		scrapData = ScrapConfig.getAllData();
		toolGroup = ToolConfig.getToolGroup();
		toolList = ToolConfig.getToolList();
		
		brokenTool = new ToolData(Material.CHAIN_COMMAND_BLOCK,ToolGroup.broken,1);
		List<Ench> enchList = new ArrayList<Ench>();
		for(Ench en : Ench.values()) {
			enchList.add(en);
		}
		brokenToolGroup = new ToolGroupData(ToolGroup.broken,enchList);
	}
	
	public static EnchData getEnchData(String id){
		for(EnchData ed : enchData) {
			if(ed.getType().getID().equals(id)){
				return ed;
			}
		}
		return null;
	}
	
	public static ToolData getToolData(Material mate){
		for(ToolData td : toolList) {
			if(td.getType().equals(mate)){
				return td;
			}
		}
		if(mate.equals(Material.CHAIN_COMMAND_BLOCK)) {
			return brokenTool;
		}
		return null;
	}
	
	public static ToolGroupData getToolGroup(ToolGroup group){
		for(ToolGroupData tg : toolGroup) {
			if(tg.getID().equals(group)){
				return tg;
			}
		}
		if(group.equals(ToolGroup.broken)) {
			return brokenToolGroup;
		}
		return null;
	}
	
	public static List<EnchData> getEnchList(){
		return enchData;
	}
	public static List<ToolData> getToolList(){
		return toolList;
	}
	public static List<ToolGroupData> getGroupList(){
		return toolGroup;
	}

	public static List<ScrapData> getScrapList(){
		return scrapData;
	}
	
	public static boolean isTool(Material mate) {
		ToolData td = EnchManager.getToolData(mate);
		if(td == null) {
			return false;
		}
		ToolGroupData tgd = td.getToolGroupData();
		if(tgd == null) {
			return false;
		}
		return true;
	}
	public static double getUnitDamage(ToolData td,List<Enchant> enchList) {
		int scrapUnit = td.getScrap();
		int allScrap = 0;
		int enchCost = 0;
		
		for(Enchant en : enchList) {
			enchCost += en.getExpCost();
			allScrap += en.getLevel();
		}
		enchCost *= scrapUnit;
		enchCost /= 20;
		allScrap += enchCost;
		allScrap = Math.max(allScrap,scrapUnit);
		
		double unitDamage = td.getType().getMaxDurability();
		
		return unitDamage / allScrap;
	}
}