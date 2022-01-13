package com.github.redshirt53072.growthapi.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;

import javax.sql.DataSource;

import com.github.redshirt53072.growthapi.BaseAPI;
import com.github.redshirt53072.growthapi.message.LogManager;
import com.github.redshirt53072.growthapi.server.GrowthPluginManager;
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
	private static DataSource mysqlData = null;
	
	/**
	 * 接続情報の取得
	 * @return 接続情報
	 */
	public static DataSource getDataSource() {
		return mysqlData;
	}
	/**
	 * データベースに接続する初期処理
	 */
	public static void init() {
		MysqlDataSource dataSource = new MysqlConnectionPoolDataSource();
		dataSource.setServerName(MySQLConfig.getHost());
		dataSource.setPortNumber(MySQLConfig.getPort());
		dataSource.setDatabaseName(MySQLConfig.getDatabase());
		dataSource.setUser(MySQLConfig.getUser());
		dataSource.setPassword(MySQLConfig.getPass());
		try {
			testConnect(dataSource);
		} catch(SQLException ex){
			LogManager.logError("データベース接続が確立できませんでした。", BaseAPI.getInstance(), ex, Level.SEVERE);
			GrowthPluginManager.stopServer("データベース接続確立の", GrowthPluginManager.StopReason.ERROR);
		}
		mysqlData = dataSource;
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
