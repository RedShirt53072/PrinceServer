package com.github.redshirt53072.economyapi.item;


import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;




public class ItemUtil {
	
	public static ItemStack getLiquidEmerald() {
		ItemStack le = new ItemStack(Material.COMMAND_BLOCK);
		ItemMeta leMeta = le.getItemMeta();
		leMeta.setCustomModelData(1000);
		leMeta.setDisplayName("リキッドエメラルド");
		le.setItemMeta(leMeta);
		return le;
	}
	
	public static int countItem(Player p,ItemStack item,boolean autoRemove) {
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
			if(stack.isSimilar(item)){
				count += stack.getAmount();
				if(autoRemove) {
					if(i == 36) {
						inv.setItemInOffHand(null);
					}else {
						inv.clear(i);
					}
				}
			}
		}
		return count;
	}
	
	public static void giveItem(Player p,ItemStack item,int count) {
		int stack = item.getMaxStackSize();
		while(count > 0) {
			if(count > stack) {
				item.setAmount(stack);
			}else {
				item.setAmount(count);
			}
			dropItem(p,item);
			count -= stack;
		}
	}
	private static void dropItem(Player p,ItemStack item) {
		if(-1 == p.getInventory().firstEmpty()){
			//足元ドロップ
			Item i = (Item) p.getWorld().spawnEntity(p.getLocation(), EntityType.DROPPED_ITEM);
			i.setItemStack(item);
			i.setPickupDelay(0);
			i.setOwner(p.getUniqueId());
			i.setGlowing(true);
			i.setInvulnerable(true);
			i.setCustomName(p.getName() + "の落とし物");
			i.setCustomNameVisible(true);
			return;
		}
		p.getInventory().addItem(item);
		return;
	}
}
