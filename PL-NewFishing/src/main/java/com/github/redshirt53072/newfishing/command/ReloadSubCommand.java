package com.github.redshirt53072.newfishing.command;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;

import com.github.redshirt53072.growthapi.command.ManagementCommand;
import com.github.redshirt53072.growthapi.command.SubCommand;
import com.github.redshirt53072.growthapi.message.MessageManager;

public class ReloadSubCommand implements SubCommand{
	private static ReloadSubCommand sub;
	
	public static void register(){
		sub = new ReloadSubCommand();
		ManagementCommand.register("fish", sub);
		
	}
	
	@Override
    public void onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage("[error]コンソールからは実行できません。");
        	return;
		}
		Player p = (Player)sender;
		if(args.length < 2) {
			MessageManager.sendSpecial(ChatColor.RED + "[error]必要な項目が未記入です。", p);
        	return;
        }
		
		switch(args[1]) {
		case "reload":
			reload
			MessageManager.sendSpecial("釣りデータを再読み込みしました。", p);
			return;
		}
		MessageManager.sendSpecial(ChatColor.RED + "[error]無効なサブコマンドです。", p);
    	
    }
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args){
		if(!(sender instanceof Player)) {
			sender.sendMessage("[error]コンソールからは実行できません。");
        	return;
		}
		List<String> tab = new ArrayList<String>();
		if(args.length == 2) {
            tab.add("reload");
        }
        return tab;
	}
	
}

