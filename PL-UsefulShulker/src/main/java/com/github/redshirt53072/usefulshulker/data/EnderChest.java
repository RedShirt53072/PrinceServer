package com.github.redshirt53072.usefulshulker.data;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import com.github.redshirt53072.growthapi.BaseAPI;
import com.github.redshirt53072.growthapi.database.SQLInterface;
import com.github.redshirt53072.growthapi.gui.GuiManager;
import com.github.redshirt53072.growthapi.player.InitListener;
import com.github.redshirt53072.growthapi.player.PlayerManager;
import com.github.redshirt53072.growthapi.util.Serializer;

public class EnderChest extends SQLInterface implements InitListener{
	public EnderChest() {
		super(BaseAPI.getInstance());
	}
	public void closeSave(Player player,Inventory inv,int page){
		PlayerManager.addAsyncLock("ec", player);
		UUID uuid = player.getUniqueId();
		String playerData = Serializer.toJson(inv,0,26);
		for (int i = 0; i < 27; i++) {
			inv.clear(i);
		}
		new Thread() {
            @Override
            public void run() {
            	connect();
            	EnderChestSqlSender sender = new EnderChestSqlSender(connectData);
            	sender.delete(uuid,page);
        		sender.insert(uuid,page, playerData);
            	close();
            	PlayerManager.removeAsyncLock("ec", player);
            }
    	}.start();
	}
	
	public void openLoad(Player player,Inventory inv,int page) {
		UUID uuid = player.getUniqueId();
		PlayerManager.addAsyncLock("ec", player);
		new Thread() {
            @Override
            public void run() {
            	connect();
            	EnderChestSqlSender sender = new EnderChestSqlSender(connectData);
        		String data = sender.read(uuid,page);
            	close();
            	Bukkit.getScheduler().runTask(plugin, new Runnable() {
    	    		@Override
    	    		public void run() {
    	    			GuiManager.clearItem(inv, 0, 26);
    	    			Serializer.toGui(data, inv, 0, 26);
    	    			PlayerManager.removeAsyncLock("ec", player);
    	    		}
    	    	});
            }
    	}.start();
	}
	public void pageChange(Player player,Inventory inv,int oldPage,int newPage) {
		UUID uuid = player.getUniqueId();
		String playerData = Serializer.toJson(inv,0,26);
		PlayerManager.addAsyncLock("ec", player);
		new Thread() {
            @Override
            public void run() {
            	connect();
            	EnderChestSqlSender sender = new EnderChestSqlSender(connectData);
            	sender.delete(uuid,oldPage);
        		sender.insert(uuid,oldPage, playerData);
        		String data = sender.read(uuid,newPage);
            	close();
            	Bukkit.getScheduler().runTask(plugin, new Runnable() {
    	    		@Override
    	    		public void run() {
    	    			GuiManager.clearItem(inv, 0, 26);
    	    			Serializer.toGui(data, inv, 0, 26);
    	    			PlayerManager.removeAsyncLock("ec", player);
        	    	}
    	    	});
            }
    	}.start();
	}
	@Override
	public void onInit(Player player) {
		UUID uuid = player.getUniqueId();
		new Thread() {
            @Override
            public void run() {
            	connect();
            	EnderChestSqlSender sender = new EnderChestSqlSender(connectData);
            	sender.init(uuid);;
        		close();
            }
    	}.start();
	}
}