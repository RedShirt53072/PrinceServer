package com.github.redshirt53072.growthapi.command;

import java.util.logging.Level;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.redshirt53072.growthapi.BaseAPI;
import com.github.redshirt53072.growthapi.message.LogManager;
import com.github.redshirt53072.growthapi.message.MessageManager;
import com.github.redshirt53072.growthapi.server.PluginManager;
import com.github.redshirt53072.growthapi.server.PluginManager.StopReason;
import com.github.redshirt53072.growthapi.server.Maintenance;
/**
 * デフォルトの/opのコマンドを置き換えている
 * @author redshirt
 *
 */
public final class OPCommand implements CommandExecutor{
	/**
	 * opコマンド実行時に割り込ませる
	 */
	@Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender instanceof Player) {
			PluginManager.kickPlayer((Player)sender, "コンソールのみのコマンドの使用", StopReason.GRIEFING);
			return true;
		}
		
		if(!Maintenance.isMain()) {
			MessageManager.sendCommandError("現在メンテナンスが行われていません。", sender);
			return true;
		}

		if(args.length == 0) {
			MessageManager.sendCommandError("必要な項目が未記入です。", sender);
			return true;
		}
		String name = args[0];
		Player p = sender.getServer().getPlayer(name);
		if(p != null) {
			if(Maintenance.isOPPlayer(p)) {
				p.setOp(true);
				LogManager.logInfos(BaseAPI.getInstance(), Level.INFO,name + "にバニラOP権限を付与しました。","このバニラOP権限は" + name + "のログアウト時とメンテナンス終了時に自動的に剝奪されます。");
				return true;
			}
			MessageManager.sendCommandError(name + "はOPとして登録されていません。", sender);
			return true;
		}
		MessageManager.sendCommandError(name + "というプレイヤーは現在サーバー上にログインしていないようです。", sender);
		return true;
	}
}
