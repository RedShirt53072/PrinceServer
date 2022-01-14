package com.github.redshirt53072.growthapi;


import java.util.logging.Level;

import org.bukkit.plugin.RegisteredServiceProvider;

import com.github.redshirt53072.growthapi.command.ManagementCommand;
import com.github.redshirt53072.growthapi.database.MySQLConfig;
import com.github.redshirt53072.growthapi.database.SQLManager;
import com.github.redshirt53072.growthapi.gui.GuiManager;
import com.github.redshirt53072.growthapi.gui.PlayerInvAction;
import com.github.redshirt53072.growthapi.message.LogManager;
import com.github.redshirt53072.growthapi.server.GrowthPlugin;
import com.github.redshirt53072.growthapi.server.GrowthPluginManager;
import com.github.redshirt53072.growthapi.server.GrowthPluginManager.StopReason;
import com.github.redshirt53072.growthapi.server.StopCommand;

import net.milkbowl.vault.economy.Economy;


/**
 * BaseAPIのメインクラス
 * @author redshirt
 *
 */
public final class BaseAPI extends GrowthPlugin{
	/**
	 * プラグインインスタンス
	 */
	private static BaseAPI plugin;
	/**
	 * vaultのインスタンス
	 */
	private static Economy econ = null;
	
	
	private static boolean useMySQL = true;
	/**
	 * 読み込み時
	 */
	@Override
	public void onEnable() {
		//general
		name = "BaseAPI";
		version = "2.0.0";
		plugin = this;
		LogManager.registerLogger(this);
		this.saveDefaultConfig();
		
		//emergency
		GrowthPluginManager.registerEmergency(new GuiManager());
		
		//event
		new PlayerInvAction();
		
		//vault
		if (!setupEconomy() ) {
			LogManager.logError("前提プラグイン(vault)が読み込めません。", plugin, new Throwable(), Level.SEVERE);
			GrowthPluginManager.stopServer("前提プラグイン(vault)の読み込み失敗による", StopReason.ERROR);	
			return;
        }
		
		//config
		boolean useMysql = MySQLConfig.init();
		
		//sql
		if(useMysql) {
			SQLManager.init();
		}else {
			LogManager.logInfo("Mysqlデータベースの使用は無効化されました", this, Level.INFO);
			useMySQL = false;
		}
		
		//command
		this.getCommand("stop").setExecutor(new StopCommand());
		this.getCommand("manage").setExecutor(new ManagementCommand());

		//massage
		LogManager.logInfo(getPluginName() + "を読み込みました", this, Level.INFO);
	}
	/**
	 * vaultの初期処理
	 * @return 成功したか
	 */
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
	/**
	 * 終了時
	 */
	@Override
	public void onDisable() {
		LogManager.logInfo(getPluginName() + "を終了しました", this, Level.INFO);	
	}
	
	/**
	 * vaultインスタンスの取得
	 * @return vaultインスタンス
	 */
	public static boolean canUseMySQL() {
		return useMySQL;
	}
	
	/**
	 * vaultインスタンスの取得
	 * @return vaultインスタンス
	 */
	public static Economy getVault() {
		return econ;
	}
	
	/**
	 * インスタンス取得
	 * @return apiインスタンス
	 */
	public static BaseAPI getInstance() {
		return plugin;
	}
}
