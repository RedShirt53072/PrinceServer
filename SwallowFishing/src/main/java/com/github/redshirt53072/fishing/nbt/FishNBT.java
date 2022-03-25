package com.github.redshirt53072.fishing.nbt;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.github.redshirt53072.api.item.ItemNBTLoader;
import com.github.redshirt53072.fishing.SwallowFishing;


public class FishNBT extends ItemNBTLoader{

    public FishNBT(ItemStack item) {
    	super(item,SwallowFishing.getInstance());
    }
    public void init(int size,int rarity,int quality,Player player,int price,int fishID) {
    	super.writeInt("fishsize", size);
    	super.writeInt("rarity", rarity);
    	super.writeInt("quality", quality);
    	super.writeString("username", player.getName());
    	super.writeInt("price", price);
    	super.writeInt("fishname", fishID);
    }
    public Integer getSize() {
    	return super.readInt("fishsize");
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
    public Integer getFishName() {
    	return super.readInt("fishname");
    }
}
