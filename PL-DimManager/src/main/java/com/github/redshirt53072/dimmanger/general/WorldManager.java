package com.github.redshirt53072.dimmanger.general;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
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
            	logEnd();
            }
    	}.start();
    	//config
    	Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
    		@Override
    		public void run() {
    			String dim = DimData.getStart();
    			p.teleport(DimData.getLocation(dim));
    			p.setGameMode(DimData.getGamemode(dim));
    		}
    	});
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
