package com.github.redshirt53072.survival.util;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.bukkit.Bukkit;

import com.github.redshirt53072.api.message.MessageManager;
import com.github.redshirt53072.api.message.MessageManager.MessageLevel;
import com.github.redshirt53072.api.message.SoundManager;
import com.github.redshirt53072.api.message.SoundManager.CustomSound;
import com.github.redshirt53072.api.message.TextBuilder;
import com.github.redshirt53072.survival.GrowthSurvival;

public final class HourChime {
	public static void startLoop(){
		LocalDateTime now = LocalDateTime.now();
		int nextHour = now.getHour() + 1;
		if(nextHour == 24) {
			nextHour = 0;
		}
		LocalDateTime next = now.with(LocalTime.of(nextHour, 0));
		
		Timer timer = new Timer(true);
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				Bukkit.getScheduler().runTask(GrowthSurvival.getInstance(), new Runnable() {
	                @Override
	                public void run() {
	                	MessageManager.sendAllPlayer(MessageLevel.INFO,TextBuilder.plus(String.valueOf(LocalDateTime.now().getHour()) ,"時をお知らせします。"));
	                	SoundManager.sendAllPlayer(CustomSound.Notice);
	                 }
	            });
			}
		};
		Date date = Date.from(next.atZone(
		        ZoneId.systemDefault()).toInstant());
		
		timer.scheduleAtFixedRate(task, date,3600000L);
	}
}
