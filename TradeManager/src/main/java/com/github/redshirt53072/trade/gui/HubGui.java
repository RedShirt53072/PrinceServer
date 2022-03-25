package com.github.redshirt53072.trade.gui;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.event.inventory.InventoryClickEvent;

import com.github.redshirt53072.api.gui.Gui;
import com.github.redshirt53072.api.item.ItemBuilder;
import com.github.redshirt53072.trade.TradeManager;
import com.github.redshirt53072.trade.data.VillagerManager.ProfData;

public class HubGui extends Gui{
	private ProfData[] profData;
	
	public HubGui() {
    	super(TradeManager.getInstance());
    }
    
    @Override
    public boolean onClick(InventoryClickEvent event){
    	int slot = event.getRawSlot();
    	
    	if(slot > 17 ||slot < 0) {
    		return false;
    	}
    	if(slot > profData.length - 1) {
    		return true;
    	}
    	
    	event.setCancelled(true);
    	
    	close();
    	
    	new TradeTableGui(profData[slot]).open(player);
        return true;
    }
    
	@Override
	public void onRegister() {
    	inv = Bukkit.createInventory(null, 18, "交易テーブル編集-職業選択");
    	profData = ProfData.values();
    	for(int i = 0;i < 18;i++) {
    		if(i < profData.length) {
        		inv.setItem(i, new ItemBuilder(profData[i].getIconItem()).setName(ChatColor.WHITE + profData[i].getName() + "の交易を編集する").build());	
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
