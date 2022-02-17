package com.github.redshirt53072.usefulshulker.gui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import com.github.redshirt53072.growthapi.gui.Gui;
import com.github.redshirt53072.growthapi.item.ItemBuilder;
import com.github.redshirt53072.growthapi.player.PlayerManager;
import com.github.redshirt53072.usefulshulker.UsefulShulker;
import com.github.redshirt53072.usefulshulker.data.ECLock;
import com.github.redshirt53072.usefulshulker.data.EnderChest;

public class OpEnderGui extends Gui{
	private static List<Player> editPlayers = new ArrayList<Player>();
	
	private int openedPage = 1;
	private Player target; 
	private boolean editMode; 
	
	public OpEnderGui(Player target,boolean editMode) {
    	super(UsefulShulker.getInstance());
    	this.target = target;
    	this.editMode = editMode;
    	if(editMode) {
        	editPlayers.add(target);
    	}
	}
	
	public static boolean isOpenedPlayer(Player player) {
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
				text = text + ChatColor.YELLOW + "<選択中>";
			}
			if(lockedPage < index) {
				model += 10;
				text = text + ChatColor.RED + "<未開放>";
				lore.add(ChatColor.WHITE + "合計" + ChatColor.GOLD + EnderGui.calcCost(index,target) + "Ɇ" + ChatColor.WHITE + "でこのスロットを開放できます。");
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
    	inv = Bukkit.createInventory(null, 36, target.getName() + "-" + (editMode ? "編集モード" : "閲覧モード"));
    	
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
