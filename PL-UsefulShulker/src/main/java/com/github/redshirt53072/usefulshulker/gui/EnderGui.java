package com.github.redshirt53072.usefulshulker.gui;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import com.github.redshirt53072.growthapi.gui.Gui;
import com.github.redshirt53072.growthapi.item.ItemBuilder;
import com.github.redshirt53072.growthapi.money.MoneyManager;
import com.github.redshirt53072.growthapi.player.PlayerManager;
import com.github.redshirt53072.usefulshulker.UsefulShulker;
import com.github.redshirt53072.usefulshulker.data.ECLock;
import com.github.redshirt53072.usefulshulker.data.EnderChest;

public class EnderGui extends Gui{
	private int openedPage = 1;
	
	public EnderGui() {
    	super(UsefulShulker.getInstance());
    }
    
	static public int calcCost(int slot,Player player) {
		int unlock = ECLock.getPage(player);
		int totalCost = 0;
		
		for(int i = 1;unlock + i <= slot;i++) {
			totalCost += ((int)Math.pow(2,unlock + i)) * 500;	
		}
		
		return totalCost;
	}
	
	private void load() {
		//ページ
    	int unlockedPage = ECLock.getPage(player);
    	for(int index = 1;index < 10;index ++) {
			int model = 3010 + index;
			String text = ChatColor.WHITE.toString() + index + "ページ";
			ArrayList<String> lore = new ArrayList<String>();
			if(openedPage == index) {
				model -= 10;
				text = text + ChatColor.YELLOW + "<選択中>";
			}
			if(unlockedPage < index) {
				model += 10;
				text = text + ChatColor.RED + "<未開放>";
				lore.add(ChatColor.WHITE + "合計" + ChatColor.GOLD + calcCost(index,player) + "Ɇ" + ChatColor.WHITE + "でこのスロットを開放できます。");
			}
			inv.setItem(index + 26, new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setLore(lore).setModelData(model).setName(text).build());
		}
	}
	
	
    @Override
    public boolean onClick(InventoryClickEvent event){
    	if(PlayerManager.isAsyncLocked(player,"ec")) {
    		return true;
    	}
    	int slot = event.getRawSlot();
    	if(slot > 35||slot < 27) {
    		return false;
    	}
    	
    	slot -= 26;
    	if(openedPage == slot) {
    		player.playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, 1,1);
    		return true;
    	}
    	
    	if(ECLock.getPage(player) < slot) {
    		int nowEme = (int) MoneyManager.get(player);
    		if(nowEme > calcCost(slot,player)) {
    			new ConfirmCheck(slot).open(this);
    			return true;
    		}
            player.playSound(player.getLocation(), Sound.BLOCK_CHEST_LOCKED, 1,1);
    		return true;
    	}
    	
    	new EnderChest().pageChange(player, inv, openedPage,slot);
    	
    	openedPage = slot;
    	load();
    	
        player.playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, 1,1);
        return true;
    }
    @Override
	public void onReturn() {
    	load();
    	super.onReturn();
    }
    
	@Override
	public void onRegister() {
    	inv = Bukkit.createInventory(null, 36, "エンダーチェスト");
    	
    	new EnderChest().openLoad(player, inv, openedPage);
    	
    	load();
    	
        player.playSound(player.getLocation(), Sound.BLOCK_ENDER_CHEST_OPEN, 1,1);
        player.openInventory(inv);
	}
	@Override
	public void onClose() {
		if(!PlayerManager.isAsyncLocked(player, "ec")) {
	    	new EnderChest().closeSave(player, inv, openedPage);
		}
    	player.playSound(player.getLocation(), Sound.BLOCK_ENDER_CHEST_CLOSE, 1,1);
	}
}
