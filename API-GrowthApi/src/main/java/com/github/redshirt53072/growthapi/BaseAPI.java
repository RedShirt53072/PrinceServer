package com.github.redshirt53072.growthapi;


import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.RegisteredServiceProvider;

import com.github.redshirt53072.growthapi.command.MaintenanceCommand;
import com.github.redshirt53072.growthapi.command.ManagementCommand;
import com.github.redshirt53072.growthapi.command.OPCommand;
import com.github.redshirt53072.growthapi.command.StopCommand;
import com.github.redshirt53072.growthapi.database.MySQLConfig;
import com.github.redshirt53072.growthapi.database.SQLManager;
import com.github.redshirt53072.growthapi.event.PlayerInOut;
import com.github.redshirt53072.growthapi.gui.GuiManager;
import com.github.redshirt53072.growthapi.gui.PlayerInvAction;
import com.github.redshirt53072.growthapi.message.LogManager;
import com.github.redshirt53072.growthapi.message.TextBuilder;
import com.github.redshirt53072.growthapi.player.PlayerManager;
import com.github.redshirt53072.growthapi.server.GrowthPlugin;
import com.github.redshirt53072.growthapi.server.PluginManager;
import com.github.redshirt53072.growthapi.server.PluginManager.StopReason;
import com.github.redshirt53072.growthapi.server.PluginManager.TPS;
import com.github.redshirt53072.growthapi.server.Maintenance;

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
	
	private static boolean stoped = false;
	
	/**
	 * 読み込み時
	 */
	@Override
	public void onEnable() {
		//general
		name = "BaseAPI";
		version = "3.0.0";
		plugin = this;
		LogManager.registerLogger(this);
		this.saveDefaultConfig();

		PermissionAttachment attach = Bukkit.getConsoleSender().addAttachment(plugin);
		attach.setPermission(Bukkit.getPluginManager().getPermission("growth.console"), true);
		PermissionAttachment attach2 = Bukkit.getConsoleSender().addAttachment(plugin);
		attach2.setPermission(Bukkit.getPluginManager().getPermission("growth.op"), true);
		
		
		//vault
		if (!setupEconomy() ) {
			LogManager.logError("前提プラグイン(vault)が読み込めません。", plugin, new Throwable(), Level.SEVERE);
			PluginManager.stopServer("前提プラグイン(vault)の読み込み失敗による", StopReason.ERROR);	
			return;
        }
		
		//emergency
		PluginManager.registerEmergency(new GuiManager());
		PlayerManager.registerLogout(new GuiManager());
		
		//event
		new PlayerInvAction();
		new PlayerInOut();
		
		//config
		MySQLConfig.init();
		Maintenance.reload();
		if(Maintenance.isMain()) {
			LogManager.logInfo("現在メンテナンス状態になっています。", this, Level.WARNING);
		}
		
		//mysql
		SQLManager.init(plugin);
		
		//command
		this.getCommand("stop").setExecutor(new StopCommand());
		this.getCommand("op").setExecutor(new OPCommand());
		this.getCommand("manage").setExecutor(new ManagementCommand());
		this.getCommand("main").setExecutor(new MaintenanceCommand());
		
		//tps
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new TPS(), 100L, 1L);
		TPS.start();
		
		//massage
		LogManager.logInfo(TextBuilder.plus(getPluginName(),"を読み込みました"), this, Level.INFO);
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
		if(!stoped) {
			PluginManager.onStop("不明な原因", StopReason.OTHER);
		}
		
		LogManager.logInfo(TextBuilder.plus(getPluginName(),"を終了しました"), this, Level.INFO);	
	}
	
	public static void setStoped() {
		stoped = true;
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
