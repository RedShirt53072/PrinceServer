package com.github.redshirt53072.dimmanager.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.World.Environment;

import com.github.redshirt53072.growthapi.config.ConfigManager;
import com.github.redshirt53072.growthapi.message.LogManager;
import com.github.redshirt53072.growthapi.util.DataFolder;
import com.github.redshirt53072.growthapi.util.Flag;
import com.github.redshirt53072.dimmanager.DimManager;
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
		
		List<DimData> homeList = new ArrayList<DimData>();
		
		List<String> homes = manager.getStringArray("", "home");
		
		for(String key : manager.getKeys("", "dim")) {
			DimData dd = readDim(key,manager);
			if(dd == null) {
				continue;
			}
			try {
				DimID.valueOf(dd.getName()).getName();
			}catch(Exception ex) {
				LogManager.logInfos(DimManager.getInstance(),Level.WARNING,
						"ディメンション名は事前に定義されたIDのみ使用が許可されています。",
						dd.getName() + "のデータは自動的に削除されました。");
	        	delete(dd.getName());
				continue;
			}
			if(start != null) {
				if(dd.getName().equals(start)) {
					startData = dd;
				}
			}
			for(String hom : homes) {
				if(dd.getName().equals(hom)) {
					homeList.add(dd);
					break;
				}
			}
			
			list.add(dd);
		}
		DataFolder<World> world = new DataFolder<World>();
		Bukkit.getWorlds().forEach(w ->{if(w.getEnvironment().equals(Environment.NORMAL)) {
			world.setData(w);
		}});
		Location normalLoc = world.getData().getSpawnLocation();
    	DimData normal = new DimData("normal",normalLoc,GameMode.SURVIVAL,world.getData().getUID(),true);
		list.add(normal);
		
		manager.logConfig("", "を読み込みました。");
		return new DimAllData(list,homeList,startData);
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
	
	public static void setHome(String dim) {
		ConfigManager manager = new ConfigManager(DimManager.getInstance(),"data","world.yml");
		
		int index = manager.getNextIndex("home");
		
		manager.setData("home" + index, dim);
		
		manager.logConfig("home" + index,"に" + dim + "を設定しました。");
	}
	
	public static boolean deleteHome(String dim) {
		ConfigManager manager = new ConfigManager(DimManager.getInstance(),"data","world.yml");
		for(String key : manager.getKeys("", "home")) {
			String hom = manager.getString(key);
			if(hom == null) {
				continue;
			}
			if(hom.equals(dim)) {
				manager.deleteData(key);
				return true;
			}
		}
		return false;
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
		deleteHome(name);

		String start = manager.getString("start");
		if(name.equals(start)) {
			setStart("NONE");
		}
	}
}
