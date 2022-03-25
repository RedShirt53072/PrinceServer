package com.github.redshirt53072.survival.gui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import com.github.redshirt53072.api.gui.Gui;
import com.github.redshirt53072.api.item.ItemBuilder;
import com.github.redshirt53072.api.item.ItemLibrary;
import com.github.redshirt53072.api.item.ItemUtil;
import com.github.redshirt53072.api.message.SoundManager;
import com.github.redshirt53072.api.message.TextBuilder;
import com.github.redshirt53072.survival.GrowthSurvival;
import com.github.redshirt53072.survival.ench.EnchData.ScrapData;
import com.github.redshirt53072.survival.ench.EnchManager;

public class ScrapGui extends Gui{
	private	List<ScrapData> scrapList = new ArrayList<ScrapData>();
	
	public ScrapGui() {
	    super(GrowthSurvival.getInstance());
	}
	
	@Override
	public void onRegister() {
		inv = Bukkit.createInventory(null, 27, "スクラップモード");
		
		scrapList = EnchManager.getScrapList();
		
		setEmptyItem(1,2,8,10,11,17,19,20,26);
		
		
		List<String> lore = new ArrayList<String>();
		lore.add(TextBuilder.quickBuild(ChatColor.WHITE, "ツールからエンチャントを削除することができます。"));
		lore.add(new TextBuilder(ChatColor.WHITE).addClick("クリック").addText("でエンチャント削除モードに切り替え").build());
		
		
		inv.setItem(0, new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setName(new TextBuilder(ChatColor.WHITE)
				.addText("-エンチャント削除モード-")
				.build()).setLore(lore).setModelData(3502).build());
		
		List<String> lore2 = new ArrayList<String>();
		lore2.add(TextBuilder.quickBuild(ChatColor.WHITE, "スクラップでツールを修理することができます。"));
		lore2.add(new TextBuilder(ChatColor.WHITE).addClick("クリック").addText("で修理モードに切り替え").build());
		
		inv.setItem(9, new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setName(new TextBuilder(ChatColor.WHITE)
				.addText("-修理モード-")
				.build()).setLore(lore2).setModelData(3500).build());	
		
		List<String> lore3 = new ArrayList<String>();
		lore3.add(TextBuilder.quickBuild(ChatColor.WHITE, "鉱石をスクラップに加工することができます。"));
		
		inv.setItem(18, new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setName(new TextBuilder(ChatColor.WHITE)
				.addText("-スクラップモード-")
				.addColorText(ChatColor.YELLOW,"<選択中>").build())
				.setLore(lore3).setModelData(3603).setDummyEnch().build());	
		
		rendering();
		
		player.playSound(player.getLocation(), Sound.BLOCK_ENDER_CHEST_OPEN, 1,1);
		player.openInventory(inv);
	}
	
	private void rendering() {
		for(int i = 0;i < 15;i ++) {
			int index = i;
			if(i < 5) {
				index += 3;
			}else if(i < 10) {
				index += 7;
			}else {
				index += 11;
			}
			if(i < scrapList.size()) {
				ScrapData sd = scrapList.get(i);
				int materialAmount = sd.getAmount();
				int scrapAmount = 1;
				
				if(materialAmount < 0) {
					scrapAmount = 0 - materialAmount;
					materialAmount = 1;
				}
				List<String> lore = new ArrayList<String>();
				if(ItemUtil.countItem(player, new ItemStack(sd.getItem())) < materialAmount) {
					lore.add(new TextBuilder(ChatColor.RED).addText("<鉱石が足りません>").build());
				}else {
					lore.add(new TextBuilder(ChatColor.WHITE).addClick("左クリック").addText("で1個加工する").build());
					lore.add(new TextBuilder(ChatColor.WHITE).addClick("右クリック").addText("で一括加工する").build());
				}
				
				inv.setItem(index, new ItemBuilder(sd.getItem()).setName(new TextBuilder(ChatColor.WHITE)
						.addText(sd.getName())
						.addNumText(materialAmount, "個")
						.addColorText(ChatColor.GRAY, " → ")
						.addText("スクラップ")
						.addNumText(scrapAmount, "個")
						.build()).setLore(lore).build());
				
				continue;
			}
			setEmptyItem(index);
		}
	}
	
	@Override
	public void onClose() {
		
		player.playSound(player.getLocation(), Sound.BLOCK_ENDER_CHEST_CLOSE, 1,1);
	}
	
	@Override
	public boolean onClick(InventoryClickEvent event) {
		int slot = event.getRawSlot();
		if(slot > 26) {
			return true;
		}
    	int line = slot % 9;
		if(line == 0) {
			if(slot == 0) {
				//エンチャ削除へ
				onClose();
				close();
				new RemoveEnchGui().open(player);
				return true;
			}
			if(slot == 9) {
				//修理へ
				onClose();
				close();
				new RepairGui().open(player);
				return true;
			}
		}
		if(line == 1 || line == 8){
			return true;
		}
		
		
		//スクラップ交換
		int index = slot;
		
		if(2 < slot && slot < 8){
			index -= 3;
		}
		if(11 < slot && slot < 17){
			index -= 7;
		}
		if(20 < slot && slot < 26){
			index -= 11;
		}
		if(index >= 0 && index < scrapList.size()) {
			calcScrap(scrapList.get(index),event.getClick().equals(ClickType.RIGHT));			
		}
		return true;
	}
	
	private void calcScrap(ScrapData sd,boolean max){
		int materialAmount = sd.getAmount();
		int scrapAmount = 1;
		
		if(materialAmount < 0) {
			scrapAmount = 0 - materialAmount;
			materialAmount = 1;
		}
		int count = ItemUtil.countItem(player, new ItemStack(sd.getItem()));
		
		count /= materialAmount;
		
		if(count <= 0) {
			SoundManager.sendCancel(player);
			return;
		}
		if(!max) {
			count = 1;
		}
		SoundManager.sendPickUp(player);
		ItemUtil.removeItem(player, new ItemStack(sd.getItem()), materialAmount * count);
		ItemUtil.giveItems(player, ItemLibrary.getScrap(), scrapAmount * count);
		rendering();
	}
}