package com.github.redshirt53072.shulker.temp;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;

import com.github.redshirt53072.api.message.LogManager;
import com.github.redshirt53072.api.player.InitListener;
import com.github.redshirt53072.shulker.UsefulShulker;
import com.github.redshirt53072.shulker.data.EnderChest;

public class MoveEnderChest implements InitListener{
	@Override
	public void onInit(Player player) {
		Bukkit.getScheduler().runTask(UsefulShulker.getInstance(), new Runnable() {
    		@Override
    		public void run() {
    			Inventory enderChest = player.getEnderChest();
    			boolean moved = false;
    			for(int slot = 0;slot < 9;slot++) {
    				ItemStack nowItem = enderChest.getItem(slot);
    	    		if(nowItem != null) {
    	    			if(nowItem.getType().equals(Material.SHULKER_BOX)) {
    	        			BlockStateMeta pageMeta = (BlockStateMeta)nowItem.getItemMeta();
    	                	ShulkerBox box = (ShulkerBox)pageMeta.getBlockState();
    	                	Inventory boxInv = box.getInventory();
    	                	
    	                	new EnderChest().closeSave(player, boxInv, slot + 1);
    	    				
    	        			enderChest.clear(slot);	
    	        	    	moved = true;
    	            	}
    	        	}
    	        }
    			if(moved) {
    				LogManager.logInfo(player.getName() + "のエンダーチェストのデータベース移行を行いました。", UsefulShulker.getInstance(), Level.WARNING);	
    			}
    			
    		}
    	});
	}
	
}
