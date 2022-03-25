package com.github.redshirt53072.shulker.bank;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import com.github.redshirt53072.api.gui.MoneyGui;
import com.github.redshirt53072.api.item.ItemBuilder;
import com.github.redshirt53072.api.item.ItemLibrary;
import com.github.redshirt53072.api.item.ItemUtil;
import com.github.redshirt53072.api.message.SoundManager;
import com.github.redshirt53072.api.message.TextBuilder;
import com.github.redshirt53072.api.money.MoneyManager;
import com.github.redshirt53072.shulker.UsefulShulker;

public class BankGui extends MoneyGui{
	private int balance = 0;
	private int emerald = 0;	
	
	private int inEme[] = {1,1,1};
	private int outEme[] = {1,1,1};
	
	public BankGui() {
	    super(UsefulShulker.getInstance());	    
	}
	
	@Override
	public void onRegister() {
		inv = Bukkit.createInventory(null, 36, "Prince銀行");

		setEmptyItem(9,10,12,13,17,18,22,23,25,26);
		
		renderingMoney();
		
		renderingButton(true,0);
		renderingButton(true,1);
		renderingButton(true,2);
		renderingButton(false,0);
		renderingButton(false,1);
		renderingButton(false,2);
		
		inv.setItem(11, new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setName(TextBuilder.quickBuild(ChatColor.WHITE,"全て預け入れる")).setModelData(3407).build());	
		inv.setItem(24, new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setName(TextBuilder.quickBuild(ChatColor.WHITE,"全て引き出す")).setModelData(3408).build());	
		
		player.playSound(player.getLocation(), Sound.BLOCK_ENDER_CHEST_OPEN, 1,1);
		player.openInventory(inv);
	}
	
	private void renderingButton(boolean in,int type) {
		String v = in ? "預け入れる" : "引き出す";
		int slot = (in ? 19 : 14) + type;
		int amount = in ? inEme[type] : outEme[type];
		
		List<String> lore = new ArrayList<String>();
		lore.add(new TextBuilder(ChatColor.WHITE).addClick("右クリック").addText("で",v,"個数を切り替える").build());
		
		if(type == 0) {
			inv.setItem(slot, new ItemBuilder(Material.EMERALD).setName(new TextBuilder(ChatColor.WHITE).addText("エメラルドを").addNumText(amount, "個").addText(v).build()).setLore(lore).setAmount(amount).build());
		}else if(type == 1) {	
			inv.setItem(slot, new ItemBuilder(Material.EMERALD_BLOCK).setName(new TextBuilder(ChatColor.WHITE).addText("エメラルドブロックを").addNumText(amount, "個").addText(v).build()).setLore(lore).setAmount(amount).build());
		}else {
			inv.setItem(slot, new ItemBuilder(ItemLibrary.getLiquidEmerald()).setName(new TextBuilder(ChatColor.WHITE).addText("リキッドエメラルドを").addNumText(amount, "個").addText(v).build()).setLore(lore).setAmount(amount).build());
		}
	}
	
	private void renderingMoney() {
		//値段更新
		emerald = 0;
		emerald += ItemUtil.countItem(player, new ItemStack(Material.EMERALD));
		emerald += ItemUtil.countItem(player, new ItemStack(Material.EMERALD_BLOCK)) * 64;
		emerald += ItemUtil.countItem(player, ItemLibrary.getLiquidEmerald()) * 4096;
		
		balance = (int) MoneyManager.get(player);
		String text1 = new TextBuilder(ChatColor.WHITE).addText("口座残高：").addMoneyText(balance).build();
		renderingMoney(0, balance, 
				new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setName(text1).setModelData(3200).build(), 
				text1);
		String text2 = new TextBuilder(ChatColor.WHITE).addText("手持ちエメラルド：").addNumText(emerald,"個").build();
		renderingMoney(3, emerald, 
				new ItemBuilder(Material.EMERALD).setName(text2).build(), 
				text2);
	}
	
