package com.github.redshirt53072.dimmanger.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;

import com.github.redshirt53072.baseapi.config.ConfigManager;
import com.github.redshirt53072.baseapi.util.DataFolder;
import com.github.redshirt53072.baseapi.util.Flag;
import com.github.redshirt53072.dimmanger.general.DimManager;

public class DimData {
	
	
	/*
	start: lobby
	dim1:
  		name: mining
  		uuid: aadse5sf4e54f
  		gamemode: SURVIVAL
  		visible: true
  		tpx: 0
  		tpy: 10
  		tpz: 0
  		yaw: 0
  		pitch: 0
	 */
	
	public static void init() {
		ConfigManager manager = new ConfigManager(DimManager.getInstance(),"data","world");
		manager.configInit();
		
	}
	
	public static String getStart() {
		ConfigManager manager = new ConfigManager(DimManager.getInstance(),"data","world");
		
		String start = manager.getString("start");
		
		manager.logConfig("start","を読み込みました。");
		return start;
    }
	
	public static List<String> getList(boolean getAll){
		List<String> list = new ArrayList<String>();
		ConfigManager manager = new ConfigManager(DimManager.getInstance(),"data","world");
		
		Set<String> keys = manager.getKeys("", "dim");
		
		if(getAll) {
			keys.forEach(key -> list.add(manager.getString(key + ".name")));
		}else {
			keys.forEach(key -> {if(manager.getInt(key + ".visible") == 1) {
				list.add(manager.getString(key + ".name"));
			}});
		}
		manager.logConfig("dim*.name","を読み込みました。");
		return list;
	}
	
	public static GameMode getGamemode(String name) {
		DataFolder<String> mode = new DataFolder<String>();
		ConfigManager manager = new ConfigManager(DimManager.getInstance(),"data","world");
		
		Set<String> keys = manager.getKeys("", "dim");
		keys.forEach(key -> {if(manager.getString(key + ".name").equals(name)) {
			mode.setData(manager.getString(key + ".gamemode"));
		}});
		manager.logConfig("dim.gamemode","を読み込みました。");
		
		return GameMode.valueOf(mode.getData());
	}
	public static Location getLocation(String name) {
		ConfigManager manager = new ConfigManager(DimManager.getInstance(),"data","world");
		DataFolder<Location> loc = new DataFolder<Location>();
		
		Set<String> keys = manager.getKeys("", "dim");
		keys.forEach(key -> {if(manager.getString(key + ".name").equals(name)) {
			UUID uuid = UUID.fromString(manager.getString(key + ".uuid"));
			int x = manager.getInt(key + ".x");
			int y = manager.getInt(key + ".y");
			int z = manager.getInt(key + ".z");
			int yaw = manager.getInt(key + ".yaw");
			int pitch = manager.getInt(key + ".pitch");
			loc.setData(new Location(Bukkit.getWorld(uuid),x,y,z,yaw,pitch));
		}});
		manager.logConfig("dim.loc","を読み込みました。");
		return loc.getData();
	}
	
	public static void setStart(String start) {
		ConfigManager manager = new ConfigManager(DimManager.getInstance(),"data","world");
		
		if(start == null) {
			manager.setData("start", "NONE");
		}else {
			manager.setData("start", start);
		}
		
		manager.logConfig("start","を" + start + "に設定しました。");
    }
	
	public static boolean addDimension(World world,String name,GameMode mode,boolean alldim, int x,int y,int z,int yaw,int pitch) {
		ConfigManager manager = new ConfigManager(DimManager.getInstance(),"data","world");
		
		String uuid = world.getUID().toString();
		
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
		manager.setData("dim" + index + ".gamemode", mode.toString());
		manager.setData("dim" + index + ".uuid", uuid);
		if(alldim) {
			manager.setData("dim" + index + ".visible", 1);	
		}else {
			manager.setData("dim" + index + ".visible", 0);	
		}
		manager.setData("dim" + index + ".x", x);
		manager.setData("dim" + index + ".y", y);
		manager.setData("dim" + index + ".z", z);
		manager.setData("dim" + index + ".yaw", yaw);
		manager.setData("dim" + index + ".pitch", pitch);
		
		manager.logConfig("dim" + index,"に" + name + "のディメンションを追加しました。");
		return true;
    }
	
	
}
