package com.github.redshirt53072.baseapi.server;


import org.bukkit.plugin.java.JavaPlugin;


/**
 * Apiのメインクラスの継承用class
 * @author redshirt
 *
 */
public abstract class GrowthApi extends JavaPlugin{
	/**
	 * Api名取得
	 * @return Api名
	 */
	abstract public String getApiName();
	/**
	 * バージョン取得
	 */
	abstract public String getApiVersion();
	/**
	 * バージョン整合性確認
	 */
	abstract public boolean checkVersion(String ver);

}
