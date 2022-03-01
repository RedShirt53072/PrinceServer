package com.github.redshirt53072.dimmanager.command;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import com.github.redshirt53072.growthapi.message.MessageManager;
import com.github.redshirt53072.growthapi.message.SoundManager;
import com.github.redshirt53072.growthapi.message.TextBuilder;
import com.github.redshirt53072.growthapi.player.PlayerManager;
import com.github.redshirt53072.dimmanager.Teleporter;
import com.github.redshirt53072.dimmanager.data.DimData;
import com.github.redshirt53072.dimmanager.data.WorldManager;

public class HomeCommand implements TabExecutor{
	
	@Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(!(sender instanceof Player)) {
			MessageManager.sendCommandError("コンソールからは実行できません。", sender);
        	return true;
		}
		Player p = (Player)sender;

		World world = p.getWorld();
		UUID uuid = world.getUID();
		
		DimData dim = null;
		
		for(DimData dd : WorldManager.getHomeData()) {
			if(dd.getUUID().equals(uuid)) {
				dim = dd;
				break;
			}
		}
		if(dim == null) {
			MessageManager.sendCommandError("このディメンションではホーム機能は使用できません", p);
	    	return true;		
		}
		
		if(args.length < 1) {
			MessageManager.sendCommandError("必要な項目を記入してください。", p);
        	return true;
        }
		
		if(PlayerManager.isAsyncLocked(p, "dim")) {
			MessageManager.sendCommandError("現在データ保存中です。数秒待ってからもう一度コマンドを実行してください。", p);
        	return true;
        }
		
		switch(args[0]) {
		case "teleport":
			Teleporter.teleportSavedLocation(p, dim);
			MessageManager.sendInfo("ホームに移動しました。" , p);
			return true;
		case "sethere":
			new WorldManager().writeLoc(p, dim.getName(), p.getLocation());
			SoundManager.sendClick(p);
			MessageManager.sendInfo(TextBuilder.plus(dim.getName(),"でのホームを設定しました。") , p);
			return true;
		}
		return true;
    }
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args){
		
		List<String> tab = new ArrayList<String>();
		if(args.length == 1) {
			if(!(sender instanceof Player)) {
				return tab;
			}

			Player p = (Player)sender;
			DimData nowDim = WorldManager.getDimData(p.getWorld().getUID());
			boolean isMatch = false;
			
			for(DimData dd : WorldManager.getHomeData()) {
				if(dd.equals(nowDim)) {
					isMatch = true;
				}
			}
			if(!isMatch) {
				tab.add("このディメンションではホーム機能は使用できません");
		    	return tab;	
			}
			tab.add("teleport");
			tab.add("sethere");
		}
		return tab;
	}
}
