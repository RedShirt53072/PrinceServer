package com.github.redshirt53072.trademanager.gui;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;

import com.github.redshirt53072.growthapi.gui.Gui;
import com.github.redshirt53072.growthapi.message.MessageManager;
import com.github.redshirt53072.trademanager.TradeManager;
import com.github.redshirt53072.trademanager.data.TradeConfig.LevelData;
import com.github.redshirt53072.trademanager.data.TradeConfig.ProfessionData;
import com.github.redshirt53072.trademanager.data.VillagerManager;
import com.github.redshirt53072.trademanager.data.VillagerManager.ProfData;
import com.github.redshirt53072.trademanager.data.VillagerManager.TradeData;

public class TradeTableGui  extends Gui{
	private ProfData prof;
	private List<TradeData> trades = new ArrayList<TradeData>();
	private int[] rolls = {0,0,0,0,0};
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
    		
    		VillagerManager.setProfessionData(prof.getProfession(), result);
    		warning = -1;
    		inv.setItem(2, createItem(Material.WHITE_STAINED_GLASS_PANE,ChatColor.WHITE + "交易設定は最新の状態です",null,1,null,3400));
    		player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1,0.5F);
    		return true;
    	}
    	if(slot == 3) {
    		//close
    		player.playSound(player.getLocation(), Sound.BLOCK_BARREL_CLOSE, 1,1);
    		close();
        	new HubGui().registerPlayer(player);
    		return true;
    	}
    	if(slot < 9 && slot > 3) {
    		//アンロック数
    		if(warning == -1) {
    			warning = 0;
    		}
    		ClickType type = event.getClick();
    		if(type.equals(ClickType.RIGHT)) {
        		rolls[slot - 3] -= 1;
        	    unlockRender();
    			player.playSound(player.getLocation(), Sound.BLOCK_SHULKER_BOX_CLOSE, 1,1);
        		return true;
    		}
    		if(type.equals(ClickType.MIDDLE)) {
        		rolls[slot - 3] = 0;
    			player.playSound(player.getLocation(), Sound.BLOCK_SHULKER_BOX_CLOSE, 1,1);
        	    unlockRender();
    			return true;
    		}

    		rolls[slot - 3] += 1;
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
    		trades.add(new TradeData(slot / 9));
    		checkWarning();
    		render();
    		//総交易数
        	inv.setItem(0, createItem(Material.BOOK,ChatColor.WHITE + prof.getName() + "の総交易数:" + ChatColor.GOLD + trades.size() + "種類",null,1,null,0));
    		player.playSound(player.getLocation(), Sound.UI_CARTOGRAPHY_TABLE_TAKE_RESULT, 1,1);
    	}
    	if(line == 2 || line == 3 || line == 5) {
    		//トレードアイテム
    		ItemStack old = event.getCurrentItem();
    		ItemStack newItem = event.getCursor();
    		TradeData td = trades.get(index);
			if(old.getItemMeta().getCustomModelData() == 3405) {
    			if(newItem == null) {
    				return true;
    			}
        		player.playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1,1);
    			event.setCurrentItem(null);
        		if(line == 2) {
    				td.setBuy1(newItem);
    			}
        		if(line == 3) {
    				td.setBuy2(newItem);
    			}
        		if(line == 5) {
    				td.setSell(newItem);
    			}
        		checkWarning();
    			return false;
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
    		if(newItem == null) {
				event.setCurrentItem(createItem(Material.WHITE_STAINED_GLASS_PANE,ChatColor.WHITE + "( 空 )",null,1,null,3405));
				checkWarning();
    		}
    		return false;
    	}

    	if(line == 6) {
    		//vilexp
    		TradeData td = trades.get(index);
    		if(warning == -1) {
    			warning = 0;
    		}
    		ClickType type = event.getClick();
    		if(type.equals(ClickType.RIGHT)) {
    			//plus5
    			td.setVilExp(Math.min(td.getVilExp() + 5,50));
    			setRecipe(slot / 9 ,td);
    			player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1,0.8F);			
        		return true;
    		}
    		if(type.equals(ClickType.MIDDLE)) {
    			//reset
    			td.setVilExp(0);
    			setRecipe(slot / 9 ,td);
    			player.playSound(player.getLocation(), Sound.ITEM_BOTTLE_FILL, 1,1);
    			return true;
    		}
    		//plus1
    		td.setVilExp(Math.min(td.getVilExp() + 1,50));
			setRecipe(slot / 9 ,td);
			player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1,1.5F);			
    		return true;
    	}
    	
    	if(line == 7) {
    		//delete
    		trades.remove(index);
    		render();
    		if(warning == -1) {
    			warning = 0;
    		}
    		//総交易数
        	inv.setItem(0, createItem(Material.BOOK,ChatColor.WHITE + prof.getName() + "の総交易数:" + ChatColor.GOLD + trades.size() + "種類",null,1,null,0));
        	
    		player.playSound(player.getLocation(), Sound.BLOCK_BEACON_DEACTIVATE, 1,1);
    		return true;
    	}
    	
    	//スクロール
    	if(slot == 17) {
    		viewIndex = Math.min(viewIndex + 5,trades.size());
    		
    		render();
    		player.playSound(player.getLocation(), Sound.UI_LOOM_TAKE_RESULT, 1,0.7F);
    		return true;
    	}
    	if(slot == 26) {
    		viewIndex = Math.min(viewIndex + 1,trades.size());
    		
    		render();
    		player.playSound(player.getLocation(), Sound.UI_LOOM_TAKE_RESULT, 1,1);
    		return true;
    	}
    	if(slot == 44) {
    		viewIndex = Math.max(viewIndex - 1,1);
    		
    		render();
    		player.playSound(player.getLocation(), Sound.UI_LOOM_TAKE_RESULT, 1,1);
    		return true;
    	}
    	if(slot == 53) {
    		viewIndex = Math.max(viewIndex - 5,1);
    		
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
			rolls[i] = ld.getRoll();
			for(MerchantRecipe mr : ld.getAllRecipe()) {
				trades.add(new TradeData(i,mr));
			}
		}
		Collections.sort(trades);
		
    	inv = Bukkit.createInventory(null, 54, prof.getName() + "の交易設定");
    	//職業表示
		inv.setItem(0, createItem(prof.getIconItem(),ChatColor.WHITE + prof.getName() + "(これまでに" + ChatColor.GOLD + baseData.getVersion() + "回" + ChatColor.WHITE + "更新済み)",null,1,null,0));
		//総交易数
    	inv.setItem(0, createItem(Material.BOOK,ChatColor.WHITE + prof.getName() + "の総交易数:" + ChatColor.GOLD + trades.size() + "種類",null,1,null,0));
    	
		//保存とキャンセル
    	inv.setItem(2, createItem(Material.WHITE_STAINED_GLASS_PANE,ChatColor.WHITE + "交易設定は最新の状態です",null,1,null,3400));
		inv.setItem(3, createItem(Material.WHITE_STAINED_GLASS_PANE,ChatColor.WHITE + "キャンセルして職業一覧に戻る",null,1,null,3401));
		
		//交易追加ボタン
		for(int i = 1;i < 6;i++) {
			inv.setItem(i * 9, createItem(Material.WHITE_STAINED_GLASS_PANE,ChatColor.WHITE.toString() + i + "レベルで開放される交易を追加する",null,1,null,3030 + i));
		}
		
		//スクロールボタン
		inv.setItem(17, createItem(Material.WHITE_STAINED_GLASS_PANE,ChatColor.WHITE +"上に5個スクロール",null,1,null,3312));
		inv.setItem(26, createItem(Material.WHITE_STAINED_GLASS_PANE,ChatColor.WHITE +"上に1個スクロール",null,1,null,3302));
		inv.setItem(44, createItem(Material.WHITE_STAINED_GLASS_PANE,ChatColor.WHITE +"下に1個スクロール",null,1,null,3303));
		inv.setItem(53, createItem(Material.WHITE_STAINED_GLASS_PANE,ChatColor.WHITE +"下に5個スクロール",null,1,null,3313));
		
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
				if(td.getBuy1() != null || td.getBuy2() != null) {
					continue;
				}
			}
			warning ++;
		}
		
		if(warning == 0) {
			inv.setItem(2, createItem(Material.WHITE_STAINED_GLASS_PANE,ChatColor.WHITE + "交易設定への編集を保存する",null,1,null,3404));	
			return;
		}
		ArrayList<String> lore = new ArrayList<String>();
		lore.add(ChatColor.RED.toString() + warning + "個の交易で購入もしくは売却アイテムが未設定になっています");
		inv.setItem(2, createItem(Material.WHITE_STAINED_GLASS_PANE,ChatColor.WHITE + "交易設定への編集を保存する",lore,Math.min(warning,64),null,3406));
	}
	
	private void render() {
		for(int i = 0;i < 45;i ++) {
			int line = i % 9;
			if(line == 0 || line == 8) {
				continue;
			}
			inv.setItem(9 + i, createItem(Material.WHITE_STAINED_GLASS_PANE," ",null,1,null,0));
		}
		
		//交易
		if(viewIndex > trades.size()) {
			viewIndex = trades.size();
		}
		for(int i = 0;i < 5;i++){
			if(viewIndex + i > trades.size()){
				return;
			}
			setRecipe(i,trades.get(viewIndex + i));
		}
		
		//スクロール位置
		inv.setItem(35, createItem(Material.BOOK,ChatColor.WHITE + "現在" + viewIndex + "～" +  Math.min(viewIndex + 4,trades.size()) + "個目の交易を表示中",null,Math.min(viewIndex, 64),null,0));
				
	}
	

	private void unlockRender() {
		//交易アンロック上限数ボタン
		for(int i = 1;i < 6;i++) {
			setUnlockRecipe(i);
		}
	}
	
	
	private void setUnlockRecipe(int level){
    	int roll = rolls[level];
    	
		ArrayList<String> rollLore = new ArrayList<String>();
		rollLore.add(ChatColor.WHITE + "設定可能な交易アンロック上限数の範囲:" + ChatColor.GOLD + "0 ~ 50");
		rollLore.add(ChatColor.WHITE + "-左クリックで交易アンロック上限数を" + ChatColor.GOLD + "1" + ChatColor.WHITE + "増やす");
		rollLore.add(ChatColor.WHITE + "-右クリックで交易アンロック上限数を" + ChatColor.GOLD + "1" + ChatColor.WHITE + "減らす");
		rollLore.add(ChatColor.WHITE + "-ミドルクリックで交易アンロック上限数を" + ChatColor.GOLD + "0" + ChatColor.WHITE + "に設定");
    	int model = 3020;
		if(roll > 0) {
			model = 3000;
    	}
		if(roll > 50) {
			roll = 50;
		}
    	inv.setItem(3 + level, createItem(Material.WHITE_STAINED_GLASS_PANE,ChatColor.WHITE + "交易アンロック上限数:" + roll,rollLore,roll,null,model + level));	
    }
	
    private void setRecipe(int location,TradeData trade) {
    	ItemStack buy1 = trade.getBuy1();
    	ItemStack buy2 = trade.getBuy2();
    	if(buy1 == null) {
    		inv.setItem(location * 9 + 2, createItem(Material.WHITE_STAINED_GLASS_PANE,ChatColor.WHITE + "( 空 )",null,1,null,3405));
        }else {	
        	inv.setItem(location * 9 + 2, buy1);	
    	}
    	if(buy2 == null) {
    		inv.setItem(location * 9 + 3, createItem(Material.WHITE_STAINED_GLASS_PANE,ChatColor.WHITE + "( 空 )",null,1,null,3405));    		
        }else {
        	inv.setItem(location * 9 + 3, buy2);	    		
        }

		inv.setItem(location * 9 + 4, createItem(Material.WHITE_STAINED_GLASS_PANE,"",null,1,null,3300));
		ItemStack sell = trade.getSell();
		if(sell == null) {
			inv.setItem(location * 9 + 5, createItem(Material.WHITE_STAINED_GLASS_PANE,ChatColor.WHITE + "( 空 )",null,1,null,3405));
		}else {
			inv.setItem(location * 9 + 5, sell);
		}
		int exp = trade.getVilExp();
		ArrayList<String> expLore = new ArrayList<String>();
		expLore.add(ChatColor.WHITE + "設定可能な経験値の範囲:" + ChatColor.GOLD + "0EXP ~ 50EXP");
		expLore.add(ChatColor.WHITE + "-左クリックで経験値を" + ChatColor.GOLD + "1EXP" + ChatColor.WHITE + "増やす");
		expLore.add(ChatColor.WHITE + "-右クリックで経験値を" + ChatColor.GOLD + "5EXP" + ChatColor.WHITE + "増やす");
		expLore.add(ChatColor.WHITE + "-ミドルクリックで経験値を" + ChatColor.GOLD + "0EXP" + ChatColor.WHITE + "に設定");
		if(exp < 1) {
			inv.setItem(location * 9 + 6, createItem(Material.GLASS_BOTTLE,ChatColor.WHITE + "村人への経験値:" + ChatColor.GOLD + "0EXP",expLore,1,null,0));
		}else {
			if(exp > 50) {
				exp = 50;
			}
			inv.setItem(location * 9 + 6, createItem(Material.EXPERIENCE_BOTTLE,ChatColor.WHITE + "村人への経験値:" + ChatColor.GOLD + exp + "EXP",expLore,exp,null,0));
		}
		inv.setItem(location * 9 + 7, createItem(Material.WHITE_STAINED_GLASS_PANE,ChatColor.WHITE + "この交易を" + ChatColor.RED + "削除する",null,1,null,3402));
		
		int level = trade.getLevel();
		if(sell != null) {
			if(buy1 != null || buy2 != null) {
				inv.setItem(location * 9 + 1, createItem(Material.WHITE_STAINED_GLASS_PANE,ChatColor.WHITE + "開放レベル:" + ChatColor.GOLD + level + "lv",null,1,null,3000 + level));		
				return;
			}
		}
		inv.setItem(location * 9 + 1, createItem(Material.WHITE_STAINED_GLASS_PANE,ChatColor.WHITE + "開放レベル:" + ChatColor.GOLD + level + "lv",null,1,null,3010 + level));
    }
    
    
    
	
}
