package com.gmail.akashirt53072.minegame;


import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.gmail.akashirt53072.minegame.enums.MessageType;

public class MessageManager {
	
	
	
	public static void send(Player player,String text,MessageType type) {
		switch(type) {
		case WARNING:
			player.sendMessage(ChatColor.RED + "【警告】" + text);		
			return;
		case IMPORTANT:
			player.sendMessage(ChatColor.GOLD + "【重要】" + ChatColor.WHITE + text);		
			return;
		case INFO:
			player.sendMessage(ChatColor.GREEN + "【情報】" + ChatColor.WHITE + text);		
			return;
		case SPECIAL:
			player.sendMessage(ChatColor.LIGHT_PURPLE + "【特殊】" + ChatColor.WHITE + text);		
			return;
		}
	}
	
	public static void sendAllPlayer(String text,MessageType type,Main plugin) {
		for(Player p : plugin.getPlayers()) {
			send(p,text,type);
		}
	}
	
	public static void sendToOP(String text,MessageType type,Main plugin) {
		for(Player p : plugin.getMainWorld().getPlayers()) {
			if(p.isOp()) {
				send(p,text,type);
			}
		}
	}
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
