package com.github.redshirt53072.usefulshulker;
//playerの行動をトリガーに様々な処理クラスへと取り次ぐクラス
import org.bukkit.event.Listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.github.redshirt53072.usefulshulker.gui.EnderGui;
import com.github.redshirt53072.usefulshulker.gui.ShulkerGui;

import org.bukkit.event.block.Action;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public final class PlayerAction implements Listener {
	UsefulShulker plugin;
    public PlayerAction() {
    	this.plugin = UsefulShulker.getInstance();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    //左右クリック
    @EventHandler(priority = EventPriority.NORMAL)
    public void mouseClick(PlayerInteractEvent event) {
        Action action = event.getAction();
        if (action.equals(Action.RIGHT_CLICK_BLOCK) || action.equals(Action.RIGHT_CLICK_AIR)){
        	boolean conti = checkItemClick(event);
        	if(!conti) {
        		return;
        	}
    	}
        if (action.equals(Action.RIGHT_CLICK_BLOCK)){
	    	Player player = event.getPlayer();
	    	Block clicked = event.getClickedBlock();
	    	if(clicked == null) {
	    		return;
	    	}
	    	if(!clicked.getType().equals(Material.ENDER_CHEST)) {
	    		return;
	    	}
			event.setCancelled(true);
	    	if(!player.hasPermission(Bukkit.getPluginManager().getPermission("enderchest.open"))) {
	    		return;
	    	}
	    	
			//open
			new EnderGui().open(player);
        }
   	}
    
    private boolean checkItemClick(PlayerInteractEvent event) {
    	Player player = event.getPlayer();
    	if(player.isSneaking()) {
    		return true;
    	}
    	PlayerInventory inv = player.getInventory();
    	ItemStack item = inv.getItemInMainHand();
    	if(item == null) {
    		return true;
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
    		if(item.getType().equals(Material.ENDER_CHEST)){
    	    	if(!player.hasPermission(Bukkit.getPluginManager().getPermission("enderchest.open"))) {
    	    		return true;
    	    	}
    			event.setCancelled(true);
    			
    			//open
    			new EnderGui().open(player);
    	   		return false;
    		}
    		return true;
    	}
    	event.setCancelled(true);
    	//open
    	new ShulkerGui().open(player);
    	return false;
    }
}