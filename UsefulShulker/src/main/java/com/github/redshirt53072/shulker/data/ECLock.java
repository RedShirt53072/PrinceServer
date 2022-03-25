package com.github.redshirt53072.shulker.data;

import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.OfflinePlayer;

import com.github.redshirt53072.api.BaseAPI;
import com.github.redshirt53072.api.database.SQLInterface;
import com.github.redshirt53072.api.message.LogManager;
import com.github.redshirt53072.shulker.UsefulShulker;

public class ECLock extends SQLInterface{
	private static Map<UUID,Integer> data;
	
	public ECLock() {
		super(BaseAPI.getInstance());
	}
	private void save(OfflinePlayer player,int page){
		UUID uuid = player.getUniqueId();
		new Thread() {
            @Override
            public void run() {
            	connect();
            	ECLockSqlSender sender = new ECLockSqlSender(connectData);
            	sender.delete(uuid);
        		sender.insert(uuid,page);
            	close();
            }
    	}.start();
	}
	
	public void reload() {
		new Thread() {
            @Override
            public void run() {
            	connect();
            	ECLockSqlSender sender = new ECLockSqlSender(connectData);
        		data = sender.readAll();
            	close();
            }
    	}.start();
	}
	public static void setPage(OfflinePlayer player,int page) {
		if(page > 0 && page < 10) {	
			new ECLock().save(player, page);
			data.replace(player.getUniqueId(), page);
			return;
		}
		LogManager.logInfo(player.getName() + "のエンダーチェストアンロックページデータベースに不正な値を保存しようとしています。 値：" + page, UsefulShulker.getInstance(), Level.WARNING);
	}
	
	public static int getPage(OfflinePlayer player) {
		Integer result = data.get(player.getUniqueId());
		if(result == null) {
			new ECLock().save(player, 1);
			data.put(player.getUniqueId(), 1);
			return 1;
		}
		return result;
	}
}