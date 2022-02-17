package com.github.redshirt53072.growthapi.player;

import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import com.github.redshirt53072.growthapi.BaseAPI;
import com.github.redshirt53072.growthapi.database.SQLInterface;
import com.github.redshirt53072.growthapi.message.LogManager;
import com.github.redshirt53072.growthapi.message.MessageManager;
import com.github.redshirt53072.growthapi.util.Serializer;

public class PlayerStorage extends SQLInterface{
	
	public PlayerStorage() {
		super(BaseAPI.getInstance());
		if(!BaseAPI.canUseMySQL()) {
			LogManager.logError("[Database]アイテムストレージ機能の使用にはMySQLデータベース接続が必要です。", BaseAPI.getInstance(), new Throwable(), Level.SEVERE);
		}
	}
	public void savePlayer(Player player ,boolean clearData){
		UUID uuid = player.getUniqueId();
		String playerData = Serializer.toJson(player);
		new Thread() {
            @Override
            public void run() {
            	connect();
            	StorageSqlSender sender = new StorageSqlSender(connectData);
            	sender.delete(uuid);
        		sender.insert(uuid, playerData);
            	close();
            	if(clearData) {
            		Bukkit.getScheduler().runTask(plugin, new Runnable() {
        	    		@Override
        	    		public void run() {
        	    			player.getInventory().clear();
        	    			for(PotionEffect e : player.getActivePotionEffects()){
        	    				player.removePotionEffect(e.getType());
        	    			}
        	    			player.setHealth(20);
        	    			player.setFoodLevel(20);
        	    			player.setTotalExperience(0);
        	    			player.setSaturation(10);
        	    		 	MessageManager.sendImportant("アイテム持ち込み制限のあるディメンションに移動したため、一時的にアイテムが没収されました。", player);
            	    	}
            		});
            	}
            }
    	}.start();
	}
	
	public void loadPlayer(Player player) {
		UUID uuid = player.getUniqueId();
		new Thread() {
            @Override
            public void run() {
            	connect();
            	StorageSqlSender sender = new StorageSqlSender(connectData);
        		String data = sender.read(uuid);
            	Bukkit.getScheduler().runTask(plugin, new Runnable() {
    	    		@Override
    	    		public void run() {
    	    			player.getInventory().clear();
    	    			for(PotionEffect e : player.getActivePotionEffects()){
    	    				player.removePotionEffect(e.getType());
    	    			}
    	    		 	Serializer.toPlayer(data,player);
    	    		 	MessageManager.sendImportant("通常ディメンションに戻ったため、アイテムが返却されました。", player);
    	    		}
    	    	});
            	close();
            }
    	}.start();
	}
	
}