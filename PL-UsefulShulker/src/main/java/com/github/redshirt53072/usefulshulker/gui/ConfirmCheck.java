package com.github.redshirt53072.usefulshulker.gui;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.InventoryClickEvent;

import com.github.redshirt53072.growthapi.gui.ChildGui;
import com.github.redshirt53072.growthapi.item.ItemBuilder;
import com.github.redshirt53072.growthapi.message.MessageManager;
import com.github.redshirt53072.growthapi.money.MoneyManager;
import com.github.redshirt53072.usefulshulker.UsefulShulker;
import com.github.redshirt53072.usefulshulker.data.ECLock;

public class ConfirmCheck  extends ChildGui{
	private int payPage;
	private int cost;
    public ConfirmCheck(int payPage) {
    	super(UsefulShulker.getInstance());
    	this.payPage = payPage;
    }
    
    @Override
    public boolean onClick(InventoryClickEvent event){
    	int slot = event.getRawSlot();
    	event.setCancelled(true);
    	
    	if(slot == 11) {
    		int nowEme = (int) MoneyManager.get(player);
    		if(nowEme > cost) {
    			player.playSound(player.getLocation(), Sound.BLOCK_BEACON_POWER_SELECT, 1,1);
    			ECLock.setPage(player,payPage);
    			MoneyManager.remove(player, cost);
    			MessageManager.sendImportant(ChatColor.GOLD.toString() + cost + "Ɇ" + ChatColor.WHITE + "を使用してエンダーチェストが" + payPage + "ページまで拡張されました。", player);
    			close();
        		return true;
    		}
    		player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1,(float)0.5);
    		MessageManager.sendImportant("Ɇが足りないため、エンダーチェストの拡張ができません。 所持:" + ChatColor.GOLD + nowEme + "Ɇ" + ChatColor.WHITE + "/必要:" + ChatColor.GOLD +  cost + "Ɇ", player);
    		close();
        	return true;
    	}
    	if(slot == 15) {
    		player.playSound(player.getLocation(), Sound.BLOCK_BEACON_DEACTIVATE, 1,1);
    		close();
    		return true;
    	}
    	return true;
    }
    
	@Override
	public void onRegister() {
    	inv = Bukkit.createInventory(null, 27, payPage + "ページまで拡張しますか？");
    	
		cost = EnderGui.calcCost(payPage, player);

		inv.setItem(11, new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setName(ChatColor.GOLD.toString() + cost + "Ɇ" + ChatColor.WHITE + "支払って確定する").setModelData(3400).build());
		inv.setItem(15, new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setName(ChatColor.WHITE + "キャンセルする").setModelData(3401).build());
		
		player.openInventory(inv);
		player.playSound(player.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 1,1);
	}
	@Override
	public void onClose() {
    	player.playSound(player.getLocation(), Sound.BLOCK_BEACON_DEACTIVATE, 1,1);
    	super.asyncReturn();
	}
}