package com.github.redshirt53072.api.npc;

import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;

public abstract class Npc {
	protected String name;
	
	public Npc(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	/**
	 * 
	 * @param player
	 * @return キャンセルするかどうか
	 */
	public abstract boolean onClick(Player player,Villager vil);
}
