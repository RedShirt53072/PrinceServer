package com.github.redshirt53072.shulker;


import java.util.logging.Level;

import com.github.redshirt53072.api.BaseAPI;
import com.github.redshirt53072.api.message.LogManager;
import com.github.redshirt53072.api.message.TextBuilder;
import com.github.redshirt53072.api.npc.NpcManager;
import com.github.redshirt53072.api.player.PlayerManager;
import com.github.redshirt53072.api.server.GrowthPlugin;
import com.github.redshirt53072.api.server.PluginManager;
import com.github.redshirt53072.api.server.PluginManager.StopReason;
import com.github.redshirt53072.shulker.bank.BankCommand;
import com.github.redshirt53072.shulker.bank.BankNpc;
import com.github.redshirt53072.shulker.command.ECSubCommand;
import com.github.redshirt53072.shulker.data.ECLock;
import com.github.redshirt53072.shulker.data.EnderChest;
import com.github.redshirt53072.shulker.temp.MoveEnderChest;


/**
 * BaseApiのメインクラス
 * @author redshirt
 *
 */
public final class UsefulShulker extends GrowthPlugin{
	/**
	 * インスタンス
	 */
	static private UsefulShulker plugin;
	
	/**
	 * 読み込み時
	 */
	@Override
	public void onEnable() {
		//general
		name = "shulker";
		version = "3.0.0";
		plugin = this;
		PluginManager.registerApi(plugin,true);
		
		
		//依存チェック
		if(!BaseAPI.getInstance().checkVersion("3.0.0")) {
			LogManager.logError("前提プラグイン(GrowthAPI)のバージョンが正しくありません。", this, new Throwable(), Level.SEVERE);
			PluginManager.stopServer("プラグインバージョンの不整合による", StopReason.ERROR);
		}
		
		//sql
		new ECLock().reload();
		
		//config
		
		//command
		ECSubCommand.register();

		this.getCommand("bank").setExecutor(new BankCommand());
		
		//listener
		new PlayerAction();
		PlayerManager.registerInit(new EnderChest());
		PlayerManager.registerInit(new MoveEnderChest());
		
		NpcManager.registerNpc(new BankNpc());
		
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
	public static UsefulShulker getInstance() {
		return plugin;
	}
	
}
