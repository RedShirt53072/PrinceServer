package com.github.redshirt53072.api.command;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
/**
 * /manageのサブコマンドのクラス
 * @author redshirt
 *
 */
public interface SubCommand {
	/**
	 * コマンド実行時
	 * @param sender
	 * @param command
	 * @param label
	 * @param args
	 */
    abstract public void onCommand(CommandSender sender, Command command, String label, String[] args);
	/**
	 * コマンド補完時
	 * @param sender
	 * @param command
	 * @param alias
	 * @param args
	 * @return 補完される候補リスト
	 */
    abstract public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args);
	
}

