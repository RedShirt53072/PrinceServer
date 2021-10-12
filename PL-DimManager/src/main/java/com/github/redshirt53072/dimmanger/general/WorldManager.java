package com.github.redshirt53072.dimmanger.general;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;

import com.github.redshirt53072.baseapi.database.SqlDataLoader;
import com.github.redshirt53072.dimmanger.data.DimData;
import com.github.redshirt53072.dimmanger.data.WorldSqlSender;

public class WorldManager extends SqlDataLoader{
	private static List<String> worlds = new ArrayList<String>();
	private static List<String> allDims = new ArrayList<String>();
	
	public WorldManager() {
		super(DimManager.getInstance());
	}
	
	public void login(Player p) {
		//sql
		String plName = p.getName();
		new Thread() {
            @Override
            public void run() {
            	connect();
            	WorldSqlSender sender = new WorldSqlSender(connectData);
        		Location loc = sender.read(p.getUniqueId(), "normal");
        		if(loc == null) {
        			sender.insert("normal", p.getUniqueId(), p.getWorld().getUID());
        		}
        		close();
            	logEnd("player_loc","に" + plName + "の初期処理を");
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
			String plName = p.getName();
			new Thread() {
	            @Override
	            public void run() {
	            	connect();
	            	WorldSqlSender sender = new WorldSqlSender(connectData);
	        		sender.update("normal", uuid,loc);
	        		
	        		close();
	            	logEnd("player_loc","に" + plName + "の座標保存処理を");
	            }
	    	}.start();
		}
		
	}
	
	public Location readLoc(Player p) {
		connect();
    	WorldSqlSender sender = new WorldSqlSender(connectData);
		Location loc = sender.read(p.getUniqueId(), "normal");
		
		close();
    	logEnd("player_loc","に" + p.getName() + "の座標読み込み処理を");
    	return loc;
	}
	
	
    public static void reload() {
    	//configから読み込み
    	allDims = DimData.getList(true);
    	allDims.add("normal");
    	worlds = DimData.getList(false);
    	allDims.add("normal");
    }
    
    
    public static List<String> getWorlds() {
    	return worlds;
    }
    
    public static List<String> getAllDims() {
    	return allDims;
    }
    
}
