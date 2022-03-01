package com.github.redshirt53072.growthsurvival;

import java.util.logging.Level;

import com.github.redshirt53072.growthapi.BaseAPI;
import com.github.redshirt53072.growthapi.message.LogManager;
import com.github.redshirt53072.growthapi.message.TextBuilder;
import com.github.redshirt53072.growthapi.server.GrowthPlugin;
import com.github.redshirt53072.growthapi.server.PluginManager;
import com.github.redshirt53072.growthapi.server.PluginManager.StopReason;
import com.github.redshirt53072.growthsurvival.command.EnchSubCommand;


public class GrowthSurvival extends GrowthPlugin{
	/**
	 * インスタンス
	 */
	static private GrowthSurvival plugin;
	
	@Override
	public void onEnable() {
		//general
		name = "GrowthSurvival";
		version = "3.0.0";
		plugin = this;
		PluginManager.registerApi(plugin);
		
		
		//依存チェック
		if(!BaseAPI.getInstance().checkVersion("3.0.0")) {
			LogManager.logError("前提プラグイン(GrowthAPI)のバージョンが正しくありません。", this, new Throwable(), Level.SEVERE);
			PluginManager.stopServer("プラグインバージョンの不整合による", StopReason.ERROR);
		}
		
		//config
		
		//command
		EnchSubCommand.register();
		
		//listener
		new PlayerAction();
		new MobDrop();
		
		//メモリ
	
		//message
		LogManager.logInfo(TextBuilder.plus(getPluginName(),"を読み込みました"), this, Level.INFO);
		
	}
	/**
	 * 終了時
	 */
	@Override
	public void onDisable() {
		LogManager.logInfo(TextBuilder.plus(getPluginName(),"を終了しました"), this, Level.INFO);	
	}
	
	
	/**
	 * インスタンス取得
	 * @return apiインスタンス
	 */
	public static GrowthSurvival getInstance() {
		return plugin;
	}
}
