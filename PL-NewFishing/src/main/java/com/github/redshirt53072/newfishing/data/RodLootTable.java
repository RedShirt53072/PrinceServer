package com.github.redshirt53072.newfishing.data;

import java.util.ArrayList;

public class RodLootTable {
	private DimLootData overData;
	private ArrayList<TimeLootData> endData;
	
	public RodLootTable(DimLootData over,ArrayList<TimeLootData> end) {
		this.overData = over;
    	this.endData = end;
    }
	public DimLootData getOver() {
    	return overData;
    }
	public ArrayList<TimeLootData> getEnd() {
    	return endData;
	}
}
