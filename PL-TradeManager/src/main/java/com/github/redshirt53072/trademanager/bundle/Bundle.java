package com.github.redshirt53072.trademanager.bundle;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.github.redshirt53072.growthapi.item.ItemNBTLoader;
import com.github.redshirt53072.growthapi.item.ItemTag;
import com.github.redshirt53072.trademanager.TradeManager;
import com.github.redshirt53072.trademanager.gui.BundleGui;

public class Bundle extends ItemNBTLoader {
	private static ItemStack savedItem;
	
	public Bundle(ItemStack box) {
    	super(box,TradeManager.getInstance());
    }
	
	public void setType(ItemTag type) {
    	writeString("type",type.toString());
    }
    
    public ItemTag getType() {
    	String type = readString("type");
    	ItemTag tag;
    	try{
    		tag = ItemTag.valueOf(type);
    	}catch(Exception ex) {
    		writeString("type","NONE");
    		return ItemTag.NONE;
    	}
    	return tag;
    }
    public static void openGui(Player p,ItemStack item) {
    	savedItem = item;
    	new BundleGui().registerPlayer(p);
	}
    
    public static ItemStack getSavedItem() {
    	return savedItem;
    }
    
    public static ItemStack getNewBox(ItemTag tag) {
    	ItemStack item = new ItemStack(Material.COMMAND_BLOCK);
    	ItemMeta meta = item.getItemMeta();
    	meta.setCustomModelData(3001);
    	meta.setDisplayName(ChatColor.WHITE + tag.getName() + "の詰め合わせ");
    	item.setItemMeta(meta);
    	new Bundle(item).setType(tag);
    	return item;
    }
    
}
