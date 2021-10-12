package com.github.redshirt53072.baseapi.database;

import java.sql.Connection;
import java.util.logging.Level;

import com.github.redshirt53072.baseapi.message.LogManager;
import com.github.redshirt53072.baseapi.server.GrowthApi;

public abstract class SqlSender {
	protected Connection connectData;
	protected String table;
	protected GrowthApi plugin;
	
    protected SqlSender(Connection connect,String table,GrowthApi plugin) {
    	connectData = connect;
    	this.table = table;
    	this.plugin = plugin;
    }
    protected void logSevere(String sql,Exception ex) {
		LogManager.logError("[database]" + table + "#" + sql + "に失敗しました。", plugin, ex, Level.SEVERE);
	}
    protected void logWarning(String sql) {
		LogManager.logError("[database]" + table + "#" + sql + "の実行結果が不正です。", plugin, new Throwable(), Level.WARNING);
	}
    protected void logWarning(String sql,Exception ex) {
		LogManager.logError("[database]" + table + "#" + sql + "に失敗しました。", plugin, ex, Level.WARNING);
	}
    protected void logFiner(String sql) {
		LogManager.logInfo("[database]" + table + "#" + sql + "を送信しました。", plugin, Level.FINER);
	}
}
