package com.github.redshirt53072.newfishing.config;

import java.util.ArrayList;

import org.bukkit.block.Biome;

import com.github.redshirt53072.newfishing.NewFishing;
import com.github.redshirt53072.newfishing.data.RodLootTable;
import com.github.redshirt53072.newfishing.data.RarityLootData;
import com.github.redshirt53072.newfishing.data.DimLootData;
import com.github.redshirt53072.newfishing.data.FishData;
import com.github.redshirt53072.newfishing.data.FishLootData;
import com.github.redshirt53072.newfishing.data.TimeLootData;
import com.github.redshirt53072.newfishing.data.YLootData;

public class LootConfig extends DataConfig{
	private NewFishing plugin;
	private ArrayList<FishData> fishData;
	public LootConfig(NewFishing plugin,ArrayList<FishData> fishData) {
		super(plugin,"loottable.yml");
		this.plugin = plugin;
		this.fishData = fishData;
	}
	public RodLootTable getAllData() {
		//overworld
		DimLootData overData = null;
		if(containData("overworld")) {
			overData = readDim();
		}else {
			plugin.getLogger().warning("[error]loottable.yml内にoverworldの項目がありません");
			plugin.stopFishing();
		}
		//the end
		ArrayList<TimeLootData> endData = null;
		if(containData("theend")) {
			endData = readTimeList("theend");
		}else {
			plugin.getLogger().warning("[error]loottable.yml内にtheendの項目がありません");
			plugin.stopFishing();
		}
		return new RodLootTable(overData,endData);
	}
	private DimLootData readDim() {
		//overworldのみ
		ArrayList<TimeLootData> defData = new ArrayList<TimeLootData>();
		//デフォルト
		if(containData("overworld.def")) {
			defData = readTimeList("overworld.def");
		}else {
			plugin.getLogger().warning("[error]loottable.yml内、overworldにdef(デフォルト)の項目がありません");
			plugin.stopFishing();
		}
		//biome
		ArrayList<RarityLootData> biomeData = new ArrayList<RarityLootData>();
		for(int i = 1;containData("overworld.biome" + i);i ++) {
			biomeData.add(readBiome("overworld.biome" + i));
		}
		return new DimLootData(biomeData,defData);
	}
	private RarityLootData readBiome(String path) {
		//biome
		ArrayList<Biome> BiomeList = new ArrayList<Biome>();
		for(int i = 1;containData(path + ".type" + i);i ++) {
			String rawBiome = getStringData(path + ".type" + i);
			try {
				BiomeList.add(Biome.valueOf(rawBiome));
			}catch (IllegalArgumentException ex){
				plugin.getLogger().warning("[error]loottable.yml内、" + path + ".type" + i + "のバイオームの値が不正です");
				plugin.stopFishing();
			}
		}
		if(BiomeList.isEmpty()) {
			plugin.getLogger().warning("[error]loottable.yml内、" + path + "に有効なtype1の項目がありません");
			plugin.stopFishing();
		}
		//time
		ArrayList<TimeLootData> TimeData = readTimeList(path);
		return new RarityLootData(BiomeList,TimeData);
	}
	private ArrayList<TimeLootData> readTimeList(String path) {
		ArrayList<TimeLootData> data = new ArrayList<TimeLootData>();
		for(int i = 1;containData(path + ".time" + i);i ++) {
			data.add(readTime(path + ".time" + i));
		}
		if(data.isEmpty()) {
			plugin.getLogger().warning("[error]loottable.yml内、" + path + "にtime1の項目がありません");
			plugin.stopFishing();
		}
		//24000tickになってるか検証
		//一つずつ両端の値をキャッシュに入れていく。連番なら消す。最後に0以下と23999以上が残ってればＯＫ
		ArrayList<Integer> box = new ArrayList<Integer>();
		for(TimeLootData t : data) {
			int start = t.getStart();
			boolean b = true;
			for(int i = 0;box.size() > i;i++) {
				if(start - 1 == box.get(i)) {
					box.remove(i);
					b = false;
				}
			}
			if(b) {
				box.add(start);
			}
			int end = t.getEnd();
			boolean b2 = true;
			for(int i = 0;box.size() > i;i++) {
				if(end + 1 == box.get(i)) {
					box.remove(i);
					b2 = false;
				}
			}
			if(b2) {
				box.add(end);
			}
		}
		int size = box.size();
		if(size == 2 ) {
			if(box.get(0) < 1 && box.get(1) > 23998) {
				return data;
			}
			if(box.get(1) < 1 && box.get(0) > 23998) {
				return data;	
			}
		}
		plugin.getLogger().warning("[error]loottable.yml内、" + path + "では全時刻の網羅ができていません");
		plugin.stopFishing();
		return data;
	}
	private TimeLootData readTime(String path) {
		ArrayList<YLootData> data = new ArrayList<YLootData>();
		//start
		int start = getIntData(path + ".start");
		//end
		int end = getIntData(path + ".end");
		if(!(start < end)) {
			plugin.getLogger().warning("[error]loottable.yml内、" + path + "でstartがendより大きい、または同値になっています");
			plugin.stopFishing();
		}
		//y
		for(int i = 1;containData(path + ".y" + i);i ++) {
			data.add(readY(path + ".y" + i));
		}
		if(data.isEmpty()) {
			plugin.getLogger().warning("[error]loottable.yml内、" + path + "にy1の項目がありません");
			plugin.stopFishing();
		}
		//Y0~255まで網羅されてるか検証
		//一つずつ両端の値をキャッシュに入れていく。連番なら消す。最後に0と255が残ってればＯＫ
		ArrayList<Integer> box = new ArrayList<Integer>();
		for(YLootData t : data) {
			int ystart = t.getStart();
			boolean b = true;
			for(int i = 0;box.size() > i;i++) {
				if(ystart - 1 == box.get(i)) {
					box.remove(i);
					b = false;
				}
			}
			if(b) {
				box.add(ystart);
			}
			int yend = t.getEnd();
			boolean b2 = true;
			for(int i = 0;box.size() > i;i++) {
				if(yend + 1 == box.get(i)) {
					box.remove(i);
					b2 = false;
				}
			}
			if(b2) {
				box.add(yend);
			}
		}
		int size = box.size();
		if(size == 2 ) {
			if(box.get(0) < 1 && box.get(1) > 254) {
				return new TimeLootData(start,end,data);
			}
			if(box.get(1) < 1 && box.get(0) > 254) {
				return new TimeLootData(start,end,data);	
			}
		}
		plugin.getLogger().warning("[error]loottable.yml内、" + path + "では全Y座標の網羅ができていません");
		plugin.stopFishing();		
		return new TimeLootData(start,end,data);
	}
	private YLootData readY(String path) {
		//start
		int start = getIntData(path + ".start");
		//end
		int end = getIntData(path + ".end");
		if(!(start < end)) {
			plugin.getLogger().warning("[error]loottable.yml内、" + path + "でstartがendより大きい、または同値になっています");
			plugin.stopFishing();
		}
		//rarity
		ArrayList<ArrayList<FishLootData>> data = new ArrayList<ArrayList<FishLootData>>();
		for(int i = 1;i < 8;i ++) {
			if(containData(path + ".rarity" + i)) {
				data.add(readRarity(path + ".rarity" + i));
			}else {
				plugin.getLogger().warning("[error]loottable.yml内、" + path + "でrarity" + i + "の項目がありません");
				plugin.stopFishing();
			}
		}
		
		return new YLootData(start,end,data);
	}
	
	private ArrayList<FishLootData> readRarity(String path) {
		ArrayList<FishLootData> data = new ArrayList<FishLootData>();
		for(int i = 1;containData(path + ".fish" + i);i ++) {
			data.add(readFish(path + ".fish" + i));
		}
		if(data.isEmpty()) {
			plugin.getLogger().warning("[error]loottable.yml内、" + path + "にfish1の項目がありません");
			plugin.stopFishing();
		}
		return data;
	}
	private FishLootData readFish(String path) {
		//id
		String id = getStringData(path + ".id");
		boolean match = false;
		for(FishData f : fishData) {
			if(f.getId().equals(id)) {
				match = true;
			}
		}
		if(match == false) {
			plugin.getLogger().warning("[error]loottable.yml内、" + path + "のidの魚は存在しません");
			plugin.stopFishing();
		}
		//roll
		int roll = getIntData(path + ".roll");
		return new FishLootData(id,roll);
	}
}