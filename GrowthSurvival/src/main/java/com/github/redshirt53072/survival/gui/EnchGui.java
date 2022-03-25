package com.github.redshirt53072.survival.gui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;

import com.github.redshirt53072.api.gui.Gui;
import com.github.redshirt53072.api.item.ItemBuilder;
import com.github.redshirt53072.api.message.TextBuilder;
import com.github.redshirt53072.survival.GrowthSurvival;

public abstract class EnchGui extends Gui{
	protected int expCost = 0;
	protected int playerExp = 0;
	
	public EnchGui() {
	    super(GrowthSurvival.getInstance());	    
	}
	
	protected void renderingCheck() {
		playerExp = player.getLevel();
		if(expCost == 0) {
			inv.setItem(12, new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setName(TextBuilder.quickBuild(ChatColor.WHITE, "適切なアイテムを入れてください。")).setModelData(3411).build());	
			return;
		}
		if(expCost > playerExp) {
			inv.setItem(12, new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setName(new TextBuilder(ChatColor.WHITE)
					.addText("経験値が足りません。(現在：")
					.addNumText(playerExp, "Lv")
					.addText("/必要：")
					.addNumText(expCost, "Lv")
					.addText(")")
					.build()).setModelData(3409).build());	
			return;
		}
		inv.setItem(12, new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setName(new TextBuilder(ChatColor.WHITE).addClick("クリック").addText("で決定").build()).setModelData(3400).build());
	}
	
	protected void renderingExp() {
		//経験値更新
		int cost = expCost;
		if(expCost > 999) {
			cost = 999;
		}
		
		List<Integer> numList = new ArrayList<Integer>();

		boolean start = false;
		for(int i = 2;i >= 0;i--) {
			if(cost >= Math.pow(10, i)) {
				start = true;
			}
			if(start) {
				int number = (int)(cost / Math.pow(10, i));
				cost %= Math.pow(10, i);
				numList.add(number);	
			}
		}
		if(numList.isEmpty()) {
			numList.add(0);
		}
		String text = new TextBuilder(ChatColor.WHITE).addText("必要経験値：").addNumText(expCost, "Lv").build();
		for(int i = 0;i <= 2;i++) {
			if(numList.size() > i){
				inv.setItem(15 - i, new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setName(text).setModelData(3010 + numList.get(numList.size() - i - 1)).build());
			}else {
				inv.setItem(15 - i, new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setName(text).setModelData(3201).build());	
			}
		}
		inv.setItem(16, new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setName(text).setModelData(3202).build());	
		
	}
	
	protected int calcExp(int exp){
		if(exp < 16) {
			return exp;
		}else if(exp < 51) {
			double x = exp;
			x /= 15;
			double result = Math.log(x) / Math.log(2);
			result *= 5;
			result += 15;
			return (int)result;
		}else if(exp < 81) {
			double x = exp;
			x /= 10;
			double result = Math.log(x) / Math.log(2);
			result *= 10;
			result += 0.5;
			return (int)result;	
		}else if(exp < 151) {
			double x = exp;
			x /= 10;
			double result = Math.log(x) / Math.log(2);
			result = Math.pow(result, 1.3);
			result *= 10;
			result -= 11;
			return (int)result;	
		}else {
			double x = exp;
			x /= 10;
			double result = Math.log(x) / Math.log(2);
			result = Math.pow(result, 1.7);
			result *= 10;
			result -= 53.6;
			return (int)result;	
		}
	}
}
