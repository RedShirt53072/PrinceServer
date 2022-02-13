package com.github.redshirt53072.dimmanager;

import java.util.logging.Level;

import com.github.redshirt53072.growthapi.BaseAPI;
import com.github.redshirt53072.growthapi.message.LogManager;
import com.github.redshirt53072.growthapi.server.GrowthPluginManager;
import com.github.redshirt53072.growthapi.server.GrowthPluginManager.StopReason;
import com.github.redshirt53072.growthapi.server.GrowthPlugin;
import com.github.redshirt53072.dimmanager.command.DimAllCommand;
import com.github.redshirt53072.dimmanager.command.DimSubCommand;
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
		version = "2.3.0";
		plugin = this;
		LogManager.registerLogger(this);
		this.saveDefaultConfig();
		
		//依存チェック
		if(!BaseAPI.getInstance().checkVersion("2.3.0")) {
			LogManager.logError("前提プラグイン(GrowthAPI)のバージョンが正しくありません。", this, new Throwable(), Level.SEVERE);
			GrowthPluginManager.stopServer("プラグインバージョンの不整合による", StopReason.ERROR);
		}
		//emergency
		GrowthPluginManager.registerEmergency(new WorldManager());
		
		
		//config
		WorldManager.reload();
		
		//sql
		if(!BaseAPI.canUseMySQL()) {
			LogManager.logError("DimManagerではMySQLデータベースの使用が必須ですが、現在無効化されています", this, new Throwable(), Level.SEVERE);
			GrowthPluginManager.stopServer("前提機能の無効化による", GrowthPluginManager.StopReason.ERROR);
		}
		
		//event
		new PlayerLogin();
		
		//メモリ
		WorldManager.reload();
		
		//command
		DimSubCommand.register();
		
		this.getCommand("world").setExecutor(new WorldCommand());
		this.getCommand("alldim").setExecutor(new DimAllCommand());
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
	public static DimManager getInstance() {
		return plugin;
	}
	
}
