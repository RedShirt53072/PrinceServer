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
import com.github.redshirt53072.api.item.ItemLibrary;
import com.github.redshirt53072.api.item.ItemUtil;
import com.github.redshirt53072.api.message.SoundManager;
import com.github.redshirt53072.api.message.TextBuilder;
import com.github.redshirt53072.survival.GrowthSurvival;
import com.github.redshirt53072.survival.ench.BrokenReader;
import com.github.redshirt53072.survival.ench.EnchBuilder;
import com.github.redshirt53072.survival.ench.EnchManager;
import com.github.redshirt53072.survival.ench.ToolData;
import com.github.redshirt53072.survival.ench.ToolData.ToolGroup;
import com.github.redshirt53072.survival.ench.ToolData.ToolGroupData;

public class RepairGui extends EnchGui{
	private ItemStack in1 = null;
	private ItemStack in2 = null;
	private ItemStack out = null;
	private int remainderScrap = 0;
	
	public RepairGui() {
	    super();
	}
	
	@Override
	public void onRegister() {
		inv = Bukkit.createInventory(null, 27, "修理モード");

		setEmptyItem(1,2,8,10,11,17,19,20,21,22,23,24,25,26);
		
		List<String> lore = new ArrayList<String>();
		lore.add(TextBuilder.quickBuild(ChatColor.WHITE, "ツールからエンチャントを削除することができます。"));
		lore.add(new TextBuilder(ChatColor.WHITE).addClick("クリック").addText("でエンチャント削除モードに切り替え").build());
		
		
		inv.setItem(0, new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setName(new TextBuilder(ChatColor.WHITE)
				.addText("-エンチャント削除モード-")
				.build()).setLore(lore).setModelData(3502).build());
		
		List<String> lore2 = new ArrayList<String>();
		lore2.add(TextBuilder.quickBuild(ChatColor.WHITE, "スクラップでツールを修理することができます。"));
		lore2.add(TextBuilder.quickBuild(ChatColor.GRAY, "ツールを1つ目、スクラップを2つ目のスロットに入れます。"));
		lore2.add(TextBuilder.quickBuild(ChatColor.GRAY, "スクラップ1個で回復できる耐久力は、"));
		lore2.add(TextBuilder.quickBuild(ChatColor.GRAY, "エンチャントが多く付いているほど少なくなります。"));
		lore2.add(TextBuilder.quickBuild(ChatColor.GRAY, "経験値コストはスクラップ5個あたり1レベルかかります。"));
		
		inv.setItem(9, new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setName(new TextBuilder(ChatColor.WHITE)
				.addText("-修理モード-")
				.addColorText(ChatColor.YELLOW,"<選択中>")
				.build()).setLore(lore2).setDummyEnch().setModelData(3600).build());	
		
		List<String> lore3 = new ArrayList<String>();
		lore3.add(TextBuilder.quickBuild(ChatColor.WHITE, "鉱石をスクラップに加工することができます。"));
		lore3.add(new TextBuilder(ChatColor.WHITE).addClick("クリック").addText("でスクラップモードに切り替え").build());
		
		inv.setItem(18, new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setName(TextBuilder.quickBuild(ChatColor.WHITE,"-スクラップモード-"))
				.setLore(lore3).setModelData(3503).build());	
		
		
		inv.setItem(4, new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setName(" ").setModelData(3413).build());	
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
    				inv.setItem(3, new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setName(TextBuilder.quickBuild(ChatColor.GRAY,"ツールのスロット")).setModelData(3405).build());	
    			}else {
    				inv.setItem(3, in1.clone());
    			}
    			//in2
    			if(in2 == null) {
    				inv.setItem(5, new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setName(TextBuilder.quickBuild(ChatColor.GRAY,"スクラップのスロット")).setModelData(3405).build());	
    			}else {
    				inv.setItem(5, in2.clone());
    			}
    			//calc
    			remainderScrap = 0;
				expCost = 0;
				out = null;
    			calcOut();
    			
