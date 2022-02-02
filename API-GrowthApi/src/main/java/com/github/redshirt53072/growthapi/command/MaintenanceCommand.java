package com.github.redshirt53072.growthapi.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import com.github.redshirt53072.growthapi.message.MessageManager;
import com.github.redshirt53072.growthapi.message.MessageManager.MessageLevel;

/**
 * デフォルトの/stopのコマンドを置き換えている
 * @author redshirt
 *
 */
public final class MaintenanceCommand implements TabExecutor{
	private static boolean isClose = false;
	
	
	public static boolean isClose() {
		return isClose; 
	}
	
	/**
	 * op以外を蹴り、再度openが実行されるまでopのみ入れるようにする
	 */
	@Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if(args.length > 0) {
			if(args[0].equals("open")) {
				MessageManager.sendOPPlayer(MessageLevel.SPECIAL
						,"このサーバーは" + sender.getName() + "によってメンテナンスを終了しました。"
						,"現在OPを持たないプレイヤーでもログインができる状態になっています。"
						,"メンテナンス状態を再開する場合は/main closeを実行してください。");
				isClose = false;
				return true;
			}
        }
		int op = 0;
		int kick = 0;
		for(Player p : sender.getServer().getOnlinePlayers()){
			if(p.hasPermission(Bukkit.getPluginManager().getPermission("growth.op"))){
				MessageManager.sendSpecial("このサーバーは" + sender.getName() + "によってメンテナンスを開始しました。", p);
				MessageManager.sendSpecial("現在OPを持たないプレイヤーはログインができない状態になっています。", p);
				MessageManager.sendSpecial("なお、サーバーを再起動すると自動でメンテナンス状態は解除されてしまいますのでご注意ください。", p);
				MessageManager.sendSpecial("メンテナンス状態を解除する場合は/main openを実行してください。", p);
				op ++;
				continue;
			}
			if(p.isOp()){
				MessageManager.sendSpecial("このサーバーは" + sender.getName() + "によってメンテナンスを開始しました。", p);
				MessageManager.sendSpecial("現在OPを持たないプレイヤーはログインができない状態になっています。", p);
				MessageManager.sendSpecial("あなたはOP権限を持っていますので、引き続きサーバーの通常の機能が使える状態です。", p);
				MessageManager.sendSpecial("ただし、growth.opのパーミッションを持っていませんので、メンテナンス状態の解除はできません。", p);
				MessageManager.sendSpecial("必要に応じて/main openを実行してもらってください。", p);
				MessageManager.sendSpecial("なお、サーバーを再起動するとメンテナンス状態は自動で解除されます。", p);
				op ++;
				continue;
			}
			kick ++;
			p.kickPlayer(ChatColor.RED + "[maintenance]このサーバーはメンテナンスを開始しました。");
		}
		isClose = true;
		MessageManager.sendSpecial("[メンテナンス開始]" + kick + "人のプレイヤーがキックされ、" + op + "人のOP権限持ちのプレイヤーがサーバーに残りました。", sender);
		MessageManager.sendSpecial("[メンテナンス開始]明示的にメンテナンス状態を解除する場合は/maint openを実行してください。", sender);
		return true;
    }
	/**
	 * コマンド補完時
	 */
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args){
		List<String> tab = new ArrayList<String>();
		if(args.length == 1) {
			tab.add("メンテナンスを開始します");
			tab.add("open");
			tab.add("close");
		}
		return tab;
	}
}
