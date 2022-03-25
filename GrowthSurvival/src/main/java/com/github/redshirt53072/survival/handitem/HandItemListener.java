package com.github.redshirt53072.survival.handitem;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public interface HandItemListener {
	/**
	 * 
	 * @param event
	 * @return 続けるか
	 */
	public boolean onClick(PlayerInteractEvent event);
	/**
	 * 
	 * @param player
	 */
	public boolean onUpdate(Player player,ItemStack item);	
	
}
