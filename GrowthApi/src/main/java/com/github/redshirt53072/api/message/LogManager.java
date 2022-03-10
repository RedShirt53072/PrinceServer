package com.github.redshirt53072.api.message;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import com.github.redshirt53072.api.message.MessageManager.MessageLevel;
import com.github.redshirt53072.api.message.SoundManager.CustomSound;
import com.github.redshirt53072.api.server.GrowthPlugin;
/**
 * ログ出力に使うクラス
 * @author redshirt
 *
 */
public final class LogManager {
	/**
	 * 保存されたプラグインごとのLogger
	 */
	private static Map<GrowthPlugin,Logger> loggers = new HashMap<GrowthPlugin,Logger>();
	

	/**
	 * メッセージのみの通常のログを複数送る場合に使用できる
	 * なお、ログファイルの容量膨張対策でログレベルがInfo以上でないと出力されない
	 * @param plugin プラグインクラス
	 * @param level ログレベル
	 * @param msgs テキストメッセージ
	 */
	public static void logInfos(GrowthPlugin plugin,Level level,String... msgs) {
		for(String msg : msgs) {
			logInfo(msg,plugin,level);
		}
	}
	
	/**
	 * Throwableを用いないメッセージのみの通常のログ
	 * なお、ログファイルの容量膨張対策でログレベルがInfo以上でないと出力されない
	 * @param message テキストメッセージ
	 * @param plugin プラグインクラス
	 * @param level ログレベル
	 */
	public static void logInfo(String message,GrowthPlugin plugin,Level level) {
		if(level.equals(Level.INFO) || level.equals(Level.WARNING) || level.equals(Level.SEVERE)) {
			//op
			MessageManager.sendOPPlayer(MessageLevel.SPECIAL,TextBuilder.plus("[info]",message));
			try{
				if(level.equals(Level.WARNING) || level.equals(Level.SEVERE)) {
					SoundManager.sendOP(CustomSound.Alert);
				}else {
					SoundManager.sendOP(CustomSound.Notice);
				}
			}catch(Exception ex) {	}
		}
		
		Logger savedLogger = loggers.get(plugin);
		
		if(savedLogger == null) {
			return;
		}
        // ログの出力
		savedLogger.log(level, message);
	}
	
	/***
	 * Throwable(Exception)を用いるログ
	 * Exceptionをcatchした場合はそのまま投げて良い
	 * それがない場合はnew Throwable()をすると現在の位置情報が詰め込まれるのでこれを投げるのがおすすめ
	 * なお、ログレベルはWARNING以上でないと通常のログ扱いになる
	 * @param message テキストメッセージ
	 * @param plugin プラグインクラス
	 * @param location エラー箇所
	 * @param level ログレベル
	 */
	public static void logError(String message,GrowthPlugin plugin,Throwable location,Level level) {
		if(!(level.equals(Level.WARNING) || level.equals(Level.SEVERE))) {
			logInfo(message,plugin,level);
			return;
		}
		
		//op
		MessageManager.sendOPPlayer(MessageLevel.SPECIAL,TextBuilder.plus("[error]内容：",message));
		MessageManager.sendOPPlayer(MessageLevel.SPECIAL,TextBuilder.plus("[error]場所：",location.toString()));   
		try{
			SoundManager.sendOP(CustomSound.Alert);
		}catch(Exception ex) {	}
		Logger savedLogger = loggers.get(plugin);
		
		
		if(savedLogger == null) {
			return;
		}
        // ログの出力
		
		savedLogger.log(level, message,location);
        
        
	}
	/**
	 * プラグインごとにLoggerを生成し、登録する
	 * @param plugin プラグインインスタンス
	 */
	 public static void registerLogger(GrowthPlugin plugin) {
		//ログファイル取得
		File root = plugin.getDataFolder();
		if(!root.exists()) {
			plugin.saveDefaultConfig();
			root = plugin.getDataFolder();	
			if(root.exists()) {
				plugin.getLogger().log(Level.WARNING,"プラグインフォルダを作成しました。");
			}else {
				plugin.getLogger().log(Level.SEVERE,"プラグインフォルダが作成できません。");
				return;
			}
		}
		
		File folderDir = new File(root,"log");
		if(!folderDir.exists()) {
			if(!folderDir.mkdir()) {
				plugin.getLogger().log(Level.WARNING,TextBuilder.plus(folderDir.toString(),"が作成できませんでした。"));
				return;	
			}
		}
        
        LocalDateTime now = LocalDateTime.now();
		String time = TextBuilder.plus(String.valueOf(now.getYear()),"_"
				,String.valueOf(now.getMonthValue()),"_"
				,String.valueOf(now.getDayOfMonth()),"_"
				,String.valueOf(now.getHour()),"_",String.valueOf(now.getMinute())
				,".log");
		File logFile = new File(folderDir, time);
        if(!logFile.exists()) {
        	try{
        		logFile.createNewFile();
        	} catch(IOException ex) {
        		plugin.getLogger().log(Level.WARNING, TextBuilder.plus(logFile.toString(),"が作成できませんでした。"));
        		ex.printStackTrace();
        		return;
        	}
        }
        
        Logger logger = Logger.getLogger(plugin.getPluginName());
        
        try{ 
        	Handler handler = new FileHandler(logFile.toString());
        	handler.setFormatter(new SimpleFormatter());
        	logger.addHandler(handler);
        	logger.setLevel(Level.FINE);
        }catch(IOException ex){
        	plugin.getLogger().log(Level.WARNING, TextBuilder.plus(logFile.toString(),"にログを出力できませんでした。"));
    		ex.printStackTrace();
    		return;
        }
        loggers.put(plugin, logger);
	}
}