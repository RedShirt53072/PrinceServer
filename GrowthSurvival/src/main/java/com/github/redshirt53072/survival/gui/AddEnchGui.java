package com.github.redshirt53072.survival.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;

import com.github.redshirt53072.api.item.ItemBuilder;
import com.github.redshirt53072.api.item.ItemUtil;
import com.github.redshirt53072.api.message.LogManager;
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

public class AddEnchGui extends EnchGui{
	private ItemStack in1 = null;
	private ItemStack in2 = null;
	private ItemStack out = null;
	
	private boolean isLocked = false;
	
	public AddEnchGui() {
	    super();	    
	}
	
	@Override
	public void onRegister() {
		inv = Bukkit.createInventory(null, 27, "エンチャント合成モード");

		setEmptyItem(1,2,8,10,11,17,18,19,20,21,22,23,24,25,26);
		
		List<String> lore = new ArrayList<String>();
		lore.add(TextBuilder.quickBuild(ChatColor.WHITE, "ツールとエンチャント本を合成することができます。"));
		lore.add(TextBuilder.quickBuild(ChatColor.GRAY, "合成にかかる経験値コストは合成する"));
		lore.add(TextBuilder.quickBuild(ChatColor.GRAY, "エンチャントの種類やレベルに応じて高くなります。"));
		
		inv.setItem(0, new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setName(new TextBuilder(ChatColor.WHITE)
				.addText("-エンチャント合成モード-")
				.addColorText(ChatColor.YELLOW,"<選択中>")
				.build()).setModelData(3602).setLore(lore).setDummyEnch().build());
		
		List<String> lore2 = new ArrayList<String>();
		lore2.add(TextBuilder.quickBuild(ChatColor.WHITE, "アイテムに名前をつけることができます。"));
		lore2.add(TextBuilder.quickBuild(ChatColor.GRAY, "このモードは素手の状態で設置された金床を"));
		lore2.add(new TextBuilder(ChatColor.WHITE).addClick("スニークしながら右クリック").addText("すると開くことができます。").build());
		
		inv.setItem(9, new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setName(TextBuilder.quickBuild(ChatColor.WHITE,"-アイテム名編集モード-"))
				.setModelData(3501).setLore(lore2).build());	
		
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
    				inv.setItem(3, new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setName(TextBuilder.quickBuild(ChatColor.GRAY,"元のアイテムのスロット")).setModelData(3405).build());	
    			}else {
    				inv.setItem(3, in1.clone());
    			}
    			//in2
    			if(in2 == null) {
    				inv.setItem(5, new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setName(TextBuilder.quickBuild(ChatColor.GRAY,"追加するアイテムのスロット")).setModelData(3405).build());	
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
    						inv.setItem(7, new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setName(TextBuilder.quickBuild(ChatColor.RED,"最大レベルに達しているか組み合わせが間違っています。")).setModelData(3401).build());		
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
			//in1はツールかエンチャ本
			//in2はエンチャ本か同じMaterialのツール
			EnchBookBuilder ebb1 = EnchBookBuilder.getBuilder(in1);
			if(ebb1 != null) {
				EnchBookBuilder ebb2 = EnchBookBuilder.getBuilder(in2);
				if(ebb2 != null) {
					//book to book
					calcBookToBook(ebb1,ebb2);
					return;
				}
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
			
			EnchBookBuilder ebb2 = EnchBookBuilder.getBuilder(in2);
			if(ebb2 != null) {
				//book to tool
				
				if(td.getToolGroup().equals(ToolGroup.broken)) {
					BrokenReader builder = BrokenReader.getBuilder(in1,true);
					
					calcBookToTool(builder,ebb2);
					return;
				}
				EnchBuilder builder = new EnchBuilder(in1,tgd);
				
				calcBookToTool(builder,ebb2);
				return;
			}
			
			ToolData td2 = EnchManager.getToolData(in2.getType());
			if(td2 == null) {
				return;
			}
			if(td.getToolGroup().equals(ToolGroup.broken)) {
				boolean is2Broken = td2.getToolGroup().equals(ToolGroup.broken);
				
				BrokenReader builder1 = BrokenReader.getBuilder(in1,is2Broken);
				if(builder1 == null) {
					return;
				}
				td = builder1.getToolData();
				
				if(is2Broken) {
					BrokenReader builder2 = BrokenReader.getBuilder(in1,false);
					if(builder2 == null) {
						return;
					}
					td2 = builder2.getToolData();
					if(!td.equals(td2)) {
						return;
					}
					
					calcToolToTool(builder1,builder2,td,false,true);
					return;
				}
				
				if(!td.equals(td2)) {
					return;
				}
				
				EnchBuilder builder2 = new EnchBuilder(in2,tgd);
				
				calcToolToTool(builder1,builder2,td,true,true);
				return;
			}

			EnchBuilder builder1 = new EnchBuilder(in1,tgd);
			
			if(td2.getToolGroup().equals(ToolGroup.broken)) {
				BrokenReader builder2 = BrokenReader.getBuilder(in1,false);
				if(builder2 == null) {
					return;
				}
				td2 = builder2.getToolData();
				if(!td.equals(td2)) {
					return;
				}
				
				calcToolToTool(builder1,builder2,td,false,true);
				return;
			}
			
			if(!td.equals(td2)) {
				return;
			}
			//tool to tool
			EnchBuilder builder2 = new EnchBuilder(in2,tgd);
			
			calcToolToTool(builder1,builder2,td,true,false);
			
			return;
		}
		return;
	}
	
