package com.github.redshirt53072.growthsurvival.collection;


import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.InventoryClickEvent;

import com.github.redshirt53072.growthapi.gui.Gui;
import com.github.redshirt53072.growthapi.item.ItemBuilder;
import com.github.redshirt53072.growthapi.message.TextBuilder;
import com.github.redshirt53072.growthapi.server.GrowthPlugin;

public abstract class CollectionGui extends Gui{
	protected ItemCollection itemCol;
	protected int contentsSize;
	protected int trueContentsSize;
	protected int viewLine = 1;
	
	public CollectionGui(GrowthPlugin plugin,ItemCollection itemCol) {
		super(plugin);
		this.itemCol = itemCol;
	}
	
	@Override
    public boolean onClick(InventoryClickEvent event){
    	int slot = event.getRawSlot();
		
		if(slot < 0 || slot > 53) {
			return true;
		}

    	if(slot == 0 || slot == 9) {
    		return true;
    	}
    	if(slot < 8 && slot > 0) {
    		//メインソート
    		onMainSortClick(event);
    		return true;
    	}
    	if(slot == 8) {
    		//hubに戻る
    		player.playSound(player.getLocation(), Sound.BLOCK_BARREL_CLOSE, 1,1);
    		close();
        	new CollectionHubGui().open(player);
    		return true;
    	}
    	int line = slot % 9;
    	if(line == 0) {
    		onSubSortClick(event);
    		return true;
    	}
    	if(line == 8) {
    		//スクロール
        	if(slot == 17) {
        		if(viewLine <= 1) {
            		player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1,0.5F);
        			return true;
        		}
        		viewLine = Math.max(viewLine - 5,1);
        		
        		rendering();
        		player.playSound(player.getLocation(), Sound.UI_LOOM_TAKE_RESULT, 1,0.7F);
        		return true;
        	}
        	if(slot == 26) {
        		if(viewLine <= 1) {
            		player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1,0.5F);
        			return true;
        		}
        		viewLine = Math.max(viewLine - 1,1);
        		
        		rendering();
        		player.playSound(player.getLocation(), Sound.UI_LOOM_TAKE_RESULT, 1,1);
        		return true;
        	}
        	if(slot == 44) {
        		if(viewLine == getContentsLine()) {
        			player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1,0.5F);
        			return true;
        		}
        		viewLine = Math.min(viewLine + 1,getContentsLine());
        		
        		rendering();
        		player.playSound(player.getLocation(), Sound.UI_LOOM_TAKE_RESULT, 1,1);
        		return true;
        	}
        	if(slot == 53) {
        		if(viewLine == getContentsLine()) {
        			player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1,0.5F);
        			return true;
        		}
        		viewLine = Math.min(viewLine + 5,getContentsLine());
        		
        		rendering();
        		player.playSound(player.getLocation(), Sound.UI_LOOM_TAKE_RESULT, 1,0.7F);
        		return true;
        	}
        	return true;
    	}
    	//図鑑の中身をクリック
    	onContentsClick(event);
    	return true;
    }
	
	@Override
	public void onRegister() {
		
    	inv = Bukkit.createInventory(null, 54, itemCol.getName());
    	
		//静的なボタン
    	inv.setItem(0, new ItemBuilder(itemCol.getIconItem()).setName(TextBuilder.quickBuild(ChatColor.WHITE,itemCol.getName())).build());
		inv.setItem(8, new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setName(TextBuilder.quickBuild(ChatColor.WHITE,"図鑑一覧に戻る")).setModelData(3401).build());
		
		//スクロールボタン
		inv.setItem(17, new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setName(TextBuilder.quickBuild(ChatColor.WHITE,"上に5個スクロール")).setModelData(3312).build());
		inv.setItem(26, new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setName(TextBuilder.quickBuild(ChatColor.WHITE,"上に1個スクロール")).setModelData(3302).build());
		inv.setItem(44, new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setName(TextBuilder.quickBuild(ChatColor.WHITE,"下に1個スクロール")).setModelData(3303).build());
		inv.setItem(53, new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setName(TextBuilder.quickBuild(ChatColor.WHITE,"下に5個スクロール")).setModelData(3313).build());
		
		onRegisterLoad();
		
		rendering();
		
		player.openInventory(inv);
		
		player.playSound(player.getLocation(), Sound.UI_LOOM_SELECT_PATTERN, 1,1);
	}
	
	@Override
	public void onClose() {
		player.playSound(player.getLocation(), Sound.BLOCK_BARREL_CLOSE, 1,1);
	}
	
	protected int getContentsLine() {
		int size = contentsSize;
		return size / 7 + 1;
	}
	
	protected void rendering() {
		//ViewLine補正
		int line = getContentsLine();
		if(viewLine > line) {
			viewLine = line;
		}
		if(viewLine == 0 && line > 0) {
			viewLine = 1;
		}
		
		//検索結果
    	String name = new TextBuilder(ChatColor.WHITE)
    			.addText("コレクション数:")
    			.addNumText(contentsSize, "個")
    			.addText("/",String.valueOf(trueContentsSize),"個")
    			.build();
    	List<String> lore = new ArrayList<String>();
    	int per = trueContentsSize * 100;
    	per /= contentsSize;
    	if(per == 100) {
        	lore.add(new TextBuilder(ChatColor.WHITE).addClick("コレクション完了率：").addColorText(ChatColor.AQUA,String.valueOf(per),"%").build());
    	}else if(per > 80) {
    		lore.add(new TextBuilder(ChatColor.WHITE).addClick("コレクション完了率：").addColorText(ChatColor.GREEN,String.valueOf(per),"%").build());	
    	}else if(per > 50) {
    		lore.add(new TextBuilder(ChatColor.WHITE).addClick("コレクション完了率：").addColorText(ChatColor.YELLOW,String.valueOf(per),"%").build());	
    	}else{
    		lore.add(new TextBuilder(ChatColor.WHITE).addClick("コレクション完了率：").addColorText(ChatColor.GRAY,String.valueOf(per),"%").build());
    	}
    	
    	List<String> sort = getSortList();
    	if(!sort.isEmpty()) {
    		lore.add(TextBuilder.quickBuild(ChatColor.WHITE,"条件"));	
    	}
    	for(String lo : sort) {
    		lore.add(TextBuilder.quickBuild(ChatColor.WHITE,"-",lo));
    	}
		inv.setItem(9, new ItemBuilder(Material.PAPER).setName(name).setAmount(contentsSize).setLore(lore).build());
    	
		//スクロールインデックス
		inv.setItem(35, ItemBuilder.quickBuild(Material.BOOK,viewLine * 7 - 6,TextBuilder.quickBuild(ChatColor.WHITE,"現在"
				,String.valueOf(Math.max(viewLine * 7 - 6,0))
				,"～",String.valueOf(Math.min(viewLine * 7 + 28,contentsSize))
				,"個目の交易を表示中")));
		
		//コンテンツ
		renderingContents();
		//メインソート
		renderingMainSort();
		//サブソート
		renderingSubSort();
	}
	
	protected abstract void onRegisterLoad();
	
	protected abstract void onContentsClick(InventoryClickEvent event);

	protected abstract void onSubSortClick(InventoryClickEvent event);
	
	protected abstract void onMainSortClick(InventoryClickEvent event);
	
	protected abstract List<String> getSortList();
	
	protected abstract void renderingContents();
	
	protected abstract void renderingMainSort();
	
	protected abstract void renderingSubSort();	
}