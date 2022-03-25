package com.github.redshirt53072.world.data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;

import com.github.redshirt53072.api.database.SQLInterface;
import com.github.redshirt53072.api.player.InitListener;
import com.github.redshirt53072.api.player.LogoutListener;
import com.github.redshirt53072.api.player.PlayerManager;
import com.github.redshirt53072.api.server.EmergencyListener;
import com.github.redshirt53072.world.DimManager;
import com.github.redshirt53072.world.data.DimData.DimAllData;

public class WorldManager extends SQLInterface implements EmergencyListener,InitListener,LogoutListener{
	private static List<DimData> worlds = new ArrayList<DimData>();
	
	private static DimAllData dimData;
	
	public WorldManager() {
		super(DimManager.getInstance());
	}
	@Override
	public void onInit(Player p) {
		//sql
		new Thread() {
            @Override
            public void run() {
            	connect();
            	WorldSqlSender sender = new WorldSqlSender(connectData);
        		
        		for(DimData dd : dimData.getDimData()){
        			sender.insert(dd.getName(), p.getUniqueId(), dd.getLocation());
        		}
        		close();
            }
    	}.start();
	}
	@Override
	public void onLogout(Player p) {
		saveNormal(p);
	}
	
	public void saveNormal(Player p) {
		//sql
		if(!p.getWorld().getEnvironment().equals(Environment.CUSTOM)) {
			writeLoc(p,"normal",p.getLocation());
		}
	}
	
	@Override
	public void onEmergency() {
		for(Player p : Bukkit.getOnlinePlayers()){
			saveNormal(p);
		}
	}
	
	public Location readLoc(OfflinePlayer p,String worldName) {
		connect();
    	WorldSqlSender sender = new WorldSqlSender(connectData);
		Location loc = sender.read(p.getUniqueId(), worldName);
		
		close();
    	return loc;
	}
	
	public void writeLoc(Player p,String worldName,Location loc) {
		Location loc2 = loc.clone();
		UUID uuid = p.getUniqueId();
		PlayerManager.addAsyncLock("dim", p);
		new Thread() {
            @Override
            public void run() {
            	connect();
            	WorldSqlSender sender = new WorldSqlSender(connectData);
        		sender.update(worldName,uuid, loc2);
        		close();
        		PlayerManager.removeAsyncLock("dim", p);
            }
		}.start();
	}
	
    public static void reload() {
    	//configから読み込み
    	dimData = DimConfig.reload();
    	
    	worlds = new ArrayList<DimData>();
    	for(DimData dd : dimData.getDimData()){
    		if(dd.isVisible()) {
    			worlds.add(dd);
    	    }
    	}
    }
    
    public static DimData getStart() {
    	return dimData.getStart();
    }
    
    public static DimData getNormal() {
    	for(DimData dd : dimData.getDimData()){
    		if(dd.getName().equals("normal")) {
    			return dd;
    		}
    	};
    	return null;
    }
    
    public static List<DimData> getHomeData() {
    	return dimData.getHomeDim();
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
    
    public static List<DimData> getWorlds() {
    	return worlds;
    }
    
    public static List<DimData> getAllDims() {
    	return dimData.getDimData();
    }
}