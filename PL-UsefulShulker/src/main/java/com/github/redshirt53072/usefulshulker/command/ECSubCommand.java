package com.github.redshirt53072.usefulshulker.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.redshirt53072.growthapi.command.ManagementCommand;
import com.github.redshirt53072.growthapi.command.SubCommand;
import com.github.redshirt53072.growthapi.message.MessageManager;
import com.github.redshirt53072.growthapi.util.TextManager;
import com.github.redshirt53072.usefulshulker.data.ECLock;

public class ECSubCommand implements SubCommand{
	private static ECSubCommand sub;
	
	public static void register(){
		sub = new ECSubCommand();
		ManagementCommand.register("enderchest", sub);
	}
	
	@Override
    public void onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(args.length < 4) {
			MessageManager.sendSpecial(ChatColor.RED + "[error]必要な項目が未記入です。", sender);
        	return;
        }
		
		if(args[1].equals("setunlockpage")) {
			for(Player allplayer : Bukkit.getOnlinePlayers()){
				if(args[2].equals(allplayer.getName())){
					Integer page = TextManager.toNaturalNumber(args[3]);
					if(page == null) {
						MessageManager.sendSpecial(ChatColor.RED + "[error]無効なページ数です。", sender);	
						return;
					}
					if(page < 1 || page > 9) {
						MessageManager.sendSpecial(ChatColor.RED + "[error]無効なページ数です。", sender);	
						return;
					}
					ECLock.setPage(allplayer, page);
					MessageManager.sendSpecial(allplayer.getName() + "のエンダーチェストの拡張をセットしました。", sender);	
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
            tab.add("setunlockpage");
        }else if(args.length == 3) {
        	String text1 = args[1];
        	if(text1.equals("setunlockpage")) {
        		for(Player p : Bukkit.getOnlinePlayers()){
            		tab.add(p.getName());
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