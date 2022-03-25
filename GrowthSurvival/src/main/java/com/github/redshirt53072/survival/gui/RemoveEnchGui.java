package com.github.redshirt53072.survival.gui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import com.github.redshirt53072.api.item.ItemBuilder;
import com.github.redshirt53072.api.item.ItemUtil;
import com.github.redshirt53072.api.message.SoundManager;
import com.github.redshirt53072.api.message.TextBuilder;
import com.github.redshirt53072.survival.GrowthSurvival;
import com.github.redshirt53072.survival.ench.BrokenReader;
import com.github.redshirt53072.survival.ench.EnchBookBuilder;
import com.github.redshirt53072.survival.ench.EnchBuilder;
import com.github.redshirt53072.survival.ench.EnchManager;
import com.github.redshirt53072.survival.ench.Enchant;
import com.github.redshirt53072.survival.ench.EnchantBuilder;
import com.github.redshirt53072.survival.ench.ToolData;
import com.github.redshirt53072.survival.ench.ToolData.ToolGroup;
import com.github.redshirt53072.survival.ench.ToolData.ToolGroupData;

public class RemoveEnchGui extends EnchGui{
	private ItemStack in1 = null;
	private ItemStack in2 = null;
	private ItemStack out = null;
	
	private boolean isLocked = false;
	
	public RemoveEnchGui() {
	    super();
	}
	
	@Override
	public void onRegister() {
		inv = Bukkit.createInventory(null, 27, "エンチャント削除モード");

		setEmptyItem(1,2,8,10,11,17,19,20,21,22,23,24,25,26);
		
		List<String> lore = new ArrayList<String>();
		lore.add(TextBuilder.quickBuild(ChatColor.WHITE, "ツールからエンチャントを削除することができます。"));
		lore.add(TextBuilder.quickBuild(ChatColor.GRAY, "ツールのみを入れるとすべてのエンチャントを削除できます。"));
		lore.add(TextBuilder.quickBuild(ChatColor.GRAY, "この場合の経験値コストは常に1です。"));
		lore.add(TextBuilder.quickBuild(ChatColor.GRAY, "削除したい種類のエンチャントが含まれたエンチャント本を"));
		lore.add(TextBuilder.quickBuild(ChatColor.GRAY, "2番目のスロットに入れることで削除対象を指定することもできます。"));
		lore.add(TextBuilder.quickBuild(ChatColor.GRAY, "この場合の経験値コストは削除するエンチャントの"));
		lore.add(TextBuilder.quickBuild(ChatColor.GRAY, "種類やレベルに応じて高くなります。"));
		
		
		inv.setItem(0, new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setName(new TextBuilder(ChatColor.WHITE)
				.addText("-エンチャント削除モード-")
				.addColorText(ChatColor.YELLOW,"<選択中>")
				.build()).setLore(lore).setDummyEnch().setModelData(3602).build());
		
		List<String> lore2 = new ArrayList<String>();
		lore2.add(TextBuilder.quickBuild(ChatColor.WHITE, "スクラップでツールを修理することができます。"));
		lore2.add(new TextBuilder(ChatColor.WHITE).addClick("クリック").addText("で修理モードに切り替え").build());
		
		inv.setItem(9, new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setName(TextBuilder.quickBuild(ChatColor.WHITE,"-修理モード-"))
				.setLore(lore2).setModelData(3500).build());	
		List<String> lore3 = new ArrayList<String>();
		lore3.add(TextBuilder.quickBuild(ChatColor.WHITE, "鉱石をスクラップに加工することができます。"));
		lore3.add(new TextBuilder(ChatColor.WHITE).addClick("クリック").addText("でスクラップモードに切り替え").build());
		
		inv.setItem(18, new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setName(TextBuilder.quickBuild(ChatColor.WHITE,"-スクラップモード-"))
				.setLore(lore3).setModelData(3503).build());	
		
		
		inv.setItem(4, new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setName(" ").setModelData(3414).build());	
		inv.setItem(6, new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setName(" ").setModelData(3300).build());	
		
		rendering();
		
		player.playSound(player.getLocation(), Sound.BLOCK_ENDER_CHEST_OPEN, 1,1);
		player.openInventory(inv);
	}
	
