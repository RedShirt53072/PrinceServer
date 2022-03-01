package com.github.redshirt53072.growthapi.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.redshirt53072.growthapi.server.PluginManager;
import com.github.redshirt53072.growthapi.server.PluginManager.StopReason;
/**
 * デフォルトの/stopのコマンドを置き換えている
 * @author redshirt
 *
 */
public final class StopCommand implements CommandExecutor{
	/**
	 * stopコマンド実行時にPluginManagerを割り込ませる
	 */
	@Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender instanceof Player) {
			PluginManager.kickPlayer((Player)sender, "コンソールのみのコマンドの使用", StopReason.GRIEFING);
			return true;
		}
		
		PluginManager.stopServer("手動コマンドによる", StopReason.NORMAL);
		return true;
    }
}
