package com.github.redshirt53072.survival.ench;

import org.bukkit.ChatColor;

import com.github.redshirt53072.api.message.TextBuilder;
import com.github.redshirt53072.api.message.TextManager;

public class Enchant {
	private EnchData type;
	private int level;
	
	public Enchant(EnchData type,int level) {
		this.level = level;
		this.type = type;
    }
	public EnchData getType() {
		return type;
	}
	public int getLevel() {
		return level;
	}
	public String getText() {
		String text = type.getName();
		if(type.getMaxLevel() != 1) {
			String num = TextManager.toRomeNumber(level);
			if(num != null) {
				text = TextBuilder.quickBuild(ChatColor.GRAY, text," ",num);	
			}
		}else {
			text = TextBuilder.quickBuild(ChatColor.GRAY, text);
		}
		return text;
	}
	public String getRawData() {
		return TextBuilder.plus(type.getType().getID(),",",String.valueOf(level));
	}
	public int getExpCost() {
		return type.getExpCost(level);
	}
}
