package com.gmail.akashirt53072.minegame.config;

import java.time.LocalDateTime;

import org.bukkit.entity.Player;

import com.gmail.akashirt53072.minegame.Main;
import com.gmail.akashirt53072.minegame.config.datatype.MatchData;
import com.gmail.akashirt53072.minegame.match.Match;

public class MatchLog extends DataConfig{
	private MatchData data;
	private String path;
	
	/*
	match1:
    	type: BREIGHT
  		id: 1
  		event1: 
    		player: 
    		status: 
    		time: 
  		starttime: 
  		endtime: 
  		endreason: 

	*/
	public MatchLog(Main plugin,Match match) {
		super(plugin,"log_match.yml");
		data = match.getData();
		int index = data.getId();
		path = "match" + index;
		
	}
	
	
	public void onCreate() {
 		setData(path + ".id",data.getId());
		setData(path + ".type",data.getType().toString());
		
		LocalDateTime now = LocalDateTime.now();
		String time = now.getYear() + "年" + now.getMonthValue() + "月" + now.getDayOfMonth() + "日";
		time = time + now.getHour() + "時" + now.getMinute() + "分" + now.getSecond() + "秒";
		
		setData(path + ".starttime",time);
		
	}
	
	public void onUpdate(String status,Player player) {
 		

 		int index = super.getNextIndex(path + ".event");
		setData(path + ".event" + index + ".status",status);
		setData(path + ".event" + index + ".player",player.getName());
		LocalDateTime now = LocalDateTime.now();
		String time = now.getYear() + "年" + now.getMonthValue() + "月" + now.getDayOfMonth() + "日";
		time = time + now.getHour() + "時" + now.getMinute() + "分" + now.getSecond() + "秒";
		
		setData(path + ".event" + index + ".time",time);
		
	}
	
	public void onEnd(String reason) {
		setData(path + ".endreason",reason);
		
		LocalDateTime now = LocalDateTime.now();
		String time = now.getYear() + "年" + now.getMonthValue() + "月" + now.getDayOfMonth() + "日";
		time = time + now.getHour() + "時" + now.getMinute() + "分" + now.getSecond() + "秒";
		
		setData(path + ".endtime",time);
		
	}
}