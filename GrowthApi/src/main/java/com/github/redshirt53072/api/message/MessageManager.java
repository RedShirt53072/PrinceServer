package com.github.redshirt53072.api.message;


import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.redshirt53072.api.BaseAPI;
import com.github.redshirt53072.api.server.Maintenance;
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
		sender.sendMessage(new TextBuilder(ChatColor.RED).addText("【警告】",text).build());	
		if(sender instanceof Player) {
			SoundManager.sendAlert((Player)sender);
		}
	}
	/**
	 * 「重要」を送る
	 * 必ず読んでほしい時に使用する
	 * @param text メッセージ
	 * @param player 対象プレイヤー
	 */
	public static void sendImportant(String text,CommandSender sender) {
		sender.sendMessage(new TextBuilder(ChatColor.WHITE).addColorText(ChatColor.GOLD,"【重要】").addText(text).build());
		if(sender instanceof Player) {
			SoundManager.sendNotice((Player)sender);
		}
	}
	/**
	 * 「情報」を送る
	 * 一般的なメッセージに使用する
	 * @param text メッセージ
	 * @param player 対象プレイヤー
	 */
	public static void sendInfo(String text,CommandSender sender) {
		sender.sendMessage(new TextBuilder(ChatColor.WHITE).addColorText(ChatColor.GREEN,"【情報】").addText(text).build());			
	}
	
	/**
	 * 「特殊」を送る
	 * OP用のコマンドやデバッグ機能などに使用する
	 * @param text メッセージ
	 * @param player 対象プレイヤー
	 */
	public static void sendSpecial(String text,CommandSender sender) {
		sender.sendMessage(new TextBuilder(ChatColor.WHITE).addColorText(ChatColor.LIGHT_PURPLE,"【特殊】").addText(text).build());
	}
	
	/**
	 * 「特殊」を送る
	 * OP用のコマンドやデバッグ機能などに使用する
	 * @param text メッセージ
	 * @param player 対象プレイヤー
	 */
	public static void sendCommandError(String text,CommandSender sender) {
		sender.sendMessage(new TextBuilder(ChatColor.WHITE).addColorText(ChatColor.RED,"【コマンドエラー】" ).addText(text).build());
		if(sender instanceof Player) {
			SoundManager.sendCancel((Player)sender);		
		}
	}
	
	
	/**
	 * 対象プレイヤー1人にメッセージを送る
	 * メッセージレベルの設定ができる
	 * @param text メッセージ
	 * @param player 対象プレイヤー
	 * @param level メッセージレベル
	 */
	public static void sendText(CommandSender sender,MessageLevel level,String... texts) {
		for(String text : texts) {	
			switch(level) {
			case WARNING:
				sendWarning(text,sender);
				break;
			case IMPORTANT:
				sendImportant(text,sender);
				break;
			case INFO:
				sendInfo(text,sender);
				break;
			case SPECIAL:
				sendSpecial(text,sender);
				break;
			}
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
			if(Maintenance.isOPPlayer(p)) {
				for(String text : texts) {	
					sendText(p,level,text);
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
				sendText(p,level,text);
			}
		}
	}
	
	/**
	 * サーバー上の全プレイヤーにメッセージを送る
	 * メッセージレベルの設定ができる
	 * @param text メッセージ
	 * @param level メッセージレベル
	 */
	public static void sendNearPlayer(MessageLevel level,Location loc,int maxDistance,String...texts) {
		World world = loc.getWorld();
		for(Player p : BaseAPI.getInstance().getServer().getOnlinePlayers()){
			if(world.equals(p.getWorld())) {
				if(loc.distanceSquared(p.getLocation()) < maxDistance) {
					for(String text : texts) {	
						sendText(p,level,text);
					}	
				}
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
