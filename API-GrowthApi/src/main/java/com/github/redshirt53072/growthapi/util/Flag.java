package com.github.redshirt53072.growthapi.util;
/**
 * TrueかFlaseかを保持できる
 * 複数の条件式を扱いやすくするために用意した
 * 初期値はfalseで、一度Trueにセットすると戻せなくなる
 * @author redshirt
 */
public class Flag {
	/**
	 * データ
	 */
	private boolean data = false;
	/**
	 * Trueにセットする
	 */
	public void setTrue() {
		data = true;
	}
	/**
	 * 今TrueなのかFalseなのかを取り出す
	 * @return flag
	 */
	public boolean getFlag() {
		return data;
	}
	
}
