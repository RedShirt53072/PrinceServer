package com.github.redshirt53072.newfishing.gui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.github.redshirt53072.newfishing.NewFishing;
import com.github.redshirt53072.growthapi.gui.Gui;
import com.github.redshirt53072.growthapi.message.MessageManager;
import com.github.redshirt53072.newfishing.nbt.FishNBT;



public class SellFishGui extends Gui{
		
	ArrayList<ItemStack> fishes = new ArrayList<ItemStack>();
	int price = 0;

	public SellFishGui() {
	    super(NewFishing.getInstance());	    
	}

	private void setEmptyItem(int slot) {
		inv.setItem(slot, createItem(Material.WHITE_STAINED_GLASS_PANE," ",null,1,null,0));
	}
	
	@Override
	public void onRegister() {
		inv = Bukkit.createInventory(null, 54, "魚をクリックして鑑定");
		setEmptyItem(36);
		inv.setItem(37, createItem(Material.DROPPER,ChatColor.WHITE + "一括返却",null,1,null,-1));	
		inv.setItem(38, createItem(Material.HOPPER,ChatColor.WHITE + "一括鑑定",null,1,null,-1));	
		setEmptyItem(39);
		inv.setItem(40, createItem(Material.GOLD_NUGGET,ChatColor.WHITE + "売却価格：" + ChatColor.GOLD + "0Ɇ",null,1,null,-1));	
		setEmptyItem(41);
		inv.setItem(42, createItem(Material.RED_WOOL,ChatColor.WHITE + "鑑定済みの魚がありません",null,1,null,-1));	
		setEmptyItem(43);
		setEmptyItem(44);
		setEmptyItem(45);
		for(int i = 46;i < 51;i ++) {
			inv.setItem(i, createItem(Material.WHITE_STAINED_GLASS_PANE," ",null,1,null,0));	
		}
		inv.setItem(51, createItem(Material.WHITE_STAINED_GLASS_PANE," ",null,1,null,3010));
		inv.setItem(52, createItem(Material.WHITE_STAINED_GLASS_PANE," ",null,1,null,3020));	
		setEmptyItem(53);
		
	}
	
	

	@Override
	public void onClose() {
		//返却
		for(int i = fishes.size() - 1; i > -1;i--) {
			removeItem(i,player);
		}
	}
	
	
	@Override
	public boolean onClick(InventoryClickEvent event) {
		int slot = event.getRawSlot();
		ItemStack clickedItem;
		if(slot > 53) {
			int trueSlot = slot - 54;
			if(trueSlot < 27) {
				trueSlot += 9;
			}else {
				trueSlot -= 27;
			}
			clickedItem = player.getInventory().getContents()[trueSlot];
		}else if (-1 < slot) {
			clickedItem = inv.getItem(slot);
		}else {
			clickedItem = null;
		}
		if(slot > 53) {
			//インベントリ内のアイテムをクリック。
			//魚なら鑑定へ。
			if(clickedItem == null) {
				return true;	
			}
			if(null == new FishNBT(plugin,clickedItem).getFishid()) {
				//エラー音
				SoundManager.sendCancel(player);
				MessageManager.sendImportant("鑑定できるのは魚だけです。", player);
				return true;
			}
			if(addItem(clickedItem,player)){
				MessageManager.sendImportant("同時にはこれ以上鑑定できません。", player);
				return true;
			}
			updateGui();
			int trueSlot = slot - 54;
			if(trueSlot < 27) {
				trueSlot += 9;
			}else {
				trueSlot -= 27;
			}
			player.getInventory().clear(trueSlot);
			return true;
		}
		if(slot < 36){
			if(clickedItem == null) {
				return true;	
			}
			//魚をクリック
			//返却へ
			removeItem(slot,player);
			updateGui();
			return true;
		}
		if(slot == 37) {	
			//一括返却
			for(int i = fishes.size() - 1; i > -1;i--) {
				removeItem(i,player);
			}
			updateGui();
			return true;
		}
		if(slot == 38) {	
			//一括鑑定
			ItemStack[] items = player.getInventory().getContents();
			for(int i = 0;i < items.length;i++){
				ItemStack item = items[i];
				if(item == null) {
					continue;
				}
				if(null == new FishNBT(plugin,item).getFishid()) {
					continue;
				}
				if(addItem(item,player)){
					updateGui();
					MessageManager.sendImportant("同時にはこれ以上鑑定できません。", player);
					return true;
				}
				player.getInventory().clear(i);
			}
			updateGui();
			return true;
		}
		if(slot == 42) {	
			//売却できるなら売却
			if(price == 0) {
				SoundManager.sendCancel(player);
				MessageManager.sendImportant("魚が一匹も鑑定されていないため、売却できませんでした。", player);
				return true;
			}
			sell(player);
			updateGui();
			return true;
		}
		//その他無効な場所のクリック
		return true;
	}
	