	private void rendering() {
		
		Bukkit.getScheduler().runTask(GrowthSurvival.getInstance(), new Runnable() {
    		@Override
    		public void run() {
    			//inからoutを作って描画
    			//in1
    			if(in1 == null) {
    				inv.setItem(3, new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setName(TextBuilder.quickBuild(ChatColor.GRAY,"元のアイテムのスロット")).setModelData(3405).build());	
    			}else {
    				inv.setItem(3, in1.clone());
    			}
    			//in2
    			if(in2 == null) {
    				inv.setItem(5, new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setName(TextBuilder.quickBuild(ChatColor.GRAY,"削除するエンチャント本のスロット")).setModelData(3405).build());	
    			}else {
    				inv.setItem(5, in2.clone());
    			}
    			//calc

				expCost = 0;
				out = null;
    			calcOut();
    			
    			if(expCost != 0) {
    				expCost = calcExp(expCost);
    			}
    			
    			//out
    			if(out == null) {
    				if(in1 == null || in2 == null) {
    					inv.setItem(7, new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setName(TextBuilder.quickBuild(ChatColor.RED,"スロットにアイテムを入れてください。")).setModelData(3401).build());
    				}else {
    					if(isLocked) {
    						inv.setItem(7, new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setName(TextBuilder.quickBuild(ChatColor.RED,"このアイテムはエンチャントの編集ができません。")).setModelData(3401).build());		
    					}else {
    						inv.setItem(7, new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setName(TextBuilder.quickBuild(ChatColor.RED,"この組み合わせでは削除できるエンチャントがありません。")).setModelData(3401).build());
    					}
    				}
    			}else {
    				inv.setItem(7, new ItemBuilder(out.clone()).addLore(new TextBuilder(ChatColor.WHITE).addClick("クリック").addText("で決定").build()).build());
    			}
    			
    			renderingExp();
    			renderingCheck();
    			
    		}
		});
		
	}
	
	private void calcOut(){
		if(in1 != null && in2 != null) {
			//in1はツール、in2はエンチャ本
			EnchBookBuilder book = EnchBookBuilder.getBuilder(in2);
			if(book == null) {
				return;
			}
			
			EnchBookBuilder ebb1 = EnchBookBuilder.getBuilder(in1);
			if(ebb1 != null) {
				//book to book
				calcBookToBook(ebb1,book);
				return;
			}
			
			ToolData td = EnchManager.getToolData(in1.getType());
			if(td == null) {
				return;
			}
			ToolGroupData tgd = td.getToolGroupData();
			if(tgd == null) {
				return;
			}
			if(tgd.getID().equals(ToolGroup.broken)) {
				//book to broken
				EnchantBuilder builder = BrokenReader.getBuilder(in1,true);
				if(builder == null) {
					return;
				}
				calcBookToTool(builder,book);
				return;
			}
			EnchantBuilder builder = new EnchBuilder(in1,tgd);
			//book to tool
			calcBookToTool(builder,book);
			return;
		}
		if(in1 != null && in2 == null) {
			EnchantBuilder builder = EnchBookBuilder.getBuilder(in1);
			if(builder != null) {
				//book
				expCost = 1;
				builder.removeAll();
				out = builder.build();
				return;
			}
			ToolData td = EnchManager.getToolData(in1.getType());
			if(td == null) {
				return;
			}
			ToolGroupData tgd = td.getToolGroupData();
			if(tgd == null) {
				return;
			}
			if(tgd.getID().equals(ToolGroup.broken)) {
				//broken
				builder = BrokenReader.getBuilder(in1,true);
				if(builder == null) {
					return;
				}
				expCost = 1;
				builder.removeAll();
				out = builder.build();
				
				return;
			}
			builder = new EnchBuilder(in1, tgd);
			//tool
			expCost = 1;
			builder.removeAll();
			out = builder.build();
			
			return;
		}
		return;
	}
	private void calcBookToBook(EnchBookBuilder ebb1,EnchBookBuilder ebb2) {
		boolean canBuild = false;
		for(Enchant en : ebb2.getEnchantList()) {
			int exp = ebb1.removeEnchant(en.getType());
			if(exp != 0) {
				expCost += exp;
				canBuild = true;
			}
		}
		if(canBuild) {
			if(ebb1.getEnchantList().isEmpty()){
				out = ItemBuilder.quickBuild(Material.BOOK, 1, null);
				return;
			}
			out = ebb1.build();
		}
	}
	private void calcBookToTool(EnchantBuilder builder,EnchBookBuilder book) {
		if(builder.isLocked()){
			isLocked = true;
			return;
		}
		isLocked = false;
		boolean canBuild = false;
		for(Enchant en : book.getEnchantList()) {
			int exp = builder.removeEnchant(en.getType());
			if(exp != 0) {
				expCost += exp;
				canBuild = true;
			}
		}
		if(canBuild) {
			out = builder.build();
		}
	}
	
	@Override
	public void onClose() {
		//アイテム保存
		if(in1 != null) {
			ItemUtil.giveItem(player, in1);	
		}
		if(in2 != null) {
			ItemUtil.giveItem(player, in2);
		}
		
		player.playSound(player.getLocation(), Sound.BLOCK_ENDER_CHEST_CLOSE, 1,1);
	}
	
