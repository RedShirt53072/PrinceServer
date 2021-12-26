package com.github.redshirt53072.economyapi.gui;
//playerの行動をトリガーに様々な処理クラスへと取り次ぐクラス
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import com.github.redshirt53072.economyapi.general.EconomyApi;


public final class PlayerInvAction implements Listener {
	EconomyApi plugin;
    public PlayerInvAction() {
    	this.plugin = EconomyApi.getInstance();
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