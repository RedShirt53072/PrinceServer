package com.github.redshirt53072.trademanager.command;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.RayTraceResult;

import com.github.redshirt53072.growthapi.command.ManagementCommand;
import com.github.redshirt53072.growthapi.command.SubCommand;
import com.github.redshirt53072.growthapi.message.MessageManager;
import com.github.redshirt53072.trademanager.data.VillagerManager;
import com.github.redshirt53072.trademanager.gui.HubGui;

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
			MessageManager.sendSpecial(ChatColor.RED + "[error]必要な項目が未記入です。", p);
        	return;
        }
		
		switch(args[1]) {
		case "modify":
			new HubGui().registerPlayer(p);
			MessageManager.sendSpecial("交易設定GUIを開きました。", p);
			return;
		case "toggle":
			if(args.length < 4) {
				MessageManager.sendSpecial(ChatColor.RED + "[error]必要な項目が未記入です。", p);
	        	return;
	        }
			String uuidStr = args[2];
			UUID uuid;
			try {
				uuid = UUID.fromString(uuidStr);
			}catch (Exception ex){
				MessageManager.sendSpecial(ChatColor.RED + "[error]無効なUUIDです。", p);	
				return;
			}
			Entity entity = p.getServer().getEntity(uuid);
			if(entity == null) {
				MessageManager.sendSpecial(ChatColor.RED + "[error]無効なUUIDです。", p);	
				return;	
			}
			if(entity.getType().equals(EntityType.VILLAGER)) {
				MessageManager.sendSpecial(ChatColor.RED + "[error]そのUUIDのエンティティは村人ではありません。", p);	
				return;	
			}
			
			VillagerManager manager = new VillagerManager((Villager)entity);
			if(args[3].equals("enable")) {
				MessageManager.sendSpecial(entity.getCustomName() + "の交易テーブルの適用を有効化しました。", p);
	        	manager.setVersion(0);
				return;
			}
			if(args[3].equals("disable")) {
				MessageManager.sendSpecial(entity.getCustomName() + "の交易テーブルの適用を無効化しました。", p);
				manager.setVersion(-1);
				return;
			}
			int now = manager.getVersion();
			if(now > -1) {
				MessageManager.sendSpecial(entity.getCustomName() + "の交易テーブルの適用を無効化しました。", p);
				manager.setVersion(-1);
				return;
			}
			MessageManager.sendSpecial(entity.getCustomName() + "の交易テーブルの適用を有効化しました。", p);
        	manager.setVersion(0);
			return;	
		}
		MessageManager.sendSpecial(ChatColor.RED + "[error]無効なサブコマンドです。", p);
    	
    }
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args){
		List<String> tab = new ArrayList<String>();
		if(!(sender instanceof Player)) {
			sender.sendMessage("[error]コンソールからは実行できません。");
        	return tab;
		}

		Player p = (Player)sender;
		if(args.length == 2) {
            tab.add("modify");
            tab.add("toggle");
        }else {
        	String text1 = args[1];
        	if (text1.equals("toggle")) {
        		if(args.length == 3) {
        			tab.add("個別の村人ごとに交易テーブルの適用を切り替えるコマンドです");
        			tab.add("村人に視線を合わせるとUUIDがタブ補完できます");
        			Location loc = p.getLocation();
        			loc.add(0, 1.5, 0);
        			Predicate<Entity> pre = (Entity e) -> {
        				if(e.getType().equals(EntityType.VILLAGER)) {
        					return true;
        				}
        				return false;
        			};
        			RayTraceResult result = p.getWorld().rayTraceEntities(loc, p.getLocation().getDirection(), 7, pre);
        			Entity hit = result.getHitEntity();
        			if(null != hit) {
        				LivingEntity hit2 = (LivingEntity)hit;
        				hit2.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING,200,1,false,false));
            			tab.add(hit.getUniqueId().toString());
        			}
        		}
        		if(args.length == 5) {
        			tab.add("enable");
        			tab.add("disable");
        		}
        	}
        }
        return tab;
	}
	
}

