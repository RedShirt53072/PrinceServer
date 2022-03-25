package com.github.redshirt53072.survival.gui;


import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.AnvilInventory;

import com.github.redshirt53072.api.gui.Gui;
import com.github.redshirt53072.api.message.SoundManager;
import com.github.redshirt53072.survival.GrowthSurvival;

//ほんとに金床。名前をつける機能のみ

public class AnvilGui extends Gui{
	private AnvilInventory anvil;
	
	public AnvilGui() {
	    super(GrowthSurvival.getInstance());	    
	}
	
	@Override
	public void onRegister() {
		inv = player.getOpenInventory().getTopInventory();
		anvil = (AnvilInventory)inv;
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
				if(inv.getItem(0) == null) {
	    			return false;
				}
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
