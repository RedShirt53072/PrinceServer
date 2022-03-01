package com.github.redshirt53072.growthapi.server;


import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.github.redshirt53072.growthapi.BaseAPI;
import com.github.redshirt53072.growthapi.database.SQLManager;
import com.github.redshirt53072.growthapi.message.LogManager;
import com.github.redshirt53072.growthapi.message.MessageManager;
import com.github.redshirt53072.growthapi.message.MessageManager.MessageLevel;
import com.github.redshirt53072.growthapi.message.TextBuilder;
import com.github.redshirt53072.growthapi.player.PlayerManager;

/**
 * GrowthAPI依存のPluginを管理する
 * @author redshirt
 *
 */
public final class PluginManager{
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
	public static void registerApi(GrowthPlugin plugin,boolean useDatabase) {
		plugins.add(plugin);
		LogManager.registerLogger(plugin);
		plugin.saveDefaultConfig();
		if(useDatabase) {
			SQLManager.init(plugin);		
		}
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
		onStop(message,reason);
		BaseAPI.setStoped();
		BaseAPI.getInstance().getServer().shutdown();
	}
	
	public static void onStop(String message,StopReason reason) {
		//EmergencyListenerの実行
		try {
			emergencies.forEach(eme -> {
				eme.onEmergency();
			});
		}catch(Exception ex){
			LogManager.logError("サーバーデータの保存中にエラーが発生しました。" ,BaseAPI.getInstance(), ex, Level.SEVERE);	
		}
		LogManager.logInfo(TextBuilder.plus("サーバーが停止しました。理由:",message,reason.toString()), BaseAPI.getInstance(), Level.INFO);
	}
	
	public static void kickPlayer(Player player,String message,StopReason reason) {
		PlayerManager.removePermission(player);
		Bukkit.getScheduler().runTask(BaseAPI.getInstance(), new Runnable() {
    		@Override
    		public void run() {
    			if(reason.equals(StopReason.GRIEFING)) {
    				player.kickPlayer("不正な行動が検出されたため、あなたは自動的にキックされました。");	
    			}else {
    				player.kickPlayer("予期しない不具合が発生したため、あなたは自動的にキックされました。");		
    			}
    		}
    	});
		
		LogManager.logInfo(TextBuilder.plus("プレイヤー",player.getName(),"を自動キックしました。理由:",message,reason.toString()), BaseAPI.getInstance(), Level.SEVERE);
	}
	
	/**
	 * サーバー停止理由の列挙型
	 */
	public enum StopReason{
		ERROR,
		GRIEFING,
		OTHER,
		NORMAL;
	}
	
	public static enum TPSLevel{
		NORMAL,
		WARNING,
		SEVERE
	}
	
	public static class TPS implements Runnable{
		private static int tickCount = 0;
		private static long[] tickData= new long[600];
		
		public static double currentTPS = 20;
		
		public static TPSLevel tempLevel = TPSLevel.NORMAL;
		
		public static TPSLevel level = TPSLevel.NORMAL;
		
		public void run() {
			tickData[(tickCount% tickData.length)] = System.currentTimeMillis();
			tickCount += 1;
			int tps = getTPS();
			
			TPSLevel nowLevel;
			
			if(tps < 14) {
				nowLevel = TPSLevel.SEVERE;
				if(!level.equals(nowLevel)) {
					if(tempLevel.equals(nowLevel)) {
						LogManager.logInfo("サーバーのTPSが14未満まで低下しています！", BaseAPI.getInstance(), Level.SEVERE);
						MessageManager.sendAllPlayer(MessageLevel.WARNING, "サーバーの負荷が非常に大きくなっています。","サーバーが停止する危険性もありますので、作業内容を保存しておくことをおすすめします。","ラグが長く続くようであれば運営までご報告ください。");
						level = nowLevel;
					}else {
						tempLevel = nowLevel;
					}
				}
			}else if(tps < 17) {
				nowLevel = TPSLevel.WARNING;
				if(!level.equals(nowLevel)) {
					if(tempLevel.equals(nowLevel)) {
						if(level.equals(TPSLevel.SEVERE)) {
							LogManager.logInfo("サーバーのTPSが14以上まで改善しました。", BaseAPI.getInstance(), Level.INFO);
						}else {
							LogManager.logInfo("サーバーのTPSが17未満まで低下しています。", BaseAPI.getInstance(), Level.WARNING);
							MessageManager.sendAllPlayer(MessageLevel.IMPORTANT, "サーバーの負荷が大きくなっています。","原因に心当たりがある場合はできるだけその動作を止めてください。");							
						}
						level = nowLevel;
					}else {
						tempLevel = nowLevel;
					}
				}
			}else {
				nowLevel = TPSLevel.NORMAL;
				if(!level.equals(nowLevel)) {
					if(tempLevel.equals(nowLevel)) {
						LogManager.logInfo("サーバーのTPSが17以上まで改善しました。", BaseAPI.getInstance(), Level.INFO);
						MessageManager.sendAllPlayer(MessageLevel.INFO, "サーバーの負荷が一定程度改善しました。");
						level = nowLevel;
					}else {
						tempLevel = nowLevel;
					}
				}
			}
		}
		
		public static int getTPS() {
			return (int)currentTPS;
		}
		
		private static double calculateTPS(int ticks) {
			if(tickCount < ticks)
				return 20.0D;
			int target = (tickCount - 1 - ticks) % tickData.length;
			long elapsed = System.currentTimeMillis() - tickData[target];
			
			return ticks / (elapsed / 1000.0D);
		}
		public static void start() {
			Bukkit.getScheduler().scheduleSyncRepeatingTask(BaseAPI.getInstance(), new Runnable() {
				@Override
				public void run() {
					currentTPS = calculateTPS(100);
				}
			}, 0, 20);
		}
	}
}
