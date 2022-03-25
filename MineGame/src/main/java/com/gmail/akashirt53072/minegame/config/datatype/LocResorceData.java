package com.gmail.akashirt53072.minegame.config.datatype;

import java.util.ArrayList;

public class LocResorceData {
	private LocationData serverLobby;
	private LocationData mainLobby;
	private ArrayList<LocationData> waitingRoom;
	private ArrayList<MapLocData> map;
	
	public LocResorceData(LocationData serverLobby,LocationData mainLobby,ArrayList<LocationData> waitingRoom,ArrayList<MapLocData> map) {
		this.serverLobby = serverLobby;
		this.mainLobby = mainLobby;
		this.waitingRoom = waitingRoom;
		this.map = map;
    }
	public ArrayList<LocationData> getWaitingRoom() {
    	return waitingRoom;
    }
	public ArrayList<MapLocData> getMap() {
    	return map;
    }
	public LocationData getMainLobby() {
    	return mainLobby;
    }
	public LocationData getServerLobby() {
    	return serverLobby;
    }
}
