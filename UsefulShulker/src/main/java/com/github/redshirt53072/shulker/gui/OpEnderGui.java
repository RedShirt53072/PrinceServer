package com.github.redshirt53072.shulker.gui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.event.inventory.InventoryClickEvent;

import com.github.redshirt53072.api.gui.Gui;
import com.github.redshirt53072.api.item.ItemBuilder;
import com.github.redshirt53072.api.message.TextBuilder;
import com.github.redshirt53072.api.player.PlayerManager;
import com.github.redshirt53072.shulker.UsefulShulker;
import com.github.redshirt53072.shulker.data.ECLock;
import com.github.redshirt53072.shulker.data.EnderChest;

public class OpEnderGui extends Gui{
	private static List<OfflinePlayer> editPlayers = new ArrayList<OfflinePlayer>();
	
	private int openedPage = 1;
	private OfflinePlayer target; 
	private boolean editMode; 
	
	public OpEnderGui(OfflinePlayer target,boolean editMode) {
    	super(UsefulShulker.getInstance());
    	this.target = target;
    	this.editMode = editMode;
    	if(editMode) {
        	editPlayers.add(target);
    	}
	}
	
	public static boolean isOpenedPlayer(OfflinePlayer player) {
		return editPlayers.contains(player);
	}
	
	private void load() {
		//ページ
    	int lockedPage = ECLock.getPage(target);
    	for(int index = 1;index < 10;index ++) {
			int model = 3010 + index;
			String text = index + "ページ";
			ArrayList<String> lore = new ArrayList<String>();
			if(openedPage == index) {
				model -= 10;
				text = new TextBuilder(text,ChatColor.WHITE).addColorText(ChatColor.YELLOW,"<選択中>").build();
			}
			if(lockedPage < index) {
				model += 10;
				text = new TextBuilder(text,ChatColor.WHITE).addColorText(ChatColor.RED, "<未開放>").build();
				lore.add(new TextBuilder(ChatColor.WHITE).addText("合計").addMoneyText(EnderGui.calcCost(index,target)).addText("でこのスロットを開放できます。").build());
			}
			inv.setItem(index + 26, new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setLore(lore).setModelData(model).setName(text).build());
		}
	}
	
	
    @Override
    public boolean onClick(InventoryClickEvent event){
    	if(PlayerManager.isAsyncLocked(target,"ec")) {
    		return true;
    	}
    	int slot = event.getRawSlot();
    	if(slot > 35||slot < 27) {
    		return false;
    	}
    	
    	slot -= 26;
    	if(openedPage == slot) {
    		player.playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, 1,1);
    		return true;
    	}
    	
    	if(ECLock.getPage(target) < slot) {
    		player.playSound(player.getLocation(), Sound.BLOCK_CHEST_LOCKED, 1,1);
    		return true;
    	}
    	if(editMode) {
    		new EnderChest().pageChange(target, inv, openedPage,slot);
        }else{
        	new EnderChest().openLoad(target, inv,slot);	
        }
    	
    	openedPage = slot;
    	load();
    	
        player.playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, 1,1);
        return true;
    }
    
	@Override
	public void onRegister() {
    	inv = Bukkit.createInventory(null, 36, TextBuilder.plus(target.getName(),"-",(editMode ? "編集モード" : "閲覧モード")));
    	
    	new EnderChest().openLoad(target, inv, openedPage);
    	
    	load();
    	
        player.playSound(player.getLocation(), Sound.BLOCK_ENDER_CHEST_OPEN, 1,1);
        player.openInventory(inv);
	}
	@Override
	public void onClose() {
		if(editMode) {
        	editPlayers.remove(target);
        	if(!PlayerManager.isAsyncLocked(target, "ec")) {
    	    	new EnderChest().closeSave(target, inv, openedPage);
    		}
		}
    	player.playSound(player.getLocation(), Sound.BLOCK_ENDER_CHEST_CLOSE, 1,1);
	}
}
