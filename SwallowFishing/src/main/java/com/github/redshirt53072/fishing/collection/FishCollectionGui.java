package com.github.redshirt53072.fishing.collection;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.InventoryClickEvent;

import com.github.redshirt53072.api.item.ItemBuilder;
import com.github.redshirt53072.api.message.SoundManager;
import com.github.redshirt53072.fishing.SwallowFishing;
import com.github.redshirt53072.fishing.data.FishManager;
import com.github.redshirt53072.fishing.data.RarityLootData;
import com.github.redshirt53072.fishing.data.FishData.BiomeGroup;
import com.github.redshirt53072.fishing.data.FishData.Time;
import com.github.redshirt53072.growthsurvival.collection.CollectionGui;

public class FishCollectionGui extends CollectionGui{
	private List<RarityLootData> data;
	private List<CollectedFish> collectedList = new ArrayList<CollectedFish>();
	
	private BiomeGroup[] biomeList = BiomeGroup.values();
	private Time[] timeList = Time.values();

	private int biomeIndex = 1;

	private int selectedBiome = 0;
	private int selectedTime = 0;
	private int raritySort = 0;
	
	public FishCollectionGui(FishCollection fc) {
		super(SwallowFishing.getInstance(), fc);
	}
	
	@Override
	protected void onRegisterLoad() {
		data = FishManager.getAllFish();
		
		setEmptyItem(18);
		setEmptyItem(27);
		
		inv.setItem(1,new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setName("5個戻る").setModelData(3311).build());
		inv.setItem(7, new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setName("5個進む").setModelData(3310).build());
		
		
	}

	@Override
	protected void onContentsClick(InventoryClickEvent event) {
		return;
	}

	@Override
	protected void onSubSortClick(InventoryClickEvent event) {
		int slot = event.getRawSlot();
		if(slot == 36) {
			if(raritySort == 5) {
				raritySort = 0;
			}else {
				raritySort ++;
			}
			SoundManager.sendClick(player);
		}
		if(slot == 45) {
			if(selectedTime == timeList.length) {
				selectedTime = 0;
			}else {
				selectedTime ++;
			}
			SoundManager.sendClick(player);
		}
		rendering();
		return;
	}

	@Override
	protected void onMainSortClick(InventoryClickEvent event) {
		int slot = event.getRawSlot();
		if(slot == 1) {
			if(biomeIndex == 1) {
        		player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1,0.5F);
        		return;
			}
			biomeIndex = Math.max(1, biomeIndex - 5);
    		player.playSound(player.getLocation(), Sound.UI_LOOM_TAKE_RESULT, 1,0.7F);
    		renderingMainSort();
			return;
		}
		if(slot == 7) {
			if(biomeIndex == biomeList.length) {
        		player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1,0.5F);
        		return;
			}
			biomeIndex = Math.min(biomeList.length, biomeIndex + 5);

    		player.playSound(player.getLocation(), Sound.UI_LOOM_TAKE_RESULT, 1,0.7F);
    		renderingMainSort();
    		return;
		}
		int clicked = biomeIndex + slot - 2;
		if(selectedBiome == clicked) {
			selectedBiome = 0;
		}else {
			selectedBiome = clicked;
		}
		SoundManager.sendClick(player);
		rendering();
		return;
	}

	@Override
	protected void renderingContents() {
		//10～16,,,,46～52
		
		
		
	}

	@Override
	protected void renderingMainSort() {
		//2～6
		List<String> lore = new ArrayList<String>();
		lore.add(ChatColor.GREEN + "" + ChatColor.UNDERLINE + "クリック" + ChatColor.RESET + ChatColor.WHITE + "でこのバイオームを選択する");
		List<String> selectedLore = new ArrayList<String>();
		selectedLore.add(ChatColor.GREEN + "" + ChatColor.UNDERLINE + "クリック" + ChatColor.RESET + ChatColor.WHITE + "でバイオームの選択を解除する");
		
		for(int i = 0;i < 5;i++) {
			BiomeGroup bi = biomeList[biomeIndex - 1 + i];
			if(selectedBiome == biomeIndex + i) {
				inv.setItem(i + 2, new ItemBuilder(bi.getSelectedIconItem()).setName(bi.getName()).setLore(selectedLore).build());
				continue;
			}
			inv.setItem(i + 2, new ItemBuilder(bi.getIconItem()).setName(bi.getName()).setLore(lore).build());
		}
	}

	@Override
	protected void renderingSubSort() {
		//36
		List<String> loreRare = new ArrayList<String>();
		if(raritySort == 0) {
			loreRare.add(ChatColor.GREEN + "" + ChatColor.UNDERLINE + "クリック" + ChatColor.RESET + ChatColor.WHITE + "でレア度条件を設定する");
			inv.setItem(36, new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setModelData(3201).setName("レア度条件：なし").setLore(loreRare).build());	
		}else {	
			if(raritySort == 5) {
				loreRare.add(ChatColor.GREEN + "" + ChatColor.UNDERLINE + "クリック" + ChatColor.RESET + ChatColor.WHITE + "でレア度条件をリセットする");
			}else {
				loreRare.add(ChatColor.GREEN + "" + ChatColor.UNDERLINE + "クリック" + ChatColor.RESET + ChatColor.WHITE + "でレア度条件を切り替える");
			}
			inv.setItem(36, new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setModelData(3000 + raritySort).setName("レア度条件：" + getRarityText()).setLore(loreRare).build());
		}
		
		List<String> loreTime = new ArrayList<String>();
		//45
		if(selectedTime == 0) {
			loreTime.add(ChatColor.GREEN + "" + ChatColor.UNDERLINE + "クリック" + ChatColor.RESET + ChatColor.WHITE + "で時間帯条件を設定する");
			inv.setItem(45, new ItemBuilder(Material.CLOCK).setLore(loreTime).build());
		}else {
			if(selectedTime == timeList.length) {
				loreTime.add(ChatColor.GREEN + "" + ChatColor.UNDERLINE + "クリック" + ChatColor.RESET + ChatColor.WHITE + "で時間帯条件をリセットする");
			}else {
				loreTime.add(ChatColor.GREEN + "" + ChatColor.UNDERLINE + "クリック" + ChatColor.RESET + ChatColor.WHITE + "で時間帯条件を切り替える");
			}
			inv.setItem(45, new ItemBuilder(timeList[selectedTime - 1].getIconItem()).setName("時間帯条件:" + timeList[selectedTime - 1].getName()).setLore(loreTime).build());
		}
	}

	@Override
	protected List<String> getSortList() {
		List<String> sorts = new ArrayList<String>();
		if(raritySort != 0) {
			sorts.add("レア度：" + getRarityText());
		}
		if(selectedBiome != 0) {
			sorts.add("バイオーム：" + biomeList[selectedBiome - 1].getName());
		}
		if(selectedTime != 0) {
			sorts.add("時間帯：" + timeList[selectedTime - 1].getName());
		}
		return sorts;
	}
	
	private String getRarityText() {
		switch(raritySort) {
		case 1:
			return "";
		case 2:
			return "";
		case 3:
			return "";	
		case 4:
			return "";	
		case 5:
			return "";
		}
		return "";
	}
}