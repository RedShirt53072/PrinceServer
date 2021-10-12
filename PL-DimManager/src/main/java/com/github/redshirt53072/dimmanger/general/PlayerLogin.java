package com.github.redshirt53072.dimmanger.general;

import org.bukkit.event.Listener;

import org.bukkit.event.player.PlayerQuitEvent;

import com.github.redshirt53072.baseapi.server.GrowthApi;

import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import org.bukkit.entity.Player;


public final class PlayerLogin implements Listener {
	public PlayerLogin(GrowthApi plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
	
    @EventHandler(priority = EventPriority.NORMAL)
    public void getLoginPlayer(PlayerJoinEvent event) {
    	Player player = event.getPlayer();
    	new WorldManager().login(player);
    }
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void getLogoutPlayer(PlayerQuitEvent event) {
    	Player player = event.getPlayer();
    	//座標保存
    	new WorldManager().logout(player);
    	
    }
}
