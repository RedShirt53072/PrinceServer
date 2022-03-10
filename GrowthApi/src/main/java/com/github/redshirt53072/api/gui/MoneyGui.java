package com.github.redshirt53072.api.gui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.github.redshirt53072.api.item.ItemBuilder;
import com.github.redshirt53072.api.server.GrowthPlugin;

public abstract class MoneyGui extends Gui{
	
	public MoneyGui(GrowthPlugin plugin) {
		super(plugin);
	}
	
	protected void renderingMoney(int line,int price,ItemStack unit,String text){
		if(price > 99999999) {
			price = 99999999;
		}
		if(price < 0) {
			price = 0;
		}
		
		List<Integer> numList = new ArrayList<Integer>();
		boolean start = false;
		for(int i = 7;i >= 0;i--) {
			if(price >= Math.pow(10, i)) {
				start = true;
			}
			if(start) {
				int number = (int)(price / Math.pow(10, i));
				price %= Math.pow(10, i);
				numList.add(number);		
			}
		}
		if(numList.isEmpty()) {
			numList.add(0);
		}
		int size = numList.size();
		int max = (size + 1) / 2 + 4;
		int min = 4 - (size + 1) / 2;
		for(int i = 0;i < 9;i++) {
			int slot = line * 9 + i;
			
			if(i > max) {
				setEmptyItem(slot);
				continue;
			}
			if(i == max) {
				inv.setItem(slot, unit);
				continue;
			}
			if(i < max && max - size <= i) {
				inv.setItem(slot, new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setName(text).setModelData(3010 + numList.get(size + i - max)).build());	
				continue;
			}
			
			if(min == i) {
				inv.setItem(slot, new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setName(text).setModelData(3201).build());
				continue;
			}
			if(i < min) {
				setEmptyItem(slot);
				continue;
			}
		}
	}
}