package com.github.redshirt53072.trade.bundle;

import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.github.redshirt53072.api.gui.GuiManager;
import com.github.redshirt53072.api.item.ItemTag;
import com.github.redshirt53072.trade.TradeManager;



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
        	
        	Player player = event.getPlayer();
        	if(GuiManager.getGui(player) != null) {
        		return;
        	}
        	PlayerInventory inv = player.getInventory();
        	ItemStack item = inv.getItemInMainHand();
        	if(item == null) {
        		return;
        	}
        	if(!item.getType().equals(Material.COMMAND_BLOCK)) {
        		return;
        	}
        	if(!item.getItemMeta().hasCustomModelData()) {
        		return;
        	}
        	if(item.getItemMeta().getCustomModelData() != 3001) {
        		return;
        	}
        	ItemTag tag = new Bundle(item).getType();
        	if(tag.equals(ItemTag.NONE)) {
        		SpecialItem.doRandom(player, item);
        		return;
        	}
        	Bundle.openGui(player,item.clone());
        	event.setCancelled(true);
        	return;
    	}
   	}
    
}