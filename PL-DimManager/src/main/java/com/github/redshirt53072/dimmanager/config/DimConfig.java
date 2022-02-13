package com.github.redshirt53072.dimmanager.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;

import com.github.redshirt53072.growthapi.config.ConfigManager;
import com.github.redshirt53072.growthapi.util.Flag;
import com.github.redshirt53072.dimmanager.DimManager;
import com.github.redshirt53072.dimmanager.data.DimData;
import com.github.redshirt53072.dimmanager.data.DimData.DimAllData;

public class DimConfig {
	/*
	start: lobby
	dim1:
  		name: mining
  		uuid: aadse5sf4e54f
  		gamemode: SURVIVAL
  		visible: true
  		location: loc
	 */
	
	public static DimAllData reload() {
		ConfigManager manager = new ConfigManager(DimManager.getInstance(),"data","world.yml");
		manager.configInit();
		String start = manager.getString("start");
		DimData startData = null;
		List<DimData> list = new ArrayList<DimData>();
		
		for(String key : manager.getKeys("", "dim")) {
			DimData dd = readDim(key,manager);
			if(dd == null) {
				continue;
			}
			if(start != null) {
				if(dd.getName().equals(start)) {
					startData = dd;
				}
			}
			list.add(dd);
		}
		
		manager.logConfig("", "を読み込みました。");
		return new DimAllData(list,startData);
	}
	
	private static DimData readDim(String path,ConfigManager manager){
		boolean cancel = false;
		
		String name = manager.getString(path + ".name");
		if(name == null) {
			cancel = true;
			manager.logWarning(path + ".name", "の値が不正です");
		}
		
		Integer visible = manager.getInt(path + ".visible");
		if(visible == null) {
			cancel = true;
			manager.logWarning(path + ".name", "の値が不正です");
		}
		boolean isVisible = false;
		if(visible != 0) {
			isVisible = true;
		}
		
		String rawMode = manager.getString(path + ".gamemode");
		GameMode mode = GameMode.ADVENTURE;
		try{
			mode = GameMode.valueOf(rawMode);
		}catch(Exception ex) {
			cancel = true;
			manager.logWarning(path + ".gamemode", "の値が不正です");
		}
		Location loc = null;
		try{
			loc = manager.getLocation(path + ".location");
			loc.getX();
		}catch(Exception ex) {
			cancel = true;
			manager.logWarning(path + ".location", "の値が不正です");
		}
		UUID uuid = null;
		try{
			uuid = UUID.fromString(manager.getString(path + ".uuid"));
			Bukkit.getWorld(uuid).getName();
		}catch(Exception ex) {
			cancel = true;
			manager.logWarning(path + ".uuid", "の値が不正です");
		}
		
		if(cancel) {
			return null;
		}
		return new DimData(name,loc,mode,uuid,isVisible);
	}
	
	public static void setStart(String start) {
		ConfigManager manager = new ConfigManager(DimManager.getInstance(),"data","world.yml");
		
		if(start == null) {
			manager.setData("start", "NONE");
		}else {
			manager.setData("start", start);
		}
		
		manager.logConfig("start","を" + start + "に設定しました。");
    }
	
	public static boolean addDimension(DimData data) {
		ConfigManager manager = new ConfigManager(DimManager.getInstance(),"data","world.yml");
		
		String uuid = data.getLocation().getWorld().getUID().toString();
		String name = data.getName();
		
		Flag match = new Flag();
		Set<String> keys = manager.getKeys("", "dim");
		keys.forEach(key -> {if(uuid.equals(manager.getString(key + ".uuid"))) {
			match.setTrue();
		}});
		if(match.getFlag()) {
			manager.logConfig("dim","に" + name + "のディメンションは追加できませんでした。");	
			return false;
		}
		int index = manager.getNextIndex("dim");
		
		manager.setData("dim" + index + ".name", name);
		manager.setData("dim" + index + ".gamemode", data.getGamemode().toString());
		if(data.isVisible()) {
			manager.setData("dim" + index + ".visible", 1);	
		}else {
			manager.setData("dim" + index + ".visible", 0);	
		}
		manager.setData("dim" + index + ".uuid", uuid);
		manager.setData("dim" + index + ".location", data.getLocation());
		
		manager.logConfig("dim" + index,"に" + name + "のディメンションを追加しました。");
		return true;
    }
	
	public static void delete(String name) {
		ConfigManager manager = new ConfigManager(DimManager.getInstance(),"data","world.yml");
		Set<String> keys = manager.getKeys("", "dim");
		keys.forEach(key -> {if(name.equals(manager.getString(key + ".name"))) {
			manager.deleteData(key);
		}});
	}
}
