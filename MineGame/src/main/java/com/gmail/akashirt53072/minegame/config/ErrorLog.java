package com.gmail.akashirt53072.minegame.config;

import java.time.LocalDateTime;

import com.gmail.akashirt53072.minegame.Main;
import com.gmail.akashirt53072.minegame.MessageManager;
import com.gmail.akashirt53072.minegame.enums.MessageType;

public class ErrorLog extends DataConfig{
	private Main plugin;
	public ErrorLog(Main plugin) {
		super(plugin,"log_error.yml");
		this.plugin = plugin;
	}
	/*
	error1:
		time: 
		file: 
		message: 
	*/
	
	public void writeError(String loc,String message) {
		int index = super.getNextIndex("error");
		String path = "error" + index;
		LocalDateTime now = LocalDateTime.now();
		String time = now.getYear() + "年" + now.getMonthValue() + "月" + now.getDayOfMonth() + "日";
		time = time + now.getHour() + "時" + now.getMinute() + "分" + now.getSecond() + "秒";
		//ログ
		setData(path + ".time",time);
		setData(path + ".file",loc);
		setData(path + ".message",message);
		//コンソール+OP持ち
		plugin.getLogger().warning("[error]内容：" + message);
		plugin.getLogger().warning("[error]場所：" + loc);
		
		//op
		MessageManager.sendToOP("[error]内容：" + message,MessageType.SPECIAL, plugin);        				
		MessageManager.sendToOP("[error]場所：" + loc,MessageType.SPECIAL, plugin);     
		
	}
}