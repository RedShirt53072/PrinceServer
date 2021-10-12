package com.github.redshirt53072.baseapi.event;

import org.bukkit.event.Event;

/**
 * 分岐などのイベント発生時の処理を書くクラス
 * 使い方サンプル
 * @author redshirt
 * 
 */
public class SampleListener implements GrowthBaseListener<Event>{
	public boolean onEvent(Event e){
		return true;//排他処理かどうか
	}
}