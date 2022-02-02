package com.github.redshirt53072.newfishing.nbt;

import org.bukkit.inventory.ItemStack;

import com.github.redshirt53072.growthapi.item.ItemNBTLoader;
import com.github.redshirt53072.newfishing.NewFishing;


public class FishingRodNBT extends ItemNBTLoader{

    public FishingRodNBT(ItemStack item) {
    	super(item,NewFishing.getInstance());
    }
    public void init(String rodName) {
    	super.writeString("rodid", rodName);
    }
    
    public String getRodID() {
    	return super.readString("rodid");
    }
}
