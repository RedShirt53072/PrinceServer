package com.github.redshirt53072.baseapi.server;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.github.redshirt53072.baseapi.server.ApiManager.StopReason;

public class StopCommand implements CommandExecutor{
	
	@Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		ApiManager.stopServer("手動コマンドによる", StopReason.NORMAL);
		return true;
    }
}
