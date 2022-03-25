package com.gmail.akashirt53072.minegame.gui;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.gmail.akashirt53072.minegame.Main;
import com.gmail.akashirt53072.minegame.SoundManager;
import com.gmail.akashirt53072.minegame.config.datatype.MapLocData;
import com.gmail.akashirt53072.minegame.match.MatchManager;


public class SelectMapGui extends Gui{
	ArrayList<MapLocData> data;
	MapLocData nowMap;
	int length;
	int maxPage;
	int nowPage;
	
	public SelectMapGui(Main plugin,boolean isPrivate) {
	    super(plugin,isPrivate,GuiID.SELECTMAP);
	}

	@Override
	public void onCreate() {
		inv = Bukkit.createInventory(null, 27, "マップ選択");
		for(int index = 0;index < 27;index ++) {
			inv.setItem(index, createItem(Material.BLACK_STAINED_GLASS_PANE,"読み込み中...",null,1,null,-1));
		}
	}

	public void updateMapData(ArrayList<MapLocData> data,MapLocData nowMap) {
		//インスタンスにデータ保存+GUI表示
		//被り削除
		ArrayList<MapLocData> newData = new ArrayList<MapLocData>(); 
		for(MapLocData m : data) {
			boolean isOriginal = true;
			if(m.getType().equals(nowMap.getType())) {
				isOriginal = false;
			}
			for(MapLocData n : newData) {
				if(m.getType().equals(n.getType())){
					isOriginal = false;
					break;
				}
			}
			if(isOriginal) {
				newData.add(m);
			}
		}
		
		this.data = newData;
		this.nowMap = nowMap;
		nowPage = 1;
		
		length = newData.size();
		//ページ数計算
		if(length < 27) {
			maxPage = 1;
		}else if(length < 51){
			maxPage = 2;
		}else{
			int extra = (length - 51 ) / 24;
			maxPage = 3 + extra;
		}
		
		updateGui();
	}
	
	private void updateGui() {
		inv.clear();
		ArrayList<String> lore = new ArrayList<String>();
		lore.add(ChatColor.GOLD + "現在選択中");
		inv.setItem(0, createItem(nowMap.getIcon(),ChatColor.WHITE + nowMap.getName(),lore,1,Enchantment.MENDING,-1));
		if(maxPage == 1) {
			for(int i = 0;i < data.size();i ++) {
				inv.setItem(i + 1, createItem(data.get(i).getIcon(),ChatColor.WHITE + data.get(i).getName(),null,1,null,-1));		
			}
			return;
		}
		if(nowPage == 1) {
			inv.setItem(26, createItem(Material.ARROW,ChatColor.WHITE + "次のページへ",null,1,null,-1));
			for(int i = 0;i < 25;i ++) {
				inv.setItem(i + 1, createItem(data.get(i).getIcon(),ChatColor.WHITE + data.get(i).getName(),null,1,null,-1));		
			}
			return;
		}
		inv.setItem(18, createItem(Material.ARROW,ChatColor.WHITE + "前のページへ",null,1,null,-1));
		if(nowPage == maxPage) {
			for(int i = 0;i < 17;i ++) {
				int index = i + 25 + (maxPage - 2) * 24;
				inv.setItem(i + 1, createItem(data.get(index).getIcon(),ChatColor.WHITE + data.get(index).getName(),null,1,null,-1));		
			}
			for(int i = 18;i < 26;i ++) {
				int index = i + 24 + (maxPage - 2) * 24;
				inv.setItem(i + 1, createItem(data.get(index).getIcon(),ChatColor.WHITE + data.get(index).getName(),null,1,null,-1));		
			}
			return;
		}
		inv.setItem(26, createItem(Material.ARROW,ChatColor.WHITE + "次のページへ",null,1,null,-1));
		for(int i = 0;i < 17;i ++) {
			int index = i + 25 + (maxPage - 2) * 24;
			inv.setItem(i + 1, createItem(data.get(index).getIcon(),ChatColor.WHITE + data.get(index).getName(),null,1,null,-1));		
		}
		for(int i = 18;i < 25;i ++) {
			int index = i + 24 + (maxPage - 2) * 24;
			inv.setItem(i + 1, createItem(data.get(index).getIcon(),ChatColor.WHITE + data.get(index).getName(),null,1,null,-1));		
		}
	}
	

	
	@Override
	public void addPlayer(Player player) {
		super.addPlayer(player);
	}


	@Override
	public boolean onClick(int slot,ItemStack cursor,Player player) {
		if(slot > 26) {
			return false;
		}
		if(slot == 0){
			return true;
		}
		if(slot == 18) {	
			if(nowPage != 1) {
				nowPage --;
				updateGui();
				SoundManager.sendClick(player);
				return true;	
			}
		}
		if(slot == 26) {	
			if(nowPage < maxPage) {
				nowPage ++;
				updateGui();
				SoundManager.sendClick(player);
				return true;	
			}
		}
		if(slot < 0) {
			return false;
		}
		MapLocData map = nowMap;
		if(nowPage == 1) {
			if(slot - 1 >= data.size()) {
				return true;
			}
			map = data.get(slot - 1);
		}else {
			if(slot < 18) {
				if(slot + (nowPage - 1) * 24 >= data.size()) {
					return true;
				}
				map = data.get(slot + (nowPage - 1) * 24);
			}else {
				if(slot - 1 + (nowPage - 1) * 24 >= data.size()) {
					return true;
				}
				map = data.get(slot - 1 + (nowPage - 1) * 24);
			}
		}
		SoundManager.sendSelect(player);
		new MatchManager(plugin).changeMap(player,map);	
		return true;
	}
	
	@Override
	public void close(Player p) {
		super.close(p);
	}
	@Override
	public void onClose(Player p) {
		super.onClose(p);
	}
	
	@Override
	public void onRemove() {
	}
}
