package com.github.redshirt53072.economyapi.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.github.redshirt53072.baseapi.message.MessageManager;
import com.github.redshirt53072.baseapi.util.TextManager;
import com.github.redshirt53072.economyapi.item.ItemUtil;
import com.github.redshirt53072.economyapi.money.MoneyManager;
public class EmeraldCommand implements TabExecutor{
	
	@Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage("[error]コンソールからは実行できません。");
        	return true;
		}
		Player p = (Player)sender;
		if(args.length < 1) {
			MessageManager.sendWarning(ChatColor.RED + "[error]必要な項目が未記入です。", p);
        	return true;
        }
		
		
		if(args[0].equals("deposit")) {
			//預ける
			int price = 0;
			price += ItemUtil.countItem(p, new ItemStack(Material.EMERALD), true);
			price += ItemUtil.countItem(p, new ItemStack(Material.EMERALD_BLOCK), true) * 64;
			price += ItemUtil.countItem(p, ItemUtil.getLiquidEmerald(), true) * 4096;
			
			if(price == 0) {
				MessageManager.sendWarning(ChatColor.RED + "[error]インベントリ内にエメラルドがありませんでした。", p);
	        	return true;
	        }
			MoneyManager.add(p, price);

			MessageManager.sendImportant(price + "Ɇを口座に預け入れました。", p);
			return true;
		}
		if(args[0].equals("withdraw")) {
			if(args.length < 2) {
				MessageManager.sendWarning(ChatColor.RED + "[error]金額を指定してください。", p);
	        	return true;
	        }
			int price = TextManager.toNaturalNumber(args[1]);
			if(price > 1000000000) {
				MessageManager.sendWarning(ChatColor.RED + "[error]金額指定が無効な値です。", p);
	        	return true;
			}
			if(!MoneyManager.remove(p, price)) {
				MessageManager.sendWarning(ChatColor.RED + "[error]口座のɆが足りていません。", p);
				return true;
			}
			int liquid = price / 4096;
			int block = (price % 4096) / 64;
			int eme = (price % 4096) % 64;
			
			ItemUtil.giveItem(p, ItemUtil.getLiquidEmerald(), liquid);
			ItemUtil.giveItem(p, new ItemStack(Material.EMERALD_BLOCK), block);
			ItemUtil.giveItem(p, new ItemStack(Material.EMERALD), eme);

			MessageManager.sendImportant(price + "Ɇを口座から引き出しました。", p);
			return true;
		}

		MessageManager.sendSpecial(ChatColor.RED + "[error]無効なサブコマンドです。", p);
    	return true;
    }
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args){
		List<String> tab = new ArrayList<String>();
		if(args.length == 1) {
			tab.add("deposit");
			tab.add("withdraw");
		}else if(args.length == 2 && args[0].equals("withdraw")) {
			tab.add("<金額>");
		}
		return tab;
	}
}
