package com.gmail.akashirt53072.minegame.config.datatype;

import java.util.ArrayList;

import com.gmail.akashirt53072.minegame.Main;
import com.gmail.akashirt53072.minegame.config.cache.LocationManager;
import com.gmail.akashirt53072.minegame.enums.GameType;
import com.gmail.akashirt53072.minegame.enums.MatchStatus;
import com.gmail.akashirt53072.minegame.gui.SelectMapGui;

public class MatchData {
	private GameType type;
	private int id;
	private MatchStatus status;
	private MatchTeamData wait;
	private ArrayList<MatchTeamData> teams;
	private MapLocData map;
	private SelectMapGui gui = null;
	private Main plugin;
	
	public MatchData(int id,GameType type,Main plugin) {
		this.type = type;
		this.id = id;
		this.status = MatchStatus.WAITING;
		LocationManager manager = new LocationManager(plugin);
		MapLocData mapData = manager.getUsableMap(type).get(0);
		manager.useMap(mapData);
		this.map = mapData;
		this.wait = new MatchTeamData("wait",new ArrayList<MatchPlayerData>());
		this.teams = new ArrayList<MatchTeamData>();
		this.plugin = plugin;
    }
	
	public void setMap(MapLocData mapData) {
		LocationManager manager = new LocationManager(plugin);
		manager.returnMap(map);
		manager.useMap(mapData);
		map = mapData;
    }
	
	public void setGui(SelectMapGui gui) {
		this.gui = gui;
	}

	public void setStatus(MatchStatus status) {
		this.status = status;
	}
	
	public SelectMapGui getGui() {
		return gui;
	}
	
	public MapLocData getMap() {
    	return map;
    }
	
	public int getId() {
    	return id;
    }
	public GameType getType() {
    	return type;
    }
	public MatchStatus getStatus() {
    	return status;
    }
	public ArrayList<MatchTeamData> getTeams() {
    	return teams;
	}
	public MatchTeamData getWait() {
    	return wait;
	}
}
