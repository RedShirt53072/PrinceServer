package com.github.redshirt53072.newfishing.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class SellCommand implements CommandExecutor{
	
	@Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
            Player player = (Player) sender;
            gui
            return true;
		}
		sender.sendMessage("[error]コンソールからは実行できません。");
        return true;
    }
}