	private void updateButton(boolean in,int type){
		if(in) {
			int amount = inEme[type];
			
			if(amount == 1) {
				inEme[type] = 8;
			}else if(amount == 8) {
				inEme[type] = 32;
			}else if(amount == 32) {
				inEme[type] = 64;
			}else {
				inEme[type] = 1;
			}
		}else {
			int amount = outEme[type];
			
			if(amount == 1) {
				outEme[type] = 8;
			}else if(amount == 8) {
				outEme[type] = 32;
			}else if(amount == 32) {
				outEme[type] = 64;
			}else {
				outEme[type] = 1;
			}
		}
		SoundManager.sendClick(player);
		
		renderingButton(in,type);
	}
	
	@Override
	public void onClose() {
		player.playSound(player.getLocation(), Sound.BLOCK_ENDER_CHEST_CLOSE, 1,1);
	}
	
	@Override
	public boolean onClick(InventoryClickEvent event) {
		int slot = event.getRawSlot();
		if(slot > 35) {
			ItemStack clickedItem = event.getCurrentItem();
			if(clickedItem == null) {
				return true;	
			}
			if(clickedItem.isSimilar(new ItemStack(Material.EMERALD))) {
				Bukkit.getScheduler().runTask(plugin, new Runnable() {
    	    		@Override
    	    		public void run() {
    	    			player.getInventory().clear(event.getSlot());
    	    			MoneyManager.add(player, clickedItem.getAmount());
    					renderingMoney();
    					SoundManager.sendPickUp(player);
    	    		}
				});
				return true;
			}
			if(clickedItem.isSimilar(new ItemStack(Material.EMERALD_BLOCK))) {
				Bukkit.getScheduler().runTask(plugin, new Runnable() {
    	    		@Override
    	    		public void run() {
    	    			player.getInventory().clear(event.getSlot());
    	    			MoneyManager.add(player, clickedItem.getAmount() * 64);
    					renderingMoney();
    					SoundManager.sendPickUp(player);
    	    		}
				});
				return true;
			}
			if(clickedItem.isSimilar(new ItemStack(ItemLibrary.getLiquidEmerald()))) {
				Bukkit.getScheduler().runTask(plugin, new Runnable() {
    	    		@Override
    	    		public void run() {
    	    			player.getInventory().clear(event.getSlot());
    	    			MoneyManager.add(player, clickedItem.getAmount() * 4096);
    					renderingMoney();
    					SoundManager.sendPickUp(player);
    	    		}
				});
				return true;
			}

			SoundManager.sendCancel(player);
			return true;
		}
		
		if(slot == 11) {
			int eme = ItemUtil.removeItem(player, new ItemStack(Material.EMERALD));
			int emeBlock = ItemUtil.removeItem(player, new ItemStack(Material.EMERALD_BLOCK));
			int liquidEme = ItemUtil.removeItem(player, ItemLibrary.getLiquidEmerald());
			int price = eme + emeBlock * 64 + liquidEme * 4096;
			if(price == 0) {
				SoundManager.sendCancel(player);
				return true;
			}
			MoneyManager.add(player, price);

			SoundManager.sendPickUp(player);
			renderingMoney();				
			
			return true;
		}
		if(slot == 24) {
			if(balance == 0) {
				SoundManager.sendCancel(player);
				return true;
			}
			int liquid = balance / 4096;
			int block = (balance % 4096) / 64;
			int eme = (balance % 4096) % 64;
			
			ItemUtil.giveItems(player, ItemLibrary.getLiquidEmerald(), liquid);
			ItemUtil.giveItems(player, new ItemStack(Material.EMERALD_BLOCK), block);
			ItemUtil.giveItems(player, new ItemStack(Material.EMERALD), eme);

			MoneyManager.reset(player);

			SoundManager.sendPickUp(player);
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
		if(event.getClick().equals(ClickType.RIGHT)) {
			if(slot == 14){
				//e
				updateButton(false,0);
				return true;
			}
			if(slot == 15){
				//eb
				updateButton(false,1);
				return true;
			}
			if(slot == 16){
				//le
				updateButton(false,2);
				return true;
			}
			if(slot == 19){
				//e
				updateButton(true,0);
				return true;
			}
			if(slot == 20){
				//e
				updateButton(true,1);
				return true;
			}
			if(slot == 21){
				//le
				updateButton(true,2);
				return true;
			}
			return true;
		}
		//その他無効な場所のクリック
		return true;
	}
}
