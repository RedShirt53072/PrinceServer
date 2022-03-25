package com.gmail.akashirt53072.minegame.listener;

import org.bukkit.event.Listener;

import org.bukkit.event.player.PlayerQuitEvent;

import com.gmail.akashirt53072.minegame.Main;
import com.gmail.akashirt53072.minegame.PlayerManager;
import com.gmail.akashirt53072.minegame.PlayerTick;
import com.gmail.akashirt53072.minegame.enums.PlayerStatus;
import com.gmail.akashirt53072.minegame.nbt.NBTPlayerStatus;

import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.entity.Player;


public final class PlayerLogin implements Listener {
    Main plugin;
	public PlayerLogin(Main plugin) {
    	this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
	
    @EventHandler(priority = EventPriority.NORMAL)
    public void getLoginPlayer(PlayerJoinEvent event) {
    	Player player = event.getPlayer();
    	
    	PlayerStatus type = new NBTPlayerStatus(plugin,player).getType();
    	
    	if(type.equals(PlayerStatus.BREAKING)) {
    		//ログアウト(break)→試合
			new PlayerManager(plugin,event.getPlayer()).onLogin();
    	}
    	
    	//tickLoop
		new PlayerTick(plugin,player).runTaskLater(plugin,1);
		
		
    }
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void getLogoutPlayer(PlayerQuitEvent event) {
    	onPlayerLogout(event);
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void getLeaveWorldPlayer(PlayerChangedWorldEvent event) {
    	onPlayerLogout(event);
    }
    
    public void onPlayerLogout(PlayerEvent event) {
    	Player player = event.getPlayer();
    	PlayerStatus type = new NBTPlayerStatus(plugin,player).getType();
    	if(type.equals(PlayerStatus.lOGOUT)) {
    		return;
    	}
    	if(type.equals(PlayerStatus.BREAKING)) {
    		return;
    	}
    	//ログアウト系
    	new PlayerManager(plugin,player).onLogout();
    	
    }
}
