package com.github.redshirt53072.usefulshulker.data;

import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.entity.Player;

import com.github.redshirt53072.growthapi.BaseAPI;
import com.github.redshirt53072.growthapi.database.SQLInterface;
import com.github.redshirt53072.growthapi.message.LogManager;
import com.github.redshirt53072.usefulshulker.UsefulShulker;

public class ECLock extends SQLInterface{
	private static Map<UUID,Integer> data;
	
	public ECLock() {
		super(BaseAPI.getInstance());
	}
	private void save(Player player,int page){
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
	public static void setPage(Player player,int page) {
		if(page > 0 && page < 10) {	
			new ECLock().save(player, page);
			data.replace(player.getUniqueId(), page);
			return;
		}
		LogManager.logInfo(player.getName() + "のエンダーチェストアンロックページデータベースに不正な値を保存しようとしています。 値：" + page, UsefulShulker.getInstance(), Level.WARNING);
	}
	
	public static int getPage(Player player) {
		Integer result = data.get(player.getUniqueId());
		if(result == null) {
			new ECLock().save(player, 1);
			return 1;
		}
		return result;
	}
}