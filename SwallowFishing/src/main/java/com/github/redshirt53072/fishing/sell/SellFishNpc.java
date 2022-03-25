package com.github.redshirt53072.fishing.sell;

import org.bukkit.entity.Player;

import com.github.redshirt53072.api.npc.Npc;

public class SellFishNpc extends Npc{

	public SellFishNpc() {
		super("sellfish");
	}

	@Override
	public boolean onClick(Player player) {
		new SellFishGui().open(player);
		return true;
	}

}
