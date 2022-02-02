package com.github.redshirt53072.growthapi.event;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

import com.github.redshirt53072.growthapi.BaseAPI;
import com.github.redshirt53072.growthapi.command.MaintenanceCommand;
import com.github.redshirt53072.growthapi.message.MessageManager;
import com.github.redshirt53072.growthapi.message.MessageManager.MessageLevel;
import com.github.redshirt53072.growthapi.server.GrowthPlugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;


/**
 * playerの行動をトリガーにGUIの処理クラスへと取り次ぐクラス
 * @author akash
 *
 */
public final class PlayerInOut implements Listener {
    
	public PlayerInOut() {
    	GrowthPlugin plugin = BaseAPI.getInstance();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onLogin(final PlayerLoginEvent event) {
    	if(MaintenanceCommand.isClose()) {
        	Player p = event.getPlayer();
        	if(p.hasPermission(Bukkit.getPluginManager().getPermission("growth.op"))){
        		MessageManager.sendSpecial("[maintenance]このサーバーはメンテナンス中です。", p);
        		MessageManager.sendOPPlayer(MessageLevel.SPECIAL, ChatColor.YELLOW + p.getName() + "がgrowth.opのパーミッションを利用してログインしました。");
        		return;
        	}
        	if(p.isOp()){
        		MessageManager.sendSpecial("[maintenance]このサーバーはメンテナンス中です。", p);
        		MessageManager.sendOPPlayer(MessageLevel.SPECIAL, ChatColor.YELLOW + p.getName() + "がOP権限を利用してログインしました。");
        		return;
        	}
    		event.disallow(Result.KICK_OTHER, "[maintenance]このサーバーはメンテナンス中です。");
    	}
    }
}