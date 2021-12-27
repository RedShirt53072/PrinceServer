package com.gmail.akashirt53072.usefulshulker;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.github.redshirt53072.baseapi.message.MessageManager;
import com.github.redshirt53072.economyapi.money.MoneyManager;
import com.gmail.akashirt53072.usefulshulker.data.InvIDType;
import com.gmail.akashirt53072.usefulshulker.data.PlayerNBT;

public class ConfirmCheck {
	private Player player;
    public ConfirmCheck(Player player) {
    	this.player = player;
    }
    public void open() {
    	new PlayerNBT(player).setInvID(InvIDType.CONFIRMCHECK);
     	
    	int payPage = new PlayerNBT(player).getPayPage();
    	Inventory inv = Bukkit.createInventory(null, 27, payPage + "ページまでのエンダーチェストの拡張をしますか？");
    	
		int cost = EnderInv.calcCost(payPage, player);

		inv.setItem(11, createItem(Material.LIME_WOOL,ChatColor.GOLD.toString() + cost + ChatColor.WHITE +"Ɇ支払って確定する",null,1,null,0));
		inv.setItem(15, createItem(Material.RED_WOOL,ChatColor.WHITE + "キャンセルする",null,1,null,0));
		
		player.openInventory(inv);
        new PlayerNBT(player).setInvID(InvIDType.ENDERCHEST);
        player.playSound(player.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 1,1);
    }
    
    
	private ItemStack createItem(Material material,String name,ArrayList<String> lore,int amount,Enchantment ench,int itemModel){
		final ItemStack item = new ItemStack(material, amount);
	    final ItemMeta meta = item.getItemMeta();
	    meta.setDisplayName(name);
	    if(lore != null) {
	    	meta.setLore(lore);
	    }
	    if(ench != null) {
	    	meta.addEnchant(ench, 1, true);
	    }
	    if(itemModel > 0) {
		    meta.setCustomModelData(itemModel);
	    }
	    meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
	    meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
	    item.setItemMeta(meta);
		return item;
	}
    
    
    public void onClick(InventoryClickEvent event){
    	int slot = event.getRawSlot();
    	event.setCancelled(true);
    	
    	if(slot == 11) {
    		player.playSound(player.getLocation(), Sound.BLOCK_BEACON_DEACTIVATE, 1,1);
    		player.closeInventory();
    		return;
    	}
    	if(slot == 15) {
    		int nowEme = (int) MoneyManager.get(player);
    		int payPage = new PlayerNBT(player).getPayPage();
    		int cost = EnderInv.calcCost(payPage, player);
    		if(nowEme > cost) {
    			player.playSound(player.getLocation(), Sound.BLOCK_BEACON_POWER_SELECT, 1,1);
    			new PlayerNBT(player).setUnlockedPage(payPage);
    			MoneyManager.remove(player, cost);
    			MessageManager.sendImportant(ChatColor.GOLD.toString() + cost + "Ɇ" + ChatColor.WHITE + "を使用してエンダーチェストが" + (payPage + 1) + "ページまで拡張されました。", player);
    			player.closeInventory();
        		return;
    		}
    		player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1,(float)0.5);
    		MessageManager.sendImportant("Ɇが足りないため、エンダーチェストの拡張ができません。 所持:" + ChatColor.GOLD + nowEme + "Ɇ" + ChatColor.WHITE + "/必要:" + ChatColor.GOLD +  cost + "Ɇ", player);
    		player.closeInventory();
    		
        	return;
    	}
    }
    
    
    public void close() {
        new EnderInv(player).open();
    }
}
