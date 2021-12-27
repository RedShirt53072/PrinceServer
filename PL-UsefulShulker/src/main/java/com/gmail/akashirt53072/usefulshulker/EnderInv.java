package com.gmail.akashirt53072.usefulshulker;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.ShulkerBox;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;

import com.github.redshirt53072.economyapi.money.MoneyManager;
import com.gmail.akashirt53072.usefulshulker.data.InvIDType;
import com.gmail.akashirt53072.usefulshulker.data.PlayerNBT;

public class EnderInv {
	private Player player;
    public EnderInv(Player player) {
    	this.player = player;
    }
    public void open() {
    	Inventory enderChest = player.getEnderChest();
    	pageInit(enderChest);
    	Inventory inv = Bukkit.createInventory(null, 36, "エンダーチェスト");
    	load(inv,0);
        new PlayerNBT(player).setInvID(InvIDType.ENDERCHEST);
    	new PlayerNBT(player).setPage(0);
        player.playSound(player.getLocation(), Sound.BLOCK_ENDER_CHEST_OPEN, 1,1);
        player.openInventory(inv);
    }
    
    private void load(Inventory inv,int slot) {
    	Inventory enderChest = player.getEnderChest();
    	ItemStack page1 = enderChest.getItem(slot);
    	BlockStateMeta pageMeta = (BlockStateMeta)page1.getItemMeta();
    	ShulkerBox box = (ShulkerBox)pageMeta.getBlockState();
    	Inventory boxInv = box.getInventory();
    	for(int index = 0;index < 27;index ++) {
			inv.setItem(index, boxInv.getItem(index));
		}
    	//ページ
    	int lockedPage = new PlayerNBT(player).getUnlockedPage();
    	for(int index = 1;index < 10;index ++) {
			int model = 3010 + index;
			String text = ChatColor.WHITE.toString() + index + "ページ";
			ArrayList<String> lore = new ArrayList<String>();
			if(slot + 1 == index) {
				model -= 10;
				text = text + ChatColor.YELLOW + "<選択中>";
			}
			if(lockedPage < index) {
				model += 10;
				text = text + ChatColor.RED + "<未開放>";
				lore.add(ChatColor.WHITE + "合計" + ChatColor.GOLD + calcCost(index - 1,player) + "Ɇでこのスロットを開放できます。");
			}
			inv.setItem(index + 26, createItem(Material.WHITE_STAINED_GLASS_PANE,text,lore,1,null,model));
		}
    	new PlayerNBT(player).setPage(slot);
    }
    
    private void save(Inventory inv) {
    	int slot = new PlayerNBT(player).getPage();
    	Inventory enderChest = player.getEnderChest();
    	ItemStack page = enderChest.getItem(slot);
    	BlockStateMeta pageMeta = (BlockStateMeta)page.getItemMeta();
    	ShulkerBox box = (ShulkerBox)pageMeta.getBlockState();
    	Inventory boxInv = box.getInventory();
    	for(int index = 0;index < 27;index ++) {
    		boxInv.setItem(index, inv.getItem(index));
		}
    	pageMeta.setBlockState(box);
    	page.setItemMeta(pageMeta);
    	enderChest.setItem(slot, page);
    }
    
    private Inventory pageInit(Inventory enderChest) {
    	for(int slot = 0;slot < 9;slot++) {
    		ItemStack nowItem = enderChest.getItem(slot);
        	if(nowItem != null) {
        		if(nowItem.getType().equals(Material.SHULKER_BOX)) {
            		continue;
            	}
        	}
        	enderChest.setItem(slot, new ItemStack(Material.SHULKER_BOX));	
    	}
    	return enderChest;
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
    
	static public int calcCost(int slot,Player player) {
		//あとで計算式は変える
		int unlock = new PlayerNBT(player).getUnlockedPage();
		return (unlock + slot ) * (slot - unlock + 1) * 500;
	}
	
    
    public void onClick(InventoryClickEvent event){
    	int slot = event.getRawSlot();
    	if(slot > 35||slot < 27) {
    		return;
    	}
    	event.setCancelled(true);
    	
    	slot -= 27;
    	int nowSlot = new PlayerNBT(player).getPage();
    	if(nowSlot == slot) {
    		player.playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, 1,1);
    		return;
    	}
    	if(!(new PlayerNBT(player).getUnlockedPage() > slot)) {
    		
    		int nowEme = (int) MoneyManager.get(player);
    		
    		if(nowEme > calcCost(slot,player)) {
    			save(event.getClickedInventory());
        		new PlayerNBT(player).setSafeClose(true);
    			new PlayerNBT(player).setPayPage(slot);
    			player.closeInventory();
    			new ConfirmCheck(player).open();
    		}
            player.playSound(player.getLocation(), Sound.BLOCK_CHEST_LOCKED, 1,1);
    		return;
    	}
    	
    	Inventory inv = player.getOpenInventory().getTopInventory();
    	save(inv);
    	load(inv,slot);
    	
        player.playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, 1,1);
        
    }
    
    public void close(Inventory inv) {
    	if(new PlayerNBT(player).getSafeClose()) {
    		new PlayerNBT(player).setSafeClose(false);
    		return;
    	}
    	
    	save(inv);
        new PlayerNBT(player).setInvID(InvIDType.NULLINV);
        player.playSound(player.getLocation(), Sound.BLOCK_ENDER_CHEST_CLOSE, 1,1);
    }
}
