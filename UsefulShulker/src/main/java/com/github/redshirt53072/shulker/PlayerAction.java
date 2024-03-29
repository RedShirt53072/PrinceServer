package com.github.redshirt53072.shulker;
//playerの行動をトリガーに様々な処理クラスへと取り次ぐクラス
import org.bukkit.event.Listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.github.redshirt53072.api.gui.GuiManager;
import com.github.redshirt53072.api.message.MessageManager;
import com.github.redshirt53072.api.message.SoundManager;
import com.github.redshirt53072.api.player.PlayerManager;
import com.github.redshirt53072.shulker.gui.EnderGui;
import com.github.redshirt53072.shulker.gui.OpEnderGui;
import com.github.redshirt53072.shulker.gui.ShulkerGui;

import org.bukkit.event.block.Action;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
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
    	Player player = event.getPlayer();
    	if(GuiManager.getGui(player) != null) {
    		return;
    	}
        Action action = event.getAction();
        if (action.equals(Action.RIGHT_CLICK_BLOCK) || action.equals(Action.RIGHT_CLICK_AIR)){
        	boolean conti = checkItemClick(event);
        	if(!conti) {
        		return;
        	}
    	}
        if (action.equals(Action.RIGHT_CLICK_BLOCK)){
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
	    	if(player.getGameMode().equals(GameMode.CREATIVE)) {
	    		SoundManager.sendCancel(player);
	    		return;
	    	}
	    	if(PlayerManager.isAsyncLocked(player,"ec")) {
	    		return;
	    	}
	    	if(OpEnderGui.isOpenedPlayer(player)) {
	    		MessageManager.sendSpecial("現在メンテナンスのため、一時的にエンダーチェストが開けなくなっています。", player);
	    		MessageManager.sendSpecial("数分待ってもこのメッセージが表示される場合は運営にご報告ください。", player);
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
    	ItemStack item = event.getItem();
    	if(item == null) {
    		return true;
    	}
    	if(GuiManager.getGui(player) != null) {
    		return true;
    	}
    	
    	Material type = item.getType();
    	if(!type.equals(Material.SHULKER_BOX) &&
    			!type.equals(Material.BLACK_SHULKER_BOX) &&
    			!type.equals(Material.BLUE_SHULKER_BOX) &&
    			!type.equals(Material.BROWN_SHULKER_BOX) &&
    			!type.equals(Material.CYAN_SHULKER_BOX) &&
    			!type.equals(Material.GRAY_SHULKER_BOX) &&
    			!type.equals(Material.GREEN_SHULKER_BOX) &&
    			!type.equals(Material.LIGHT_BLUE_SHULKER_BOX) &&
    			!type.equals(Material.LIGHT_GRAY_SHULKER_BOX) &&
    			!type.equals(Material.LIME_SHULKER_BOX) &&
    			!type.equals(Material.MAGENTA_SHULKER_BOX) &&
    			!type.equals(Material.ORANGE_SHULKER_BOX) &&
    			!type.equals(Material.PINK_SHULKER_BOX) &&
    			!type.equals(Material.PURPLE_SHULKER_BOX) &&
    			!type.equals(Material.RED_SHULKER_BOX) &&
    			!type.equals(Material.WHITE_SHULKER_BOX) &&
    			!type.equals(Material.YELLOW_SHULKER_BOX)) {
    		if(type.equals(Material.ENDER_CHEST)){
    	    	event.setCancelled(true);
    	    	if(!player.hasPermission(Bukkit.getPluginManager().getPermission("enderchest.open"))) {
    	    		return true;
    	    	}
    	    	if(player.getGameMode().equals(GameMode.CREATIVE)) {
    	    		SoundManager.sendCancel(player);
    	    		return true;
    	    	}
    	    	if(PlayerManager.isAsyncLocked(player,"ec")) {
    	    		return true;
    	    	}
    	    	
    	    	if(OpEnderGui.isOpenedPlayer(player)) {
    	    		MessageManager.sendSpecial("現在メンテナンスのため、一時的にエンダーチェストが開けなくなっています。", player);
    	    		MessageManager.sendSpecial("数分待ってもこのメッセージが表示される場合は運営にご報告ください。", player);
    	    		return true;
    	    	}
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