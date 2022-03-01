package com.github.redshirt53072.growthapi.server;


import java.util.ArrayList;

import org.bukkit.plugin.java.JavaPlugin;

import com.github.redshirt53072.growthapi.message.TextManager;


/**
 * GrowthAPI依存Pluginのメインクラスの継承用abstractクラス
 * @author redshirt
 *
 */
public abstract class GrowthPlugin extends JavaPlugin{
	/**
	 * 表示名
	 */
	protected String name = "GrowthPlugin1";
	/**
	 * A.B.Cの形式のバージョン名
	 */
	protected String version = "0.0.0";
	
	/**
	 * プラグイン名取得
	 * @return プラグイン名
	 */
	public String getPluginName() {
		return name;
	}
	/**
	 * バージョン取得
	 * @return x.y.zのバージョン
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * 依存関係のバージョンの整合性の確認でテキストのバージョン同士を比較する
	 * A.B.CのAは同じでなければならず、BとCは必要なバージョンを超えていれば良い
	 * @param require 利用側で必要なAPIのバージョン
	 * @return バージョンが正しいかどうか
	 */
	public boolean checkVersion(String require) {
		String[] inVer = version.split("\\.");
		if(inVer.length != 3){
			return false;
		}
		String[] reqVer = require.split("\\.");
		if(reqVer.length != 3){
			return false;
		}
		
		ArrayList<Integer> inNums = new ArrayList<Integer>();
		ArrayList<Integer> reqNums = new ArrayList<Integer>();
		
		for(String inStr : inVer) {
			Integer inNum = TextManager.toNumber(inStr);
			if(inNum == null) {
				return false;
			}
			inNums.add(inNum);
		}

		for(String reqStr : reqVer) {
			Integer reqNum = TextManager.toNumber(reqStr);
			if(reqNum == null) {
				return false;
			}
			reqNums.add(reqNum);
		}

		if(inNums.get(0) != reqNums.get(0)) {
			return false;
		}
		if(inNums.get(1) != reqNums.get(1)) {
			return false;
		}
		if(inNums.get(2) >= reqNums.get(2)){
			return true;
		}
		
		return false;
	}
}
