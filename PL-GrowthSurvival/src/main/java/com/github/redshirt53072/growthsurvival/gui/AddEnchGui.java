package com.github.redshirt53072.growthsurvival.gui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import com.github.redshirt53072.growthapi.gui.Gui;
import com.github.redshirt53072.growthapi.gui.MoneyGui;
import com.github.redshirt53072.growthapi.item.ItemBuilder;
import com.github.redshirt53072.growthapi.item.ItemLibrary;
import com.github.redshirt53072.growthapi.item.ItemUtil;
import com.github.redshirt53072.growthapi.message.SoundManager;
import com.github.redshirt53072.growthapi.message.TextBuilder;
import com.github.redshirt53072.growthapi.money.MoneyManager;
import com.github.redshirt53072.growthsurvival.GrowthSurvival;

public class AddEnchGui extends Gui{
	private int expCost = 0;
	
	
	public AddEnchGui() {
	    super(GrowthSurvival.getInstance());	    
	}
	
	@Override
	public void onRegister() {
		inv = Bukkit.createInventory(null, 27, "エンチャントの合成");

		setEmptyItem(1,2,8,10,11,17,18,19,20,21,22,23,24,25,26);
		
		
		inv.setItem(0, new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setName(new TextBuilder(ChatColor.WHITE)
				.addText("エンチャントの合成")
				.addColorText(ChatColor.YELLOW,"<選択中>")
				.build()).setModelData(3500).build());
		inv.setItem(9, new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setName(TextBuilder.quickBuild(ChatColor.WHITE,"アイテム名の編集")).setModelData(3502).build());	

		inv.setItem(3, new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setName(TextBuilder.quickBuild(ChatColor.WHITE,"元のアイテムのスロット")).setModelData(3405).build());	
		inv.setItem(4, new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setName(" ").setModelData(3413).build());	
		inv.setItem(5, new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setName(TextBuilder.quickBuild(ChatColor.WHITE,"追加するアイテムのスロット")).setModelData(3405).build());	
		inv.setItem(6, new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setName(" ").setModelData(3300).build());	
		inv.setItem(7, new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setName(TextBuilder.quickBuild(ChatColor.RED,"スロットにアイテムを入れてください。")).setModelData(3501).build());	
		
		player.playSound(player.getLocation(), Sound.BLOCK_ENDER_CHEST_OPEN, 1,1);
		player.openInventory(inv);
	}
	
	private void renderingCheck() {
		
		
	}
	
	private void renderingExp() {
		//経験値更新
		int cost = expCost;
		if(expCost > 999) {
			cost = 999;
		}
		
		List<Integer> numList = new ArrayList<Integer>();
		
		for(int i = 2;i >= 0;i--) {
			if(cost > Math.pow(10, i)) {
				int number = (int)(cost / Math.pow(10, i));
				cost %= Math.pow(10, i);
				numList.add(number);	
			}
		}
		if(numList.isEmpty()) {
			numList.add(0);
		}
		String text = ;
		for(int i = 2;i >= 0;i--) {
			if(numList.size() > i){
				inv.setItem(15 - i, new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setName(text).setModelData(3010 + numList.get(i)).build());
			}else {
				inv.setItem(15 - i, new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setName(text).setModelData(3201).build());	
			}
		}
		
		
	}
	
	@Override
	public void onClose() {
		player.playSound(player.getLocation(), Sound.BLOCK_ENDER_CHEST_CLOSE, 1,1);
	}
	
	@Override
	public boolean onClick(InventoryClickEvent event) {
		int slot = event.getRawSlot();
		if(slot > 36) {
			return true;
		}
		
		if(slot == 11) {
			int eme = ItemUtil.removeItem(player, new ItemStack(Material.EMERALD));
			int emeBlock = ItemUtil.removeItem(player, new ItemStack(Material.EMERALD_BLOCK));
			int liquidEme = ItemUtil.removeItem(player, ItemLibrary.getLiquidEmerald());
			int price = eme + emeBlock * 64 + liquidEme * 4096;
			MoneyManager.add(player, price);
			
			renderingMoney();
			return true;
		}
		if(slot == 24) {
			int liquid = balance / 4096;
			int block = (balance % 4096) / 64;
			int eme = (balance % 4096) % 64;
			
			ItemUtil.giveItems(player, ItemLibrary.getLiquidEmerald(), liquid);
			ItemUtil.giveItems(player, new ItemStack(Material.EMERALD_BLOCK), block);
			ItemUtil.giveItems(player, new ItemStack(Material.EMERALD), eme);

			MoneyManager.reset(player);
			
			renderingMoney();
			return true;
		}
		
		if(event.getClick().equals(ClickType.LEFT)) {
			if(slot == 14){
				//e
				int amount = (outEme[0] < balance) ? outEme[0] : balance;
				if(amount == 0) {
					SoundManager.sendCancel(player);
					return true;
				}
				SoundManager.sendPickUp(player);
				MoneyManager.remove(player, amount);
				ItemUtil.giveItems(player,new ItemStack(Material.EMERALD) , amount);
				renderingMoney();
				return true;
			}
			if(slot == 15){
				//eb
				int amount = (outEme[1] * 64 < balance) ? outEme[1] : balance / 64;
				if(amount == 0) {
					SoundManager.sendCancel(player);
					return true;
				}
				SoundManager.sendPickUp(player);
				MoneyManager.remove(player, amount * 64);
				ItemUtil.giveItems(player,new ItemStack(Material.EMERALD_BLOCK) , amount);
				renderingMoney();
				return true;
			}
			if(slot == 16){
				//le
				int amount = (outEme[2] * 4096 < balance) ? outEme[2] : balance / 4096;
				if(amount == 0) {
					SoundManager.sendCancel(player);
					return true;
				}
				SoundManager.sendPickUp(player);
				MoneyManager.remove(player, amount * 4096);
				ItemUtil.giveItems(player, ItemLibrary.getLiquidEmerald(), amount);
				renderingMoney();
				return true;
			}
			if(slot == 19){
				//e
				int amount = ItemUtil.removeItem(player, new ItemStack(Material.EMERALD),inEme[0]);
				if(amount == 0) {
					SoundManager.sendCancel(player);
					return true;
				}
				SoundManager.sendPickUp(player);
				MoneyManager.add(player, amount);
				renderingMoney();
				return true;
			}
			if(slot == 20){
				//eb
				int amount = ItemUtil.removeItem(player, new ItemStack(Material.EMERALD_BLOCK),inEme[1]);
				if(amount == 0) {
					SoundManager.sendCancel(player);
					return true;
				}
				SoundManager.sendPickUp(player);
				MoneyManager.add(player, amount * 64);
				renderingMoney();
				return true;
			}
			if(slot == 21){
				//le
				int amount = ItemUtil.removeItem(player, ItemLibrary.getLiquidEmerald(),inEme[2]);
				if(amount == 0) {
					SoundManager.sendCancel(player);
					return true;
				}
				SoundManager.sendPickUp(player);
				MoneyManager.add(player, amount * 4096);
				renderingMoney();
				return true;
			}
			return true;
		}
		//その他無効な場所のクリック
		return true;
	}
}
