package com.gmail.akashirt53072.minegame.config.datatype;

import java.util.UUID;

import com.gmail.akashirt53072.minegame.enums.MatchPlayerStatus;

public class MatchPlayerData {
	private UUID uuid;
	private String name;
	private MatchPlayerStatus status;
	
	public MatchPlayerData(UUID uuid,String name ,MatchPlayerStatus status) {
		this.uuid = uuid;
		this.name = name;
		this.status = status;
    }
	public String getId() {
    	return name;
    }
	public UUID getUUID() {
    	return uuid;
    }
	public MatchPlayerStatus getStatus() {
    	return status;
    }
	
	
	public void setStatus(MatchPlayerStatus status) {
    	this.status = status;
    }
	
	
	
	
}