	private void calcToolToTool(EnchantBuilder builder1,EnchantBuilder builder2,ToolData td,boolean calcDamage,boolean maxDamage) {
		if(builder1.isLocked()){
			isLocked = true;
			return;
		}
		if(builder2.isLocked()){
			isLocked = true;
			return;
		}
		isLocked = false;
		boolean canBuild = false;
		for(Enchant en : builder2.getEnchantList()) {
			int exp = builder1.addEnchant(en);
			if(exp != 0) {
				expCost += exp;
				canBuild = true;
			}
		}
		if(calcDamage) {
			//耐久計算
			ItemStack item1 = builder1.getItem();
			Damageable meta1 = (Damageable) item1.getItemMeta();
			int damage;
			int max = in2.getType().getMaxDurability();
			if(maxDamage) {
				damage = max;
			}else {
				damage = meta1.getDamage();
			}
			
			if(damage != 0) {
				canBuild = true;
				Damageable meta2 = (Damageable) in2.getItemMeta();
				double dur = max - meta2.getDamage();
				double ratio = dur / max;
				double unitDamage = EnchManager.getUnitDamage(td, builder1.getEnchantList());
				double useScrap = td.getScrap() * ratio;
				
				int repair = (int)Math.ceil(unitDamage * useScrap);
				damage -= repair;
				if(damage < 0) {
					damage = 0;
				}
				
				meta1.setDamage(damage);
				item1.setItemMeta(meta1);
				expCost += Math.max((int)Math.ceil(useScrap / 5),1);
				
			}
		}
		
		if(canBuild) {
			out = builder1.build();
		}
	}
	private void calcBookToBook(EnchBookBuilder ebb1,EnchBookBuilder ebb2) {
		boolean canBuild = false;
		for(Enchant en : ebb2.getEnchantList()) {
			int exp = ebb1.addEnchant(en);
			if(exp != 0) {
				expCost += exp;
				canBuild = true;
			}
		}
		if(canBuild) {
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
			int exp = builder.addEnchant(en);
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
					if(in1.getType().equals(Material.ENCHANTED_BOOK)) {
						if(!clicked.getType().equals(Material.ENCHANTED_BOOK)) {
							SoundManager.sendCancel(player);
							return true;
						}
					}else {
						if(!clicked.getType().equals(Material.ENCHANTED_BOOK)) {
							if(!EnchManager.isTool(clicked.getType())) {
								SoundManager.sendCancel(player);
								return true;	
							}
						}
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
					if(in1 != null && in1.getType().equals(Material.ENCHANTED_BOOK)) {
						if(!type.equals(Material.ENCHANTED_BOOK)) {
							SoundManager.sendCancel(player);
							return true;
						}
					}else {
						if(!type.equals(Material.ENCHANTED_BOOK)) {
							if(!EnchManager.isTool(cursor.getType())) {
								SoundManager.sendCancel(player);
								return true;	
							}
						}
					}
				}
			}
			
			if(in2 == null) {
				if(event.getAction().equals(InventoryAction.SWAP_WITH_CURSOR)){
					in2 = event.getCursor().clone();
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
		if(slot == 0) {
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
		
		
		//その他無効な場所のクリック
		return true;
	}
}
