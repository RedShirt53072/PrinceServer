package com.github.redshirt53072.growthapi.event;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerQuitEvent;

import com.github.redshirt53072.growthapi.BaseAPI;
import com.github.redshirt53072.growthapi.command.MaintenanceCommand;
import com.github.redshirt53072.growthapi.gui.Gui;
import com.github.redshirt53072.growthapi.gui.GuiManager;
import com.github.redshirt53072.growthapi.message.MessageManager;
import com.github.redshirt53072.growthapi.message.MessageManager.MessageLevel;
import com.github.redshirt53072.growthapi.player.PlayerManager;
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
    public void onLogin(PlayerLoginEvent event) {
    	Player p = event.getPlayer();
    	if(MaintenanceCommand.isClose()) {
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
    	PlayerManager.initPlayer(p);
    }
    @EventHandler(priority = EventPriority.HIGH)
    public void onLogout(PlayerQuitEvent event) {
    	Player p = event.getPlayer();
        Gui gui = GuiManager.getGui(p);
        if(gui != null) {
            gui.onEmergency();    	
        }
    }
}