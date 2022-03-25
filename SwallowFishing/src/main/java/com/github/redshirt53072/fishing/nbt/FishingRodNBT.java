package com.github.redshirt53072.fishing.nbt;

import org.bukkit.inventory.ItemStack;

import com.github.redshirt53072.api.item.ItemNBTLoader;
import com.github.redshirt53072.fishing.SwallowFishing;


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
