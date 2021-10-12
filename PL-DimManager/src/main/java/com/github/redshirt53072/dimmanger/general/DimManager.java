package com.github.redshirt53072.dimmanger.general;


import java.util.logging.Level;

import com.github.redshirt53072.baseapi.database.MysqlConfig;
import com.github.redshirt53072.baseapi.database.SqlManager;
import com.github.redshirt53072.baseapi.general.BaseApi;
import com.github.redshirt53072.baseapi.message.LogManager;
import com.github.redshirt53072.baseapi.server.ApiManager;
import com.github.redshirt53072.baseapi.server.ApiManager.StopReason;
import com.github.redshirt53072.baseapi.server.GrowthApi;
import com.github.redshirt53072.baseapi.server.StopCommand;
import com.github.redshirt53072.dimmanger.command.DimAllCommand;
import com.github.redshirt53072.dimmanger.command.WorldCommand;
import com.github.redshirt53072.dimmanger.data.DimData;
import com.github.redshirt53072.dimmanger.data.WorldSqlSender;


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
		api = this;
		if(!BaseApi.getInstance().checkVersion("0.1.0")) {
			LogManager.logError("前提プラグインのバージョンが正しくありません。", api, new Throwable(), Level.SEVERE);
			ApiManager.stopServer("プラグインバージョンの不整合による", StopReason.ERROR);
		}
		
		//config
		DimData.init();
		
		//listener
		new PlayerLogin(this);
		
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
