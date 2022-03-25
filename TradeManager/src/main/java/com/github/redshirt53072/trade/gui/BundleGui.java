package com.github.redshirt53072.trade.gui;


import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import com.github.redshirt53072.api.gui.Gui;
import com.github.redshirt53072.api.item.ItemBuilder;
import com.github.redshirt53072.api.item.ItemTag;
import com.github.redshirt53072.api.item.ItemUtil;
import com.github.redshirt53072.api.message.TextBuilder;
import com.github.redshirt53072.trade.TradeManager;
import com.github.redshirt53072.trade.bundle.Bundle;

public class BundleGui extends Gui{
	private ItemStack item;
	private ItemTag tag;
	
	public BundleGui() {
    	super(TradeManager.getInstance());
    }
    
    @Override
    public boolean onClick(InventoryClickEvent event){
    	int slot = event.getRawSlot();
    	
    	if(slot > 26 ||slot < 0) {
    		return true;
    	}
    	if(slot >= tag.getItems().size()) {
    		return true;
    	}
    	ItemUtil.removeItem(player, item, item.getAmount());
    	Material mate = tag.getItems().get(slot);
    	if(!mate.equals(Material.AIR)) {
    		ItemStack nowItem = new ItemStack(mate);
    		ItemUtil.giveItems(player, nowItem, item.getAmount());
    	}
		close();
    	player.playSound(player.getLocation(), Sound.ITEM_BUNDLE_DROP_CONTENTS, 1,1);
    	return true;
    }
    
	@Override
	public void onRegister() {
    	inv = Bukkit.createInventory(null, 27, "どのアイテムを選びますか？");
    	
    	item = Bundle.getSavedItem();
    	tag = new Bundle(item).getType();
    	int amount = item.getAmount();
    	
		ArrayList<String> lore = new ArrayList<String>();
		lore.add(new TextBuilder(ChatColor.WHITE).addClick("クリック").addText("でこのアイテムを手に入れる").build());
    	
    	List<Material> items = tag.getItems();
    	for(int i = 0;i < 27;i++) {
    		if(i < items.size()) {
    			if(items.get(i).equals(Material.AIR)) {
    				continue;
    			}
        		inv.setItem(i, new ItemBuilder(items.get(i)).setAmount(amount).setLore(lore).build());
        		continue;
    		}
    		super.setEmptyItem(i);
    	}
    	
    	player.playSound(player.getLocation(), Sound.ITEM_BUNDLE_REMOVE_ONE, 1,1);
        player.openInventory(inv);
	}
	
	@Override
	public void onClose() {
        player.playSound(player.getLocation(), Sound.ITEM_BUNDLE_INSERT, 1,1);
	}
}
