package com.github.redshirt53072.baseapi.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;

import javax.sql.DataSource;

import com.github.redshirt53072.baseapi.general.BaseApi;
import com.github.redshirt53072.baseapi.message.LogManager;
import com.github.redshirt53072.baseapi.server.ApiManager;
import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;
import com.mysql.cj.jdbc.MysqlDataSource;

public class SqlManager {
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
	 * 
	 * @param plugin
	 */
	public static void init() {
		MysqlDataSource dataSource = new MysqlConnectionPoolDataSource();
		dataSource.setServerName(MysqlConfig.getHost());
		dataSource.setPortNumber(MysqlConfig.getPort());
		dataSource.setDatabaseName(MysqlConfig.getDatabase());
		dataSource.setUser(MysqlConfig.getUser());
		dataSource.setPassword(MysqlConfig.getPass());
		try {
			testConnect(dataSource);
		} catch(SQLException ex){
			LogManager.logError("データベース接続が確立できませんでした。", BaseApi.getInstance(), ex, Level.SEVERE);
			ApiManager.stopServer("データベース接続確立の", ApiManager.StopReason.ERROR);
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
