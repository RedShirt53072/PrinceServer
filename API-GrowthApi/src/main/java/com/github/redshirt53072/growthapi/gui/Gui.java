package com.github.redshirt53072.growthapi.gui;

import java.util.logging.Level;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import com.github.redshirt53072.growthapi.message.LogManager;
import com.github.redshirt53072.growthapi.server.GrowthPlugin;


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
	    onRegister();
	}
	
	protected void registerChild(ChildGui child) {
		this.child = child;
	}
	
	protected void close() {
		//BaseAPI.getInstance().getLogger().log(Level.INFO,"close1:" + plugin.getPluginName());
		GuiManager.addNotClose(player);
		GuiManager.remove(player);
		player.closeInventory();
		//BaseAPI.getInstance().getLogger().log(Level.INFO,"close2:" + plugin.getPluginName());
	}
	
	public void onEmergency() {
		onClose();
		close();
	}

	public void onReturn() {
		child = null;
		player.openInventory(inv);
	}
	
	abstract public void onRegister();

	abstract public boolean onClick(InventoryClickEvent event);

	abstract public void onClose();
}