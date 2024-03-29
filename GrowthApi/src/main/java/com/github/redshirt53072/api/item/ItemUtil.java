package com.github.redshirt53072.api.item;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.github.redshirt53072.api.message.TextBuilder;

public class ItemUtil {
	public static int removeItem(Player p,ItemStack sampleItem,int maxRemove) {
		int removed = 0;
		PlayerInventory inv = p.getInventory();
		for(int i = 0;i < 37;i++) {
			ItemStack stack = null;
			if(i == 36) {
				stack = inv.getItemInOffHand();
			}else {
				stack = inv.getItem(i);
			}
			if(stack == null) {
				continue;
			}
			if(stack.isSimilar(sampleItem)){
				int amount = stack.getAmount();
				if(maxRemove > removed) {
					if(maxRemove >= amount + removed) {
						if(i == 36) {
							inv.setItemInOffHand(null);
						}else {
							inv.clear(i);
						}
						removed += amount;
						if(removed == maxRemove) {
							break;
						}
					}else {
						int toRemove =  maxRemove - removed;
						stack.setAmount(amount - toRemove);
						removed += toRemove;
						break;
					}
				}
			}
		}
		return removed;
	}
	public static int removeItem(Player p,ItemStack sampleItem) {
		int removed = 0;
		PlayerInventory inv = p.getInventory();
		for(int i = 0;i < 37;i++) {
			ItemStack stack = null;
			if(i == 36) {
				stack = inv.getItemInOffHand();
			}else {
				stack = inv.getItem(i);
			}
			if(stack == null) {
				continue;
			}
			if(stack.isSimilar(sampleItem)){
				int amount = stack.getAmount();
				if(i == 36) {
					inv.setItemInOffHand(null);
				}else {
					inv.clear(i);
				}
				removed += amount;
			}
		}
		return removed;
	}
	
	public static int countItem(Player p,ItemStack sampleItem) {
		int count = 0;
		PlayerInventory inv = p.getInventory();
		for(int i = 0;i < 37;i++) {
			ItemStack stack = null;
			if(i == 36) {
				stack = inv.getItemInOffHand();
			}else {
				stack = inv.getItem(i);
			}
			if(stack == null) {
				continue;
			}
			if(stack.isSimilar(sampleItem)){
				int amount = stack.getAmount();
				count += amount;
			}
		}
		return count;
	}
	
	public static void giveItem(Player p,ItemStack item) {
		giveItems(p,item,item.getAmount());
	}
	
	public static void giveItems(Player p,ItemStack sampleItem,int count) {
		int stack = sampleItem.getMaxStackSize();
		while(count > 0) {
			if(count > stack) {
				sampleItem.setAmount(stack);
			}else {
				sampleItem.setAmount(count);
			}
			if(-1 == p.getInventory().firstEmpty()){
				dropItem(p,sampleItem);
			}
			p.getInventory().addItem(sampleItem);
			count -= stack;
		}
	}
	public static void dropPublicItem(Location loc,ItemStack item) {
		//足元ドロップ
		Item i = (Item) loc.getWorld().spawnEntity(loc, EntityType.DROPPED_ITEM);
		i.setItemStack(item);
		i.setPickupDelay(5);
		i.setCustomName("落とし物");
		i.setCustomNameVisible(true);
	}
	
	public static void dropItem(Player p,ItemStack item) {
		//足元ドロップ
		Item i = (Item) p.getWorld().spawnEntity(p.getLocation(), EntityType.DROPPED_ITEM);
		i.setItemStack(item);
		i.setPickupDelay(0);
		i.setOwner(p.getUniqueId());
		i.setInvulnerable(true);
		i.setCustomName(
				new TextBuilder(ChatColor.WHITE)
    			.addPlayerName(p) 
    			.addText("の落とし物")
    			.build());
		i.setCustomNameVisible(true);
	}
	
	public static boolean isNotAir(ItemStack item) {
		if(item == null) {
			return false;
		}
		return !item.getType().equals(Material.AIR);
	}
	
}
