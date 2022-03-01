package com.github.redshirt53072.dimmanager.data;

import java.util.List;
import java.util.UUID;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;

public final class DimData {
	private String name;
	private GameMode mode;
	private boolean isVisible;
	private Location loc;
	private UUID uuid;
	private DimID id;
	
	public DimData(String name,Location loc,GameMode mode,UUID uuid,boolean isVisible){
		this.loc = loc;
		this.isVisible = isVisible;
		this.mode = mode;
		this.name = name;
		this.uuid = uuid;
		id = DimID.valueOf(name);
	}
	
	public DimID getID() {
		return id;
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
		private List<DimData> homeDim;
		
		private DimData start;
		
		public DimAllData(List<DimData> data,List<DimData> homeDim,DimData start){
			this.data = data;
			this.homeDim = homeDim;
			this.start = start;
		}
		
		public DimData getStart() {
			return start;
		}
		public List<DimData> getDimData() {
			return data;
		}
		public List<DimData> getHomeDim() {
			return homeDim;
		}
		public boolean isHomeDim(World world) {
			UUID uuid = world.getUID();
			for(DimData dd : homeDim) {
				if(dd.getUUID().equals(uuid)){
					return true;
				}
			}
			return false;
		}
	}
}