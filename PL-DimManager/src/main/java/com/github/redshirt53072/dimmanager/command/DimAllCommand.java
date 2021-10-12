package com.github.redshirt53072.dimmanager.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import com.github.redshirt53072.baseapi.message.MessageManager;
import com.github.redshirt53072.baseapi.util.Flag;
import com.github.redshirt53072.dimmanager.data.DimData;
import com.github.redshirt53072.dimmanager.general.DimManager;
import com.github.redshirt53072.dimmanager.general.WorldManager;

public class DimAllCommand implements TabExecutor{
	
	@Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage("[error]コンソールからは実行できません。");
        	return true;
		}
		Player p = (Player)sender;
		if(args.length < 1) {
			MessageManager.sendWarning(ChatColor.RED + "[error]必要な項目が未記入です。", p);
        	return true;
        }
		
		Flag match1 = new Flag(); 
		WorldManager.getAllDims().forEach(dim -> {if(args[0].equals(dim)) {
			match1.setTrue();
		}});
		if(!match1.getFlag()) {
			MessageManager.sendSpecial(ChatColor.RED + "[error]無効なディメンション名です。", p);
        	return true;
		}
		//座標保存
		new WorldManager().logout(p);
		
		//tp
    	//config
    	Bukkit.getScheduler().runTaskAsynchronously(DimManager.getInstance(), new Runnable() {
    		@Override
    		public void run() {
    			Location loc;
    			GameMode mode;
    			if(args[0].equals("normal")) {
    				loc = new WorldManager().readLoc(p);
    				mode = GameMode.SURVIVAL;
    			}else {
    				mode = DimData.getGamemode(args[0]);
        			loc = DimData.getLocation(args[0]);	
    			}
    			
    	    	Bukkit.getScheduler().runTask(DimManager.getInstance(), new Runnable() {
    	    		@Override
    	    		public void run() {
    	    			p.teleport(loc);
    	    			p.setGameMode(mode);	
    	    		}
    	    	});	
    		}
    	});
		
		return true;
    }
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args){
		List<String> tab = new ArrayList<String>();
		if(args.length == 1) {
			tab.addAll(WorldManager.getAllDims());
		}
		return tab;
	}
}
