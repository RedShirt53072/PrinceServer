package com.github.redshirt53072.baseapi.command;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public interface SubCommand {
	
    abstract public void onCommand(CommandSender sender, Command command, String label, String[] args);
	abstract public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args);
	
}

