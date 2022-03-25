package com.github.redshirt53072.world;

import org.bukkit.event.Listener;

import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

import com.github.redshirt53072.api.message.LogManager;
import com.github.redshirt53072.api.player.PlayerStorage;
import com.github.redshirt53072.world.data.DimData;
import com.github.redshirt53072.world.data.WorldManager;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;


public final class PlayerLogin implements Listener {
	
	
	public PlayerLogin() {
		DimManager plugin = DimManager.getInstance();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void loginSpawn(PlayerSpawnLocationEvent event) {
    	DimData dd = WorldManager.getStart();
    	if(dd == null){
    		return;
    	}
    	Player player = event.getPlayer();
    	GameMode old = player.getGameMode();
    	GameMode mode = dd.getGamemode();
    	if(mode.equals(GameMode.CREATIVE) && !old.equals(GameMode.CREATIVE)) {
			new PlayerStorage().savePlayer(player, true);
		}
		if(old.equals(GameMode.CREATIVE) && !mode.equals(GameMode.CREATIVE)) {
			new PlayerStorage().loadPlayer(player);
		}
		Bukkit.getScheduler().runTask(DimManager.getInstance(), new Runnable() {
    		@Override
    		public void run() {
    	
    			player.setGameMode(mode);
    		}
		});
		event.setSpawnLocation(dd.getLocation());
    }
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void respawnPlayer(PlayerRespawnEvent event) {
    	Player player = event.getPlayer();
    	Location oldLoc = player.getLocation();
    	Location newLoc = event.getRespawnLocation();
    	if(oldLoc.getWorld().getEnvironment().equals(Environment.CUSTOM)) {
    		DimData dd = WorldManager.getDimData(oldLoc.getWorld().getUID());
    		if(dd == null){
    			event.setRespawnLocation(WorldManager.getStart().getLocation());
    			return;
    		}
    		event.setRespawnLocation(dd.getLocation());
    		return;
    	}
    	if(newLoc.getWorld().getEnvironment().equals(Environment.CUSTOM)) {
    		Location loc = Bukkit.getWorld(WorldManager.getNormal().getUUID()).getSpawnLocation();
    		event.setRespawnLocation(loc);
		}
    	//座標保存
    	new WorldManager().saveNormal(player);
    }
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void teleport(PlayerTeleportEvent event) {
    	Player player = event.getPlayer();
    	
    	if(Teleporter.isTPPLayer(player)) {
    		return;
    	}
    	
    	Location oldLoc = event.getFrom();
    	Location newLoc = event.getTo();
    	
    	World oldWorld = oldLoc.getWorld();
    	World newWorld = newLoc.getWorld();
    	
    	if(oldWorld.equals(newWorld)) {
    		return;
    	}else {
    		if(!oldWorld.getEnvironment().equals(Environment.CUSTOM)){
    			if(!newWorld.getEnvironment().equals(Environment.CUSTOM)){
    	    		return;
    	    	}
    		}
    	}
    	if(null == WorldManager.getDimData(newWorld.getUID())) {
    		return;
    	}
    	
    	LogManager.logInfo("DimManager以外によるディメンションを跨ぐテレポートは禁止されています。", DimManager.getInstance(), Level.WARNING);
    	
    	event.setCancelled(true);
    }
}