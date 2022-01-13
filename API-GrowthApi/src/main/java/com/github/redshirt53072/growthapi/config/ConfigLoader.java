package com.github.redshirt53072.growthapi.config;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.github.redshirt53072.growthapi.message.LogManager;
import com.github.redshirt53072.growthapi.server.GrowthPlugin;
/**
 * configの操作クラス
 * @author redshirt
 *
 */
public final class ConfigLoader {
	/** configのインスタンス*/
	private FileConfiguration config = null;
	/**ディレクトリのファイル*/
	private File dirFile;
	/**configのファイル*/
	private File configFile;
	/**ファイル名*/
	private String fileName;
	/**プラグインインスタンス*/
	private GrowthPlugin plugin;
	
	/**
	 * 基本コンストラクタ
	 * @param plugin プラグイン
	 * @param fileName　ファイル名
	 */
	public ConfigLoader(GrowthPlugin plugin, String fileName) {
		this.plugin = plugin;
		this.fileName = fileName;
		configFile = new File(plugin.getDataFolder(), fileName);
	}
	
	/**
	 * ディレクトリ分ける時のコンストラクタ
	 * @param plugin プラグイン
	 * @param dirName ディレクトリ名
	 * @param fileName ファイル名
	 */
	public ConfigLoader(GrowthPlugin plugin, String dirName,String fileName) {
		this.plugin = plugin;
		this.fileName = dirName + "\\" + fileName;
		dirFile = new File(plugin.getDataFolder(), dirName);
		configFile = new File(dirFile, fileName);
	}
	
	/**
	 * ファイル作成の初期処理
	 */
	public boolean saveDefaultConfig() {
		reloadConfig();
		if(dirFile != null) {
			if(!dirFile.exists()) {
				dirFile.mkdirs();
		    }
		}
		if (!configFile.exists()) {
			plugin.saveResource(fileName, false);
		}
		return true;
	}
	/**
	 * リロード
	 */
	public void reloadConfig() {
		config = YamlConfiguration.loadConfiguration(configFile);
	}
	/**
	 * configインスタンス取得
	 * @return config
	 */
	public FileConfiguration getConfig() {
		if (config == null) {
			reloadConfig();
		}
		if (config == null) {
			LogManager.logError("configの読み込みに失敗しました。", plugin, new Throwable(), Level.WARNING);
		}
		return config;
	}
	/**
	 * config保存
	 */
	public void saveConfig() {
		if (config == null) {
			LogManager.logError("configの保存に失敗しました。", plugin, new Throwable(), Level.WARNING);
			return;
		}
		try {
			getConfig().save(configFile);
		} catch (IOException ex) {
			LogManager.logError("configの保存に失敗しました。", plugin, ex, Level.WARNING);
		}
	}
	
}
