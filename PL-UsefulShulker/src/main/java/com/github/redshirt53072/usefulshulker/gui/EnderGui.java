package com.github.redshirt53072.usefulshulker.gui;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;

import com.github.redshirt53072.growthapi.gui.Gui;
import com.github.redshirt53072.growthapi.money.MoneyManager;
import com.github.redshirt53072.usefulshulker.UsefulShulker;
import com.github.redshirt53072.usefulshulker.data.PlayerECNBT;

public class EnderGui extends Gui{
	
	private int openedPage = 0;
	
	public EnderGui() {
    	super(UsefulShulker.getInstance());
    }
    
    private void load() {
    	Inventory enderChest = player.getEnderChest();
    	ItemStack page1 = enderChest.getItem(openedPage);
    	BlockStateMeta pageMeta = (BlockStateMeta)page1.getItemMeta();
    	ShulkerBox box = (ShulkerBox)pageMeta.getBlockState();
    	Inventory boxInv = box.getInventory();
    	for(int index = 0;index < 27;index ++) {
			inv.setItem(index, boxInv.getItem(index));
		}
    	//ページ
    	int lockedPage = new PlayerECNBT(player).getUnlockedPage();
    	for(int index = 1;index < 10;index ++) {
			int model = 3010 + index;
			String text = ChatColor.WHITE.toString() + index + "ページ";
			ArrayList<String> lore = new ArrayList<String>();
			if(openedPage + 1 == index) {
				model -= 10;
				text = text + ChatColor.YELLOW + "<選択中>";
			}
			if(lockedPage < index) {
				model += 10;
				text = text + ChatColor.RED + "<未開放>";
				lore.add(ChatColor.WHITE + "合計" + ChatColor.GOLD + calcCost(index - 1,player) + "Ɇ" + ChatColor.WHITE + "でこのスロットを開放できます。");
			}
			inv.setItem(index + 26, createItem(Material.WHITE_STAINED_GLASS_PANE,text,lore,1,null,model));
		}
    }
    
    private void save() {
    	Inventory enderChest = player.getEnderChest();
    	ItemStack page = enderChest.getItem(openedPage);
    	BlockStateMeta pageMeta = (BlockStateMeta)page.getItemMeta();
    	ShulkerBox box = (ShulkerBox)pageMeta.getBlockState();
    	Inventory boxInv = box.getInventory();
    	for(int index = 0;index < 27;index ++) {
    		boxInv.setItem(index, inv.getItem(index));
		}
    	pageMeta.setBlockState(box);
    	page.setItemMeta(pageMeta);
    	enderChest.setItem(openedPage, page);
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
    
	static public int calcCost(int slot,Player player) {

		int unlock = new PlayerECNBT(player).getUnlockedPage();
		int totalCost = 0;
		
		for(int i = 1;unlock + i < slot + 2;i++) {
			totalCost += ((int)Math.pow(2,unlock + i)) * 500;	
		}
		
		return totalCost;
	}
	
    @Override
    public boolean onClick(InventoryClickEvent event){
    	int slot = event.getRawSlot();
    	if(slot > 35||slot < 27) {
    		return false;
    	}
    	
    	event.setCancelled(true);
    	
    	slot -= 27;
    	if(openedPage == slot) {
    		player.playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, 1,1);
    		return true;
    	}
    	
    	if(!(new PlayerECNBT(player).getUnlockedPage() > slot)) {
    		int nowEme = (int) MoneyManager.get(player);
    		if(nowEme > calcCost(slot,player)) {
    			save();
    			close();
    			new ConfirmCheck(slot).open(player);
    			return true;
    		}
            player.playSound(player.getLocation(), Sound.BLOCK_CHEST_LOCKED, 1,1);
    		return true;
    	}
    	
    	save();
    	openedPage = slot;
    	load();
    	
        player.playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, 1,1);
        return true;
    }
    
	@Override
	public void onRegister() {
    	Inventory enderChest = player.getEnderChest();
    	pageInit(enderChest);
    	inv = Bukkit.createInventory(null, 36, "エンダーチェスト");
    	load();
        player.playSound(player.getLocation(), Sound.BLOCK_ENDER_CHEST_OPEN, 1,1);
        player.openInventory(inv);
	}
	@Override
	public void onClose() {
    	save();
        player.playSound(player.getLocation(), Sound.BLOCK_ENDER_CHEST_CLOSE, 1,1);
	}
}
