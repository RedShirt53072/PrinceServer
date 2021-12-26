package com.github.redshirt53072.economyapi.money;

import java.util.logging.Level;

import org.bukkit.entity.Player;

import com.github.redshirt53072.baseapi.message.LogManager;
import com.github.redshirt53072.economyapi.general.EconomyApi;

import net.milkbowl.vault.economy.Economy;

public class MoneyManager{
	
	
	public static void add(Player player,double money) {
		Economy eco = EconomyApi.getVault();
		eco.depositPlayer(player, money);
		LogManager.logInfo("[EconomyApi]" + money + "Eを" + player.getName() + "に与えました。", EconomyApi.getInstance(), Level.FINER);
	}
	public static boolean remove(Player player,double money) {
		Economy eco = EconomyApi.getVault();
		if(eco.getBalance(player) - money < 0) {
			LogManager.logInfo("[EconomyApi]口座内に十分なお金がなかったため" + money + "Eを" + player.getName() + "から徴収できませんでした。", EconomyApi.getInstance(), Level.INFO);
			return false;
		}
		eco.withdrawPlayer(player, money);
		LogManager.logInfo("[EconomyApi]" + money + "Eを" + player.getName() + "から徴収しました。", EconomyApi.getInstance(), Level.FINER);
		return true;
	}
	public static boolean pay(Player from,Player to,double money) {
		Economy eco = EconomyApi.getVault();
		if(eco.getBalance(from) - money < 0) {
			LogManager.logInfo("[EconomyApi]口座内に十分なお金がなかったため" + money + "Eを" + from.getName() + "から" + to.getName() + "へ送金できませんでした。", EconomyApi.getInstance(), Level.INFO);
			return false;
		}
		eco.withdrawPlayer(from, money);
		eco.depositPlayer(to, money);
		LogManager.logInfo("[EconomyApi]" + money + "Eを" + from.getName() + "から" + to.getName() + "へ送金しました。", EconomyApi.getInstance(), Level.FINER);
		return true;
	}
	public static double get(Player player) {
		Economy eco = EconomyApi.getVault();
		double money = eco.getBalance(player);
		LogManager.logInfo("[EconomyApi]" +  player.getName() + "の所持金(" + money + "E)を取得しました。", EconomyApi.getInstance(), Level.FINER);
		return money;
	}
	
	
}
