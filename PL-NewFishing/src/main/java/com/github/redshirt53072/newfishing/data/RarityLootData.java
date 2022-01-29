package com.github.redshirt53072.newfishing.data;

import java.util.ArrayList;

import org.bukkit.block.Biome;

public class RarityLootData {
	private ArrayList<Biome> type;
	private ArrayList<TimeLootData> data;
	
	public RarityLootData(ArrayList<Biome> type,ArrayList<TimeLootData> data) {
		this.type = type;
    	this.data = data;
    }
	public ArrayList<Biome> getType() {
    	return type;
    }
	public ArrayList<TimeLootData> getData(){
		return data;
	}
}
