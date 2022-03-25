package com.gmail.akashirt53072.minegame.config.datatype;

import org.bukkit.Location;

public class LocationData {
	private String id;
	private Location loc;
	
	public LocationData(String id, Location loc) {
		this.id = id;
		this.loc = loc;
    }
	public String getId() {
    	return id;
    }
	public Location getLocation() {
    	return loc;
	}
}
