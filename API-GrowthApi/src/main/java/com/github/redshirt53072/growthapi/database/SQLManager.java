package com.github.redshirt53072.growthapi.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import javax.sql.DataSource;

import com.github.redshirt53072.growthapi.BaseAPI;
import com.github.redshirt53072.growthapi.message.LogManager;
import com.github.redshirt53072.growthapi.server.GrowthPlugin;
import com.github.redshirt53072.growthapi.server.PluginManager;
import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;
import com.mysql.cj.jdbc.MysqlDataSource;
/**
 * データベースへの接続を管理するクラス
 * @author redshirt
 *
 */
public final class SQLManager {
	/**
	 * mysql接続データ
	 */
	private static Map<GrowthPlugin,DataSource> mysqlData = new HashMap<GrowthPlugin,DataSource>();
	
	
	/**
	 * 接続情報の取得
	 * @return 接続情報
	 */
	public static DataSource getDataSource(GrowthPlugin gp) {
		return mysqlData.get(gp);
	}
	/**
	 * データベースに接続する初期処理
	 */
	public static void init(GrowthPlugin gp) {
		MysqlDataSource dataSource = new MysqlConnectionPoolDataSource();
		dataSource.setServerName(MySQLConfig.getHost());
		dataSource.setPortNumber(MySQLConfig.getPort());
		dataSource.setDatabaseName(MySQLConfig.getDatabase());
		String pass = MySQLConfig.getPass(gp.getName());
		if(pass == null) {
			LogManager.logInfo(gp.getName() + "のユーザーが設定されていません。", BaseAPI.getInstance(), Level.WARNING);
			LogManager.logError("データベース接続が確立できませんでした。", BaseAPI.getInstance(), new Exception(), Level.SEVERE);
			PluginManager.stopServer("データベース接続確立の", PluginManager.StopReason.ERROR);
		}
		dataSource.setUser(gp.getName());
		dataSource.setPassword(pass);
		try {
			testConnect(dataSource);
		} catch(SQLException ex){
			LogManager.logError("データベース接続が確立できませんでした。", BaseAPI.getInstance(), ex, Level.SEVERE);
			PluginManager.stopServer("データベース接続確立の", PluginManager.StopReason.ERROR);
		}
		mysqlData.put(gp, dataSource);
	}
	/**
	 * 接続テスト
	 * @param dataSource SQLデータ
	 * @throws SQLException
	 */
	private static void testConnect(DataSource dataSource) throws SQLException {
	    try (Connection conn = dataSource.getConnection()) {
	        if (!conn.isValid(1000)) {
	            throw new SQLException("Could not establish database connection.");
	        }
	    }
	}
	
	
}
