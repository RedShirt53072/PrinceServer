package com.github.redshirt53072.usefulshulker.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.ShulkerBox;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.BlockStateMeta;

import com.github.redshirt53072.growthapi.gui.Gui;
import com.github.redshirt53072.usefulshulker.UsefulShulker;

public class ShulkerGui extends Gui{
    public ShulkerGui() {
    	super(UsefulShulker.getInstance());
    }
    
    
	@Override
	public void onRegister() {
    	inv = Bukkit.createInventory(null, 27, "シュルカーボックス");
    	PlayerInventory nowInv = player.getInventory();
    	ItemStack handItem = nowInv.getItemInMainHand();
    	BlockStateMeta handMeta = (BlockStateMeta)handItem.getItemMeta();
    	ShulkerBox box = (ShulkerBox)handMeta.getBlockState();
    	Inventory boxInv = box.getInventory();
		for(int index = 0;index < 27;index ++) {
			inv.setItem(index, boxInv.getItem(index));
		}
		player.openInventory(inv);

    	player.playSound(player.getLocation(), Sound.BLOCK_SHULKER_BOX_OPEN, 1,1);
    	
	}
	
	@Override
	public boolean onClick(InventoryClickEvent event) {
		ItemStack item = event.getCurrentItem();
        if(item == null) {
    		return false;
    	}
        if(!item.getType().equals(Material.SHULKER_BOX) &&
    			!item.getType().equals(Material.BLACK_SHULKER_BOX) &&
    			!item.getType().equals(Material.BLUE_SHULKER_BOX) &&
    			!item.getType().equals(Material.BROWN_SHULKER_BOX) &&
    			!item.getType().equals(Material.CYAN_SHULKER_BOX) &&
    			!item.getType().equals(Material.GRAY_SHULKER_BOX) &&
    			!item.getType().equals(Material.GREEN_SHULKER_BOX) &&
    			!item.getType().equals(Material.LIGHT_BLUE_SHULKER_BOX) &&
    			!item.getType().equals(Material.LIGHT_GRAY_SHULKER_BOX) &&
    			!item.getType().equals(Material.LIME_SHULKER_BOX) &&
    			!item.getType().equals(Material.MAGENTA_SHULKER_BOX) &&
    			!item.getType().equals(Material.ORANGE_SHULKER_BOX) &&
    			!item.getType().equals(Material.PINK_SHULKER_BOX) &&
    			!item.getType().equals(Material.PURPLE_SHULKER_BOX) &&
    			!item.getType().equals(Material.RED_SHULKER_BOX) &&
    			!item.getType().equals(Material.WHITE_SHULKER_BOX) &&
    			!item.getType().equals(Material.YELLOW_SHULKER_BOX)) {
    		return false;
    	}
    	return true;
	}
	
	@Override
	public void onClose() {
		ItemStack handItem = player.getInventory().getItemInMainHand();
    	BlockStateMeta handMeta = (BlockStateMeta)handItem.getItemMeta();
    	ShulkerBox box = (ShulkerBox)handMeta.getBlockState();
    	Inventory boxInv = box.getInventory();
    	for(int index = 0;index < 27;index ++) {
    		boxInv.setItem(index, inv.getItem(index));
		}
    	handMeta.setBlockState(box);
    	handItem.setItemMeta(handMeta);
        player.playSound(player.getLocation(), Sound.BLOCK_SHULKER_BOX_CLOSE, 1,1);
	}
}
