package com.github.redshirt53072.shulker.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.redshirt53072.api.command.ManagementCommand;
import com.github.redshirt53072.api.command.SubCommand;
import com.github.redshirt53072.api.gui.Gui;
import com.github.redshirt53072.api.gui.GuiManager;
import com.github.redshirt53072.api.message.MessageManager;
import com.github.redshirt53072.api.message.TextManager;
import com.github.redshirt53072.shulker.data.ECLock;
import com.github.redshirt53072.shulker.gui.EnderGui;
import com.github.redshirt53072.shulker.gui.OpEnderGui;

public class ECSubCommand implements SubCommand{
	private static ECSubCommand sub;
	
	private static List<OfflinePlayer> offlinePlayers = new ArrayList<OfflinePlayer>();
	
	public static void register(){
		sub = new ECSubCommand();
		ManagementCommand.register("enderchest", sub);
		
		for(OfflinePlayer op : Bukkit.getOfflinePlayers()) {
			offlinePlayers.add(op);
		}
	}
	
	@Override
    public void onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(args.length < 3) {
			MessageManager.sendCommandError("必要な項目が未記入です。", sender);
        	return;
        }
		
		String name = args[2];
		
		OfflinePlayer target = null;
		
		for(OfflinePlayer op : offlinePlayers) {
			if(op.getName().equals(name)){
				target = op;
				break;
			}
		}
		
		if(target == null) {
			MessageManager.sendCommandError("無効なプレイヤー名です。", sender);	
			return;
		}
		
		if(args[1].equals("setunlockpage")) {
			Integer page = TextManager.toNaturalNumber(args[3]);
			if(page == null) {
				MessageManager.sendCommandError("無効なページ数です。", sender);	
				return;
			}
			if(page < 1 || page > 9) {
				MessageManager.sendCommandError("無効なページ数です。", sender);	
				return;
			}
			ECLock.setPage(target, page);
			MessageManager.sendSpecial(target.getName() + "のエンダーチェストの拡張をセットしました。", sender);	
			return;
			
		}
		
		
		
		if(args[1].equals("view")) {
			if(!(sender instanceof Player)) {
				MessageManager.sendCommandError("このサブコマンドはコンソールからは実行できません。", sender);
				return;
			}
			Player player = (Player)sender;
			new OpEnderGui(target,false).open(player);
			return;
		}
		if(args[1].equals("edit")) {
			if(!(sender instanceof Player)) {
				MessageManager.sendCommandError("このサブコマンドはコンソールからは実行できません。", sender);
				return;
			}
			Player player = (Player)sender;
			if(target.isOnline()){
				Gui targetGui = GuiManager.getGui(target.getPlayer());
				if(targetGui != null) {
					if(targetGui instanceof EnderGui) {
						MessageManager.sendCommandError("現在対象プレイヤーがエンダーチェストを開いているため、編集を開始できません", sender);			
						return;
					}
				}	
			}
			
			if(OpEnderGui.isOpenedPlayer(target)) {
				MessageManager.sendCommandError("現在他の権限持ちプレイヤーがこのプレイヤーのエンダーチェストを編集モードで開いているため、編集を開始できません。", sender);
				return;
			}
			new OpEnderGui(target,true).open(player);
			return;
		}
		
		MessageManager.sendCommandError("無効なサブコマンドです。", sender);	
    }
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args){
		List<String> tab = new ArrayList<String>();
		if(args.length == 2) {
			if(sender instanceof Player) {
	            tab.add("view");
	            tab.add("edit");
			}
            tab.add("setunlockpage");
        }else if(args.length == 3) {
        	String text1 = args[1];

			if(sender instanceof Player) {
				if(text1.equals("setunlockpage") || text1.equals("view") || text1.equals("edit")) {
					List<OfflinePlayer> offPlayers = new ArrayList<OfflinePlayer>();
					for(OfflinePlayer op : Bukkit.getOfflinePlayers()) {
						tab.add(op.getName());
		        		offPlayers.add(op);
					}
					offlinePlayers = offPlayers;
	        	}
			}else {
				if(text1.equals("setunlockpage")) {
					List<OfflinePlayer> offPlayers = new ArrayList<OfflinePlayer>();
					for(OfflinePlayer op : Bukkit.getOfflinePlayers()) {
						tab.add(op.getName());
		        		offPlayers.add(op);
					}
					offlinePlayers = offPlayers;
	        	}
			}
        	
        }else if(args.length == 4){
        	String text1 = args[1];
        	if(text1.equals("setunlockpage")) {
        		tab.add("開放済みページ数");
        	}
        }
        return tab;
	}
}