package com.github.redshirt53072.api.command;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import com.github.redshirt53072.api.BaseAPI;
import com.github.redshirt53072.api.message.LogManager;
import com.github.redshirt53072.api.message.MessageManager;
import com.github.redshirt53072.api.message.TextBuilder;
import com.github.redshirt53072.api.message.MessageManager.MessageLevel;
import com.github.redshirt53072.api.server.Maintenance;
import com.github.redshirt53072.api.server.PluginManager;
import com.github.redshirt53072.api.server.PluginManager.StopReason;

/**
 * デフォルトの/stopのコマンドを置き換えている
 * @author redshirt
 *
 */
public final class MaintenanceCommand implements TabExecutor{
	
	/**
	 * op以外を蹴り、再度openが実行されるまでopのみ入れるようにする
	 */
	@Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender instanceof Player) {
			PluginManager.kickPlayer((Player)sender, "コンソールのみのコマンドの使用", StopReason.GRIEFING);
			return true;
		}
		if(args.length == 0) {
			MessageManager.sendCommandError("必要な項目が未記入です。", sender);
			return true;
		}
		if(args[0].equals("open")) {
			if(Maintenance.isMain() && !Maintenance.isTempMain()) {
				LogManager.logInfos(BaseAPI.getInstance(),Level.INFO 
						,"メンテナンスの終了が設定されました。"
						,"次回再起動時からOPを持たないプレイヤーでもログインができる状態になります。"
						,"メンテナンス状態を再開する場合は/main closeを実行してください。");
				Maintenance.open();
			}else {
				MessageManager.sendText(sender, MessageLevel.SPECIAL, "現在メンテナンスは有効化されていません。");
			}
			return true;
		}
		if(args[0].equals("close")) {
			if(Maintenance.isTempMain() || !Maintenance.isMain()) {
				int kick = 0;
				for(Player p : sender.getServer().getOnlinePlayers()){
					kick ++;
					
					p.kickPlayer(new TextBuilder(ChatColor.WHITE)
							.addColorText(ChatColor.RED,"[maintenance]")
							.addText("このサーバーはメンテナンスを開始しました。")
							.build());
				}
				Maintenance.close();
				MessageManager.sendSpecial(TextBuilder.plus("[メンテナンス開始]",String.valueOf(kick),"人のプレイヤーがキックされました。"), sender);
				MessageManager.sendSpecial("[メンテナンス開始]growth.op付きプレイヤーを追加する場合は/main addop <username>を実行してください。", sender);
				MessageManager.sendSpecial("[メンテナンス開始]権限なしでプレイヤーを追加する場合は/main addnormal <username>を実行してください。", sender);
			}else{
				MessageManager.sendCommandError("既にメンテナンスが有効化されています。", sender);
			
			}
			return true;
		}
			
		if(args[0].equals("addop")) {
			if(Maintenance.isTempMain() || !Maintenance.isMain()) {
				MessageManager.sendText(sender, MessageLevel.SPECIAL, "現在メンテナンスは有効化されていません。");
				return true;
			}
			if(args.length < 2) {
				MessageManager.sendCommandError("必要な項目が未記入です。", sender);
				return true;
			}
			if(Maintenance.addOP(args[1])) {
				MessageManager.sendSpecial(args[1] + "にgrowth.opを付与しました。", sender);
			}else {
				MessageManager.sendCommandError(args[1] + "は登録のないプレイヤー名です。", sender);
			}
			return true;
		}
		if(args[0].equals("addnormal")) {
			if(Maintenance.isTempMain() || !Maintenance.isMain()) {
				MessageManager.sendText(sender, MessageLevel.SPECIAL, "現在メンテナンスは有効化されていません。");
				return true;
			}
			if(args.length < 2) {
				MessageManager.sendCommandError("必要な項目が未記入です。", sender);
				return true;
			}
			if(Maintenance.addNormal(args[1])) {
				MessageManager.sendSpecial(args[1] + "にテストプレイ用ログイン権限を付与しました。", sender);
			}else {
				MessageManager.sendCommandError(args[1] + "は登録のないプレイヤー名です。", sender);
			}
			return true;
		}
		if(args[0].equals("removeperm")) {
			if(Maintenance.isTempMain() || !Maintenance.isMain()) {
				MessageManager.sendText(sender, MessageLevel.SPECIAL, "現在メンテナンスは有効化されていません。");
				return true;
			}
			if(args.length < 2) {
				MessageManager.sendCommandError("必要な項目が未記入です。", sender);
				return true;
			}
			
			if(Maintenance.removePlayer(args[1])) {
				MessageManager.sendSpecial(args[1] + "の権限を剝奪しました。", sender);
			}else {
				MessageManager.sendCommandError(args[1] + "は登録のないプレイヤー名です。", sender);
			}
			return true;
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
		if(sender instanceof Player) {
			return tab;
		}
		if(args.length == 1) {
			if(!Maintenance.isTempMain() && Maintenance.isMain()) {
				tab.add("open");
				tab.add("addop");
				tab.add("addnormal");
				tab.add("removeperm");	
			}else{
				tab.add("close");
			}
		}else if (args.length == 2) {
			if(Maintenance.isMain()) {
				if(args[0].equals("addop")) {
					tab.addAll(Maintenance.getOpList());
					if(tab.isEmpty()){
						tab.add("現在growth.op権限を持っているプレイヤーがいません");
					}
				}else if(args[0].equals("addnormal")){
					tab.addAll(Maintenance.getJoinList());
					if(tab.isEmpty()){
						tab.add("現在テストプレイ用ログイン権限を持っているプレイヤーがいません");
					}
				}else if(args[0].equals("removeperm")) {
					tab.addAll(Maintenance.getNowList());
					if(tab.isEmpty()){
						tab.add("現在メンテナンス時の権限を持っているプレイヤーがいません");
					}
				}
			}
		}
		return tab;
	}
}
