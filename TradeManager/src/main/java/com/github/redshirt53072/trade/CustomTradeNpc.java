package com.github.redshirt53072.trade;

import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;

import com.github.redshirt53072.api.npc.Npc;
import com.github.redshirt53072.api.npc.NpcManager;

public class CustomTradeNpc extends Npc{

	public static boolean isMatch(Villager vil) {
		return NpcManager.matchNpcType(vil, "customtrade");
	}
	
	public CustomTradeNpc() {
		super("customtrade");
	}

	@Override
	public boolean onClick(Player player,Villager vil) {

		player.openMerchant(vil, true);
		return true;
	}
}
