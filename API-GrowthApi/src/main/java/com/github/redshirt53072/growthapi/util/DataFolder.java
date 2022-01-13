package com.github.redshirt53072.growthapi.util;
/**
 * 好きなデータを保持できる
 * forEachの中から外のデータを扱うために用意した
 * 初期値がnullなので注意
 * @author redshirt
 *
 * @param <D> データ型
 */
public class DataFolder<D> {
	/**
	 * データ
	 */
	private D data = null;
	/**
	 * データを入れる
	 * @param d データ
	 */
	public void setData(D d) {
		data = d;
	}
	/**
	 * データを取り出す
	 * @return データ
	 */
	public D getData() {
		return data;
	}
	
}
