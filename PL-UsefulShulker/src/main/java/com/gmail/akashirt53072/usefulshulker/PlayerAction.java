package com.gmail.akashirt53072.usefulshulker;
//playerの行動をトリガーに様々な処理クラスへと取り次ぐクラス
import org.bukkit.event.Listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.gmail.akashirt53072.usefulshulker.data.InvIDType;
import com.gmail.akashirt53072.usefulshulker.data.PlayerNBT;

import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
			new EnderInv(player).open();
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
    			new EnderInv(player).open();
    	   		return false;
    		}
    		return true;
    	}
    	event.setCancelled(true);
    	//open
    	new ShulkerInv(player).open();
    	return false;
    }
    
    @EventHandler
    public void onInventoryClose(final InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        InvIDType type = new PlayerNBT(player).getInvID();
    	if(type.equals(InvIDType.SHULKERBOX)) {
            //close
        	new ShulkerInv(player).close(event.getInventory());
        	return;
    	}
    	if(type.equals(InvIDType.ENDERCHEST)) {
    		//close
			new EnderInv(player).close(event.getInventory());;
    		return;
    	}
    	if(type.equals(InvIDType.CONFIRMCHECK)) {
    		new ConfirmCheck(player).close();
    		return;
    	}
    }
    @EventHandler
    public void onInventoryClick(final InventoryClickEvent event) {
    	Player player = (Player)event.getWhoClicked();
    	InvIDType type = new PlayerNBT(player).getInvID();
    	if(type.equals(InvIDType.ENDERCHEST)) {
    		//enderchest page change
    		new EnderInv(player).onClick(event);
    		
    		return;
    	}
    	if(type.equals(InvIDType.CONFIRMCHECK)) {
    		new ConfirmCheck(player).onClick(event);
    		return;
    	}
    	if(!type.equals(InvIDType.SHULKERBOX)) {
    		return;
    	}
        ItemStack item = event.getCurrentItem();
        if(item == null) {
    		return;
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
    		return;
    	}
    	event.setCancelled(true);
    }
    @EventHandler
    public void onInventoryClick(final AsyncPlayerChatEvent event) {
    	String text = event.getMessage();
    	if(text.matches("colorcode.*")) {
    		event.setCancelled(true);
    		for(Player p : Bukkit.getOnlinePlayers()){
    			text.replaceAll("colorcode", "");
    			p.sendMessage(ChatColor.YELLOW + text);
    		}
    		return;
    	}
    	if(text.matches("kyoto.*")) {
    		event.setCancelled(true);
    		for(Player p : Bukkit.getOnlinePlayers()){
    			p.sendMessage("そうだ");
    			p.sendMessage("京都、");
    			p.sendMessage("いこう。");	
    		}
    		return;
    	}
    }
}