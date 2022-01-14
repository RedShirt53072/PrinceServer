package com.github.redshirt53072.usefulshulker.data;

import org.bukkit.entity.Entity;

import com.github.redshirt53072.growthapi.util.PlayerNBTLoader;
import com.github.redshirt53072.usefulshulker.UsefulShulker;



public class PlayerECNBT extends PlayerNBTLoader{
	
    public PlayerECNBT(Entity entity) {
    	super(entity,UsefulShulker.getInstance());
    }
    
    
    public void setUnlockedPage(int page) {
    	writeInt("unlockpage",page);
    }
    
    public int getUnlockedPage() {
    	Integer page = readInt("unlockpage");
    	if(page == null) {
    		setUnlockedPage(1);
        	return 1;
    	}
    	return page;
    }
    
}
