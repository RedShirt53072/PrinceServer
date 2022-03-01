package com.github.redshirt53072.growthapi.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import com.github.redshirt53072.growthapi.BaseAPI;
import com.github.redshirt53072.growthapi.message.LogManager;
import com.github.redshirt53072.growthapi.message.MessageManager;
import com.github.redshirt53072.growthapi.server.PluginManager;
import com.github.redshirt53072.growthapi.server.Maintenance;
import com.github.redshirt53072.growthapi.server.PluginManager.StopReason;


/**
 * /manageの管理クラス
 * @author redshirt
 *
 */
public final class ManagementCommand implements TabExecutor{
	/**
	 * 登録されたサブコマンド
	 */
	private static Map<String,SubCommand> subCommands = new HashMap<String,SubCommand>();
	
	/**
	 * /manageのサブコマンドを登録する
	 * @param name 登録サブコマンド名
	 * @param sub サブコマンドインスタンス
	 */
	public static void register(String name,SubCommand sub) {
		subCommands.put(name,sub);
	}
	/**
	 * コマンド実行時
	 */
	@Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(!Maintenance.isMain()) {
			MessageManager.sendCommandError("現在メンテナンスが行われていません。", sender);
			return true;
		}
		if(sender instanceof Player) {
			Player p = (Player)sender;
			if(!Maintenance.canOPCommand(p)) {
				LogManager.logInfo(p.getName() + "はgrowth.opのパーミッションを不正に取得しています!",BaseAPI.getInstance(),Level.SEVERE);
	    		PluginManager.kickPlayer(p, "OPパーミッションを取得する", StopReason.GRIEFING);
				return true;
			}	
		}
		
		if(args.length == 0) {
			MessageManager.sendCommandError("必要な項目が未記入です。", sender);
			return true;
		}
		String arg = args[0];
		for(String name : subCommands.keySet()) {
			if(name.equals(arg)) {
				subCommands.get(name).onCommand(sender, command, label, args);
				return true;
			}
		}
		
		MessageManager.sendCommandError("無効なサブコマンドです。", sender);
		return true;
    }
	/**
	 * コマンド補完時
	 */
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args){
		List<String> tab = new ArrayList<String>();
		if(args.length == 1) {
			subCommands.forEach((String name , SubCommand subCommand) -> tab.add(name));
		}else if(args.length > 1){
			String sub = args[0];
			subCommands.forEach((String name , SubCommand subCommand) -> {if(name.equals(sub)) {
				tab.addAll(subCommand.onTabComplete(sender, command, sub, args));
			}});
		}
		return tab;
	}
}

