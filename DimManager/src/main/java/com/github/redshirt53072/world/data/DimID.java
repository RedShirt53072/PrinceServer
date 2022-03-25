package com.github.redshirt53072.world.data;

import org.bukkit.Material;


public enum DimID {
	
	normal("通常ワールド",Material.GRASS_BLOCK),
	lobby("ロビー",Material.OAK_SAPLING),
	building("建築ワールド",Material.CRAFTING_TABLE),
	mining("採掘ワールド",Material.DIAMOND_ORE),
	creative("クリエイティブワールド",Material.STRUCTURE_VOID),
	minigame("ミニゲームエリア",Material.SPYGLASS);
	
	private String name;
	private Material icon;
	
	private DimID(String name,Material icon) {
		this.name = name;
		this.icon = icon;
	}
	
	public String getName() {
		return name;
	}
	
	public Material getIcon() {
		return icon;
	}
}
