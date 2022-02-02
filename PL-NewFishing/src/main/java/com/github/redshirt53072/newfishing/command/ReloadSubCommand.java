package com.github.redshirt53072.newfishing.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.github.redshirt53072.growthapi.command.ManagementCommand;
import com.github.redshirt53072.growthapi.command.SubCommand;
import com.github.redshirt53072.growthapi.message.MessageManager;
import com.github.redshirt53072.newfishing.data.FishManager;

public class ReloadSubCommand implements SubCommand{
	private static ReloadSubCommand sub;
	
	public static void register(){
		sub = new ReloadSubCommand();
		ManagementCommand.register("fish", sub);
	}
	
	@Override
    public void onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(args.length < 2) {
			MessageManager.sendSpecial(ChatColor.RED + "[error]必要な項目が未記入です。", sender);
        	return;
        }
		
		switch(args[1]) {
		case "reload":
			FishManager.reload();
			MessageManager.sendSpecial("釣りデータを再読み込みしました。", sender);
			return;
		}
		MessageManager.sendSpecial(ChatColor.RED + "[error]無効なサブコマンドです。", sender);
    }
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args){
		List<String> tab = new ArrayList<String>();
		if(args.length == 2) {
            tab.add("reload");
        }
        return tab;
	}
	
}

