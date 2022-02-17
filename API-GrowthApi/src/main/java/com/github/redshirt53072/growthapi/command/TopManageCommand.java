package com.github.redshirt53072.growthapi.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;


/**
 * /manageの管理クラス
 * @author redshirt
 *
 */
public final class TopManageCommand implements TabExecutor{
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
		if(args.length == 0) {
        	return false;
        }
		if(args.length > 0) {

			String arg = args[0];
			subCommands.forEach((String name , SubCommand subCommand) -> {if(name.equals(arg)) {
				subCommand.onCommand(sender, command, label, args);
			}});
		}
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

