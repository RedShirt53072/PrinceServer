package com.github.redshirt53072.newfishing.nbt;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.github.redshirt53072.growthapi.item.ItemNBTLoader;
import com.github.redshirt53072.newfishing.NewFishing;


public class FishNBT extends ItemNBTLoader{

    public FishNBT(ItemStack item) {
    	super(item,NewFishing.getInstance());
    }
    public void init(double size,int rarity,int quality,Player player,int price,String fishName) {
    	super.writeDouble("fishsize", size);
    	super.writeInt("rarity", rarity);
    	super.writeInt("quality", quality);
    	super.writeString("username", player.getName());
    	super.writeInt("price", price);
    	super.writeString("fishname", fishName);
    }
    public Double getSize() {
    	return super.readDouble("fishsize");
    }
    public Integer getRarity() {
    	return super.readInt("rarity");
    }
    public Integer getQuality() {
    	return super.readInt("quality");
    }
    public String getPlayer() {
    	return super.readString("username");
    }
    public Integer getPrice() {
    	return super.readInt("price");
    }
    public String getFishName() {
    	return super.readString("fishname");
    }
}
