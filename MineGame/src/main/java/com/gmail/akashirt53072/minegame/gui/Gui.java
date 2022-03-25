package com.gmail.akashirt53072.minegame.gui;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.gmail.akashirt53072.minegame.Main;
import com.gmail.akashirt53072.minegame.nbt.NBTGui;



public abstract class Gui {
	protected ArrayList<Player> players;
	protected Main plugin;
	protected Inventory inv;
	protected GuiID type;
	protected boolean isPrivate;
	
	public Gui(Main plugin,boolean isPrivate,GuiID type) {
	    this.plugin = plugin;
	    this.players = new ArrayList<Player>();
		this.isPrivate = isPrivate;
		this.type = type;
	    onCreate();
	}
	
	public ArrayList<Player> getPlayers() {
		return players;
	}

	public GuiID getType() {
		return type;
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
	
	public void addPlayer(Player player) {
		players.add(player);
		player.openInventory(inv);
		new NBTGui(plugin,player).setID(type);
		
	}

	
	public abstract boolean onClick(int slot,ItemStack cursor,Player player);

	public void close(Player p) {
		onClose(p);
		p.closeInventory();	
		
	}
	public void onClose(Player p) {
		new NBTGui(plugin,p).setID(GuiID.NONE);	
		players.remove(p);
		if(isPrivate) {
			new GuiManager(plugin).remove(this);
		}else if(players.isEmpty()){
			new GuiManager(plugin).remove(this);	
		}
	}
	
	abstract public void onRemove();
}
