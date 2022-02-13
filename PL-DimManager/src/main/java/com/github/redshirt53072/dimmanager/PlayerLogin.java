package com.github.redshirt53072.dimmanager;

import org.bukkit.event.Listener;

import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

import com.github.redshirt53072.dimmanager.data.DimData;
import com.github.redshirt53072.dimmanager.data.WorldManager;

import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;


public final class PlayerLogin implements Listener {
	public PlayerLogin() {
		DimManager plugin = DimManager.getInstance();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
	
    @EventHandler(priority = EventPriority.NORMAL)
    public void loginPlayer(PlayerJoinEvent event) {
    	Player player = event.getPlayer();
    	new WorldManager().login(player);
    }
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void loginPlayer(PlayerSpawnLocationEvent event) {
    	DimData dd = WorldManager.getStart();
    	if(dd == null){
    		return;
    	}
    	event.setSpawnLocation(dd.getLocation());
    }
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void logoutPlayer(PlayerQuitEvent event) {
    	Player player = event.getPlayer();
    	//座標保存
    	new WorldManager().logout(player);	
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void respawnPlayer(PlayerRespawnEvent event) {
    	Player player = event.getPlayer();
    	Location oldLoc = player.getLocation();
    	Location newLoc = event.getRespawnLocation();
    	if(oldLoc.getWorld().getEnvironment().equals(Environment.CUSTOM)) {
    		DimData dd = WorldManager.getDimData(oldLoc.getWorld().getUID());
    		if(dd == null){
    			return;
    		}
    		event.setRespawnLocation(dd.getLocation());
    		return;
    	}
    	if(newLoc.getWorld().getEnvironment().equals(Environment.CUSTOM)) {
    		Location loc = Bukkit.getWorld(WorldManager.getNormal()).getSpawnLocation();
    		event.setRespawnLocation(loc);
		}
    	//座標保存
    	new WorldManager().logout(player);
    }
}