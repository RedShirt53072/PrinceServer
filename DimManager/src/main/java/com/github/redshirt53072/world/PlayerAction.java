package com.github.redshirt53072.world;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.github.redshirt53072.api.gui.GuiManager;
import com.github.redshirt53072.world.gui.DimGui;

public final class PlayerAction implements Listener {
	DimManager plugin;
    public PlayerAction() {
    	this.plugin = DimManager.getInstance();
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
    	Material type = item.getType();
    	if(type.equals(Material.COMPASS)) {
    		event.setCancelled(true);
    		//open
        	new DimGui().open(player);
	
    		return false;
        }
    	return true;
    }
}