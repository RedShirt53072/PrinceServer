package com.github.redshirt53072.dimmanager.gui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.InventoryClickEvent;

import com.github.redshirt53072.dimmanager.DimManager;
import com.github.redshirt53072.dimmanager.Teleporter;
import com.github.redshirt53072.dimmanager.data.DimData;
import com.github.redshirt53072.dimmanager.data.DimID;
import com.github.redshirt53072.dimmanager.data.WorldManager;
import com.github.redshirt53072.growthapi.gui.Gui;
import com.github.redshirt53072.growthapi.item.ItemBuilder;
import com.github.redshirt53072.growthapi.message.MessageManager;
import com.github.redshirt53072.growthapi.message.SoundManager;
import com.github.redshirt53072.growthapi.message.TextBuilder;
import com.github.redshirt53072.growthapi.player.PlayerManager;

public class DimGui extends Gui{
	
	private List<DimData> dimList;
	private DimData nowDim;
	private boolean isHome = false;
	
	public DimGui() {
	    super(DimManager.getInstance());	    
	}
	
	@Override
	public void onRegister() {
		inv = Bukkit.createInventory(null, 27, "ワールドを移動する");
		
		nowDim = WorldManager.getDimData(player.getWorld().getUID());
		dimList = new ArrayList<DimData>();
		dimList.addAll(WorldManager.getWorlds());
		
		inv.setItem(0,ItemBuilder.quickBuild(nowDim.getID().getIcon(), 1, new TextBuilder(ChatColor.WHITE)
				.addText("現在のワールド：",nowDim.getID().getName())
				.build()));
		
		setEmptyItem(2,9,11,18,19,20);
		
		for(int i = 0;i < 18;i ++) {
			int index = i;
			if(i < 6) {
				index += 3;
			}else if(i < 12) {
				index += 6;
			}else {
				index += 9;
			}
			if(i < dimList.size()) {
				inv.setItem(index, new ItemBuilder(dimList.get(i).getID().getIcon()).setName(new TextBuilder(ChatColor.WHITE)
						.addClick("クリック")
						.addText("で",dimList.get(i).getID().getName(),"に移動する")
						.build()).build());
				continue;
			}
			setEmptyItem(index);
		}
		for(DimData dd : WorldManager.getHomeData()) {
			if(dd.equals(nowDim)) {
				isHome = true;
				break;
			}
		}
		if(isHome) {
			inv.setItem(1, new ItemBuilder(Material.RED_BED).setName(new TextBuilder(ChatColor.WHITE)
					.addClick("クリック")
					.addText("でホームに移動する")
					.build()).build());
			
			List<String> lore = new ArrayList<String>();
			lore.add(new TextBuilder(ChatColor.WHITE)
					.addText("ホーム地点は各ワールドに1つずつ設定可能です。")
					.build());
			lore.add(new TextBuilder(ChatColor.WHITE)
					.addText("ホーム機能は/homeのコマンドからも使用できます。")
					.build());
			inv.setItem(10, new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setName(new TextBuilder(ChatColor.WHITE)
					.addClick("クリック")
					.addText("でホームを現在の位置に設定する")
					.build()).setLore(lore).setModelData(3404).build());
			
		}else {
			setEmptyItem(1,10);
		}
		
		player.playSound(player.getLocation(), Sound.BLOCK_ENDER_CHEST_OPEN, 1,1);
		player.openInventory(inv);
	}
	
	
	@Override
	public void onClose() {
		player.playSound(player.getLocation(), Sound.BLOCK_ENDER_CHEST_CLOSE, 1,1);
	}
	
	@Override
	public boolean onClick(InventoryClickEvent event) {
		int slot = event.getRawSlot();
		if(slot > 26) {
			//インベントリ内のアイテムをクリック。
			return true;
		}
		if(isHome) {
			if(!PlayerManager.isAsyncLocked(player, "dim")) {
				if(slot == 1) {
					close();
					Teleporter.teleportSavedLocation(player, nowDim);
					MessageManager.sendInfo("ホームに移動しました。" , player);
					return true;
				}
				if(slot == 10) {
					new WorldManager().writeLoc(player, nowDim.getName(), player.getLocation());
					SoundManager.sendClick(player);
					MessageManager.sendInfo(TextBuilder.plus(nowDim.getID().getName(),"でのホームを設定しました。") , player);
					return true;
				}
			}
		}
		if(2 < slot && slot < 9){
			int index = slot - 3;
			if(index < dimList.size()) {
				teleport(index);
			}
			return true;
		}
		if(11 < slot && slot < 18){
			int index = slot - 6;
			if(index < dimList.size()) {
				teleport(index);
			}
			return true;
		}
		if(20 < slot && slot < 27){
			int index = slot - 9;
			if(index < dimList.size()) {
				teleport(index);
			}
			return true;
		}
		//その他無効な場所のクリック
		return true;
	}
	private void teleport(int index) {
		DimData dim = dimList.get(index);
		close();
		if(dim.getID().equals(DimID.normal)) {
			Teleporter.teleportSavedLocation(player, dim);
		}else {
			Teleporter.teleportDefault(player, dim);
		}
		MessageManager.sendInfo(TextBuilder.plus(dim.getName(),"に移動しました。"), player);
	}
	
}
