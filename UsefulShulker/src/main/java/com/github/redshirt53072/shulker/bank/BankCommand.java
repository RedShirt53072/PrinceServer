package com.github.redshirt53072.shulker.bank;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import com.github.redshirt53072.api.message.MessageManager;
import com.github.redshirt53072.api.message.SoundManager;
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
		MessageManager.sendInfo(p.getName() + "様いらっしゃいませ。Prince銀行ダイレクトでございます。", sender);
		
		new BankGui().open(p);
		
    	return true;
    }
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args){
		List<String> tab = new ArrayList<String>();
		return tab;
	}
}
