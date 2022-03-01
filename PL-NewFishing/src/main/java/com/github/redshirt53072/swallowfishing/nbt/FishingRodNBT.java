package com.github.redshirt53072.swallowfishing.nbt;

import org.bukkit.inventory.ItemStack;

import com.github.redshirt53072.growthapi.item.ItemNBTLoader;
import com.github.redshirt53072.swallowfishing.SwallowFishing;


public class FishingRodNBT extends ItemNBTLoader{

    public FishingRodNBT(ItemStack item) {
    	super(item,SwallowFishing.getInstance());
    }
    public void init(String rodName) {
    	super.writeString("rodid", rodName);
    }
    
    public String getRodID() {
    	return super.readString("rodid");
    }
}
