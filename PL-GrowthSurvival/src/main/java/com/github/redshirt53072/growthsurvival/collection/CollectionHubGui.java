package com.github.redshirt53072.growthsurvival.collection;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.event.inventory.InventoryClickEvent;

import com.github.redshirt53072.growthapi.gui.Gui;
import com.github.redshirt53072.growthapi.item.ItemBuilder;
import com.github.redshirt53072.growthapi.message.TextBuilder;
import com.github.redshirt53072.growthsurvival.GrowthSurvival;

public class CollectionHubGui  extends Gui{
	private List<ItemCollection> list;
	
	public CollectionHubGui() {
    	super(GrowthSurvival.getInstance());
    }
    
    @Override
    public boolean onClick(InventoryClickEvent event){
    	int slot = event.getRawSlot();
    	
    	if(slot > 17 ||slot < 0) {
    		return false;
    	}
    	if(slot > list.size() - 1) {
    		return true;
    	}
    	
    	event.setCancelled(true);
    	
    	close();
    	
    	list.get(slot).openGui(player);
        return true;
    }
    
	@Override
	public void onRegister() {
    	inv = Bukkit.createInventory(null, 18, "図鑑-どの図鑑を開きますか?");
    	list = CollectionManager.getColList();
    	
    	for(int i = 0;i < 18;i++) {
    		if(i < list.size()) {
        		inv.setItem(i, new ItemBuilder(list.get(i).getIconItem()).setName(TextBuilder.quickBuild(ChatColor.WHITE, list.get(i).getName(),"を開く")).build());	
        		continue;
    		}
    		super.setEmptyItem(i);
    	}
    	
    	player.playSound(player.getLocation(), Sound.BLOCK_ENDER_CHEST_OPEN, 1,1);
        player.openInventory(inv);
	}
	@Override
	public void onClose() {
        player.playSound(player.getLocation(), Sound.BLOCK_ENDER_CHEST_CLOSE, 1,1);
	}	

}
