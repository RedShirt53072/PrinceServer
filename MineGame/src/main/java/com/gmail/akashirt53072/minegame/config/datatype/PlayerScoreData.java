package com.gmail.akashirt53072.minegame.config.datatype;

import java.util.UUID;

import org.bukkit.scoreboard.Scoreboard;


public class PlayerScoreData {
	private UUID uuid;
	private String name;
	private Scoreboard board;
	private int point = 0;
	private int position = 0;
	private boolean isDouble = false;
	public PlayerScoreData(UUID uuid,String name ,Scoreboard board) {
		this.uuid = uuid;
		this.name = name;
		this.board = board;
    }
	public String getName() {
    	return name;
    }
	public UUID getUUID() {
    	return uuid;
    }
	public Scoreboard getBoard() {
    	return board;
    }
	public int getPoint() {
    	return point;
    }
	
	public int getPosition() {
    	return position;
    }
	
	public boolean isDouble() {
    	return isDouble;
    }
	
	public void setDouble(boolean isDouble) {
		this.isDouble = isDouble;
	}
	
	public void setPoint(int p) {
		point = p;
	}
	
	public void setPosition(int p) {
		position = p;
	}
}