    			//out
    			if(out == null) {
    				if(in1 == null || in2 == null) {
    					inv.setItem(7, new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setName(TextBuilder.quickBuild(ChatColor.RED,"スロットにアイテムを入れてください。")).setModelData(3401).build());
    				}else {
    					ItemMeta meta = in1.getItemMeta();
    					if(meta instanceof Damageable) {
    						if(((Damageable)meta).hasDamage()) {
    							inv.setItem(7, new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setName(TextBuilder.quickBuild(ChatColor.RED,"正しいアイテムを入れてください。")).setModelData(3401).build());			
    						}else {
    							inv.setItem(7, new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setName(TextBuilder.quickBuild(ChatColor.RED,"このツールは既に最大まで修理されています。")).setModelData(3401).build());
    						}
    					}else {
    						inv.setItem(7, new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setName(TextBuilder.quickBuild(ChatColor.RED,"正しいアイテムを入れてください。")).setModelData(3401).build());		
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
			//in1はツール、in2はスクラップ

			if(!in2.isSimilar(ItemLibrary.getScrap())) {
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
				BrokenReader builder = BrokenReader.getBuilder(in1,false);
				if(builder == null) {
					return;
				}
				td = builder.getToolData();
				int amount = in2.getAmount();
				
				double unitDamage = EnchManager.getUnitDamage(td, builder.getEnchantList());
				
				ItemStack item = builder.getItem();
				Damageable meta = (Damageable) item.getItemMeta();
				int damage = td.getType().getMaxDurability();
				
				int useScrap = (int)Math.ceil(damage / unitDamage);
				useScrap = Math.min(amount, useScrap);
				
				damage -= (int)Math.ceil(useScrap * unitDamage);
				if(damage < 0) {
					damage = 0;
				}
				
				meta.setDamage(damage);
				item.setItemMeta(meta);
				
				remainderScrap = Math.max(amount - useScrap,0);
				expCost = Math.max(useScrap / 5,1);
				out = builder.build();
				return;
			}
			
			
			EnchBuilder builder = new EnchBuilder(in1,tgd);
			int amount = in2.getAmount();
			
			double unitDamage = EnchManager.getUnitDamage(td, builder.getEnchantList());
			
			ItemStack item = builder.getItem();
			Damageable meta = (Damageable) item.getItemMeta();
			if(!meta.hasDamage()) {
				return;
			}
			int damage = meta.getDamage();
			
			
			
			int useScrap = (int)Math.ceil(damage / unitDamage);
			useScrap = Math.min(amount, useScrap);
			
			damage -= (int)Math.ceil(useScrap * unitDamage);
			if(damage < 0) {
				damage = 0;
			}
			
			meta.setDamage(damage);
			item.setItemMeta(meta);
			
			remainderScrap = Math.max(amount - useScrap,0);
			expCost = Math.max(useScrap / 5,1);
			out = builder.build();
			return;
		}
		return;
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
					if(!EnchManager.isTool(clicked.getType())) {
						SoundManager.sendCancel(player);
						return true;	
					}
					ToolData td = EnchManager.getToolData(clicked.getType());
					if(td.getToolGroup().equals(ToolGroup.horse_armor)) {
						SoundManager.sendCancel(player);
						return true;
					}
					in1 = clicked.clone();
				}else if(in2 == null){
					if(!clicked.isSimilar(ItemLibrary.getScrap())) {
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
					if(!EnchManager.isTool(cursor.getType())) {
						SoundManager.sendCancel(player);
						return true;	
					}
					ToolData td = EnchManager.getToolData(cursor.getType());
					if(td.getToolGroup().equals(ToolGroup.horse_armor)) {
						SoundManager.sendCancel(player);
						return true;
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
					if(!cursor.isSimilar(ItemLibrary.getScrap())) {
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
					if(remainderScrap == 0) {
						in2 = null;	
					}else {
						in2 = new ItemBuilder(ItemLibrary.getScrap()).setAmount(remainderScrap).build();
					}
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
					if(remainderScrap == 0) {
						in2 = null;	
					}else {
						in2 = new ItemBuilder(ItemLibrary.getScrap()).setAmount(remainderScrap).build();
					}
					out = null;
					rendering();
					return true;
				}
			}
			SoundManager.sendCancel(player);
			return true;
		}
		if(slot == 0) {
			//エンチャ削除へ
			onClose();
			close();
			new RemoveEnchGui().open(player);
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
