package com.github.redshirt53072.baseapi.general;


import java.util.logging.Level;

import com.github.redshirt53072.baseapi.database.MysqlConfig;
import com.github.redshirt53072.baseapi.database.SqlManager;
import com.github.redshirt53072.baseapi.message.LogManager;
import com.github.redshirt53072.baseapi.server.GrowthApi;
import com.github.redshirt53072.baseapi.server.StopCommand;


/**
 * BaseApiのメインクラス
 * @author redshirt
 *
 */
public final class BaseApi extends GrowthApi{
	/**
	 * インスタンス
	 */
	static private BaseApi api;
	/**
	 * 表示名
	 */
	private String name = "BaseApi";
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
		SqlManager.init();
		//config
		MysqlConfig.init();
		//database load
		
		
		//command
		this.getCommand("stop").setExecutor(new StopCommand());
		
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
	public static BaseApi getInstance() {
		return api;
	}
	
}
