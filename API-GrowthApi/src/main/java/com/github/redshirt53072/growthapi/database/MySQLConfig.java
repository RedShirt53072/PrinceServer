package com.github.redshirt53072.growthapi.database;

import com.github.redshirt53072.growthapi.config.ConfigManager;
import com.github.redshirt53072.growthapi.BaseAPI;
/**
 * mysqlの接続情報を設定するcore/mysql.ymlを読み込むクラス
 * 
 * デフォルト設定
 * Use: true ->データベースを有効化するかどうか
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
	private static String user = "user";
	/**
	 * 保存されたパスワード
	 */
	private static String password = "pass";
	
	
	/**
	 * configを読み込む初期処理
	 * @return MySQLを有効化するかどうか
	 */
	public static boolean init() {
		ConfigManager manager = new ConfigManager(BaseAPI.getInstance(),"core","mysql.yml");
		manager.configInit();
		
		String tempUse = manager.getString("Use");
		if(tempUse != null) {
			if(tempUse.equals("true")) {
				return false;
			}else if(tempUse.equals("false")){
				manager.logWarning("Use", "の読み込みに失敗しました。(値はtrueかfalseである必要があります)");		
			}
		}
		
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
		String tempUser = manager.getString("User");
		if(tempUser == null) {
			manager.logWarning("User", "の読み込みに失敗しました。");
		}else {
			user = tempUser;
		}
		String tempDatabase = manager.getString("Database");
		if(tempDatabase == null) {
			manager.logWarning("Database", "の読み込みに失敗しました。");
		}else {
			database = tempDatabase;
		}
		String tempPass = manager.getString("Password");
		if(tempPass == null) {
			manager.logWarning("PassWord", "の読み込みに失敗しました。");
		}else {
			password = tempPass;
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
	 * ユーザー名を取得
	 * @return ユーザー名
	 */
	static String getUser() {
		return user;
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
	static String getPass() {
		return password;
	}
	
}
