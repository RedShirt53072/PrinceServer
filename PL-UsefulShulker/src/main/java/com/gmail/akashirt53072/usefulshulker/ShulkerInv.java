package com.gmail.akashirt53072.usefulshulker;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.BlockStateMeta;

import com.gmail.akashirt53072.usefulshulker.data.InvIDType;
import com.gmail.akashirt53072.usefulshulker.data.PlayerNBT;

public class ShulkerInv {
	private Player player;
    public ShulkerInv(Player player) {
    	this.player = player;
    }
    public void open() {
    	PlayerInventory nowInv = player.getInventory();
    	ItemStack handItem = nowInv.getItemInMainHand();
    	BlockStateMeta handMeta = (BlockStateMeta)handItem.getItemMeta();
    	ShulkerBox box = (ShulkerBox)handMeta.getBlockState();
    	Inventory boxInv = box.getInventory();
    	Inventory inv = Bukkit.createInventory(null, 27, "シュルカーボックス");
		for(int index = 0;index < 27;index ++) {
			inv.setItem(index, boxInv.getItem(index));
		}
        player.openInventory(inv);
        new PlayerNBT(player).setInvID(InvIDType.SHULKERBOX);
        player.playSound(player.getLocation(), Sound.BLOCK_SHULKER_BOX_OPEN, 1,1);
    }
    public void close(Inventory inv) {
    	ItemStack handItem = player.getInventory().getItemInMainHand();
    	BlockStateMeta handMeta = (BlockStateMeta)handItem.getItemMeta();
    	ShulkerBox box = (ShulkerBox)handMeta.getBlockState();
    	Inventory boxInv = box.getInventory();
    	for(int index = 0;index < 27;index ++) {
    		boxInv.setItem(index, inv.getItem(index));
		}
    	handMeta.setBlockState(box);
    	handItem.setItemMeta(handMeta);
        new PlayerNBT(player).setInvID(InvIDType.NULLINV);
        player.playSound(player.getLocation(), Sound.BLOCK_SHULKER_BOX_CLOSE, 1,1);
    }
}
