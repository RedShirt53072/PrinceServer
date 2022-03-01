package com.github.redshirt53072.growthsurvival.ench;

import com.github.redshirt53072.growthapi.message.TextBuilder;
import com.github.redshirt53072.growthapi.message.TextManager;

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
				text = TextBuilder.plus(text," ",num);	
			}	
		}
		return text;
	}
}
