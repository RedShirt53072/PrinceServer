package com.github.redshirt53072.growthapi.money;

import java.util.logging.Level;

import org.bukkit.entity.Player;

import com.github.redshirt53072.growthapi.BaseAPI;
import com.github.redshirt53072.growthapi.message.LogManager;

import net.milkbowl.vault.economy.Economy;

public class MoneyManager{
	
	
	public static void add(Player player,double money) {
		Economy eco = BaseAPI.getVault();
		eco.depositPlayer(player, money);
	}
	public static boolean remove(Player player,double money) {
		Economy eco = BaseAPI.getVault();
		if(eco.getBalance(player) - money < 0) {
			LogManager.logInfo("[BaseAPI]口座内に十分なお金がなかったため" + money + "Eを" + player.getName() + "から徴収できませんでした。", BaseAPI.getInstance(), Level.INFO);
			return false;
		}
		eco.withdrawPlayer(player, money);
		return true;
	}
	public static boolean pay(Player from,Player to,double money) {
		Economy eco = BaseAPI.getVault();
		if(eco.getBalance(from) - money < 0) {
			LogManager.logInfo("[BaseAPI]口座内に十分なお金がなかったため" + money + "Eを" + from.getName() + "から" + to.getName() + "へ送金できませんでした。", BaseAPI.getInstance(), Level.INFO);
			return false;
		}
		eco.withdrawPlayer(from, money);
		eco.depositPlayer(to, money);
		return true;
	}
	public static double get(Player player) {
		Economy eco = BaseAPI.getVault();
		double money = eco.getBalance(player);
		return money;
	}
	
	
}
