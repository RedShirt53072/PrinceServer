package com.github.redshirt53072.dimmanager.data;

import java.util.List;
import java.util.UUID;

import org.bukkit.GameMode;
import org.bukkit.Location;

public final class DimData {
	private String name;
	private GameMode mode;
	private boolean isVisible;
	private Location loc;
	private UUID uuid;
	
	public DimData(String name,Location loc,GameMode mode,UUID uuid,boolean isVisible){
		this.loc = loc;
		this.isVisible = isVisible;
		this.mode = mode;
		this.name = name;
		this.uuid = uuid;
	}
	public String getName() {
		return name;
	}
	public GameMode getGamemode() {
		return mode;
	}
	public boolean isVisible() {
		return isVisible;
	}
	public Location getLocation() {
		return loc;
	}
	public UUID getUUID() {
		return uuid;
	}
	
	public final static class DimAllData {
		private List<DimData> data;
		private DimData start;
		
		public DimAllData(List<DimData> data,DimData start){
			this.data = data;
			this.start = start;
		}
		
		public DimData getStart() {
			return start;
		}
		public List<DimData> getDimData() {
			return data;
		}
	}
}
