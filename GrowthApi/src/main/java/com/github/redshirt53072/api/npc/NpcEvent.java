package com.github.redshirt53072.api.npc;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import com.github.redshirt53072.api.BaseAPI;
import com.github.redshirt53072.api.server.GrowthPlugin;

public final class NpcEvent implements Listener {
	public NpcEvent() {
    	GrowthPlugin plugin = BaseAPI.getInstance();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    @EventHandler
    public void onClick(PlayerInteractEntityEvent event) {
    	Entity entity = event.getRightClicked();
    	if(entity.getType().equals(EntityType.VILLAGER)) {
    		if(NpcManager.onClick((Villager)entity,event.getPlayer())) {
    			event.setCancelled(true);
    		}
    	}
    }
    
    @EventHandler
    public void onClick(EntityDamageByEntityEvent event) {
    	Entity entity = event.getEntity();
        if(!entity.getType().equals(EntityType.VILLAGER)) {
        	return;
        }
    	
        Entity attacker = event.getDamager();
        if(attacker.getType().equals(EntityType.PLAYER)) {
        	if(NpcManager.onClick((Villager)entity,(Player)attacker)) {
        		event.setCancelled(true);
        	}
        }
    }
}
