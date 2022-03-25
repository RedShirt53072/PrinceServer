package com.github.redshirt53072.trade.gui;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;

import com.github.redshirt53072.api.gui.Gui;
import com.github.redshirt53072.api.item.ItemBuilder;
import com.github.redshirt53072.api.message.LogManager;
import com.github.redshirt53072.api.message.MessageManager;
import com.github.redshirt53072.trade.TradeManager;
import com.github.redshirt53072.trade.data.TradeConfig.LevelData;
import com.github.redshirt53072.trade.data.TradeConfig.ProfessionData;
import com.github.redshirt53072.trade.data.TradeData;
import com.github.redshirt53072.trade.data.VillagerManager;
import com.github.redshirt53072.trade.data.VillagerManager.ProfData;

public class TradeTableGui  extends Gui{
	private ProfData prof;
	private List<TradeData> trades = new ArrayList<TradeData>();
	private int[] rolls = {0,0,0,0,0};
	private int[] amounts = {0,0,0,0,0};
	
	private int viewIndex = 1;
	private int warning = -1;
	
    public TradeTableGui(ProfData prof) {
    	super(TradeManager.getInstance());
    	this.prof = prof;
    }
    
    
    @Override
    public boolean onClick(InventoryClickEvent event){
    	int slot = event.getRawSlot();
		int index = viewIndex + slot / 9 - 1;
		
		if(slot < 0 || slot > 53) {
			
			if(event.getClick().equals(ClickType.DOUBLE_CLICK)) {
				return true;
			}
			
			return false;
		}
		
    	if(slot == 2) {
    		//save
    		if(warning < 0){
    			return true;
    		}
    		
    		if(warning > 0) {
    			MessageManager.sendWarning(warning + "個の交易で購入もしくは売却アイテムが未設定になっているため、保存ができません。", player);
        		player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1,0.5F);
    			return true;
    		}
    		
    		ProfessionData result = new ProfessionData(1);
    		
    		for(TradeData td : trades) {
    			MerchantRecipe recipe = td.convert();
    			if(recipe == null) {
        			continue;
    			}
    			result.getLevelData(td.getLevel()).addRecipe(recipe);
    		}
    		
    		for(int i = 1;i < 6;i++) {
    			result.getLevelData(i).setRoll(rolls[i - 1]);
    		}
    		
    		int version = VillagerManager.setProfessionData(prof.getProfession(), result);
    		warning = -1;
    		//職業表示
    		inv.setItem(0, ItemBuilder.quickBuild(prof.getIconItem(),1,ChatColor.WHITE + prof.getName() + "(これまでに" + ChatColor.GOLD + version + "回" + ChatColor.WHITE + "更新済み)"));
    		inv.setItem(2, new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setName(ChatColor.WHITE + "交易設定は最新の状態です").setModelData(3400).build());
    		player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1,0.5F);
    		return true;
    	}
    	if(slot == 3) {
    		//close
    		player.playSound(player.getLocation(), Sound.BLOCK_BARREL_CLOSE, 1,1);
    		close();
        	new HubGui().open(player);
    		return true;
    	}
    	if(slot < 9 && slot > 3) {
    		//アンロック数
    		
    		ClickType type = event.getClick();
    		if(type.equals(ClickType.RIGHT)) {
    			if(rolls[slot - 4] < 1) {
    				player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1,0.5F);
    				return true;
    			}
    			if(warning == -1) {
            		checkWarning();
        		}
    			rolls[slot - 4] -= 1;
        	    unlockRender();
    			player.playSound(player.getLocation(), Sound.BLOCK_SHULKER_BOX_CLOSE, 1,1);
        		return true;
    		}
    		if(type.equals(ClickType.MIDDLE) || type.equals(ClickType.DROP)) {
    			if(rolls[slot - 4] == 0) {
    				player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1,0.5F);
    				return true;
    			}
    			if(warning == -1) {
            		checkWarning();
        		}
    			rolls[slot - 4] = 0;
    			player.playSound(player.getLocation(), Sound.BLOCK_SHULKER_BOX_CLOSE, 1,1);
        	    unlockRender();
    			return true;
    		}
    		if(rolls[slot - 4] == 50) {
    			player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1,0.5F);
				return true;
			}
    		if(warning == -1) {
        		checkWarning();
    		}
    		rolls[slot - 4] += 1;
    		player.playSound(player.getLocation(), Sound.BLOCK_SHULKER_BOX_OPEN, 1,1);
    	    unlockRender();    		
    	    return true;
    	}
    	if(slot < 9) {
    		return true;
    	}
    	int line = slot % 9;
    	if(line == 0) {
    		//addRecipe
    		amounts[slot / 9 - 1] += 1;
    		trades.add(new TradeData(slot / 9));
    		Collections.sort(trades);
    		checkWarning();
    		render();
    		//総交易数
        	inv.setItem(1, new ItemBuilder(Material.BOOK).setName(ChatColor.WHITE + prof.getName() + "の総交易数:" + ChatColor.GOLD + trades.size() + "種類").setAmount(Math.max(trades.size(),1)).build());
    		player.playSound(player.getLocation(), Sound.UI_CARTOGRAPHY_TABLE_TAKE_RESULT, 1,1);
    	}
    	if(line == 2 || line == 3 || line == 5) {
    		//トレードアイテム
    		ItemStack newItem = event.getCursor().clone();
    		if(index > trades.size() || trades.size() == 0) {
    			return true;
    		}
    		
    		TradeData td = trades.get(index - 1);
    		ItemStack savedItem = null;
    		if(line == 2) {
    			savedItem = td.getBuy1();
			}
    		if(line == 3) {
    			savedItem = td.getBuy2();
    		}
    		if(line == 5) {
    			savedItem = td.getSell();
    		}
    		if(savedItem != null) {
    			if(!savedItem.getType().equals(Material.AIR)) {
    				player.playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1,1);
    	    		
    				if(line == 2) {
    					td.setBuy1(newItem);
    				}
    	    		if(line == 3) {
    					td.setBuy2(newItem);
    				}
    	    		if(line == 5) {
    					td.setSell(newItem);
    				}
    	    		trades.set(index - 1,td);
    	    		player.getOpenInventory().setCursor(savedItem);
    	    		setRecipe(slot / 9,td);
    	    		if(newItem != null) {
    	    			if(!(newItem.getType().equals(Material.AIR))){
    	    				return true;
    	        		}
    	    		}
    	    		checkWarning();
    				return true;	
        		}
    		}
    		if(newItem == null) {
				return true;
			}
			if(newItem.getType().equals(Material.AIR)) {
				return true;
			}
			
    		player.playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1,1);
			
    		if(line == 2) {
				td.setBuy1(newItem);
			}
    		if(line == 3) {
				td.setBuy2(newItem);
			}
    		if(line == 5) {
				td.setSell(newItem);
			}
    		trades.set(index - 1,td);		
    		setRecipe(slot / 9,td);
    		
    		checkWarning();
			return true;
    	}

    	if(line == 6) {
    		if(index > trades.size() || trades.size() == 0) {
    			return true;
    		}
    		//vilexpq
    		TradeData td = trades.get(index - 1);
    		if(warning == -1) {
        		checkWarning();
    		}
    		ClickType type = event.getClick();
    		if(type.equals(ClickType.RIGHT)) {
    			//plus5
        		if(td.getVilExp() == 50) {
            		player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1,0.5F);
        			return true;
        		}
    			td.setVilExp(Math.min(td.getVilExp() + 5,50));
        		trades.set(index - 1,td);
    			setRecipe(slot / 9 ,td);
    			player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1,0.8F);			
        		return true;
    		}
    		if(type.equals(ClickType.MIDDLE) || type.equals(ClickType.DROP)) {
    			//reset
    			if(td.getVilExp() == 0) {
            		player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1,0.5F);
        			return true;
        		}
    			td.setVilExp(0);
        		trades.set(index - 1,td);
    			setRecipe(slot / 9 ,td);
    			player.playSound(player.getLocation(), Sound.ITEM_BOTTLE_FILL, 1,1);
    			return true;
    		}
    		//plus1
    		if(td.getVilExp() == 50) {
        		player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1,0.5F);
    			return true;
    		}
    		td.setVilExp(Math.min(td.getVilExp() + 1,50));
    		trades.set(index - 1,td);
			setRecipe(slot / 9 ,td);
			player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1,1.5F);			
    		return true;
    	}
    	
    	if(line == 7) {
    		if(index > trades.size() || trades.size() == 0) {
    			return true;
    		}
    		//delete
    		amounts[trades.get(index - 1).getLevel() - 1] -= 1;
    		trades.remove(index - 1);
    		render();
        	checkWarning();
    		
    		//総交易数
        	inv.setItem(1, new ItemBuilder(Material.BOOK).setName(ChatColor.WHITE + prof.getName() + "の総交易数:" + ChatColor.GOLD + trades.size() + "種類").setAmount(Math.max(trades.size(),1)).build());
        	
    		player.playSound(player.getLocation(), Sound.BLOCK_BEACON_DEACTIVATE, 1,1);
    		return true;
    	}
    	
    	//スクロール
    	if(slot == 17) {
    		if(viewIndex <= 1) {
        		player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1,0.5F);
    			return true;
    		}
    		viewIndex = Math.max(viewIndex - 5,1);
    		
    		render();
    		player.playSound(player.getLocation(), Sound.UI_LOOM_TAKE_RESULT, 1,0.7F);
    		return true;
    	}
    	if(slot == 26) {
    		if(viewIndex <= 1) {
        		player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1,0.5F);
    			return true;
    		}
    		viewIndex = Math.max(viewIndex - 1,1);
    		
    		render();
    		player.playSound(player.getLocation(), Sound.UI_LOOM_TAKE_RESULT, 1,1);
    		return true;
    	}
    	if(slot == 44) {
    		if(viewIndex == trades.size()) {
    			player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1,0.5F);
    			return true;
    		}
    		viewIndex = Math.min(viewIndex + 1,trades.size());
    		
    		render();
    		player.playSound(player.getLocation(), Sound.UI_LOOM_TAKE_RESULT, 1,1);
    		return true;
    	}
    	if(slot == 53) {
    		if(viewIndex == trades.size()) {
    			player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1,0.5F);
    			return true;
    		}
    		viewIndex = Math.min(viewIndex + 5,trades.size());
    		
    		render();
    		player.playSound(player.getLocation(), Sound.UI_LOOM_TAKE_RESULT, 1,0.7F);
    		return true;
    	}
    	
    	return true;
    }
    
    @Override
	public void onRegister() {
    	ProfessionData baseData = VillagerManager.getProfessionData(prof.getProfession());
		for(int i = 1;i < 6; i++) {
			LevelData ld = baseData.getLevelData(i);
			rolls[i - 1] = ld.getRoll();
			amounts[i - 1] = ld.getAllRecipe().size();
			for(MerchantRecipe mr : ld.getAllRecipe()) {
				trades.add(new TradeData(i,mr));
			}
		}
		Collections.sort(trades);
		
    	inv = Bukkit.createInventory(null, 54, prof.getName() + "の交易設定");
    	//職業表示
		inv.setItem(0, ItemBuilder.quickBuild(prof.getIconItem(),1,ChatColor.WHITE + prof.getName() + "(これまでに" + ChatColor.GOLD + baseData.getVersion() + "回" + ChatColor.WHITE + "更新済み)"));
		//総交易数
    	inv.setItem(1, ItemBuilder.quickBuild(Material.BOOK,Math.max(trades.size(),1),ChatColor.WHITE + prof.getName() + "の総交易数:" + ChatColor.GOLD + trades.size() + "種類"));
    	
		//保存とキャンセル
    	inv.setItem(2, new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setName(ChatColor.WHITE + "交易設定は最新の状態です").setModelData(3400).build());
		inv.setItem(3, new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setName(ChatColor.WHITE + "キャンセルして職業一覧に戻る").setModelData(3401).build());
		
		
		//スクロールボタン
		inv.setItem(17, new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setName(ChatColor.WHITE +"上に5個スクロール").setModelData(3312).build());
		inv.setItem(26, new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setName(ChatColor.WHITE +"上に1個スクロール").setModelData(3302).build());
		inv.setItem(44, new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setName(ChatColor.WHITE +"下に1個スクロール").setModelData(3303).build());
		inv.setItem(53, new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setName(ChatColor.WHITE +"下に5個スクロール").setModelData(3313).build());
		
		unlockRender();
		render();
		
		player.openInventory(inv);
		
		player.playSound(player.getLocation(), Sound.UI_LOOM_SELECT_PATTERN, 1,1);
	}
	
	@Override
	public void onClose() {
		player.playSound(player.getLocation(), Sound.BLOCK_BARREL_CLOSE, 1,1);
	}
    
	private void checkWarning() {
		warning = 0;
		for(TradeData td : trades) {
			if(td.getSell() != null) {
				if(td.getBuy1() != null) {
					if(!td.getSell().getType().equals(Material.AIR)) {
						if(!td.getBuy1().getType().equals(Material.AIR)) {
							continue;
						}
					}
				}
			}
			warning ++;
		}
		
		if(warning == 0) {
			inv.setItem(2, new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setName(ChatColor.WHITE + "交易設定への編集を保存する").setModelData(3404).build());	
			return;
		}
		ArrayList<String> lore = new ArrayList<String>();
		lore.add(ChatColor.RED.toString() + warning + "個の交易で購入もしくは売却アイテムが未設定になっています");
		inv.setItem(2, new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setName(ChatColor.WHITE + "交易設定への編集を保存する").setLore(lore).setAmount(Math.max(1,Math.min(warning,64))).setModelData(3406).build());
	}
	
	private void render() {
		for(int i = 0;i < 45;i ++) {
			int line = i % 9;
			if(line == 0 || line == 8) {
				continue;
			}
			setEmptyItem(i + 9);
		}
		//交易
		if(viewIndex > trades.size()) {
			viewIndex = trades.size();
		}
		if(viewIndex == 0 && trades.size() > 0) {
			viewIndex = 1;
		}
		
		//交易追加ボタン
		for(int i = 1;i < 6;i++) {
			inv.setItem(i * 9, new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setName(ChatColor.WHITE.toString() + i + "レベルで開放される交易を追加する").setAmount(Math.max(1,amounts[i - 1])).setModelData(3030 + i).build());
		}
		
		//スクロール位置
		inv.setItem(35, ItemBuilder.quickBuild(Material.BOOK,Math.max(1,Math.min(viewIndex, 64)),ChatColor.WHITE + "現在" + viewIndex + "～" +  Math.min(viewIndex + 4,trades.size()) + "個目の交易を表示中"));
		
		for(int i = 0;i < 5;i++){
			if(viewIndex + i > trades.size() || trades.size() == 0){
				break;
			}
			setRecipe(i + 1,trades.get(viewIndex + i - 1));
		}
		
			
	}
	

	private void unlockRender() {
		//交易アンロック上限数ボタン
		for(int i = 1;i < 6;i++) {
			setUnlockRecipe(i);
		}
	}
	
	
	private void setUnlockRecipe(int level){
    	int roll = rolls[level - 1];
    	
		ArrayList<String> rollLore = new ArrayList<String>();
		rollLore.add(ChatColor.WHITE + "設定可能な交易アンロック上限数の範囲:" + ChatColor.GOLD + "0 ～ 50");
		rollLore.add(ChatColor.GREEN + "" + ChatColor.UNDERLINE + "左クリック" + ChatColor.RESET + ChatColor.WHITE + "で交易アンロック上限数を" + ChatColor.GOLD + "1" + ChatColor.WHITE + "増やす");
		rollLore.add(ChatColor.GREEN + "" + ChatColor.UNDERLINE + "右クリック" + ChatColor.RESET + ChatColor.WHITE + "で交易アンロック上限数を" + ChatColor.GOLD + "1" + ChatColor.WHITE + "減らす");
		rollLore.add(ChatColor.GREEN + "" + ChatColor.UNDERLINE + "Qキーもしくはミドルクリック" + ChatColor.RESET + ChatColor.WHITE + "で交易アンロック上限数を" + ChatColor.GOLD + "0" + ChatColor.WHITE + "に設定");
    	int model = 3020;
		if(roll > 0) {
			model = 3000;
    	}
		if(roll > 50) {
			roll = 50;
		}
    	inv.setItem(3 + level, new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setName(ChatColor.WHITE + "交易アンロック上限数:" + roll).setLore(rollLore).setAmount(Math.max(roll,1)).setModelData(model + level).build());	
    }
	
    private void setRecipe(int location,TradeData trade) {
    	ItemStack buy1 = trade.getBuy1();
    	ItemStack buy2 = trade.getBuy2();
    	if(buy1 == null) {
    		inv.setItem(location * 9 + 2, new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setName(ChatColor.WHITE + "( 空 )").setModelData(3405).build());
        }else if(buy1.getType().equals(Material.AIR)){
        	inv.setItem(location * 9 + 2, new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setName(ChatColor.WHITE + "( 空 )").setModelData(3405).build());
        }else {	
        	inv.setItem(location * 9 + 2, buy1);	
    	}
    	if(buy2 == null) {
    		inv.setItem(location * 9 + 3, new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setName(ChatColor.WHITE + "( 空 )").setModelData(3405).build());	
        }else if(buy2.getType().equals(Material.AIR)){
        	inv.setItem(location * 9 + 3, new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setName(ChatColor.WHITE + "( 空 )").setModelData(3405).build());
        }else {
        	inv.setItem(location * 9 + 3, buy2);
        }

		inv.setItem(location * 9 + 4, new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setName(" ").setModelData(3300).build());
		ItemStack sell = trade.getSell();
		if(sell == null) {
			inv.setItem(location * 9 + 5, new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setName(ChatColor.WHITE + "( 空 )").setModelData(3405).build());
		}else if(sell.getType().equals(Material.AIR)){
        	inv.setItem(location * 9 + 5, new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setName(ChatColor.WHITE + "( 空 )").setModelData(3405).build());
        }else {
			inv.setItem(location * 9 + 5, sell);
		}
		int exp = trade.getVilExp();
		ArrayList<String> expLore = new ArrayList<String>();
		expLore.add(ChatColor.WHITE + "設定可能な経験値の範囲:" + ChatColor.GOLD + "0EXP ～ 50EXP");
		expLore.add(ChatColor.GREEN + "" + ChatColor.UNDERLINE + "左クリック" + ChatColor.RESET + ChatColor.WHITE + "で経験値を" + ChatColor.GOLD + "1EXP" + ChatColor.WHITE + "増やす");
		expLore.add(ChatColor.GREEN + "" + ChatColor.UNDERLINE + "右クリック" + ChatColor.RESET + ChatColor.WHITE + "で経験値を" + ChatColor.GOLD + "5EXP" + ChatColor.WHITE + "増やす");
		expLore.add(ChatColor.GREEN + "" + ChatColor.UNDERLINE + "Qキーもしくはミドルクリック" + ChatColor.RESET + ChatColor.WHITE + "で経験値を" + ChatColor.GOLD + "0EXP" + ChatColor.WHITE + "に設定");
		if(exp < 1) {
			inv.setItem(location * 9 + 6, new ItemBuilder(Material.GLASS_BOTTLE).setName(ChatColor.WHITE + "村人への経験値:" + ChatColor.GOLD + "0EXP").setLore(expLore).build());
		}else {
			if(exp > 50) {
				exp = 50;
			}
			inv.setItem(location * 9 + 6, new ItemBuilder(Material.EXPERIENCE_BOTTLE).setName(ChatColor.WHITE + "村人への経験値:" + ChatColor.GOLD + exp + "EXP").setLore(expLore).setAmount(exp).build());
		}
		inv.setItem(location * 9 + 7, new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setName(ChatColor.WHITE + "この交易を" + ChatColor.RED + "削除する").setModelData(3402).build());
		
		int level = trade.getLevel();
		if(sell != null) {
			if(buy1 != null || buy2 != null) {
				inv.setItem(location * 9 + 1, new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setName(ChatColor.WHITE + "開放レベル:" + ChatColor.GOLD + level + "lv").setModelData(3000 + level).build());		
				return;
			}
		}
		inv.setItem(location * 9 + 1, new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setName(ChatColor.WHITE + "開放レベル:" + ChatColor.GOLD + level + "lv").setModelData(3010 + level).build());
    }
}
