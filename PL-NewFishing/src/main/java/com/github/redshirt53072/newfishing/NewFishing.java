package com.github.redshirt53072.newfishing;

import java.util.logging.Level;

import com.github.redshirt53072.growthapi.BaseAPI;
import com.github.redshirt53072.growthapi.message.LogManager;
import com.github.redshirt53072.growthapi.server.GrowthPlugin;
import com.github.redshirt53072.growthapi.server.GrowthPluginManager;
import com.github.redshirt53072.growthapi.server.GrowthPluginManager.StopReason;
import com.github.redshirt53072.newfishing.command.ReloadSubCommand;
import com.github.redshirt53072.newfishing.command.SellCommand;


public class NewFishing extends GrowthPlugin{
	/**
	 * インスタンス
	 */
	static private NewFishing plugin;
	
	@Override
	public void onEnable() {
		
		
		//general
		name = "NewFishing";
		version = "2.3.0";
		plugin = this;
		LogManager.registerLogger(this);
		this.saveDefaultConfig();
		
		
		//依存チェック
		if(!BaseAPI.getInstance().checkVersion("2.2.0")) {
			LogManager.logError("前提プラグイン(GrowthAPI)のバージョンが正しくありません。", this, new Throwable(), Level.SEVERE);
			GrowthPluginManager.stopServer("プラグインバージョンの不整合による", StopReason.ERROR);
		}
		
	
		//config
		
		//command
		ReloadSubCommand.register();
		
		this.getCommand("sellfish").setExecutor(new SellCommand());
		//listener
		new PlayerAction();
		//メモリ
	
		//message
		LogManager.logInfo(getPluginName() + "を読み込みました", this, Level.INFO);
		
	}
	/**
	 * 終了時
	 */
	@Override
	public void onDisable() {
		LogManager.logInfo(getPluginName() + "を終了しました", this, Level.INFO);	
	}
	
	
	/**
	 * インスタンス取得
	 * @return apiインスタンス
	 */
	public static NewFishing getInstance() {
		return plugin;
	}
}
