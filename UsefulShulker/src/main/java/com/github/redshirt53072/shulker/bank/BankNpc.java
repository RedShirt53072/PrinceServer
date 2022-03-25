package com.github.redshirt53072.shulker.bank;

import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;

import com.github.redshirt53072.api.message.MessageManager;
import com.github.redshirt53072.api.npc.Npc;

public class BankNpc extends Npc{

	public BankNpc() {
		super("bank");
	}

	@Override
	public boolean onClick(Player player,Villager vil) {
		new BankGui().open(player);
		MessageManager.sendInfo(player.getName() + "様いらっしゃいませ。Prince銀行ロビー本店でございます。", player);;
		return true;
	}

}
