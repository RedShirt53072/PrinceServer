package com.github.redshirt53072.trade.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.redshirt53072.api.command.ManagementCommand;
import com.github.redshirt53072.api.command.SubCommand;
import com.github.redshirt53072.api.item.ItemTag;
import com.github.redshirt53072.api.message.MessageManager;
import com.github.redshirt53072.trade.bundle.Bundle;
import com.github.redshirt53072.trade.bundle.SpecialItem;
import com.github.redshirt53072.trade.bundle.SpecialItem.SpecialItems;
import com.github.redshirt53072.trade.gui.HubGui;

public class TradeSubCommand implements SubCommand{
	private static TradeSubCommand sub;
	
	public static void register(){
		sub = new TradeSubCommand();
		ManagementCommand.register("trade", sub);
		
	}
	
	
	@Override
    public void onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage("[error]コンソールからは実行できません。");
        	return;
		}
		Player p = (Player)sender;
		if(args.length < 2) {
			MessageManager.sendCommandError("必要な項目が未記入です。", p);
        	return;
        }
		
		switch(args[1]) {
		case "modify":
			if(!p.getGameMode().equals(GameMode.CREATIVE)) {
				MessageManager.sendCommandError("クリエイティブモードにしてからもう一度コマンドを送信してください。", p);
	        	return;
			}
			new HubGui().open(p);
			MessageManager.sendSpecial("交易設定GUIを開きました。", p);
			return;
		case "getbundle" :
			if(!p.getGameMode().equals(GameMode.CREATIVE)) {
				MessageManager.sendCommandError("クリエイティブモードにしてからもう一度コマンドを送信してください。", p);
	        	return;
			}
			for(ItemTag it : ItemTag.values()){
				p.getInventory().addItem(Bundle.getNewBox(it));
			}
			for(SpecialItems si : SpecialItems.values()){
				p.getInventory().addItem(SpecialItem.getNewBox(si));
			}
			MessageManager.sendSpecial("全種類の詰め合わせを取り出しました。", p);
			return;
		}
		MessageManager.sendCommandError("無効なサブコマンドです。", p);
    	
    }
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args){
		List<String> tab = new ArrayList<String>();
		if(!(sender instanceof Player)) {
			sender.sendMessage("[error]コンソールからは実行できません。");
        	return tab;
		}
		
		if(args.length == 2) {
            tab.add("modify");
            tab.add("getbundle");
        }
        return tab;
	}
	
}

