package com.github.redshirt53072.newfishing.data;

import java.util.ArrayList;

import com.github.redshirt53072.growthapi.util.LootRoller;

public class RodLootData {
	private String id;
	private String name;
	private ArrayList<String> lore;
	private ArrayList<Integer> roll;
	private int texture;
	
	public RodLootData(ArrayList<String> lore,ArrayList<Integer> roll,String id,String name,int texture) {
		this.roll = roll;
		this.id = id;
		this.lore = lore;
		this.name = name;
		this.texture = texture;
    }
	
	public String getId() {
    	return id;
	}
	public String getName() {
    	return name;
	}
	public int getTexture() {
    	return texture;
	}
	public ArrayList<String> getLore() {
    	return lore;
    }
	
	public int getNewRarity() {
		LootRoller<Integer> roller = new LootRoller<Integer>();
		for(int i = 0;i < 5;i++) {
			roller.addData(i + 1, roll.get(i));
		}
		Integer result = roller.getRandom();
		if(result == null) {
			return 1;
		}
		return result;
	}
}
