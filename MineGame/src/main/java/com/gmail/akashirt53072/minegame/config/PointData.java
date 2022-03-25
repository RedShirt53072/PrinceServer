package com.gmail.akashirt53072.minegame.config;


import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import com.gmail.akashirt53072.minegame.Main;
import com.gmail.akashirt53072.minegame.MessageManager;
import com.gmail.akashirt53072.minegame.config.datatype.PlayerScoreData;

public class PointData extends DataConfig{
	
	/*
	player1:
	  uuid: ndioqfo
	  name: dnaoifeeoi
	  toppoint: 1000

	*/
	public PointData(Main plugin) {
		super(plugin,"data_point.yml");
	}
	
	public void updatePoint(Player p,int point) {
		int index = 0;
		for(int i = 1;containData("player" + i);i ++) {
			UUID uuid = UUID.fromString(getStringData("player" + i + ".uuid"));
			if(p.getUniqueId().equals(uuid)){
				index = i;
				break;
			}
		}
		if(index == 0) {
			index = getNextIndex("player");
			setData("player" + index + ".name",p.getName());
			setData("player" + index + ".uuid",p.getUniqueId().toString());
			setData("player" + index + ".toppoint",point);
			return;
		}
		if(point > getIntData("player" + index + ".toppoint")) {
			setData("player" + index + ".toppoint",point);	
		}
	}
	
	public int getMyTopPoint(UUID uuid) {
		for(int i = 1;containData("player" + i);i ++) {
			UUID uuid1 = UUID.fromString(getStringData("player" + i + ".uuid"));
			if(uuid1.equals(uuid)){
				return getIntData("player" + i + ".toppoint");
			}
		}
		return 0;
	}
	
	public int getAllTopPoint() {
		int top = 0;
		for(int i = 1;containData("player" + i);i ++) {
			int p = getIntData("player" + i + ".toppoint");
			if(top < p) {
				top = p;
			}
		}
		return top;
	}
	public void sendTopPoint(Player p) {
		ArrayList<PlayerScoreData> pds = new ArrayList<PlayerScoreData>();
		for(int i = 1;containData("player" + i);i ++) {
			String name = getStringData("player" + i + ".name");
			int point = getIntData("player" + i + ".toppoint");
			PlayerScoreData pd = new PlayerScoreData(null,name,null);
			pd.setPoint(point);
			pds.add(pd);
		}
		for(PlayerScoreData pd : pds) {
			int p1 = pd.getPoint();
			
			int pos = 1;
			for(PlayerScoreData sd2 : pds) {
				int p2 = sd2.getPoint();
				if(p1 < p2){
					pos ++;
				}
			}
			
			pd.setPosition(pos);
		}
		for(int i = 1;i < 11;i++) {
			for(PlayerScoreData pd : pds) {
				if(pd.getPosition() == i){
					MessageManager.sendInfo(pd.getPosition() + "位：" + ChatColor.GOLD + pd.getPoint() + "pt"+ ChatColor.WHITE + pd.getName(), p);
					break;		
				}
			}
		}
		
	}
	
}