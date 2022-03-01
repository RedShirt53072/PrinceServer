package com.github.redshirt53072.growthapi.database;

import com.github.redshirt53072.growthapi.config.ConfigManager;

import java.util.HashMap;
import java.util.Map;

import com.github.redshirt53072.growthapi.BaseAPI;
/**
 * mysqlの接続情報を設定するcore/mysql.ymlを読み込むクラス
 * 
 * デフォルト設定
 * Host: "localhost" ->ホスト名
 * Port: 3306 ->ポート
 * Database:  ->データベース名
 * User:  ->MySQLのユーザー名
 * Password:  ->MySQLのユーザーに対応したパスワード
 * 
 * @author redshirt
 *
 */
public final class MySQLConfig {
	/**
	 * 保存されたホスト
	 */
	private static String host = "localhost";
	/**
	 * 保存されたポート
	 */
	private static int port = 3306;
	/**
	 * 保存されたデータベース名
	 */
	private static String database = "test";
	/**
	 * 保存されたユーザー名
	 */
	private static Map<String,String> users = new HashMap<String,String>();
	
	
	/**
	 * configを読み込む初期処理
	 * @return MySQLを有効化するかどうか
	 */
	public static boolean init() {
		ConfigManager manager = new ConfigManager(BaseAPI.getInstance(),"core","mysql.yml");
		manager.configInit();
		
		String tempHost = manager.getString("Host");
		if(tempHost == null) {
			manager.logWarning("Host", "の読み込みに失敗しました。");
		}else {
			host = tempHost;
		}
		Integer tempPort = manager.getInt("Port");
		if(tempPort == null) {
			manager.logWarning("Port", "の読み込みに失敗しました。");
		}else {
			port = tempPort;
		}
		String tempDatabase = manager.getString("Database");
		if(tempDatabase == null) {
			manager.logWarning("Database", "の読み込みに失敗しました。");
		}else {
			database = tempDatabase;
		}
		for(String key : manager.getKeys("", "User")) {
			String user = manager.getString(key + ".Name");	
			String pass = manager.getString(key + ".Password");	
			
			if(user == null) {
				manager.logWarning(key + ".Name", "の読み込みに失敗しました。");
				continue;
			}
			if(pass == null) {
				manager.logWarning(key + "PassWord", "の読み込みに失敗しました。");
				continue;
			}
			users.put(user, pass);
		}
		
		if(users.isEmpty()) {
			manager.logWarning("User", "をひとつも読み込めませんでした。");
		}
		
		manager.logConfig("","を読み込みました。");
		return true;
    }
	/**
	 * ホスト名を取得
	 * @return ホスト名
	 */
	static String getHost() {
		return host;
	}
	/**
	 * ポートを取得
	 * @return ポート
	 */
	static int getPort() {
		return port;
	}
	/**
	 * データベース名を取得
	 * @return データベース名
	 */
	static String getDatabase() {
		return database;
	}
	/**
	 * パスワードを取得
	 * @return パスワード
	 */
	static String getPass(String key) {
		return users.get(key);
	}
	
}
