package com.github.redshirt53072.growthapi.message;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;

public class TextBuilder {
	private StringBuffer text = new StringBuffer();
	private ChatColor lastColor;
	
	public TextBuilder(ChatColor color) {
		text.append(color.toString());
		lastColor = color;
	}
	public TextBuilder(String str,ChatColor color) {
		text.append(str);
		lastColor = color;
	}
	public TextBuilder addText(String... str){
		for(String st : str){
			text.append(st);
		}
		return this;
	}
	public TextBuilder changeColor(ChatColor color){
		text.append(color.toString());
		lastColor = color;
		return this;
	}
	public TextBuilder addColor(ChatColor color){
		text.append(color.toString());
		return this;
	}
	public TextBuilder addColorText(ChatColor color,String... str){
		text.append(color.toString());
		for(String st : str){
			text.append(st);
		}
		text.append(lastColor.toString());
		return this;
	}
	public TextBuilder addPlayerName(OfflinePlayer player){
		return addColorText(ChatColor.LIGHT_PURPLE,player.getName());
	}
	
	public TextBuilder addFormatText(String str,ChatColor... format){
		for(ChatColor cc : format) {
			text.append(cc.toString());	
		}
		text.append(str);
		text.append(ChatColor.RESET.toString());
		text.append(lastColor.toString());
		return this;
	}
	
	public TextBuilder addClick(String click){
		return addFormatText(click,ChatColor.GREEN,ChatColor.UNDERLINE);
	}
	
	public TextBuilder addNumText(int number,String str){
		text.append(ChatColor.GOLD.toString());
		text.append(number);
		text.append(str);
		text.append(lastColor.toString());
		return this;
	}
	public TextBuilder addMoneyText(int amount){
		return addNumText(amount,"Ɇ");
	}
	public String build() {
		return text.toString();
	}
	
	public static String quickBuild(ChatColor color,String... texts) {
		StringBuffer text = new StringBuffer();
		text.append(color.toString());
		for(String st : texts){
			text.append(st);
		}
		return text.toString();
	}
	public static String plus(String... texts) {
		StringBuffer text = new StringBuffer();
		for(String st : texts){
			text.append(st);
		}
		return text.toString();
	}
}
