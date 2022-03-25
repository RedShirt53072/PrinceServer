package com.gmail.akashirt53072.minegame.listener;
//playerの行動をトリガーに様々な処理クラスへと取り次ぐクラス
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;

import com.gmail.akashirt53072.minegame.Main;
import com.gmail.akashirt53072.minegame.gui.GuiManager;

import org.bukkit.entity.Player;

public final class PlayerInvAction implements Listener {
	Main plugin;
    public PlayerInvAction(Main plugin) {
    	this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    @EventHandler
    public void onInventoryClick(final InventoryClickEvent event) {
        final Player player = (Player) event.getWhoClicked();
        int slot = event.getRawSlot();
        ItemStack cursor = event.getCursor(); 
        event.setCancelled(new GuiManager(plugin).onClick(player, slot,cursor));
    }
    
    @EventHandler
    public void onInventoryClose(final InventoryCloseEvent event) {
        final Player player = (Player) event.getPlayer();
        new GuiManager(plugin).onClose(player);
    }
    
}