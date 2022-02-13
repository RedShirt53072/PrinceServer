package com.github.redshirt53072.dimmanager.data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;

import com.github.redshirt53072.growthapi.database.SQLInterface;
import com.github.redshirt53072.growthapi.server.EmergencyListener;
import com.github.redshirt53072.growthapi.util.DataFolder;
import com.github.redshirt53072.dimmanager.DimManager;
import com.github.redshirt53072.dimmanager.config.DimConfig;
import com.github.redshirt53072.dimmanager.data.DimData.DimAllData;

public class WorldManager extends SQLInterface implements EmergencyListener{
	private static List<String> worlds = new ArrayList<String>();
	private static List<String> allDims = new ArrayList<String>();
	
	private static DimAllData dimData;
	private static UUID normal;
	
	public WorldManager() {
		super(DimManager.getInstance());
	}
	
	public void login(Player p) {
		//sql
		new Thread() {
            @Override
            public void run() {
            	connect();
            	WorldSqlSender sender = new WorldSqlSender(connectData);
        		Location loc = sender.read(p.getUniqueId(), "normal");
        		if(loc == null) {
        			sender.insert("normal", p.getUniqueId(), normal);
        		}
        		close();
            }
    	}.start();
	}
	
	public void logout(Player p) {
		//sql
		if(!p.getWorld().getEnvironment().equals(Environment.CUSTOM)) {
			UUID uuid = p.getUniqueId();
			Location loc = p.getLocation().clone();
			new Thread() {
	            @Override
	            public void run() {
	            	connect();
	            	WorldSqlSender sender = new WorldSqlSender(connectData);
	        		sender.update("normal", uuid,loc);
	        		close();
	            }
	    	}.start();
		}
	}
	
	@Override
	public void onEmergency() {
		for(Player p : Bukkit.getOnlinePlayers()){
			logout(p);
		}
	}
	
	public Location readLoc(Player p) {
		connect();
    	WorldSqlSender sender = new WorldSqlSender(connectData);
		Location loc = sender.read(p.getUniqueId(), "normal");
		
		close();
    	return loc;
	}
	
    public static void reload() {
    	//configから読み込み
    	dimData = DimConfig.reload();
    	allDims = new ArrayList<String>();
    	for(DimData dd : dimData.getDimData()){
    		allDims.add(dd.getName());
    	}
    	allDims.add("normal");
    	
    	worlds = new ArrayList<String>();
    	for(DimData dd : dimData.getDimData()){
    		if(dd.isVisible()) {	
    			worlds.add(dd.getName());
    	    }
    	}
    	worlds.add("normal");
    	
    	DataFolder<World> world = new DataFolder<World>();
		Bukkit.getWorlds().forEach(w ->{if(w.getEnvironment().equals(Environment.NORMAL)) {
			world.setData(w);
		}});
		normal = world.getData().getUID();
    }
    
    public static UUID getNormal() {
    	return normal;
    }
    
    public static DimData getStart() {
    	return dimData.getStart();
    }
    
    public static DimData getDimData(String name) {
    	for(DimData dd : dimData.getDimData()){
    		if(dd.getName().equals(name)) {	
    			return dd;
    		}
    	}
    	return null;
    }
    
    public static DimData getDimData(UUID uuid) {
    	for(DimData dd : dimData.getDimData()){
    		if(dd.getUUID().equals(uuid)) {	
    			return dd;
    		}
    	}
    	return null;
    }
    
    public static List<String> getWorlds() {
    	return worlds;
    }
    
    public static List<String> getAllDims() {
    	return allDims;
    }
    
    
}
