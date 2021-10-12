package com.github.redshirt53072.dimmanager.general;


import java.util.logging.Level;

import com.github.redshirt53072.baseapi.general.BaseApi;
import com.github.redshirt53072.baseapi.message.LogManager;
import com.github.redshirt53072.baseapi.server.ApiManager;
import com.github.redshirt53072.baseapi.server.ApiManager.StopReason;
import com.github.redshirt53072.dimmanager.command.DimAllCommand;
import com.github.redshirt53072.dimmanager.command.DimSubCommand;
import com.github.redshirt53072.dimmanager.command.WorldCommand;
import com.github.redshirt53072.dimmanager.data.DimData;
import com.github.redshirt53072.baseapi.server.GrowthApi;


/**
 * BaseApiのメインクラス
 * @author redshirt
 *
 */
public final class DimManager extends GrowthApi{
	/**
	 * インスタンス
	 */
	static private DimManager api;
	/**
	 * 表示名
	 */
	private String name = "DimManager";
	/**
	 * A.B.Cの形式のバージョン名
	 */
	private String version = "0.1.0";
	
	/**
	 * 読み込み時
	 */
	@Override
	public void onEnable() {
		this.saveDefaultConfig();
	
		api = this;
		if(!BaseApi.getInstance().checkVersion("0.1.0")) {
			LogManager.logError("前提プラグインのバージョンが正しくありません。", api, new Throwable(), Level.SEVERE);
			ApiManager.stopServer("プラグインバージョンの不整合による", StopReason.ERROR);
		}
		
		//config
		DimData.init();
		
		//command
		DimSubCommand.register();
		
		//listener
		new PlayerLogin(this);
		
		//メモリ
		WorldManager.reload();
		
		//command
		this.getCommand("world").setExecutor(new WorldCommand());
		this.getCommand("alldim").setExecutor(new DimAllCommand());
		
		LogManager.logInfo(this.getApiName() + "を読み込みました", this, Level.INFO);
	}
	/**
	 * 終了時
	 */
	@Override
	public void onDisable() {
		LogManager.logInfo(this.getApiName() + "を終了しました", this, Level.INFO);	
	}
	@Override
	public String getApiName() {
		return name;
	}
	@Override
	public String getApiVersion() {
		return version;
	}
	@Override
	public boolean checkVersion(String ver) {
		return ver.equals(version);
	}
	
	
	
	/**
	 * インスタンス取得
	 * @return apiインスタンス
	 */
	public static DimManager getInstance() {
		return api;
	}
	
}