package com.github.redshirt53072.swallowfishing;

import java.util.logging.Level;

import com.github.redshirt53072.growthapi.BaseAPI;
import com.github.redshirt53072.growthapi.message.LogManager;
import com.github.redshirt53072.growthapi.server.GrowthPlugin;
import com.github.redshirt53072.growthapi.server.PluginManager;
import com.github.redshirt53072.growthapi.server.PluginManager.StopReason;
import com.github.redshirt53072.growthsurvival.collection.CollectionManager;
import com.github.redshirt53072.swallowfishing.collection.FishCollection;
import com.github.redshirt53072.swallowfishing.command.FishSubCommand;
import com.github.redshirt53072.swallowfishing.command.SellCommand;
import com.github.redshirt53072.swallowfishing.data.FishManager;


public class SwallowFishing extends GrowthPlugin{
	/**
	 * インスタンス
	 */
	static private SwallowFishing plugin;
	
	@Override
	public void onEnable() {
		//general
		name = "NewFishing";
		version = "3.0.0";
		plugin = this;
		PluginManager.registerApi(plugin);
		
		//依存チェック
		if(!BaseAPI.getInstance().checkVersion("3.0.0")) {
			LogManager.logError("前提プラグイン(GrowthAPI)のバージョンが正しくありません。", this, new Throwable(), Level.SEVERE);
			PluginManager.stopServer("プラグインバージョンの不整合による", StopReason.ERROR);
		}
		
		//collection
		CollectionManager.register(new FishCollection());
		
		//config
		FishManager.reload();
		
		//command
		FishSubCommand.register();
		
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
	public static SwallowFishing getInstance() {
		return plugin;
	}
}
