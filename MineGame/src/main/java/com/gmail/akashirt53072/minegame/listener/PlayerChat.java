package com.gmail.akashirt53072.minegame.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.gmail.akashirt53072.minegame.Main;
import com.gmail.akashirt53072.minegame.enums.PlayerStatus;
import com.gmail.akashirt53072.minegame.match.MatchManager;
import com.gmail.akashirt53072.minegame.nbt.NBTPlayerStatus;


public final class PlayerChat implements Listener{
	Main plugin;
	public PlayerChat(Main plugin) {
    	this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
	
	@EventHandler
    public void onPlayerChat(final AsyncPlayerChatEvent event) {
    	Player player = event.getPlayer();

    	String text = event.getMessage();
    	
        //チャット処理
    	PlayerStatus status = new NBTPlayerStatus(plugin,player).getType();
		
    	if(status.equals(PlayerStatus.lOGOUT)) {
    		return;
    	}
    	if(status.equals(PlayerStatus.BREAKING)) {
    		return;
    	}
    	
    	event.setCancelled(true);
    	
    	
    	String st1 = text.substring(0,1);
    	if(st1.equals("!") && text.length() > 2) {
    		String st2 = text.substring(0,2);
    		if(player.isOp() && st2.equals("!o")) {
        		text = text.replaceAll("^!o", "");
        		opChat(player,text);
        		return;
    		}
    		if(st2.equals("!a")) {
    			text = text.replaceAll("^!a", "");
        		
        		allChat(player,text);
    			return;
            }
    		
    		if(status ==  PlayerStatus.ONMATCH && st2.equals("!g")) {
    			text = text.replaceAll("^!g", "");
				
				matchChat(player,text);
				return;
    		}	
    	}
    	defChat(status,player,text);	
    	
    }
	
	
	private void defChat(PlayerStatus status,Player player,String text) {
		switch(status) {
		case LOBBY:
			text = text.replaceAll("^!g", "");
			lobbyChat(player,text);
			break;
		case WAITING:
			text = text.replaceAll("^!g", "");
			matchChat(player,text);
			break;
		case ONMATCH:
			teamChat(player,text);
			break;
		default :
		}
	}
	
	private void opChat(Player player,String text) {
		Bukkit.getScheduler().runTask(plugin, new Runnable() {
            @Override
            public void run() {
            	//in minecraft
                //OP全体チャット
            	for(Player pl : plugin.getPlayers()) {
            		pl.sendMessage(ChatColor.RED + "《OP》" + ChatColor.WHITE + "<" +  player.getName() + ">"  + text);	
            	}
                plugin.getLogger().info("《OP》" + "<" +  player.getName() + ">"  + text);
            }
    	});
	}
	
	private void teamChat(Player player,String text) {
		Bukkit.getScheduler().runTask(plugin, new Runnable() {
            @Override
            public void run() {
            	//in minecraft
                //陣営チャット
            	MatchManager mod = new MatchManager(plugin);
        		mod.MessageTeamMenber(player, ChatColor.YELLOW + "《チーム》" + ChatColor.WHITE + "<" +  player.getName() + ">"  + text);
        	    
        		plugin.getLogger().info("《チーム》"  + "<" +  player.getName() + ">"  + text);
                
            }
    	});
	}
	private void matchChat(Player player,String text) {
		
		Bukkit.getScheduler().runTask(plugin, new Runnable() {
            @Override
            public void run() {
            	//in minecraft
                //試合チャット
                
            	MatchManager mod = new MatchManager(plugin);
        		mod.MessageMatchMenber(player, ChatColor.GREEN + "《試合》" + ChatColor.WHITE + "<" +  player.getName() + ">"  + text);
        		
        		plugin.getLogger().info("《試合》" + "<" +  player.getName() + ">"  + text);
                
            }
    	});
	}
	private void lobbyChat(Player player,String text) {
		Bukkit.getScheduler().runTask(plugin, new Runnable() {
            @Override
            public void run() {
            	//in minecraft
                //ロビーチャット
                
            	for(Player pl : plugin.getPlayers()) {
            		PlayerStatus status = new NBTPlayerStatus(plugin,pl).getType();
            		if(status.equals(PlayerStatus.LOBBY)) {
            			pl.sendMessage(ChatColor.GREEN + "《ロビー》" + ChatColor.WHITE + "<" +  player.getName() + ">"  + text);		
            		}
            	}
            	
            	plugin.getLogger().info("《ロビー》" + "<" +  player.getName() + ">"  + text);
                
            }
    	});
	}
	private void allChat(Player player,String text) {
		Bukkit.getScheduler().runTask(plugin, new Runnable() {
            @Override
            public void run() {
            	//in minecraft
                for(Player pl : plugin.getPlayers()) {
            		pl.sendMessage(ChatColor.GOLD + "《全体》" + ChatColor.WHITE + "<" +  player.getName() + ">"  + text);	
            	}
                plugin.getLogger().info("《全体》" + "<" +  player.getName() + ">"  + text);
                
            }
    	});
	}
}