package com.github.redshirt53072.survival;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import com.github.redshirt53072.api.gui.GuiManager;
import com.github.redshirt53072.api.item.ItemUtil;
import com.github.redshirt53072.api.message.MessageManager;
import com.github.redshirt53072.api.message.SoundManager;
import com.github.redshirt53072.api.message.TextBuilder;
import com.github.redshirt53072.survival.ench.BrokenBuilder;
import com.github.redshirt53072.survival.ench.EnchManager;
import com.github.redshirt53072.survival.ench.ToolData;
import com.github.redshirt53072.survival.ench.ToolData.ToolGroupData;
import com.github.redshirt53072.survival.gui.AddEnchGui;
import com.github.redshirt53072.survival.gui.AnvilGui;
import com.github.redshirt53072.survival.gui.RepairGui;
import com.github.redshirt53072.survival.handitem.HandItemManager;

import net.md_5.bungee.api.chat.TranslatableComponent;

public final class PlayerAction implements Listener {
	GrowthSurvival plugin;
    public PlayerAction() {
    	this.plugin = GrowthSurvival.getInstance();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void damageItem(PlayerItemBreakEvent event) {
    	ItemStack item = event.getBrokenItem();
    	Material type = item.getType();
    	if(type.equals(Material.NETHERITE_AXE) ||
    			type.equals(Material.NETHERITE_HOE) ||
    			type.equals(Material.NETHERITE_BOOTS) ||
    			type.equals(Material.NETHERITE_CHESTPLATE) ||
    			type.equals(Material.NETHERITE_HELMET) ||
    			type.equals(Material.NETHERITE_LEGGINGS) ||
    			type.equals(Material.NETHERITE_PICKAXE) ||
    			type.equals(Material.NETHERITE_SHOVEL) ||
    			type.equals(Material.NETHERITE_SWORD)) {
    		ToolData td = EnchManager.getToolData(type);
			if(td == null) {
				return;
			}
			ToolGroupData tgd = td.getToolGroupData();
			if(tgd == null) {
				return;
			}
    		
    		ItemStack broken = new BrokenBuilder(item,tgd).build();
    		
    		
    		ItemUtil.giveItem(event.getPlayer(), broken);
    	}
    	
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
	    	Material type = clicked.getType();
	    	if(type.equals(Material.ANVIL) ||
	    			type.equals(Material.CHIPPED_ANVIL) ||
	    			type.equals(Material.DAMAGED_ANVIL)) {
	    		if(player.isSneaking()) {
	    			PlayerInventory inv = player.getInventory();
	    			
	    	    	if(ItemUtil.isNotAir(inv.getItemInOffHand()) || ItemUtil.isNotAir(inv.getItemInMainHand())) {
	    	    		SoundManager.sendCancel(player);
	    	    		MessageManager.sendImportant(new TextBuilder(ChatColor.WHITE)
	    	    				.addText("アイテム名編集モードは素手の状態で")
	    	    				.addClick("スニークしながら右クリック")
	    	    				.addText("すると開くことができます。").build(), player);
	    	    		return;
	    	    	}
	    	    	new AnvilGui().open(player);
	    			return;
	    		}
	    		event.setCancelled(true);
	    		
	        	//open
	        	new AddEnchGui().open(player);
	        	return;
	    	}
	    	if(type.equals(Material.GRINDSTONE)){
	    		event.setCancelled(true);
				new RepairGui().open(player);
	    		return;
			}
		}
        
   	}
    
    private boolean checkItemClick(PlayerInteractEvent event) {
    	Player player = event.getPlayer();
    	/*if(HandItemManager.onClick(player, event)) {
    		return false;
    	}*/
    	
    	if(player.isSneaking()) {
    		return true;
    	}
    	ItemStack item = event.getItem();
    	if(item == null) {
    		return true;
    	}
    	Material type = item.getType();
    	if(type.equals(Material.ANVIL) ||
    			type.equals(Material.CHIPPED_ANVIL) ||
    			type.equals(Material.DAMAGED_ANVIL)) {
    		event.setCancelled(true);
        	//open
        	new AddEnchGui().open(player);
        	return false;
        }
    	if(type.equals(Material.GRINDSTONE)){
    		event.setCancelled(true);
			new RepairGui().open(player);
    		return false;
		}
    	return true;
    }
}