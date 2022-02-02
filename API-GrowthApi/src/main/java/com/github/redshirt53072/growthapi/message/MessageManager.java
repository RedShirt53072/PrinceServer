package com.github.redshirt53072.growthapi.message;


import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.redshirt53072.growthapi.BaseAPI;
/**
 * プレイヤーにメッセージを送る際に使う
 * @author redshirt
 *
 */
public final class MessageManager {
	
	/**
	 * 「警告」を送る
	 * 不具合や想定外の事態が起きた場合に使用する
	 * @param text メッセージ
	 * @param player 対象プレイヤー
	 */
	public static void sendWarning(String text,CommandSender sender) {
		sender.sendMessage(ChatColor.RED + "【警告】" + text);		
	}
	/**
	 * 「重要」を送る
	 * 必ず読んでほしい時に使用する
	 * @param text メッセージ
	 * @param player 対象プレイヤー
	 */
	public static void sendImportant(String text,CommandSender sender) {
		sender.sendMessage(ChatColor.GOLD + "【重要】" + ChatColor.WHITE + text);				
	}
	/**
	 * 「情報」を送る
	 * 一般的なメッセージに使用する
	 * @param text メッセージ
	 * @param player 対象プレイヤー
	 */
	public static void sendInfo(String text,CommandSender sender) {
		sender.sendMessage(ChatColor.GREEN + "【情報】" + ChatColor.WHITE + text);			
	}
	
	/**
	 * 「特殊」を送る
	 * OP用のコマンドやデバッグ機能などに使用する
	 * @param text メッセージ
	 * @param player 対象プレイヤー
	 */
	public static void sendSpecial(String text,CommandSender sender) {
		sender.sendMessage(ChatColor.LIGHT_PURPLE + "【特殊】" + ChatColor.WHITE + text);		
	}
	
	/**
	 * 対象プレイヤー1人にメッセージを送る
	 * メッセージレベルの設定ができる
	 * @param text メッセージ
	 * @param player 対象プレイヤー
	 * @param level メッセージレベル
	 */
	public static void sendText(String text,Player player,MessageLevel level) {
		switch(level) {
		case WARNING:
			sendWarning(text,player);
			break;
		case IMPORTANT:
			sendImportant(text,player);
			break;
		case INFO:
			sendInfo(text,player);
			break;
		case SPECIAL:
			sendSpecial(text,player);
			break;
		}
	}
	
	/**
	 * サーバー上のOPを持っているプレイヤーにメッセージを送る
	 * メッセージレベルの設定ができる
	 * @param text メッセージ
	 * @param level メッセージレベル
	 */
	public static void sendOPPlayer(MessageLevel level,String...texts) {
		for(Player p : BaseAPI.getInstance().getServer().getOnlinePlayers()){
			if(p.isOp()) {
				for(String text : texts) {	
					sendText(text,p,level);
				}
			}
		}
	}

	/**
	 * サーバー上の全プレイヤーにメッセージを送る
	 * メッセージレベルの設定ができる
	 * @param text メッセージ
	 * @param level メッセージレベル
	 */
	public static void sendAllPlayer(MessageLevel level,String...texts) {
		for(Player p : BaseAPI.getInstance().getServer().getOnlinePlayers()){
			for(String text : texts) {	
				sendText(text,p,level);
			}
		}
	}
	
	
	/**
	 * メッセージレベル
	 * 警告/重要/情報/特殊のメッセージの種類
	 * @author redshirt
	 *
	 */
	public enum MessageLevel{
		WARNING,
		IMPORTANT,
		INFO,
		SPECIAL;
	}
	
	
}
