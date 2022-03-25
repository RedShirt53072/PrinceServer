package com.gmail.akashirt53072.minegame.config.datatype;

import java.util.ArrayList;

public class MatchTeamData {
	private String name;
	private ArrayList<MatchPlayerData> players;
	
	public MatchTeamData(String id, ArrayList<MatchPlayerData> players) {
		this.name = id;
		this.players = players;
    }
	public String getId() {
    	return name;
    }
	public ArrayList<MatchPlayerData> getPlayers() {
    	return players;
	}
}
