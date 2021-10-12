package com.github.redshirt53072.baseapi.server;


import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;

import com.github.redshirt53072.baseapi.general.BaseApi;
import com.github.redshirt53072.baseapi.message.LogManager;

/**
 * BaseApiを管理するサーバー的クラス
 * @author redshirt
 *
 */
public final class ApiManager{
	/**
	 * APIインスタンスのリスト
	 */
	private static List<GrowthApi> apis = new ArrayList<GrowthApi>();
	
	
	/**
	 * APIインスタンスの登録
	 * @param api 登録するAPI
	 */
	public static void registerApi(GrowthApi api) {
		apis.add(api);
	}
	
	/**
	 * サーバーのプラグイン側からのストップ
	 * @param reason 理由
	 */
	public static void stopServer(String message,StopReason reason) {
		LogManager.logInfo("サーバーが停止しました。理由:" + message + reason.toString(), BaseApi.getInstance(), Level.INFO);
		BaseApi.getInstance().getServer().shutdown();
	}
	/**
	 * サーバー停止理由の列挙型
	 * @author redshirt
	 *
	 */
	public enum StopReason{
		ERROR,
		GRIEFING,
		SERVER,
		NORMAL;
	}
	
}
