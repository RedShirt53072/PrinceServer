package com.github.redshirt53072.dimmanager.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import com.github.redshirt53072.growthapi.message.MessageManager;
import com.github.redshirt53072.growthapi.message.TextBuilder;
import com.github.redshirt53072.dimmanager.Teleporter;
import com.github.redshirt53072.dimmanager.data.DimData;
import com.github.redshirt53072.dimmanager.data.DimID;
import com.github.redshirt53072.dimmanager.data.WorldManager;
import com.github.redshirt53072.dimmanager.gui.DimGui;

public class WorldCommand implements TabExecutor{
	
	@Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(!(sender instanceof Player)) {
			MessageManager.sendCommandError("コンソールからは実行できません。", sender);
        	return true;
		}
		Player p = (Player)sender;
		if(args.length == 1) {
			DimData dim = WorldManager.getDimData(args[0]);
			if(dim != null) {
				DimData nowDim = WorldManager.getDimData(p.getWorld().getUID());
				if(nowDim != null) {
					if(nowDim.equals(dim)) {
						new DimGui().open(p);
						return true;
					}
				}
				if(dim.getID().equals(DimID.normal)) {
					Teleporter.teleportSavedLocation(p, dim);
				}else {
					Teleporter.teleportDefault(p, dim);
				}
				MessageManager.sendInfo(TextBuilder.plus(dim.getName(),"に移動しました。"), sender);
				return true;
			}
		}
		
		new DimGui().open(p);
		return true;
    }
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args){
		List<String> tab = new ArrayList<String>();
		if(args.length == 1) {
			if(!(sender instanceof Player)) {
				return tab;
			}
			for(DimData dd : WorldManager.getWorlds()){
				tab.add(dd.getName());
			}
			Player p = (Player)sender;
			DimData nowDim = WorldManager.getDimData(p.getWorld().getUID());
			if(nowDim != null) {
				tab.remove(nowDim.getName());		
			}
		}
		return tab;
	}
}
