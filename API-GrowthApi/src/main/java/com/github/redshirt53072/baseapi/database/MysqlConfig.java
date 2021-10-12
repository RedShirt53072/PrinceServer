package com.github.redshirt53072.baseapi.database;

import com.github.redshirt53072.baseapi.config.ConfigManager;
import com.github.redshirt53072.baseapi.general.BaseApi;

public class MysqlConfig {
	private static String host = "localhost";
	private static int port = 3306;
	private static String database = "test";
	private static String user = "user";
	private static String password = "pass";
	
	
	/*
	Host: "localhost"
	Port: 3306
	Database: 
	User: 
	Password: 
	 */
	
	public static void init() {
		ConfigManager manager = new ConfigManager(BaseApi.getInstance(),"core","mysql.yml");
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
    }
	
	public static String getHost() {
		return host;
	}
	public static int getPort() {
		return port;
	}
	public static String getUser() {
		return user;
	}
	public static String getDatabase() {
		return database;
	}
	public static String getPass() {
		return password;
	}
	
}
