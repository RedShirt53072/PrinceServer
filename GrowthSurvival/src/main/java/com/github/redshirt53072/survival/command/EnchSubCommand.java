package com.github.redshirt53072.survival.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.github.redshirt53072.api.command.ManagementCommand;
import com.github.redshirt53072.api.command.SubCommand;
import com.github.redshirt53072.api.item.ItemUtil;
import com.github.redshirt53072.api.message.MessageManager;
import com.github.redshirt53072.survival.ench.EnchBuilder;
import com.github.redshirt53072.survival.ench.EnchManager;
import com.github.redshirt53072.survival.ench.ToolData;
import com.github.redshirt53072.survival.ench.ToolData.ToolGroupData;

public class EnchSubCommand implements SubCommand{
	private static EnchSubCommand sub;
	
	public static void register(){
		sub = new EnchSubCommand();
		ManagementCommand.register("enchant", sub);
	}
	
	@Override
    public void onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(args.length < 2) {
			MessageManager.sendCommandError("必要な項目が未記入です。", sender);
        	return;
        }
		
		if(args[1].equals("reload")) {
			EnchManager.reload();
			MessageManager.sendSpecial("エンチャントデータを再読み込みしました。", sender);
			return;
		}
		if(args[1].equals("setstatic") || args[1].equals("convert")){
			if(!(sender instanceof Player)) {
				MessageManager.sendCommandError("このサブコマンドはコンソールからは実行できません。", sender);
				return;
			}
			Player player = (Player)sender;
			if(!player.getGameMode().equals(GameMode.CREATIVE)) {
				MessageManager.sendCommandError("クリエイティブモードで実行してください。", sender);
				return;
			}
			ItemStack item = player.getInventory().getItemInMainHand();
			if(item == null) {
				MessageManager.sendCommandError("メインハンドに設定したいアイテムを持ってください。", sender);
				return;
			}
			
			ToolData td = EnchManager.getToolData(item.getType());
			if(td == null) {
				MessageManager.sendCommandError("メインハンドのアイテムはエンチャントが付与できないアイテムです。", sender);
				return;
			}
			ToolGroupData tgd = td.getToolGroupData();
			if(tgd == null) {
				MessageManager.sendCommandError("メインハンドのアイテムはエンチャントが付与できないアイテムです。", sender);
				return;
			}
			
			if(args[1].equals("setstatic")) {
				EnchBuilder builder = new EnchBuilder(item,tgd);
				builder.lock();
				ItemUtil.giveItem(player, builder.build());
				MessageManager.sendSpecial("現在のメインハンドのエンチャントを改変できないよう設定しました。", sender);
				return;	
			}else {
				ItemUtil.giveItem(player, new EnchBuilder(item,tgd).build()); 
				MessageManager.sendSpecial("現在のメインハンドのエンチャントをPrinceサーバー仕様に変換しました。", sender);
				return;
			}
			
		}
		MessageManager.sendCommandError("無効なサブコマンドです。", sender);
    }
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args){
		List<String> tab = new ArrayList<String>();
		if(args.length == 2) {
            tab.add("reload");
			if(sender instanceof Player) {
				tab.add("setstatic");
				tab.add("convert");
			}
		}
        return tab;
	}
}