package com.github.redshirt53072.growthapi.server;


import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;


import com.github.redshirt53072.growthapi.BaseAPI;
import com.github.redshirt53072.growthapi.message.LogManager;

/**
 * GrowthAPI依存のPluginを管理する
 * @author redshirt
 *
 */
public final class GrowthPluginManager{
	/**
	 * APIインスタンスのリスト
	 */
	private static List<GrowthPlugin> plugins = new ArrayList<GrowthPlugin>();
	/**
	 * 緊急時呼び出しインスタンスのリスト
	 */
	private static List<EmergencyListener> emergencies = new ArrayList<EmergencyListener>();
	
	
	/**
	 * プラグインインスタンスの登録
	 * @param plugin 登録するプラグイン
	 */
	public static void registerApi(GrowthPlugin plugin) {
		plugins.add(plugin);
	}
	
	/**
	 * サーバー停止などの緊急時に呼び出されるメソッドを登録する
	 * @param listener
	 */
	public static void registerEmergency(EmergencyListener listener) {
		emergencies.add(listener);
	}
	
	/**
	 * 明示的にサーバーストップを行う
	 * "理由：~~~ERROR"の"~~~"に当てはまるようにメッセージは設定する
	 * "OOでの不正な値による"など
	 * 
	 * @param message 理由のメッセージ
	 * @param reason 停止理由の種類
	 */
	public static void stopServer(String message,StopReason reason) {
		//EmergencyListenerの実行
		try {
			emergencies.forEach(eme -> {
				eme.onEmergency();
			});
		}catch(Exception ex){
			LogManager.logError("サーバーデータの保存中にエラーが発生しました。" ,BaseAPI.getInstance(), ex, Level.SEVERE);	
		}
		
		LogManager.logInfo("サーバーが停止しました。理由:" + message + reason.toString(), BaseAPI.getInstance(), Level.INFO);
		BaseAPI.getInstance().getServer().shutdown();
	}
	/**
	 * サーバー停止理由の列挙型
	 */
	public enum StopReason{
		ERROR,
		GRIEFING,
		SERVER,
		NORMAL;
	}
	
}
