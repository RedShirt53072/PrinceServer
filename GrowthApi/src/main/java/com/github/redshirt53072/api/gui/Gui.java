package com.github.redshirt53072.api.gui;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import com.github.redshirt53072.api.item.ItemBuilder;
import com.github.redshirt53072.api.message.LogManager;
import com.github.redshirt53072.api.server.GrowthPlugin;


public abstract class Gui {
	protected Player player;
	protected GrowthPlugin plugin;
	protected Inventory inv;
	protected ChildGui child;
	
	public Gui(GrowthPlugin plugin) {
	    this.plugin = plugin;
	}
	
	protected Gui getLastGui() {
		if(child != null) {
			return child.getLastGui();
		}
		return this;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public void open(Player player) {
		if(this.player != null) {
			LogManager.logError("[Gui]既にプレイヤーが登録されたGUIにプレイヤーを登録しようとしています。", plugin,new Throwable(), Level.WARNING);
			return;
		}
		GuiManager.openGui(this, player);
		this.player = player;
		Bukkit.getScheduler().runTask(plugin, new Runnable() {
    		@Override
    		public void run() {
    		    onRegister();
    		}
    	});
	}
	
	protected void setEmptyItem(Integer... slot) {
		for(int sl : slot) {
			inv.setItem(sl, new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setName(" ").setModelData(3410).build());		
		}
	}
	
	protected void registerChild(ChildGui child) {
		this.child = child;
	}
	
	protected void close() {
		GuiManager.addNotClose(player);
		GuiManager.remove(player);
		player.closeInventory();
	}
	
	public void onEmergency() {
		onClose();
		close();
	}

	protected void onReturn() {
		child = null;
		player.openInventory(inv);
	}
	
	abstract public void onRegister();

	abstract public boolean onClick(InventoryClickEvent event);

	abstract public void onClose();
}