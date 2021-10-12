package com.github.redshirt53072.baseapi.message;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.github.redshirt53072.baseapi.server.GrowthApi;
/**
 * ログ出力に使うクラス
 * @author redshirt
 *
 */
public class LogManager {
	
	public static void logInfo(String message,GrowthApi main,Level level) {
		
		//op
		/*MessageManager.sendToOP("[error]内容：" + message,MessageType.SPECIAL, plugin);        				
		MessageManager.sendToOP("[error]場所：" + loc,MessageType.SPECIAL, plugin);     
		*/
		
		Logger logger = getFileLogger(level,main);
		if(logger == null) {
			return;
		}
        // ログの出力
		new Thread() {
            @Override
            public void run() {
            	logger.log(level, message);
            }
    	}.start();
	}
	public static void logError(String message,GrowthApi main,Throwable loc,Level level) {
		if(!(level.equals(Level.WARNING) || level.equals(Level.SEVERE))) {
			logInfo(message,main,level);
			return;
		}
		
		
		//op
		/*MessageManager.sendToOP("[error]内容：" + message,MessageType.SPECIAL, plugin);        				
		MessageManager.sendToOP("[error]場所：" + loc,MessageType.SPECIAL, plugin);     
		*/
		
		Logger logger = getFileLogger(level,main);
		if(logger == null) {
			return;
		}
        // ログの出力
		new Thread() {
            @Override
            public void run() {
            	logger.log(level, message);
            }
    	}.start();
	}
	/**
	 * ファイルlogger生成
	 * @param level ログレベル
	 * @return 生成済みファイルlogger
	 */
	private static Logger getFileLogger(Level level,GrowthApi plugin) {
		//ログファイル取得
		File root = plugin.getDataFolder();
		if(!root.exists()) {
			plugin.saveDefaultConfig();
			root = plugin.getDataFolder();	
			if(root.exists()) {
				plugin.getLogger().log(Level.WARNING,"プラグインフォルダがありません。");
			}else {
				plugin.getLogger().log(Level.SEVERE,"プラグインフォルダが作成できません。");
				return null;
			}
		}
		
		File folderDir = new File(root,"log");
		if(!folderDir.exists()) {
			if(!folderDir.mkdir()) {
				plugin.getLogger().log(Level.WARNING,folderDir.toString() + "が作成できませんでした。");
				return null;	
			}
		}
        File levelDir = new File(folderDir,level.toString());
        if(!levelDir.exists()) {
        	if(!levelDir.mkdir()) {
        		plugin.getLogger().log(Level.WARNING,levelDir.toString() + "が作成できませんでした。");
        		return null;	
        	}
        }
        
        LocalDateTime now = LocalDateTime.now();
		String time = now.getYear() + "_" + now.getMonthValue() + "_" + now.getDayOfMonth() + "_";
		File logFile = new File(levelDir, time + level.toString() + ".log");
        if(!logFile.exists()) {
        	try{
        		logFile.createNewFile();
        	} catch(IOException ex) {
        		plugin.getLogger().log(Level.WARNING, logFile.toString() + "が作成できませんでした。");
        		ex.printStackTrace();
        		return null;
        	}
        }
        
        Logger logger = Logger.getLogger("growth-" + plugin.getApiName());
        
        try{ 
        	Handler handler = new FileHandler(logFile.toString());
        	logger.addHandler(handler);
        }catch(IOException ex){
        	plugin.getLogger().log(Level.WARNING, logFile.toString() + "にログを出力できませんでした。");
    		ex.printStackTrace();
    		return null;
        }
        return logger;
	}
}