package com.github.redshirt53072.swallowfishing.collection;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.github.redshirt53072.growthsurvival.collection.ItemCollection;

public class FishCollection extends ItemCollection{

	@Override
	public ItemStack getIconItem() {
		return new ItemStack(Material.COD);
	}

	@Override
	public String getName() {
		return "魚図鑑";
	}

	@Override
	public void openGui(Player player) {
		new FishCollectionGui(this).open(player);
	}
}
