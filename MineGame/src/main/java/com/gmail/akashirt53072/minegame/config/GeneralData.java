package com.gmail.akashirt53072.minegame.config;


import com.gmail.akashirt53072.minegame.Main;

public class GeneralData extends DataConfig{
	
	/*
	matchid: 1

	*/
	public GeneralData(Main plugin) {
		super(plugin,"data_general.yml");
	}
	
	public int getMatchID() {
		return getIntData("matchid");
	}
	public void setMatchID(int id) {
		setData("matchid",id);
	}
	
}