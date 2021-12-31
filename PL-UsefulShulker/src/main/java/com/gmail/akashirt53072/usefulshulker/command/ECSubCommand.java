package com.gmail.akashirt53072.usefulshulker.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.redshirt53072.baseapi.command.ManagementCommand;
import com.github.redshirt53072.baseapi.command.SubCommand;
import com.github.redshirt53072.baseapi.message.MessageManager;
import com.gmail.akashirt53072.usefulshulker.data.PlayerNBT;

public class ECSubCommand implements SubCommand{
	private static ECSubCommand sub;
	
	public static void register(){
		sub = new ECSubCommand();
		ManagementCommand.register("enderchest", sub);
		
	}
	
	@Override
    public void onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage("[error]コンソールからは実行できません。");
        	return;
		}
		Player p = (Player)sender;
		if(args.length < 3) {
			MessageManager.sendSpecial(ChatColor.RED + "[error]必要な項目が未記入です。", p);
        	return;
        }
		
		if(args[1].equals("reset")) {
			
			for(Player allplayer : Bukkit.getOnlinePlayers()){
				if(args[2].equals(allplayer.getName())){
					new PlayerNBT(allplayer).setUnlockedPage(1);
					MessageManager.sendSpecial(allplayer.getName() + "のエンダーチェストの拡張をリセットしました。", p);	
					return;
				}
			}
			MessageManager.sendSpecial(ChatColor.RED + "[error]無効なプレイヤー名です。", p);	
			return;
					
		}
		MessageManager.sendSpecial(ChatColor.RED + "[error]無効なサブコマンドです。", p);
    	
    }
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args){
		List<String> tab = new ArrayList<String>();
		if(args.length == 2) {
            tab.add("reset");
        }else {
        	String text1 = args[1];
        	if(text1.equals("reset")) {
        		for(Player p : Bukkit.getOnlinePlayers()){
            		tab.add(p.getName());
        		}
        	}
        }
        return tab;
	}
	
}

