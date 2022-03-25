package com.github.redshirt53072.fishing.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.github.redshirt53072.api.command.ManagementCommand;
import com.github.redshirt53072.api.command.SubCommand;
import com.github.redshirt53072.api.item.ItemUtil;
import com.github.redshirt53072.api.message.MessageManager;
import com.github.redshirt53072.fishing.data.FishManager;

public class FishSubCommand implements SubCommand{
	private static FishSubCommand sub;
	
	public static void register(){
		sub = new FishSubCommand();
		ManagementCommand.register("fish", sub);
	}
	
	@Override
    public void onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(args.length < 2) {
			MessageManager.sendCommandError("必要な項目が未記入です。", sender);
        	return;
        }
		
		switch(args[1]) {
		case "reload":
			FishManager.reload();
			MessageManager.sendSpecial("釣りデータを再読み込みしました。", sender);
			return;
		case "getrod":
			if(!(sender instanceof Player)) {
				MessageManager.sendCommandError("コンソールからは実行できません。", sender);
			   	return;
			}
			Player p = (Player)sender;
			if(args.length >= 3) {
				ItemStack item = FishManager.getRodItem(args[2]);
				if(item == null) {
					MessageManager.sendCommandError("無効な釣り竿のIDです。", sender);				
					return;	
				}
				ItemUtil.giveItem(p, item);
				MessageManager.sendSpecial("釣り竿" + args[2] + "を取り出しました。", sender);	
				return;
			}
			List<ItemStack> items = FishManager.getAllRodItem();
			for(ItemStack item : items) {
				ItemUtil.giveItem(p, item);
			}
			MessageManager.sendSpecial("全種類の釣り竿を取り出しました。", sender);
			return;
		}
		MessageManager.sendCommandError("無効なサブコマンドです。", sender);
    }
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args){
		List<String> tab = new ArrayList<String>();
		if(args.length == 2) {
            tab.add("reload");
            if(sender instanceof Player) {
                tab.add("getrod");	
            }
		}else if(args.length == 3){
			if(args[1].equals("getrod")) {
	            tab.addAll(FishManager.getRodList());
			}
		}
        return tab;
	}
	
}

