package com.github.redshirt53072.swallowfishing.gui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.github.redshirt53072.growthapi.gui.MoneyGui;
import com.github.redshirt53072.growthapi.item.ItemBuilder;
import com.github.redshirt53072.growthapi.item.ItemUtil;
import com.github.redshirt53072.growthapi.message.MessageManager;
import com.github.redshirt53072.growthapi.message.SoundManager;
import com.github.redshirt53072.growthapi.money.MoneyManager;
import com.github.redshirt53072.swallowfishing.SwallowFishing;
import com.github.redshirt53072.swallowfishing.nbt.FishNBT;



public class SellFishGui extends MoneyGui{
		
	private ArrayList<ItemStack> fishes = new ArrayList<ItemStack>();
	private int price = 0;
	private int amount = 0;	

	public SellFishGui() {
	    super(SwallowFishing.getInstance());	    
	}
	
	@Override
	public void onRegister() {
		inv = Bukkit.createInventory(null, 54, "魚を鑑定して売却する");
		for(int i = 27;i < 38;i ++) {
			setEmptyItem(i);	
		}
		setEmptyItem(39,41,43,44);
		
		inv.setItem(38, new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setName(ChatColor.WHITE + "すべて鑑定").setModelData(3407).build());	
		inv.setItem(40, new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setName(ChatColor.WHITE + "魚をすべて売却").setModelData(3409).build());	
		inv.setItem(42, new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setName(ChatColor.WHITE + "すべて返却").setModelData(3408).build());	
		
		String text = ChatColor.WHITE + "売却価格：" + ChatColor.GOLD + "0Ɇ";
		renderingMoney(5, price, 
			new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setName(text).setModelData(3200).build(), text);
		
		player.playSound(player.getLocation(), Sound.BLOCK_BARREL_OPEN, 1,1);
		player.openInventory(inv);
	}
	
	
	@Override
	public void onClose() {
		//返却
		for(int i = fishes.size() - 1; i > -1;i--) {
			removeItem(i);
		}
		player.playSound(player.getLocation(), Sound.BLOCK_BARREL_CLOSE, 1,1);
	}
	
	@Override
	public boolean onClick(InventoryClickEvent event) {
		int slot = event.getRawSlot();
		ItemStack clickedItem = event.getCurrentItem();
		if(slot > 53) {
			//インベントリ内のアイテムをクリック。
			//魚なら鑑定へ。
			if(clickedItem == null) {
				return true;	
			}
			FishNBT nbt = new FishNBT(clickedItem);
			if(null == nbt.getFishName()) {
				SoundManager.sendCancel(player);
				return true;
			}
			if(addItem(clickedItem)){
				SoundManager.sendCancel(player);
				return true;
			}
			updateGui();
			player.getInventory().clear(event.getSlot());
			return true;
		}
		if(slot < 27){
			if(clickedItem == null) {
				return true;
			}
			//魚をクリック
			//返却へ
			removeItem(slot);
			updateGui();
			return true;
		}
		if(slot == 38) {	
			//一括返却
    		if(fishes.isEmpty()) {
    			SoundManager.sendCancel(player);
    		}else {
    			player.playSound(player.getLocation(), Sound.UI_LOOM_TAKE_RESULT, 1,1);
    		}
			for(int i = fishes.size() - 1; i > -1;i--) {
				removeItem(i);
			}
			updateGui();
			return true;
		}
		if(slot == 42) {	
    		//一括鑑定
    		boolean done = false;
			ItemStack[] items = player.getInventory().getContents();
			for(int i = 0;i < items.length;i++){
				ItemStack item = items[i];
				if(item == null) {
					continue;
				}
				FishNBT nbt = new FishNBT(item);
				if(null == nbt.getFishName()) {
					continue;
				}
				if(addItem(item)){
					break;
				}
				done = true;
				player.getInventory().clear(i);
			}
			if(done) {
				player.playSound(player.getLocation(), Sound.UI_LOOM_TAKE_RESULT, 1,1);
			}else {
				SoundManager.sendCancel(player);	
			}
			
			updateGui();
			return true;
		}
		if(slot == 40) {	
			//売却できるなら売却
			if(fishes.isEmpty()) {
				SoundManager.sendCancel(player);
				return true;
			}
			sell();
			updateGui();
			inv.setItem(40, new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setName(ChatColor.WHITE + "魚をすべて売却").setModelData(3400).build());
			return true;
		}
		//その他無効な場所のクリック
		return true;
	}
	
	private boolean addItem(ItemStack item) {
		if(fishes.size() > 26) {
			return true;
		}
		SoundManager.sendPickUp(player);
		fishes.add(item);
		return false;
	}
	
	private void removeItem(int index) {
		SoundManager.sendPickUp(player);
		ItemStack item = fishes.get(index);
		ItemUtil.giveItem(player, item);
		fishes.remove(index);
	}
		
	private void updateGui() {
		//魚アイテムの更新
		for(int i = 0;i < 27;i ++) {
			inv.clear(i);
		}
		int totalPrice = 0;
		int totalAmount = 0;
		for(int i = 0;i < fishes.size();i ++) {
			ItemStack fish = fishes.get(i).clone();
			ItemMeta meta = fish.getItemMeta();
			List<String> lore = meta.getLore();
			lore.add(ChatColor.WHITE + "----------------");
			int pr = new FishNBT(fish).getPrice() * fish.getAmount();
			totalAmount += fish.getAmount();
			totalPrice += pr;
			lore.add(ChatColor.WHITE + "値段：" + ChatColor.GOLD + pr + "Ɇ");
			lore.add(ChatColor.GREEN + "" + ChatColor.UNDERLINE + "クリック" + ChatColor.RESET + ChatColor.WHITE + "で返却");
			meta.setLore(lore);
			fish.setItemMeta(meta);
			inv.setItem(i,fish);
		}
		
		//値段更新
		amount = totalAmount;
		price = totalPrice;
		String text = ChatColor.WHITE + "売却価格：" + ChatColor.GOLD + price + "Ɇ";
		renderingMoney(5, price, 
				new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setName(text).setModelData(3200).build(),text);
		
		//決定ボタン更新
		if(price > 0) {
			inv.setItem(40, new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setName(ChatColor.WHITE + "魚をすべて売却").setModelData(3416).build());		
		}else {
			inv.setItem(40, new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setName(ChatColor.WHITE + "魚をすべて売却").setModelData(3409).build());
		}
	}
	
	
	private void sell() {
		SoundManager.sendSuccess(player);
		//Eを渡す
		MoneyManager.add(player, price);
		
    	MessageManager.sendImportant(amount + "匹の魚を売却し、" + ChatColor.GOLD +  price + "Ɇ" + ChatColor.WHITE + "受け取りました。", player);
		
		//アイテム削除
		fishes.clear();
		price = 0;
	}
	
}
