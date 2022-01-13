package com.github.redshirt53072.growthapi.server;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.github.redshirt53072.growthapi.server.GrowthPluginManager.StopReason;
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
		GrowthPluginManager.stopServer("手動コマンドによる", StopReason.NORMAL);
		return true;
    }
}
