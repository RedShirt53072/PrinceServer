package com.github.redshirt53072.baseapi.event;

import org.bukkit.event.Event;
/**
 * 分岐などのイベント発生時の処理を書くクラス
 * 一般処理クラスに実装するのも推奨
 * @author redshirt
 * 
 */
public interface GrowthBaseListener<E extends Event>{
	/**
	 * イベント発生時に実行
	 * @param event 受け取るイベント
	 */
	public boolean onEvent(E event);
}