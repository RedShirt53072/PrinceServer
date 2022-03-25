package com.gmail.akashirt53072.minegame.config.datatype;

import java.util.ArrayList;

import org.bukkit.Material;

import com.gmail.akashirt53072.minegame.enums.GameType;
import com.gmail.akashirt53072.minegame.enums.MapStatus;
import com.gmail.akashirt53072.minegame.enums.MapType;

public class MapLocData {
	private MapType type;
	private String name;
	private Material icon;
	private ArrayList<LocationData> map;
	private MapStatus status;
	private ArrayList<GameType> games;
	private int id;
	
	public MapLocData(ArrayList<LocationData> map,MapType type,String name,Material icon,ArrayList<GameType> games,int id) {
		this.type = type;
		this.map = map;
		this.id = id;
		this.status = MapStatus.UNUSED;
		this.games = games;
		this.name = name;
		this.icon = icon;
    }
	
	public String getName() {
		return name;
	}
	
	public Material getIcon() {
		return icon;
	}
	
	
	public ArrayList<LocationData> getMap() {
    	return map;
    }
	public MapType getType() {
    	return type;
    }
	public ArrayList<GameType> getGameType(){
		return games;
	}
	
	public int getID() {
		return id;
	}
	
	public MapStatus getStatus() {
		return status;
	}
	public void setStatus(MapStatus st) {
		status = st;
	}
	
}
