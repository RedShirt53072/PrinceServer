package com.github.redshirt53072.growthapi.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;

import com.github.redshirt53072.growthapi.message.LogManager;
import com.github.redshirt53072.growthapi.server.GrowthPlugin;
/**
 * データベースとのデータのやり取りを取り次ぐクラス
 * 機能は結構自由に追加できる
 * @author redshirt
 *
 */
public abstract class SQLInterface {
	/**
	 * 接続データ
	 */
	protected Connection connectData;
	/**
	 * プラグインメインクラス
	 */
	protected GrowthPlugin plugin;
	/**
	 * 
	 * @param plugin プラグインメインクラス
	 */
    protected SQLInterface(GrowthPlugin plugin) {
    	this.plugin = plugin;
    }
    /**
     * データベースへの接続
     * @return 成功したか
     */
    protected boolean connect() {
    	try {
    		connectData = SQLManager.getDataSource(plugin).getConnection();
    		return true;
    	}catch(SQLException ex) {
    		logSevere(ex);
    		return false;
    	}
    }
    /**
     * データベース接続の切断
     * @return 成功したか
     */
    protected boolean close() {
    	try {
    		connectData.close();
    		return true;
    	}catch(SQLException ex) {
    		logSevere(ex);
    		return false;
    	}
    }
    /**
     * エラーをログに流す
     * @param ex エラー
     */
    public void logSevere(Exception ex) {
		LogManager.logError("[database]データベース接続にエラーが発生しました。。", plugin, ex, Level.SEVERE);
	}
}
