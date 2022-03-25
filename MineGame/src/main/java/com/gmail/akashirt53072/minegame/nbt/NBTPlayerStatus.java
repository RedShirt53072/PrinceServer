package com.gmail.akashirt53072.minegame.nbt;

import org.bukkit.entity.Player;

import com.gmail.akashirt53072.minegame.Main;
import com.gmail.akashirt53072.minegame.enums.PlayerStatus;

public class NBTPlayerStatus extends NBTLoader{

    public NBTPlayerStatus(Main plugin,Player player) {
    	super(plugin,player);
    }
    
    public void setType(PlayerStatus type) {
    	super.writeString("playerStatus", type.toString());
    }
    
    public PlayerStatus getType() {
    	String st = super.readString("playerStatus");
    	if(st == null){
    		return PlayerStatus.lOGOUT;
    	}
    	return PlayerStatus.valueOf(st);	
    	
    }
    
}
