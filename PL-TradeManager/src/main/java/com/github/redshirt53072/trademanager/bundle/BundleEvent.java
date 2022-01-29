package com.github.redshirt53072.trademanager.bundle;

import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.github.redshirt53072.trademanager.TradeManager;



public final class BundleEvent implements Listener {
	TradeManager plugin;
    public BundleEvent() {
    	this.plugin = TradeManager.getInstance();
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
    	if(!item.getType().equals(Material.COMMAND_BLOCK)) {
    		return true;
    	}
    	if(!item.getItemMeta().hasCustomModelData()) {
    		return true;
    	}
    	if(item.getItemMeta().getCustomModelData() == 3001) {
    		return true;
    	}
    	Bundle.openGui(player,item);
    	event.setCancelled(true);
    	return false;
    }
    
}