package com.github.redshirt53072.economyapi.general;


import java.util.logging.Level;

import org.bukkit.plugin.RegisteredServiceProvider;

import com.github.redshirt53072.baseapi.general.BaseApi;
import com.github.redshirt53072.baseapi.message.LogManager;
import com.github.redshirt53072.baseapi.server.ApiManager;
import com.github.redshirt53072.baseapi.server.ApiManager.StopReason;

import net.milkbowl.vault.economy.Economy;

import com.github.redshirt53072.baseapi.server.GrowthApi;
import com.github.redshirt53072.economyapi.command.EmeraldCommand;
import com.github.redshirt53072.economyapi.gui.GuiManager;


/**
 * BaseApiのメインクラス
 * @author redshirt
 *
 */
public final class EconomyApi extends GrowthApi{
	/**
	 * インスタンス
	 */
	static private EconomyApi api;
	/**
	 * 表示名
	 */
	private String name = "EconomyApi";
	/**
	 * A.B.Cの形式のバージョン名
	 */
	private String version = "1.0.0";
	
	private static Economy econ = null;
    
	
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
		
		if (!setupEconomy() ) {
			LogManager.logError("前提プラグイン(vault)が読み込めません。", api, new Throwable(), Level.SEVERE);
			ApiManager.stopServer("前提プラグイン(vault)の読み込み失敗による", StopReason.ERROR);	
			return;
        }
		
		//config
		
		//command

		this.getCommand("emerald").setExecutor(new EmeraldCommand());
		//listener
		
		//メモリ
		
		
		LogManager.logInfo(this.getApiName() + "を読み込みました", this, Level.INFO);
	}
	
	private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
	
	public static Economy getVault() {
		return econ;
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
	public static EconomyApi getInstance() {
		return api;
	}
	
}
