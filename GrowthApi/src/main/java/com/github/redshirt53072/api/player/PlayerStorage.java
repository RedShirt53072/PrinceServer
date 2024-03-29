package com.github.redshirt53072.api.player;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import com.github.redshirt53072.api.BaseAPI;
import com.github.redshirt53072.api.database.SQLInterface;
import com.github.redshirt53072.api.message.MessageManager;
import com.github.redshirt53072.api.util.Serializer;

public class PlayerStorage extends SQLInterface{
	
	public PlayerStorage() {
		super(BaseAPI.getInstance());
	}
	public void savePlayer(Player player ,boolean clearData){
		UUID uuid = player.getUniqueId();
		String playerData = Serializer.toJson(player);
		
		if(clearData) {
			player.getInventory().clear();
			for(PotionEffect e : player.getActivePotionEffects()){
				player.removePotionEffect(e.getType());
			}
			player.setHealth(20);
			player.setFoodLevel(20);
			player.setTotalExperience(0);
			player.setSaturation(10);
		 	MessageManager.sendImportant("アイテム持ち込み制限のあるディメンションに移動したため、一時的にアイテムを保存しました。", player);
    	}
		
		new Thread() {
            @Override
            public void run() {
            	connect();
            	StorageSqlSender sender = new StorageSqlSender(connectData);
            	sender.delete(uuid);
        		sender.insert(uuid, playerData);
            	close();
            }
    	}.start();
	}
	
	public void loadPlayer(Player player) {
		UUID uuid = player.getUniqueId();
		player.getInventory().clear();
		for(PotionEffect e : player.getActivePotionEffects()){
			player.removePotionEffect(e.getType());
		}
		new Thread() {
            @Override
            public void run() {
            	connect();
            	StorageSqlSender sender = new StorageSqlSender(connectData);
        		String data = sender.read(uuid);
            	Bukkit.getScheduler().runTask(plugin, new Runnable() {
    	    		@Override
    	    		public void run() {
    	    		 	Serializer.toPlayer(data,player);
    	    		 	MessageManager.sendImportant("通常ディメンションに戻ったため、保存されていたアイテムが返却されました。", player);
    	    		}
    	    	});
            	close();
            }
    	}.start();
	}
	
}