package com.github.redshirt53072.world;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.github.redshirt53072.api.message.LogManager;
import com.github.redshirt53072.api.message.MessageManager;
import com.github.redshirt53072.api.message.SoundManager;
import com.github.redshirt53072.api.message.TextBuilder;
import com.github.redshirt53072.api.player.PlayerStorage;
import com.github.redshirt53072.world.data.DimData;
import com.github.redshirt53072.world.data.WorldManager;

public final class Teleporter {
	private static Set<Player> tpPlayers = new HashSet<Player>();
	
	static boolean isTPPLayer(Player player){
		if(tpPlayers.contains(player)) {
    		tpPlayers.remove(player);
    		return true;
    	}
		return false;
	}
	
	public static void registerTPPLayer(Player p){
    	tpPlayers.add(p);
    }
	
	public static void teleportDefault(Player player,DimData dd) {
		GameMode mode = dd.getGamemode();
		GameMode old = player.getGameMode();
		
		if(mode.equals(GameMode.CREATIVE) && !old.equals(GameMode.CREATIVE)) {
			new PlayerStorage().savePlayer(player, true);
		}
		if(old.equals(GameMode.CREATIVE) && !mode.equals(GameMode.CREATIVE)) {
			new PlayerStorage().loadPlayer(player);
		}
		player.setGameMode(mode);
		teleport(player,dd.getLocation());
		SoundManager.sendSuccess(player);
	}
	
	public static void teleportSavedLocation(Player player,DimData dd) {
		new WorldManager().saveNormal(player);
		
		String world = dd.getName();
		
		new Thread() {
	        @Override
    		public void run() {
    			GameMode mode = dd.getGamemode();
    			Location loc = new WorldManager().readLoc(player, world);
    			
    			if(loc == null) {
    				MessageManager.sendWarning("移動に失敗しました。システム上の不具合の可能性もあるため、運営にご報告ください。", player);
    				LogManager.logError(new TextBuilder(ChatColor.WHITE)
    						.addPlayerName(player)
    						.addText("の",world,"への転送に失敗しました。")
    						.build(), DimManager.getInstance(), new Exception(),Level.WARNING);
    				return;
    			}
    			
    			Bukkit.getScheduler().runTask(DimManager.getInstance(), new Runnable() {
    	    		@Override
    	    		public void run() {
    	    			GameMode old = player.getGameMode();
    	    			
    	    			if(mode.equals(GameMode.CREATIVE) && !old.equals(GameMode.CREATIVE)) {
    	    				new PlayerStorage().savePlayer(player, true);
    	    			}
    	    			if(old.equals(GameMode.CREATIVE) && !mode.equals(GameMode.CREATIVE)) {
    	    				new PlayerStorage().loadPlayer(player);
    	    			}

    	    			player.setGameMode(mode);
    	    			
    	    			teleport(player,loc);
    	    			
    	    			SoundManager.sendSuccess(player);
    	    		}
    	    	});	
    		}
		}.start();
	}
	
	public static void teleportSavedLocation(Player player,OfflinePlayer target,DimData dd) {
		new WorldManager().saveNormal(player);
		
		String world = dd.getName();
		
		new Thread() {
	        @Override
    		public void run() {
    			GameMode mode = dd.getGamemode();
    			Location loc = new WorldManager().readLoc(target, world);
    			
    			if(loc == null) {
    				MessageManager.sendWarning("移動に失敗しました。システム上の不具合の可能性もあるため、運営にご報告ください。", player);
    				
    				LogManager.logError(new TextBuilder(ChatColor.WHITE)
    						.addText(world,"での")
    						.addPlayerName(target)
    						.addText("のスポーン地点への")
    						.addPlayerName(player)
    						.addText("の転送に失敗しました。")
    						.build(), DimManager.getInstance(), new Exception(),Level.WARNING);
    				return;
    			}
    			
    			Bukkit.getScheduler().runTask(DimManager.getInstance(), new Runnable() {
    	    		@Override
    	    		public void run() {
    	    			GameMode old = player.getGameMode();
    	    			
    	    			if(mode.equals(GameMode.CREATIVE) && !old.equals(GameMode.CREATIVE)) {
    	    				new PlayerStorage().savePlayer(player, true);
    	    			}
    	    			if(old.equals(GameMode.CREATIVE) && !mode.equals(GameMode.CREATIVE)) {
    	    				new PlayerStorage().loadPlayer(player);
    	    			}

    	    			player.setGameMode(mode);
    	    			
    	    			teleport(player,loc);
    	    			
    	    			SoundManager.sendSuccess(player);
    	    		}
    	    	});	
    		}
		}.start();
	}
	
	
	public static void teleport(Player player,Location loc) {
		new WorldManager().saveNormal(player);
		registerTPPLayer(player);
		player.teleport(loc);
	}
}