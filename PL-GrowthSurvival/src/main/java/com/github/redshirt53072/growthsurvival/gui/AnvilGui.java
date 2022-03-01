package com.github.redshirt53072.growthsurvival.gui;


import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.AnvilInventory;

import com.github.redshirt53072.growthapi.gui.Gui;
import com.github.redshirt53072.growthapi.message.SoundManager;
import com.github.redshirt53072.growthsurvival.GrowthSurvival;

//ほんとに金床。名前をつける機能のみ

public class AnvilGui extends Gui{
	private AnvilInventory anvil;
	
	public AnvilGui() {
	    super(GrowthSurvival.getInstance());	    
	}
	
	@Override
	public void onRegister() {
		anvil = (AnvilInventory) Bukkit.createInventory(null,InventoryType.ANVIL , "アイテム名を編集する");
		inv = anvil;
		player.playSound(player.getLocation(), Sound.BLOCK_ENDER_CHEST_OPEN, 1,1);
		player.openInventory(inv);
	}
	
	@Override
	public void onClose() {
		player.playSound(player.getLocation(), Sound.BLOCK_ENDER_CHEST_CLOSE, 1,1);
	}
	
	@Override
	public boolean onClick(InventoryClickEvent event) {
		int slot = event.getRawSlot();
		if(slot > 2) {
			ClickType type = event.getClick();
			if(type.equals(ClickType.SHIFT_LEFT) ||type.equals(ClickType.SHIFT_RIGHT)) {
				SoundManager.sendCancel(player);
				return true;
			}
			rendering();
			return false;
		}
		
		if(slot == 0) {
			rendering();
			return false;
		}
		if(slot == 1) {
			//無条件でキャンセル
			SoundManager.sendCancel(player);
			return true;
		}
		if(slot == 2) {
			return false;
		}
		
		//その他無効な場所のクリック
		return false;
	}
	
	private void rendering() {
		Bukkit.getScheduler().runTask(GrowthSurvival.getInstance(), new Runnable() {
    		@Override
    		public void run() {
    			if(inv.getItem(0) != null) {
    				anvil.setRepairCost(1);
    			}
    		}
		});
	}
}
