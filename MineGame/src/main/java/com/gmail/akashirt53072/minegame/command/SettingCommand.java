package com.gmail.akashirt53072.minegame.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import com.gmail.akashirt53072.minegame.Main;
import com.gmail.akashirt53072.minegame.MessageManager;
import com.gmail.akashirt53072.minegame.PlayerManager;
import com.gmail.akashirt53072.minegame.TextManager;
import com.gmail.akashirt53072.minegame.config.SignConfig;
import com.gmail.akashirt53072.minegame.config.cache.SignManager;
import com.gmail.akashirt53072.minegame.config.datatype.LocationData;
import com.gmail.akashirt53072.minegame.enums.SignType;


public class SettingCommand implements TabExecutor{
	
	private Main plugin;
	
	public SettingCommand(Main plugin) {
	    this.plugin = plugin;
	}
	
	@Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(args.length == 0) {
        	return false;
        }
		
		switch(args[0]) {
		case "reload":
			reload(sender);
			return true;
		case "signlist":
			new SignManager(plugin).showSignList(sender);
			return true;
		case "signdelete":
			if(args.length > 3) {
				signDelete(sender,args);
			}else {
				message(sender,"[error]削除する看板の座標を指定してください。");
			}
			return true;	
		case "signset":
			if(sender instanceof Player) {
				if(args.length > 1) {
					signSet((Player)sender,args[1]);
				}else {
					message(sender,"[error]看板のIDを指定してください。");
				}				
			}else {
				message(sender,"[error]コンソールからは看板を設定できません");
			}
			return true;
		case "kick":
			if(args.length > 1) {
	        	kick(sender,args[1]);
	        }else {
	        	message(sender,"[error]プレイヤーを指定してください。");
	        }
			return true;
		}
		return false;
    }
	private void message(CommandSender sender,String text) {
		if(sender instanceof Player) {
			MessageManager.sendSpecial(text, (Player)sender);
		}else {
		    sender.sendMessage(text);
		}
	}
	
	private void reload(CommandSender sender) {
		plugin.loadConfig();
		
		message(sender,"[info]ゲームが再読み込みされました。");
	}
	
	private void kick(CommandSender sender,String name) {
		Player player = plugin.getPlayer(name);
		
		if(player == null){
			message(sender,"[error]プレイヤー" + name + "はミニゲーム内に見つかりませんでした。");	
			return;
		}
		new PlayerManager(plugin,player).kick("オペレーター");
		
		message(sender,"[info]プレイヤー" + name + "がキックされました。");
		
	}
	private void signDelete(CommandSender sender,String[] args) {
		if(args.length < 4) {
			message(sender,"[error]削除する看板の座標を指定してください。");
			return;
		}
		
		if(sender instanceof Player) {
			Integer x = TextManager.toNumber((Player)sender, args[1]);
			Integer y = TextManager.toNumber((Player)sender, args[2]);
			Integer z = TextManager.toNumber((Player)sender, args[3]);
			if(x == null || y == null || z == null) {	
				return;
			}
			new SignConfig(plugin).deleteSign(new Location(plugin.getMainWorld(),x,y,z));
			message(sender, "[info]" +  x + "," + y + "," + z + "の看板を削除しました。");
			
		}else {
			Integer x = TextManager.toNumber(args[1]);
			Integer y = TextManager.toNumber(args[2]);
			Integer z = TextManager.toNumber(args[3]);
			if(x == null || y == null || z == null) {	
				return;
			}
			new SignConfig(plugin).deleteSign(new Location(plugin.getMainWorld(),x,y,z));
			message(sender, "[info]" +  x + "," + y + "," + z + "の看板を削除しました。");
			
		}
		
	}
	private void signSet(Player player,String id) {
		
		SignType type = SignType.NOTFOUND;
		try{
			type = SignType.valueOf(id);
		} catch(IllegalArgumentException ex){
			MessageManager.sendSpecial("[error]看板のIDが無効です。", player);
		}
		
		
		Location loc = player.getLocation();

		Location tempLoc = loc.clone();
		
		tempLoc.setY(tempLoc.getY() + 1.5);
		Block b = null;
		for(int i = 1;true;i++) {
			tempLoc.add(loc.getDirection().multiply(0.1));
			b = tempLoc.getBlock();
			if(b.getType().equals(Material.BIRCH_SIGN) 
					|| b.getType().equals(Material.BIRCH_WALL_SIGN)
					|| b.getType().equals(Material.OAK_SIGN)
					|| b.getType().equals(Material.OAK_WALL_SIGN)) {
				break;
			}
			if(i == 50) {
				MessageManager.sendSpecial("[error]視線上に看板がありません。", player);
				return; 
			}
		}
		
		new SignConfig(plugin).deleteSign(b.getLocation());
		new SignConfig(plugin).writeSign(new LocationData(type.toString(),b.getLocation()));
		
		MessageManager.sendSpecial("[info]視線上の看板を" + id + "に設定しました。", player);
	}
	
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args){
		if (sender instanceof Player) {
			List<String> tab = new ArrayList<String>();
			if(args.length == 1) {
            	tab.add("reload");
                tab.add("kick");
                tab.add("signlist");
                tab.add("signdelete");
                tab.add("signset");
            }else {
            	String text1 = args[0];
            	switch(text1) {
            	case "kick":
            		for(Player p : plugin.getMainWorld().getPlayers()){
            			tab.add(p.getName());
            		}
            		break;
            	case "signdelete":
            		if(args.length == 2) {
            			tab.add("整数のX座標");
            		}else if(args.length == 3) {
            			tab.add("整数のY座標");
            		}else if(args.length == 4) {
            			tab.add("整数のZ座標");
            		}else if(args.length == 5) {
            			tab.add("もういらないから...");
            		}
            		break;
            	case "signset":
            		for(SignType type : SignType.values()) {
            			tab.add(type.toString());
            		}
            		break;
            	}
            }
            return tab;
		}
		return null;
	}
}
