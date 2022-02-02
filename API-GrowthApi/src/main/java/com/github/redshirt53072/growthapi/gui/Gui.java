package com.github.redshirt53072.growthapi.gui;

import java.util.ArrayList;
import java.util.logging.Level;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.github.redshirt53072.growthapi.item.ItemUtil;
import com.github.redshirt53072.growthapi.message.LogManager;
import com.github.redshirt53072.growthapi.server.GrowthPlugin;



public abstract class Gui {
	protected Player player;
	protected GrowthPlugin plugin;
	protected Inventory inv;
	
	public Gui(GrowthPlugin plugin) {
	    this.plugin = plugin;
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
	
	protected ItemStack createItem(Material material,String name,ArrayList<String> lore,int amount,Enchantment ench,int itemModel){
		return ItemUtil.buildItem(material, name, lore, amount, ench, itemModel);
	}
	

	public void close() {
		//BaseAPI.getInstance().getLogger().log(Level.INFO,"close1:" + plugin.getPluginName());
		GuiManager.addNotClose(player);
		GuiManager.remove(player);
		player.closeInventory();

		//BaseAPI.getInstance().getLogger().log(Level.INFO,"close2:" + plugin.getPluginName());
	}
	
	public void onEmergency() {
		player.closeInventory();
	}
	
	abstract public void onRegister();

	abstract public boolean onClick(InventoryClickEvent event);

	abstract public void onClose();
	
}
