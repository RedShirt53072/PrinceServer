package com.github.redshirt53072.api.database;

import java.sql.Connection;
import java.util.logging.Level;

import com.github.redshirt53072.api.message.LogManager;
import com.github.redshirt53072.api.message.TextBuilder;
import com.github.redshirt53072.api.server.GrowthPlugin;
/**
 * データベースにSQL文を送るコードを書くクラス
 * @author redshirt
 *
 */
public abstract class SQLSender {
	/**
	 * 接続情報
	 */
	protected Connection connectData;
	/**
	 * 表の名前
	 */
	protected String table;
	/**
	 * プラグインメインクラス
	 */
	protected GrowthPlugin plugin;
	/**
	 * @param connect 接続情報
	 * @param table 表の名前
	 * @param plugin プラグインインスタンス
	 */
    protected SQLSender(Connection connect,String table,GrowthPlugin plugin) {
    	connectData = connect;
    	this.table = table;
    	this.plugin = plugin;
    }
    /**
     * SQL通信での重大なエラーをログに流す
     * @param sql SQL文
     * @param ex エラー
     */
    protected void logSevere(String sql,Exception ex) {
		LogManager.logError(TextBuilder.plus("[database]",table,"#",sql,"に失敗しました。"), plugin, ex, Level.SEVERE);
	}
    /**
     * SQL通信での異常をログに流す
     * @param sql SQL文
     */
    protected void logWarning(String sql) {
		LogManager.logError(TextBuilder.plus("[database]",table,"#",sql,"の実行結果が不正です。"), plugin, new Throwable(), Level.WARNING);
	}
    /**
     * SQL通信でのエラーをログに流す
     * @param sql SQl文
     * @param ex エラー
     */
    protected void logWarning(String sql,Exception ex) {
		LogManager.logError(TextBuilder.plus("[database]",table,"#",sql,"に失敗しました。"), plugin, ex, Level.WARNING);
	}
    /**
     * SQL通信でのログを流す
     * @param sql SQl文
     * @param ex エラー
     */
    protected void logInfo(String sql) {
		LogManager.logInfo(TextBuilder.plus("[database]",table,"#",sql,"を送信しました。"), plugin, Level.CONFIG);
	}
}
