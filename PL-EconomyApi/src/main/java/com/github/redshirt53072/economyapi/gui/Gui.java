package com.github.redshirt53072.economyapi.gui;

import java.util.ArrayList;
import java.util.logging.Level;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.github.redshirt53072.baseapi.message.LogManager;
import com.github.redshirt53072.baseapi.server.GrowthApi;
import com.github.redshirt53072.economyapi.general.EconomyApi;



public abstract class Gui {
	protected Player player;
	protected GrowthApi plugin;
	protected Inventory inv;
	
	public Gui(GrowthApi plugin) {
	    this.plugin = plugin;
	    onCreate();
	}
	
	public Player getPlayer() {
		return player;
	}

	public void registerPlayer(Player player) {
		if(this.player != null) {
			LogManager.logError("[Gui]既にプレイヤーが登録されたGUIにプレイヤーを登録しようとしています。", EconomyApi.getInstance(),new Throwable(), Level.WARNING);
			return;
		}
	    this.player = player;
		player.openInventory(inv);
	}
	
	protected ItemStack createItem(Material material,String name,ArrayList<String> lore,int amount,Enchantment ench,int itemModel){
		final ItemStack item = new ItemStack(material, amount);
	    final ItemMeta meta = item.getItemMeta();
	    meta.setDisplayName(name);
	    if(lore != null) {
	    	meta.setLore(lore);
	    }
	    if(ench != null) {
	    	meta.addEnchant(ench, 1, true);
	    }
	    if(itemModel > 0) {
		    meta.setCustomModelData(itemModel);
	    }
	    meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
	    meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
	    item.setItemMeta(meta);
		return item;
	}
	
	
	public abstract void onCreate();

	
	public abstract boolean onClick(InventoryClickEvent event);

	public void close() {
		onClose(player);
		player.closeInventory();	
	}
	abstract public void onClose(Player p);
	
}
