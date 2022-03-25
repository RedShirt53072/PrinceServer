package com.github.redshirt53072.survival.handitem;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;


public class HandItemManager {
	private static List<PlayerActionSet> players = new ArrayList<PlayerActionSet>();
	private static Set<HandItemListener> actionList = new HashSet<HandItemListener>();
	
	public static void register(HandItemListener lis){
		actionList.add(lis);
	}
	
	public static void update(Player player,ItemStack item){
		for(PlayerActionSet pas : players){
			if(pas.matchPlayer(player)){
				pas.update(item);
				return;
			}
		}
	}
	
	public static boolean onClick(Player player,PlayerInteractEvent event){
		ItemStack item = event.getItem();
		if(item == null) {
			return false;
		}
		
		for(PlayerActionSet pas : players){
			if(pas.matchPlayer(player)){
				return pas.onClick(event,item);
			}
		}
		PlayerActionSet newPlayer = new PlayerActionSet(player,item);
		players.add(newPlayer);
		return newPlayer.onClick(event, item);
	}
	
	private static class PlayerActionSet{
		private Player player;
		private int itemHush = 0;
		private Set<HandItemListener> actions = new HashSet<HandItemListener>();
		public PlayerActionSet(Player player,ItemStack item){
			itemHush = item.hashCode();
			this.player = player;
			for(HandItemListener hil : actionList) {
				if(hil.onUpdate(player,item)) {
					actions.add(hil);
				}
			}
		}
		
		public boolean matchPlayer(Player terget) {
			return player.getUniqueId().equals(terget.getUniqueId());
		}
		
		public void update(ItemStack item) {
			int newHush = item.hashCode();
			if(newHush != itemHush){
				for(HandItemListener hil : actionList) {
    				if(hil.onUpdate(player,item)) {
    					actions.add(hil);
    				}else {
    					actions.remove(hil);
    				}
    			}
				itemHush = newHush;
			}
		}
		
		public boolean onClick(PlayerInteractEvent event,ItemStack item) {
			update(item);
			for(HandItemListener hil : actions) {
				if(!hil.onClick(event)) {
					return true;
				}
			}
			return false;
		}
	}
}
