package com.github.redshirt53072.usefulshulker.gui;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.InventoryClickEvent;

import com.github.redshirt53072.growthapi.gui.Gui;
import com.github.redshirt53072.growthapi.message.MessageManager;
import com.github.redshirt53072.growthapi.money.MoneyManager;
import com.github.redshirt53072.usefulshulker.UsefulShulker;
import com.github.redshirt53072.usefulshulker.data.PlayerECNBT;

public class ConfirmCheck  extends Gui{
	private int payPage;
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
    		int cost = EnderGui.calcCost(payPage, player);
    		if(nowEme > cost) {
    			player.playSound(player.getLocation(), Sound.BLOCK_BEACON_POWER_SELECT, 1,1);
    			new PlayerECNBT(player).setUnlockedPage(payPage + 1);
    			MoneyManager.remove(player, cost);
    			MessageManager.sendImportant(ChatColor.GOLD.toString() + cost + "Ɇ" + ChatColor.WHITE + "を使用してエンダーチェストが" + (payPage + 1) + "ページまで拡張されました。", player);
    			End();
        		return true;
    		}
    		player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1,(float)0.5);
    		MessageManager.sendImportant("Ɇが足りないため、エンダーチェストの拡張ができません。 所持:" + ChatColor.GOLD + nowEme + "Ɇ" + ChatColor.WHITE + "/必要:" + ChatColor.GOLD +  cost + "Ɇ", player);
    		End();
        	return true;
    	}
    	if(slot == 15) {
    		player.playSound(player.getLocation(), Sound.BLOCK_BEACON_DEACTIVATE, 1,1);
    		End();
    		return true;
    	}
    	return true;
    }
    
    private void End() {
    	close();
    	new EnderGui().registerPlayer(player);
    }
    
	@Override
	public void onRegister() {
    	inv = Bukkit.createInventory(null, 27, (payPage +1) + "ページまで拡張しますか？");
    	
		int cost = EnderGui.calcCost(payPage, player);

		inv.setItem(11, createItem(Material.LIME_WOOL,ChatColor.GOLD.toString() + cost + "Ɇ" + ChatColor.WHITE + "支払って確定する",null,1,null,0));
		inv.setItem(15, createItem(Material.RED_WOOL,ChatColor.WHITE + "キャンセルする",null,1,null,0));
		player.openInventory(inv);
		player.playSound(player.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 1,1);
	}
	@Override
	public void onClose() {
    	player.playSound(player.getLocation(), Sound.BLOCK_BEACON_DEACTIVATE, 1,1);
	}
}
