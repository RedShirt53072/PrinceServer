package com.gmail.akashirt53072.usefulshulker;


import java.util.logging.Level;


import com.github.redshirt53072.baseapi.general.BaseApi;
import com.github.redshirt53072.baseapi.message.LogManager;
import com.github.redshirt53072.baseapi.server.ApiManager;
import com.github.redshirt53072.baseapi.server.ApiManager.StopReason;


import com.github.redshirt53072.baseapi.server.GrowthApi;
import com.github.redshirt53072.economyapi.general.EconomyApi;
import com.github.redshirt53072.economyapi.gui.GuiManager;


/**
 * BaseApiのメインクラス
 * @author redshirt
 *
 */
public final class UsefulShulker extends GrowthApi{
	/**
	 * インスタンス
	 */
	static private UsefulShulker api;
	/**
	 * 表示名
	 */
	private String name = "UsefulShulker";
	/**
	 * A.B.Cの形式のバージョン名
	 */
	private String version = "1.0.0";
	
	
	/**
	 * 読み込み時
	 */
	@Override
	public void onEnable() {
		this.saveDefaultConfig();
	
		api = this;
		if(!BaseApi.getInstance().checkVersion("1.0.0")) {
			LogManager.logError("前提プラグインのバージョンが正しくありません。", api, new Throwable(), Level.SEVERE);
			ApiManager.stopServer("プラグインバージョンの不整合による", StopReason.ERROR);
		}
		if(!EconomyApi.getInstance().checkVersion("1.0.0")) {
			LogManager.logError("前提プラグインのバージョンが正しくありません。", api, new Throwable(), Level.SEVERE);
			ApiManager.stopServer("プラグインバージョンの不整合による", StopReason.ERROR);
		}
		
		new PlayerAction();
		//config
		
		//command

		//listener
		
		//メモリ
	
		
		LogManager.logInfo(this.getApiName() + "を読み込みました", this, Level.INFO);
	}
	
	
	/**
	 * 終了時
	 */
	@Override
	public void onDisable() {
		GuiManager.onEmergency();
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
	public static UsefulShulker getInstance() {
		return api;
	}
	
}
