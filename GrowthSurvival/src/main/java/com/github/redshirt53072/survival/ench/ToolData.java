package com.github.redshirt53072.survival.ench;

import java.util.List;

import org.bukkit.Material;

import com.github.redshirt53072.survival.ench.EnchData.Ench;

public class ToolData {
	private Material type;
	private ToolGroup toolGroup;
	private int scrap;
	
	public ToolData(Material type,ToolGroup toolGroup,int scrap) {
		this.type = type;
    	this.scrap = scrap;
    	this.toolGroup = toolGroup;
    }
	public Material getType() {
    	return type;
    }
	public ToolGroup getToolGroup() {
    	return toolGroup;
	}
	public ToolGroupData getToolGroupData() {
    	return EnchManager.getToolGroup(toolGroup);
	}
	
	public int getScrap() {
    	return scrap;
	}
	
	public static class ToolGroupData{
		private ToolGroup id;
		private List<Ench> enchList;
		
		public ToolGroupData(ToolGroup id,List<Ench> enchList) {
			this.id = id;
			this.enchList = enchList;
		}
		public List<Ench> getEnchData(){
			return enchList;
		}
		public boolean isCanEnch(Ench ench){
			for(Ench ed : enchList) {
				if(ed.equals(ench)){
					return true;
				}
			}
			return false;
		}
		public ToolGroup getID() {
			return id;
		}
	}
	
	public static enum ToolGroup{
		broken("壊れたツール"),
		sword("剣"),
		pickaxe("ツルハシ"),
		axe("斧"),
		shovel("シャベル"),
		hoe("クワ"),
		helmet("頭防具"),
		chestplate("胴防具"),
		leggings("脚防具"),
		boots("足防具"),
		tool("ツール"),
		horse_armor("馬鎧"),
		bow("弓"),
		trident("トライデント"),
		fishingrod("釣竿"),
		shears("はさみ"),
		crossbow("クロスボウ");
		
		
		private String name;
		private ToolGroup(String name) {
			this.name = name;
		}
		public String getName() {
			return name;
		}
	}
}