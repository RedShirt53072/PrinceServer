package com.github.redshirt53072.trademanager;


import java.util.logging.Level;

import com.github.redshirt53072.growthapi.BaseAPI;
import com.github.redshirt53072.growthapi.message.LogManager;
import com.github.redshirt53072.growthapi.message.TextBuilder;
import com.github.redshirt53072.growthapi.server.GrowthPlugin;
import com.github.redshirt53072.growthapi.server.PluginManager;
import com.github.redshirt53072.growthapi.server.PluginManager.StopReason;
import com.github.redshirt53072.trademanager.bundle.BundleEvent;
import com.github.redshirt53072.trademanager.command.TradeSubCommand;
import com.github.redshirt53072.trademanager.data.VillagerManager;


/**
 * BaseApiのメインクラス
 * @author redshirt
 *
 */
public final class TradeManager extends GrowthPlugin{
	/**
	 * インスタンス
	 */
	static private TradeManager plugin;
	
	/**
	 * 読み込み時
	 */
	@Override
	public void onEnable() {
		//general
		name = "TradeManager";
		plugin = this;
		version = "3.0.0";
		PluginManager.registerApi(plugin,false);

		//依存チェック
		if(!BaseAPI.getInstance().checkVersion("3.0.0")) {
			LogManager.logError("前提プラグイン(GrowthAPI)のバージョンが正しくありません。", this, new Throwable(), Level.SEVERE);
			PluginManager.stopServer("プラグインバージョンの不整合による", StopReason.ERROR);
		}
		
		//config
		VillagerManager.reload();
		//command
		TradeSubCommand.register();
		//listener
		new VillagerEvent();
		new BundleEvent();
		
		//message
		LogManager.logInfo(TextBuilder.plus(this.getPluginName(),"を読み込みました"), this, Level.INFO);
	}
	
	
	/**
	 * 終了時
	 */
	@Override
	public void onDisable() {
		LogManager.logInfo(TextBuilder.plus(this.getPluginName(),"を終了しました"), this, Level.INFO);	
	}
	
	
	/**
	 * インスタンス取得
	 * @return apiインスタンス
	 */
	public static TradeManager getInstance() {
		return plugin;
	}
	
}
