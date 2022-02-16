package com.github.redshirt53072.usefulshulker.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.redshirt53072.growthapi.command.SubCommand;
import com.github.redshirt53072.growthapi.command.TopManageCommand;
import com.github.redshirt53072.growthapi.gui.Gui;
import com.github.redshirt53072.growthapi.gui.GuiManager;
import com.github.redshirt53072.growthapi.message.MessageManager;
import com.github.redshirt53072.usefulshulker.gui.EnderGui;
import com.github.redshirt53072.usefulshulker.gui.OpEnderGui;

public class ECOpCommand implements SubCommand{
	private static ECOpCommand sub;
	
	public static void register(){
		sub = new ECOpCommand();
		TopManageCommand.register("enderchest", sub);
	}
	
	@Override
    public void onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(args.length < 3) {
			MessageManager.sendSpecial(ChatColor.RED + "[error]必要な項目が未記入です。", sender);
        	return;
        }
		if(!(sender instanceof Player)) {
			MessageManager.sendSpecial(ChatColor.RED + "[error]コンソールからは実行できません。", sender);
		   	return;
		}
		Player player = (Player)sender;
		if(args[1].equals("view")) {
			for(Player target : Bukkit.getOnlinePlayers()){
				if(args[2].equals(target.getName())){
					new OpEnderGui(target,false).open(player);
					return;
				}
			}
			MessageManager.sendSpecial(ChatColor.RED + "[error]無効なプレイヤー名です。", sender);	
			return;
		}
		if(args[1].equals("edit")) {
			for(Player target : Bukkit.getOnlinePlayers()){
				if(args[2].equals(target.getName())){
					Gui targetGui = GuiManager.getGui(target);
					if(targetGui != null) {
						if(targetGui instanceof EnderGui) {
							MessageManager.sendSpecial(ChatColor.RED + "[info]現在対象プレイヤーがエンダーチェストを開いているため、、編集を開始できません", sender);			
							return;
						}
					}
					if(OpEnderGui.isOpenedPlayer(target)) {
						MessageManager.sendSpecial(ChatColor.RED + "[info]現在他の権限持ちプレイヤーがこのプレイヤーのエンダーチェストを編集モードで開いているため、編集を開始できません。", sender);
						return;
					}
					new OpEnderGui(target,true).open(player);
					return;
				}
			}
			MessageManager.sendSpecial(ChatColor.RED + "[error]無効なプレイヤー名です。", sender);	
			return;
		}
		MessageManager.sendSpecial(ChatColor.RED + "[error]無効なサブコマンドです。", sender);	
    }
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args){
		List<String> tab = new ArrayList<String>();
		if(args.length == 2) {
            tab.add("view");
            tab.add("edit");
        }else if(args.length == 3){
        	String text1 = args[1];
        	if(text1.equals("view") || text1.equals("edit")) {
        		for(Player p : Bukkit.getOnlinePlayers()){
            		tab.add(p.getName());
        		}
        	}
        }
        return tab;
	}
}

