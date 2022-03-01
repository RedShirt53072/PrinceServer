package com.github.redshirt53072.swallowfishing.data;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.block.Biome;

import com.github.redshirt53072.swallowfishing.data.FishData.BiomeGroup;
import com.github.redshirt53072.swallowfishing.data.FishData.FishingBiome;
import com.github.redshirt53072.swallowfishing.data.FishData.Time;

public class RarityLootData {
	private ArrayList<FishData> day = new ArrayList<FishData>();
	private ArrayList<FishData> night = new ArrayList<FishData>();
	private ArrayList<FishData> morning = new ArrayList<FishData>();
	private ArrayList<FishData> midnight = new ArrayList<FishData>();
	private ArrayList<FishData> all = new ArrayList<FishData>();
	
	private int rarity = 1;
	
	public RarityLootData(int rarity,ArrayList<FishData> dayData,ArrayList<FishData> nightData,ArrayList<FishData> midnightData,ArrayList<FishData> morningData,ArrayList<FishData> allData) {
		this.rarity = rarity;
		this.all = allData;
    	this.day = dayData;
    	this.night = nightData;
    	this.midnight = midnightData;
    	this.morning = morningData;
    }
	public int getRarity() {
    	return rarity;
    }
	public ArrayList<FishData> getDayData(){
		return day;
	}
	public ArrayList<FishData> getNightData(){
		return night;
	}
	public ArrayList<FishData> getMorningData(){
		return morning;
	}
	public ArrayList<FishData> getMidnightData(){
		return midnight;
	}
	
	public ArrayList<FishData> getAllData(){
		return all;
	}
	
	public FishData lootFish(Time time,Biome biome){
		BiomeGroup group = FishingBiome.valueOf(biome.toString()).getGroup();
		ArrayList<FishData> data = all;
		switch(time) {
		case MORNING:
			data = morning;
			break;
		case DAY:
			data = day;
			break;
		case MIDNIGHT:
			data = midnight;
			break;
		case NIGHT:
			data = night;
			break;
		}
		
		ArrayList<FishData> table = new ArrayList<FishData>();
		
		for(FishData fd : data) {
			for(BiomeGroup bg : fd.getBiome()){
				if(bg.equals(group)) {
					table.add(fd);
					break;
				}
			}
		}
		if(table.isEmpty()) {
			return null;
		}
		
		return table.get(new Random().nextInt(table.size()));
	}
}
