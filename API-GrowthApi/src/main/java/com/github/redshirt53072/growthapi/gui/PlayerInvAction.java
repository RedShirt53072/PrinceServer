package com.github.redshirt53072.growthapi.gui;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import com.github.redshirt53072.growthapi.BaseAPI;
import com.github.redshirt53072.growthapi.server.GrowthPlugin;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;


/**
 * playerの行動をトリガーにGUIの処理クラスへと取り次ぐクラス
 * @author akash
 *
 */
public final class PlayerInvAction implements Listener {
    
	public PlayerInvAction() {
    	GrowthPlugin plugin = BaseAPI.getInstance();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    @EventHandler
    public void onInventoryClick(final InventoryClickEvent event) {
    	GuiManager.onClick(event);
    }
    
    @EventHandler
    public void onInventoryClose(final InventoryCloseEvent event) {
    	Player p = (Player) event.getPlayer();
    	GuiManager.onClose(p);
    }
    
}