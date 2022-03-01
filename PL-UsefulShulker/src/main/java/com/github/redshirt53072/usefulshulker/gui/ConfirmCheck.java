package com.github.redshirt53072.usefulshulker.gui;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.InventoryClickEvent;

import com.github.redshirt53072.growthapi.gui.ChildGui;
import com.github.redshirt53072.growthapi.item.ItemBuilder;
import com.github.redshirt53072.growthapi.message.MessageManager;
import com.github.redshirt53072.growthapi.message.TextBuilder;
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
    			MessageManager.sendImportant(new TextBuilder(ChatColor.WHITE)
    					.addMoneyText(cost)
    					.addText("を使用してエンダーチェストが")
    					.addNumText(payPage, "ページ")
    					.addText("まで拡張されました。")
    					.build(), player);
    			close();
        		return true;
    		}
    		player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1,(float)0.5);
    		MessageManager.sendImportant(new TextBuilder(ChatColor.WHITE).addText("Ɇが足りないため、エンダーチェストの拡張ができません。 所持:").addMoneyText(nowEme).addText("/必要:").addMoneyText(cost).build(), player);
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
    	inv = Bukkit.createInventory(null, 27, TextBuilder.plus(String.valueOf(payPage),"ページまで拡張しますか？"));
    	
		cost = EnderGui.calcCost(payPage, player);

		inv.setItem(11, new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setName(new TextBuilder(ChatColor.WHITE)
				.addMoneyText(cost)
				.addText("支払って確定する")
				.build()).setModelData(3400).build());
		inv.setItem(15, new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setName(TextBuilder.quickBuild(ChatColor.WHITE,"キャンセルする")).setModelData(3401).build());
		
		player.openInventory(inv);
		player.playSound(player.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 1,1);
	}
	@Override
	public void onClose() {
    	player.playSound(player.getLocation(), Sound.BLOCK_BEACON_DEACTIVATE, 1,1);
    	super.asyncReturn();
	}
}