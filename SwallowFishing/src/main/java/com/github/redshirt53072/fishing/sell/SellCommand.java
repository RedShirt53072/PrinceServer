package com.github.redshirt53072.fishing.sell;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.redshirt53072.api.message.MessageManager;


public class SellCommand implements CommandExecutor{
	
	@Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
            Player player = (Player) sender;
            new SellFishGui().open(player);
            return true;
		}
		MessageManager.sendCommandError("コンソールからは実行できません。", sender);
        return true;
    }
}
