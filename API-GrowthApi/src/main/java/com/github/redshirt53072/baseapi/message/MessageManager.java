package com.github.redshirt53072.baseapi.message;


import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class MessageManager {
	
	
	public static void sendWarning(String text,Player player) {
		player.sendMessage(ChatColor.RED + "【警告】" + text);		
	}
	public static void sendImportant(String text,Player player) {
		player.sendMessage(ChatColor.GOLD + "【重要】" + ChatColor.WHITE + text);				
	}
	public static void sendInfo(String text,Player player) {
		player.sendMessage(ChatColor.GREEN + "【情報】" + ChatColor.WHITE + text);			
	}
	
	public static void sendSpecial(String text,Player player) {
		player.sendMessage(ChatColor.LIGHT_PURPLE + "【特殊】" + ChatColor.WHITE + text);		
	}
	
}