	@Override
	public boolean onClick(InventoryClickEvent event) {
		int slot = event.getRawSlot();
		if(slot > 26) {
			ItemStack clicked = event.getCurrentItem();
			if(clicked == null) {
				return false;
			}
			if(event.getAction().equals(InventoryAction.COLLECT_TO_CURSOR)) {
				SoundManager.sendCancel(player);
				return true;
			}
			if(event.getClick().equals(ClickType.SHIFT_LEFT)) {
				if(in1 == null) {
					if(!clicked.getType().equals(Material.ENCHANTED_BOOK)) {
						if(!EnchManager.isTool(clicked.getType())) {
							SoundManager.sendCancel(player);
							return true;	
						}
					}
					in1 = clicked.clone();
				}else if(in2 == null){
					if(!clicked.getType().equals(Material.ENCHANTED_BOOK)) {
						SoundManager.sendCancel(player);
						return true;
					}
					in2 = clicked.clone();
				}else {
					return true;
				}
				Bukkit.getScheduler().runTask(GrowthSurvival.getInstance(), new Runnable() {
		    		@Override
		    		public void run() {
		    			
						event.getView().setItem(slot, null);
		    		}
				});
				rendering();
				SoundManager.sendPickUp(player);
				return true;
			}
			
			return false;
		}
		
		if(slot == 3) {
			//in1
			if(event.getCursor() != null) {
				ItemStack cursor = event.getCursor().clone();
				Material type = cursor.getType();
				if(!type.equals(Material.AIR)) {
					if(!type.equals(Material.ENCHANTED_BOOK)) {
						if(!EnchManager.isTool(cursor.getType())) {
							SoundManager.sendCancel(player);
							return true;	
						}
					}
				}
			}
			if(in1 == null) {
				if(event.getAction().equals(InventoryAction.SWAP_WITH_CURSOR)){
					in1 = event.getCursor().clone();
					rendering();
		    		Bukkit.getScheduler().runTask(GrowthSurvival.getInstance(), new Runnable() {
			    		@Override
			    		public void run() {
							event.getView().setCursor(null);
			    		}
					});
					SoundManager.sendPickUp(player);
				}
				return true;
			}
			
			Bukkit.getScheduler().runTask(GrowthSurvival.getInstance(), new Runnable() {
	    		@Override
	    		public void run() {
	    			ItemStack item = inv.getItem(3);
	    			if(item == null) {
		    			in1 = null;
	    			}else {	
	    				in1 = item.clone();
					}
	    			rendering();
	    		}
			});		
			SoundManager.sendPickUp(player);
			return false;
		}
		if(slot == 5) {
			//in2
			if(event.getCursor() != null) {
				ItemStack cursor = event.getCursor().clone();
				Material type = cursor.getType();
				if(!type.equals(Material.AIR)) {
					if(!type.equals(Material.ENCHANTED_BOOK)) {
						SoundManager.sendCancel(player);
						return true;
					}
				}
			}
			if(in2 == null) {
				if(event.getAction().equals(InventoryAction.SWAP_WITH_CURSOR)){
					ItemStack cursor = event.getCursor().clone();
					
					in2 = cursor;
					rendering();
		    		Bukkit.getScheduler().runTask(GrowthSurvival.getInstance(), new Runnable() {
			    		@Override
			    		public void run() {
							event.getView().setCursor(null);
			    		}
					});
					SoundManager.sendPickUp(player);
				}
				return true;
			}
			Bukkit.getScheduler().runTask(GrowthSurvival.getInstance(), new Runnable() {
	    		@Override
	    		public void run() {
	    			ItemStack item = inv.getItem(5);
	    			if(item == null) {
		    			in2 = null;
	    			}else {	
	    				in2 = item.clone();
					}
	    			rendering();
	    		}
			});		
			SoundManager.sendPickUp(player);
			return false;
		}
		if(slot == 7) {
			//out
			if(out != null) {
				playerExp = player.getLevel();	
				if(playerExp >= expCost) {
					player.setLevel(playerExp - expCost);
					SoundManager.sendSmith(player);
					ItemStack outItem = out.clone();
					Bukkit.getScheduler().runTask(GrowthSurvival.getInstance(), new Runnable() {
			    		@Override
			    		public void run() {
			    			if(ItemUtil.isNotAir(event.getView().getCursor())){
			    				ItemUtil.giveItem(player, outItem);
			    			}else {
			    				event.getView().setCursor(outItem);		
			    			}
			    		}
					});
					in1 = null;
					in2 = null;
					out = null;
					rendering();
					return true;
				}
			}
			SoundManager.sendCancel(player);
			return true;
		}
		
		
		if(slot == 12) {
			//決定ボタン
			if(out != null) {
				playerExp = player.getLevel();	
				if(playerExp >= expCost) {
					player.setLevel(playerExp - expCost);
					SoundManager.sendSmith(player);
					ItemUtil.giveItem(player, out);
					in1 = null;
					in2 = null;
					out = null;
					rendering();
					return true;
				}
			}
			SoundManager.sendCancel(player);
			return true;
		}
		if(slot == 9) {
			//修理へ
			onClose();
			close();
			new RepairGui().open(player);
			return true;
		}
		if(slot == 18) {
			//スクラップへ
			onClose();
			close();
			new ScrapGui().open(player);
			
			return true;
		}
		
		//その他無効な場所のクリック
		return true;
	}
}
