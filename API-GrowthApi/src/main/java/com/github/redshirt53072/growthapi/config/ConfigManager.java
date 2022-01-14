package com.github.redshirt53072.growthapi.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;

import com.github.redshirt53072.growthapi.message.LogManager;
import com.github.redshirt53072.growthapi.server.GrowthPlugin;

/**
 * ConfigAPIの利用時に用いるクラス
 * @author redshirt
 * 
 */

public class ConfigManager  {
	/** 読み取ったconfigインスタンス*/
	private FileConfiguration config;
	/** configLoader*/
	private ConfigLoader configLoader;
	/** 任意のプラグインインスタンス*/
	private GrowthPlugin plugin;
	/**configファイル名 */
	private String fileName;
	/**configファイル名 */
	private String filePath;
	
	/**
	 * 基本コンストラクタ
	 * @param plugin configを保存するプラグインインスタンス
	 * @param fileName configファイル名
	 */
	public ConfigManager(GrowthPlugin plugin,String fileName) {
		this.plugin = plugin;
		this.fileName = fileName;
		this.filePath = plugin.getPluginName() + "/" + fileName;
		this.configLoader = new ConfigLoader(plugin,fileName);
		this.config = configLoader.getConfig();
	}
	/**
	 * ディレクトリ分ける時のコンストラクタ
	 * @param plugin configを保存するプラグインインスタンス
	 * @param dirName configディレクトリ名
	 * @param fileName configファイル名
	 */
	public ConfigManager(GrowthPlugin plugin, String dirName,String fileName) {
		this.plugin = plugin;
		this.fileName = dirName + "\\" + fileName;;
		this.filePath = plugin.getPluginName() + "/" + dirName + "/" + fileName;
		this.configLoader = new ConfigLoader(plugin, dirName,fileName);
		this.config = configLoader.getConfig();
	}
	/**
	 * config初期動作
	 */
	public void configInit() {
		if(new ConfigLoader(plugin,fileName).saveDefaultConfig()) {
			logConfig("","にデフォルトのconfigを生成しました。");
		}
	}
	/**
	 * 配列の次の空きindexを返す
	 * @param path 読み取りパス
	 * @return 空きindex
	 */
	public int getNextIndex(String path){
		int i = 1;
		while(containData(path + i)) {
			i ++;
		}
        return i;
    }
	/**
	 * キーのリストを取得する
	 * @param path 読み取りパス
	 * @param keyName キーの名前
	 * @return キーのリスト
	 */
	public Set<String> getKeys(String path,String keyName){
		Set<String> keys = config.getConfigurationSection(path).getKeys(false);
		keys.removeIf(key -> !key.matches("^" + keyName + ".*"));
		return keys;
    }
	
	/**
	 * Stringのデータを読み取り
	 * @param path 読み取りパス
	 * @return Stringデータ
	 */
	public String getString(String path){
		if (containData(path) && config.isString(path)) {
			return config.getString(path);
        }else {
        	return null;
        }
    }
	/**
	 * intのデータを読み取り
	 * @param path 読み取りパス
	 * @return intデータ
	 */
	public Integer getInt(String path){
		if (containData(path) && config.isInt(path)) {
			return config.getInt(path);
        }else {
			return null;
        }
    }
	/**
	 * doubleのデータを読み取り
	 * @param path 読み取りパス
	 * @return doubleデータ
	 */
	public Double getDouble(String path){
		if (config.contains(path) && config.isDouble(path)) {
			return config.getDouble(path);
        }else {
			return null;
        }
    }
	/**
	 * Stringの配列を読み取り
	 * 第二引数を空文字にするとパス内全部を読み取ってリスト化できる
	 * @param path 読み取りパス
	 * @param keyName 配列のキー
	 * @return Stringリスト
	 */
	public List<String> getStringArray(String path,String keyName){
		List<String> results = new ArrayList<String>();
		for(String key : getKeys(path,keyName)) {
			String data = getString(path + key);
			if(data == null) {
				continue;
			}
			results.add(data);
		}
		return results;
	}
	/**
	 * intの配列を読み取り
	 * 第二引数を空文字にするとパス内全部を読み取ってリスト化できる
	 * @param path 読み取りパス
	 * @param keyName 配列のキー
	 * @return Integerリスト
	 */
	public List<Integer> getIntArray(String path,String keyName){
		List<Integer> results = new ArrayList<Integer>();
		for(String key : getKeys(path,keyName)) {
			Integer data = getInt(path + key);
			if(data == null) {
				continue;
			}
			results.add(data);
		}
		return results;
	}
	/**
	 * doubleの配列を読み取り
	 * 第二引数を空文字にするとパス内全部を読み取ってリスト化できる
	 * @param path 読み取りパス
	 * @param keyName 配列のキー
	 * @return Doubleリスト
	 */
	public List<Double> getDoubleArray(String path,String keyName){
		List<Double> results = new ArrayList<Double>();
		for(String key : getKeys(path,keyName)) {
			Double data = getDouble(path + key);
			if(data == null) {
				continue;
			}
			results.add(data);
		}
		return results;
	}
	
	/**
	 * データの有無を調べる
	 * @param path 読み取りパス
	 * @return データがあるか
	 */
	public boolean containData(String path){
		return config.contains(path,true);
    }
	/**
	 * データを格納する
	 * @param path 読み取りパス
	 * @param value データ
	 */
	public void setData(String path,Object value){
		config.set(path,value);
        configLoader.saveConfig();
	}
	/**
	 * データ削除
	 * @param path 読み取りパス
	 */
	public void deleteData(String path) {
		setData(path,null);
	}
	/**
	 * configでのエラーをログに流す
	 * @param path 読み取りパス
	 * @param message メッセージ
	 */
	public void logWarning(String path,String message) {
		LogManager.logError("[config]" + filePath + "#" + path + message, plugin, new Throwable(), Level.WARNING);
	}
	/**
	 * configが書き換えや読み取りをログに流す
	 * @param path 読み取りパス
	 * @param message メッセージ
	 */
	public void logConfig(String path,String message) {
		LogManager.logInfo(filePath + "#" + path + message, plugin, Level.CONFIG);
	}
}