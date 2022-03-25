package com.gmail.akashirt53072.minegame.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import com.gmail.akashirt53072.minegame.Main;
import com.gmail.akashirt53072.minegame.PlayerManager;



public class JoinCommand implements TabExecutor{
	
	private Main plugin;
	
	public JoinCommand(Main plugin) {
	    this.plugin = plugin;
	}
	
	@Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
            Player player = (Player) sender;
            if(args.length == 0) {
            	return false;
            }
            switch(args[0]) {
            case "join":
            	new PlayerManager(plugin,player).onJoin();
            	break;
            case "leave":
            	new PlayerManager(plugin,player).onLeave();
            	break;
			default:
            	return false;
            }
            return true;
		}
        return false;
    }
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args){
		if (sender instanceof Player) {
            List<String> tab = new ArrayList<String>();
            tab.add("join");
            tab.add("leave");
            return tab;
		}
		return null;
	}
}