	private boolean addItem(ItemStack item,Player p) {
		if(fishes.size() > 35) {
			return true;
		}
		SoundManager.sendClick(p);
		fishes.add(item);
		return false;
	}
	
	private void removeItem(int index,Player p) {
		SoundManager.sendClick(p);
		if(-1 == p.getInventory().firstEmpty()){
			//足元ドロップ
			Item i = (Item) p.getWorld().spawnEntity(p.getLocation(), EntityType.DROPPED_ITEM);
			i.setItemStack(fishes.get(index));
			i.setPickupDelay(0);
			i.setOwner(p.getUniqueId());
			i.setGlowing(true);
			i.setInvulnerable(true);
			i.setCustomName(p.getName() + "の落とし物");
			i.setCustomNameVisible(true);
			fishes.remove(index);
			return;
		}
		p.getInventory().addItem(fishes.get(index));
		fishes.remove(index);
	}
		
	private void updateGui() {
		//魚アイテムの更新
		for(int i = 0;i < 36;i ++) {
			inv.clear(i);
		}
		int totalPrice = 0;
		for(int i = 0;i < fishes.size();i ++) {
			ItemStack f = fishes.get(i);
			ItemStack fish = f.clone();
			ItemMeta meta = fish.getItemMeta();
			List<String> lore = meta.getLore();
			lore.add(ChatColor.WHITE + "----------------");
			int pr = calcPrice(f);
			totalPrice += pr;
			lore.add(ChatColor.WHITE + "値段：" + ChatColor.GOLD + pr + "vil");
			lore.add(ChatColor.GREEN + "クリックで返却");
			meta.setLore(lore);
			fish.setItemMeta(meta);
			inv.setItem(i,fish);
		}
		
		//値段更新
		
		price = totalPrice;
		inv.setItem(40, createItem(Material.GOLD_NUGGET,ChatColor.WHITE + "売却価格：" + ChatColor.GOLD + price + "vil",null,1,null,0));	
		
		for(int i = 46;i < 51;i ++) {
			inv.setItem(i, createItem(Material.WHITE_STAINED_GLASS_PANE," ",null,1,null,0));	
		}
		
		for(int i = 1;true;i ++) {
			int number = totalPrice % 10;
			totalPrice /= 10;
			
			int model = 3010 + number;
			
			inv.setItem(52 - i, createItem(Material.WHITE_STAINED_GLASS_PANE," ",null,1,null,model));	
			
			if(i > 5) {
				break;
			}
			if(totalPrice < 1) {
				break;
			}
		}
		
		//決定ボタン更新
		if(price > 0) {
			inv.setItem(42, createItem(Material.LIME_WOOL,ChatColor.WHITE + "クリックで魚を全て売却",null,1,null,-1));		
		}else {
			inv.setItem(42, createItem(Material.RED_WOOL,ChatColor.WHITE + "鑑定済みの魚がありません",null,1,null,-1));	
		}
	}
	
	private int calcPrice(ItemStack item) {
		//価格計算
		FishNBT nbt = new FishNBT(plugin,item);
		int price = 0;
		switch(nbt.getRarity()) {
		case 1:
			price = 1;
			break;
		case 2:
			price = 2;
			break;
		case 3:
			price = 4;
			break;
		case 4:
			price = 8;
			break;
		case 5:
			price = 20;
			break;
		case 6:
			price = 50;
			break;
		case 7:
			price = 500;
			break;
		default:
			price = 1;		
		}
		switch(nbt.isKing()) {
		case 1:
			price *= 2;
			break;
		case 2:
			price *= 3;
			break;
		}
		if(nbt.isRareType() == 1) {
			price *= 5;
		}
		return price;
	}
	
	private void sell(Player player) {
		SoundManager.sendSuccess(player);
		//vilを渡す
		
		
		Bukkit.getScoreboardManager().getMainScoreboard().getObjective("vil_score").getScore(player.getName()).setScore(price);
		
    	MessageManager.sendImportant(fishes.size() + "匹の魚を売却し、" + ChatColor.GOLD +  price + "vil" + ChatColor.WHITE + "受け取りました。", player);
		
		//アイテム削除
		fishes.clear();
		price = 0;
	}
	
}
