package com.github.redshirt53072.api.npc;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;

import com.github.redshirt53072.api.BaseAPI;
import com.github.redshirt53072.api.player.PlayerNBTLoader;

public final class NpcManager extends PlayerNBTLoader{
	private static Set<Npc> npcs = new HashSet<Npc>();
	
	public static void registerNpc(Npc npc) {
		npcs.add(npc);
	}
	
	public static boolean matchNpcType(Villager vil,String type) {
		String nowType = getNowType(vil);
		if(nowType == null) {
			return false;
		}
		return nowType.equals(type);
		
	}
	
	static boolean onClick(Villager entity,Player player) {
		String type = new NpcManager(entity).getNpcType();
		if(type == null) {
			return false;
		}
		for(Npc npc : npcs) {
			if(type.equals(npc.getName())) {
				return npc.onClick(player,entity);
			}
		}
		return false;
	}
	
	static void setNpcType(Villager entity,String npcType) {
		new NpcManager(entity).setNpcType(npcType);
	}
	static boolean removeNpcType(Villager entity,String npcType) {
		NpcManager manager = new NpcManager(entity);
		String nowType = manager.getNpcType();
		if(nowType == null) {
			return false;
		}
		if(nowType.equals(npcType)){
			manager.setNpcType(null);
			return true;
		}
		return false;
	}
	
	static Set<Npc> getNpcList() {
		return npcs;
	}
	
	public static String getNowType(Villager entity){
		return new NpcManager(entity).getNpcType();
	}
	
	private NpcManager(Villager entity) {
    	super(entity,BaseAPI.getInstance());
    }

	public void setNpcType(String npcType) {
    	if(npcType == null) {
    		super.remove("npctype");
    	}else {
    		writeString("npctype",npcType);		
    	}
	}
    
    public String getNpcType() {
    	String type = readString("npctype");
    	return type;
    }
}
