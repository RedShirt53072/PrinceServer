package com.github.redshirt53072.dimmanger.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.github.redshirt53072.baseapi.command.ManagementCommand;
import com.github.redshirt53072.baseapi.command.SubCommand;

public class DimSubCommand implements SubCommand{
	private static DimSubCommand sub;
	
	static {
		sub = new DimSubCommand();
		ManagementCommand.register("dim", sub);
		
	}
	
	@Override
    public void onCommand(CommandSender sender, Command command, String label, String[] args) {
    	
		
    }
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args){
		List<String> tab = new ArrayList<String>();
		

		return tab;
	}
	
}

