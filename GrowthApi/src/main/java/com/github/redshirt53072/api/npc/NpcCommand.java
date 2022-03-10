package com.github.redshirt53072.api.npc;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.RayTraceResult;

import com.github.redshirt53072.api.message.MessageManager;
import com.github.redshirt53072.api.message.TextBuilder;

public class NpcCommand implements TabExecutor{
	
	@Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(!(sender instanceof Player)) {
			MessageManager.sendCommandError("このサブコマンドはコンソールからは実行できません。", sender);
			return true;
		}
		if(args.length < 3) {
			MessageManager.sendCommandError("必要な項目が未記入です。", sender);
        	return true;
        }
		
		String uuidStr = args[0];
		UUID uuid;
		try {
			uuid = UUID.fromString(uuidStr);
		}catch (Exception ex){
			MessageManager.sendCommandError("無効なUUIDです。", sender);	
			return true;
		}
		Entity entity = Bukkit.getServer().getEntity(uuid);
		if(entity == null) {
			MessageManager.sendCommandError("無効なUUIDです。", sender);	
			return true;	
		}
		if(!entity.getType().equals(EntityType.VILLAGER)) {
			MessageManager.sendCommandError("そのUUIDのエンティティは村人ではありません。", sender);	
			return true;	
		}
    	Villager vil = (Villager)entity;
		
    	String type = args[1];
    	
    	Set<Npc> list = NpcManager.getNpcList();
		for(Npc npc : list) {
			if(type.equals(npc.getName())) {
				
				if(args[2].equals("disable")) {
					if(NpcManager.removeNpcType(vil, type)) {
						MessageManager.sendSpecial(TextBuilder.plus(type,"を無効化しました。"), sender);
					}else {
						String nowType = NpcManager.getNowType(vil);
						if(nowType == null) {
							MessageManager.sendCommandError(TextBuilder.plus("この村人にはNPCタイプが設定されていません。"),sender);
							return true;
						}
						MessageManager.sendCommandError(TextBuilder.plus("この村人の現在のNPCタイプは",type,"ではなく",nowType,"です。"),sender);
					}
					return true;
				}
				String nowType = NpcManager.getNowType(vil);
				if(nowType != null) {
					if(nowType.equals(type)) {
						MessageManager.sendSpecial(TextBuilder.plus("この村人は既に",type,"に設定されています。"), sender);
						return true;
					}
				}
				
				NpcManager.setNpcType(vil, type);
				MessageManager.sendSpecial(TextBuilder.plus(type,"に設定しました。"), sender);
				return true;
			}
		}
		MessageManager.sendCommandError("無効なNPCタイプです",sender);
    	return true;
	}
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args){
		List<String> tab = new ArrayList<String>();
		if(!(sender instanceof Player)) {
    		tab.add("コンソールからは実行できません");
			return tab;
		}
		Player player = (Player)sender;
		if(args.length == 1) {
			//uuid
			tab.add("村人に視線を合わせるとUUIDがタブ補完できます");
			Location loc = player.getLocation();
			loc.add(0, 1.5, 0);
			Predicate<Entity> pre = (Entity e) -> {
				if(e.getType().equals(EntityType.VILLAGER)) {
					return true;
				}
				return false;
			};
			RayTraceResult result = player.getWorld().rayTraceEntities(loc, loc.getDirection(), 7, pre);
			if(result == null) {
				return tab;	
			}
			Entity hit = result.getHitEntity();
			if(null != hit) {
				LivingEntity hit2 = (LivingEntity)hit;
				hit2.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING,200,1,false,false));
    			tab.add(hit.getUniqueId().toString());
			}
		
		}else if(args.length == 2) {
			//type
			Set<Npc> list = NpcManager.getNpcList();
			for(Npc npc : list) {
				tab.add(npc.getName());
			}
			
		}else if(args.length == 3) {
			//true|false
        	String type = args[1];
        	
        	String uuidStr = args[0];
			UUID uuid;
			try {
				uuid = UUID.fromString(uuidStr);
			}catch (Exception ex){
				tab.add("無効な村人のUUIDです");
				return tab;
			}
			Entity entity = Bukkit.getServer().getEntity(uuid);
			if(entity == null) {
				tab.add("無効な村人のUUIDです");
				return tab;
			}
			if(!entity.getType().equals(EntityType.VILLAGER)) {
				tab.add("無効な村人のUUIDです");
				return tab;
			}
        	Villager vil = (Villager)entity;
			
			String nowType = NpcManager.getNowType(vil);
        	
        	Set<Npc> list = NpcManager.getNpcList();
			for(Npc npc : list) {
				if(type.equals(npc.getName())) {
					if(nowType == null) {
						tab.add("enable");
						return tab;
					}
					if(nowType.equals(type)) {
						tab.add("disable");
						return tab;
					}
					tab.add("enable");
					return tab;
				}
			}
        	tab.add("無効なNPCタイプです");
        }
        return tab;
	}
}