package com.github.redshirt53072.usefulshulker.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import com.github.redshirt53072.growthapi.message.MessageManager;
import com.github.redshirt53072.growthapi.message.SoundManager;
import com.github.redshirt53072.usefulshulker.bank.BankGui;
public final class BankCommand implements TabExecutor{
	
	@Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(!(sender instanceof Player)) {
			MessageManager.sendCommandError("コンソールからは実行できません。",sender);
        	return true;
		}
		Player p = (Player)sender;
		if(p.getGameMode().equals(GameMode.CREATIVE)) {
			SoundManager.sendCancel(p);
			return true;
		}
		
		new BankGui().open(p);
		
    	return true;
    }
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args){
		List<String> tab = new ArrayList<String>();
		return tab;
	}
}
