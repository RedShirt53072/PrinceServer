package com.github.redshirt53072.dimmanager.general;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;

import com.github.redshirt53072.growthapi.database.SQLInterface;
import com.github.redshirt53072.growthapi.server.EmergencyListener;
import com.github.redshirt53072.growthapi.util.DataFolder;
import com.github.redshirt53072.dimmanager.DimManager;
import com.github.redshirt53072.dimmanager.data.DimData;
import com.github.redshirt53072.dimmanager.data.WorldSqlSender;

public class WorldManager extends SQLInterface implements EmergencyListener{
	private static List<String> worlds = new ArrayList<String>();
	private static List<String> allDims = new ArrayList<String>();
	
	public WorldManager() {
		super(DimManager.getInstance());
	}
	
	public void login(Player p) {
		//sql
		DataFolder<World> world = new DataFolder<World>();
		Bukkit.getWorlds().forEach(w ->{if(w.getEnvironment().equals(Environment.NORMAL)) {
			world.setData(w);
		}});
		new Thread() {
            @Override
            public void run() {
            	connect();
            	WorldSqlSender sender = new WorldSqlSender(connectData);
        		Location loc = sender.read(p.getUniqueId(), "normal");
        		if(loc == null) {
        			sender.insert("normal", p.getUniqueId(), world.getData().getUID());
        		}
        		close();
            }
    	}.start();
    	//config
    	Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
    		@Override
    		public void run() {
    			String dim = DimData.getStart();
    			Location loc = DimData.getLocation(dim);
    			GameMode mode = DimData.getGamemode(dim);
    	    	Bukkit.getScheduler().runTask(plugin, new Runnable() {
    	    		@Override
    	    		public void run() {
    	    			p.teleport(loc);
    	    			p.setGameMode(mode);	
    	    		}
    	    	});
    		}
    	});
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
    	allDims = DimData.getList(true);
    	allDims.add("normal");
    	worlds = DimData.getList(false);
    	worlds.add("normal");
    }
    
    
    public static List<String> getWorlds() {
    	return worlds;
    }
    
    public static List<String> getAllDims() {
    	return allDims;
    }
    
}
