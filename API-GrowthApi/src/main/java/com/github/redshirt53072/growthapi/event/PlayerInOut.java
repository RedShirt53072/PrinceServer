package com.github.redshirt53072.growthapi.event;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerQuitEvent;

import com.github.redshirt53072.growthapi.BaseAPI;
import com.github.redshirt53072.growthapi.message.LogManager;
import com.github.redshirt53072.growthapi.message.TextBuilder;
import com.github.redshirt53072.growthapi.message.MessageManager;
import com.github.redshirt53072.growthapi.player.PlayerManager;
import com.github.redshirt53072.growthapi.server.GrowthPlugin;
import com.github.redshirt53072.growthapi.server.PluginManager;
import com.github.redshirt53072.growthapi.server.PluginManager.StopReason;
import com.github.redshirt53072.growthapi.server.Maintenance;

import java.util.logging.Level;

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
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onLogin(PlayerLoginEvent event) {
    	Player p = event.getPlayer();
    	
    	if(checkPerm(p)) {
    		return;
    	}
    	
    	if(Maintenance.isMain()) {
    		String mainte = buildMessage("現在サーバーはメンテナンス中です。");
    		if(Maintenance.canOPLogin(p)) {
    			MessageManager.sendSpecial(mainte, p);
        		MessageManager.sendSpecial(buildMessage("あなたはOP権限を利用してログインしました。"), p);
        		LogManager.logInfo(
        				new TextBuilder(ChatColor.WHITE)
            			.addPlayerName(p)
            			.addText("がgrowth.opの権限を利用してログインしました。")
            			.build(),BaseAPI.getInstance(),Level.INFO);	
    		}else if(Maintenance.canJoin(p)) {
        		MessageManager.sendSpecial(mainte, p);
           		MessageManager.sendSpecial(buildMessage("あなたはテストプレイ用ログイン権限を利用してログインしました。"), p);
           		LogManager.logInfo(
        				new TextBuilder(ChatColor.WHITE)
            			.addPlayerName(p) 
            			.addText("がテストプレイ用ログイン権限を利用してログインしました。")
            			.build(),BaseAPI.getInstance(),Level.INFO);	
    		}else {
        		event.disallow(Result.KICK_OTHER, mainte);
           		return;	
    		}
    	}
    	if(Maintenance.isPlayerLimitOver()) {
    		if(Maintenance.canOPLogin(p)) {
    			MessageManager.sendSpecial("現在サーバーのログイン上限人数に達しています。", p);
        		MessageManager.sendSpecial("あなたはOP権限を利用してログインしました。", p);
        		LogManager.logInfo(new TextBuilder(ChatColor.WHITE)
            			.addPlayerName(p) 
            			.addText("がgrowth.opの権限を利用して上限を超えてログインしました。")
            			.build(),BaseAPI.getInstance(),Level.INFO);	
    		}else if(Maintenance.canJoin(p)) {
        		MessageManager.sendSpecial("現在サーバーのログイン上限人数に達しています。", p);
           		MessageManager.sendSpecial("あなたはテストプレイ用ログイン権限を利用して上限を超えてログインしました。", p);
           		LogManager.logInfo(new TextBuilder(ChatColor.WHITE)
            			.addPlayerName(p) 
            			.addText("がテストプレイ用ログイン権限を利用してログインしました。")
            			.build(),BaseAPI.getInstance(),Level.INFO);	
    		}else {
        		event.disallow(Result.KICK_OTHER, "現在サーバーのログイン上限人数に達しています。");
           		return;	
    		}
    	}
    	
    	
    	PlayerManager.initPlayer(p);
    }
    
    private String buildMessage(String msg) {
    	return new TextBuilder(ChatColor.WHITE)
		.addColorText(ChatColor.RED,"[maintenance]")
		.addText(msg)
		.build();
    }
    
    private boolean checkPerm(Player p) {
    	if(p.hasPermission(Bukkit.getPluginManager().getPermission("growth.console"))){
    		PlayerManager.removePermission(p);
    		LogManager.logInfo(p.getName() + "はgrowth.consoleのパーミッションを不正に取得しています!",BaseAPI.getInstance(),Level.SEVERE);
    		PluginManager.kickPlayer(p, "コンソール用パーミッションを取得する", StopReason.GRIEFING);
    		return true;
    	}
    	if(p.hasPermission(Bukkit.getPluginManager().getPermission("growth.op"))){
    		PlayerManager.removePermission(p);
    		LogManager.logInfo(p.getName() + "はgrowth.opのパーミッションを不正に取得しています!",BaseAPI.getInstance(),Level.SEVERE);
    		PluginManager.kickPlayer(p, "OPパーミッションを取得する", StopReason.GRIEFING);
    		return true;
    	}
    	return false;
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onLogout(PlayerQuitEvent event) {
    	Player p = event.getPlayer();
    	if(p.isOp()) {
    		p.setOp(false);
    	}
    	PlayerManager.removePermission(p);
    	
        PlayerManager.logOutPlayer(p);
    }
}