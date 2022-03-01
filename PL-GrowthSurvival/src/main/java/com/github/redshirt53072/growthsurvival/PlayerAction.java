package com.github.redshirt53072.growthsurvival;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.Repairable;

import com.github.redshirt53072.growthapi.message.MessageManager;
import com.github.redshirt53072.growthapi.player.PlayerManager;

public final class PlayerAction implements Listener {
	GrowthSurvival plugin;
    public PlayerAction() {
    	this.plugin = GrowthSurvival.getInstance();
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
	    	Material type = clicked.getType();
	    	if(type.equals(Material.ANVIL) ||
	    			type.equals(Material.CHIPPED_ANVIL) ||
	    			type.equals(Material.DAMAGED_ANVIL)) {
	    		event.setCancelled(true);
	        	//open
	        	new ShulkerGui().open(player);
	        	return;
	    	}
	    	if(type.equals(Material.GRINDSTONE)){
				
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
    	if(type.equals(Material.ANVIL) ||
    			type.equals(Material.CHIPPED_ANVIL) ||
    			type.equals(Material.DAMAGED_ANVIL)) {
    		event.setCancelled(true);
        	//open
        	new ShulkerGui().open(player);
        	return false;
        }
    	if(type.equals(Material.GRINDSTONE)){
			
    		return false;
		}
    	return true;
    }
}