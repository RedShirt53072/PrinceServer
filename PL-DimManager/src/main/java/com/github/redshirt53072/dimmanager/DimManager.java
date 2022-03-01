package com.github.redshirt53072.dimmanager;

import java.util.logging.Level;

import org.bukkit.ChatColor;

import com.github.redshirt53072.growthapi.BaseAPI;
import com.github.redshirt53072.growthapi.message.LogManager;
import com.github.redshirt53072.growthapi.message.TextBuilder;
import com.github.redshirt53072.growthapi.player.PlayerManager;
import com.github.redshirt53072.growthapi.server.PluginManager;
import com.github.redshirt53072.growthapi.server.PluginManager.StopReason;
import com.github.redshirt53072.growthapi.server.GrowthPlugin;
import com.github.redshirt53072.dimmanager.command.DimSubCommand;
import com.github.redshirt53072.dimmanager.command.HomeCommand;
import com.github.redshirt53072.dimmanager.command.WorldCommand;
import com.github.redshirt53072.dimmanager.data.WorldManager;

/**
 * BaseApiのメインクラス
 * @author redshirt
 *
 */
public final class DimManager extends GrowthPlugin{
	/**
	 * インスタンス
	 */
	static private DimManager plugin;
	
	/**
	 * 読み込み時
	 */
	@Override
	public void onEnable() {
		//general
		name = "DimManager";
		version = "3.0.0";
		plugin = this;
		PluginManager.registerApi(plugin,true);
		
		//依存チェック
		if(!BaseAPI.getInstance().checkVersion("3.0.0")) {
			LogManager.logError("前提プラグイン(GrowthAPI)のバージョンが正しくありません。", this, new Throwable(), Level.SEVERE);
			PluginManager.stopServer("プラグインバージョンの不整合による", StopReason.ERROR);
		}
		
		
		//config
		WorldManager.reload();
		
		
		//event
		new PlayerLogin();
		new PlayerAction();
		
		//emergency
		PluginManager.registerEmergency(new WorldManager());		
		PlayerManager.registerInit(new WorldManager());
		PlayerManager.registerLogout(new WorldManager());
		
		//メモリ
		WorldManager.reload();
		
		//command
		DimSubCommand.register();
		
		this.getCommand("world").setExecutor(new WorldCommand());
		this.getCommand("home").setExecutor(new HomeCommand());
		
		//message
		LogManager.logInfo(new TextBuilder(ChatColor.WHITE)
				.addText(getPluginName(),"を読み込みました")
				.build() , this, Level.INFO);
	}
	/**
	 * 終了時
	 */
	@Override
	public void onDisable() {
		LogManager.logInfo(new TextBuilder(ChatColor.WHITE)
				.addText(getPluginName(),"を終了しました")
				.build(), this, Level.INFO);	
	}
	
	/**
	 * インスタンス取得
	 * @return apiインスタンス
	 */
	public static DimManager getInstance() {
		return plugin;
	}
	
}
