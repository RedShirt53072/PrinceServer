package com.gmail.akashirt53072.minegame.config.cache;

import java.util.ArrayList;

import org.bukkit.Location;

import com.gmail.akashirt53072.minegame.Main;
import com.gmail.akashirt53072.minegame.MessageManager;
import com.gmail.akashirt53072.minegame.config.ErrorLog;
import com.gmail.akashirt53072.minegame.config.datatype.LocationData;
import com.gmail.akashirt53072.minegame.config.datatype.MapLocData;
import com.gmail.akashirt53072.minegame.enums.GameType;
import com.gmail.akashirt53072.minegame.enums.MapStatus;
import com.gmail.akashirt53072.minegame.enums.MapType;
import com.gmail.akashirt53072.minegame.enums.MessageType;
import com.gmail.akashirt53072.minegame.gui.SelectMapGui;
import com.gmail.akashirt53072.minegame.match.Match;

public class LocationManager {
	private Main plugin;
	public LocationManager(Main plugin) {
		this.plugin = plugin;
	}
	
	public Location getMainLobby() {
		return plugin.getLocationData().getMainLobby().getLocation();
	}
	public Location getServerLobby() {
		return plugin.getLocationData().getServerLobby().getLocation();
	}
	public Location getWaitingRoom(GameType type) {
		for(LocationData s : plugin.getLocationData().getWaitingRoom()) {
			if(s.getId().equals(type.toString())) {
				return s.getLocation();
			}
		}
		new ErrorLog(plugin).writeError("LocationManager.getWaitingRoom", type + "の待機場所の座標データは見つかりませんでした");
		return null;
	}
	
	public Location getLocation(Match match,MapType map,String locName,int id) {
		for(MapLocData m : plugin.getLocationData().getMap()) {
			if(m.getType().equals(map) && m.getID() == id) {
				for(LocationData s : m.getMap()) {
					if(s.getId().equals(locName)) {
						return s.getLocation();
					}
				}
				new ErrorLog(plugin).writeError("LocationManager.getLocation", "マップ" + map + "に" + locName + "の座標データは見つかりませんでした");
				
				return null;
			}
		}
		new ErrorLog(plugin).writeError("LocationManager.getLocation", map + "のマップデータは見つかりませんでした");
		return null;
	}
	
	
	
	public ArrayList<MapLocData> getUsableMap(GameType match) {
		ArrayList<MapLocData> data = new ArrayList<MapLocData>();
		for(MapLocData m : plugin.getLocationData().getMap()) {
			if(m.getStatus().equals(MapStatus.USED)) {
				continue;
			}
			for(GameType gt : m.getGameType()) {
				if(match.equals(gt)){
					data.add(m);
				}
			}
		}
		
		return data;
	}
	
	public boolean useMap(MapLocData data){
		if(data.getStatus().equals(MapStatus.USED)){
			new ErrorLog(plugin).writeError("LocationManager.getLocation", data.getType() + "のマップは現在使用中です。");
			
			return false;
		}
		data.setStatus(MapStatus.USED);
		//他試合のマップ選択GUI更新
		for(Match m : plugin.getMatchData()){
			SelectMapGui gui = m.getData().getGui();
			if(gui == null) {
				continue;
			}
			gui.updateMapData(getUsableMap(m.getData().getType()), m.getData().getMap());
		}
		return true;
	}
	
	
	public void returnMap(MapLocData map) {
		map.setStatus(MapStatus.UNUSED);
		//他試合のマップ選択GUI更新
		for(Match m : plugin.getMatchData()){
			SelectMapGui gui = m.getData().getGui();
			if(gui == null) {
				continue;
			}
			gui.updateMapData(getUsableMap(m.getData().getType()), m.getData().getMap());
		}
	}
	
	
}
