package com.github.redshirt53072.survival.ench;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

public class EnchData {
	private Ench type;
	private List<Ench> exclusive;
	private String name;
	private ArrayList<String> lore;
	private int maxLevel;
	private int exp;
	
	public EnchData(Ench type, List<Ench> exclusive,String name,ArrayList<String> lore,int maxLevel,int exp) {
		this.exclusive = exclusive;
		this.type = type;
    	this.name = name;
    	this.lore = lore;
    	this.maxLevel = maxLevel;
    	this.exp = exp;
    }
	/*
	public boolean canAdd(Ench ench){
		for(Ench na : cantExist) {
			if(na.equals(ench)){
				return false;
			}
		}
		return true;
	}*/
	public Ench getType() {
    	return type;
    }
	public List<Ench> getExclusive(){
		return exclusive;
	}
	public String getName() {
    	return name;
	}
	public ArrayList<String> getLore() {
    	return lore;
    }
	public int getExpCost(int level) {
    	return level * level * exp;
	}
	public int getMaxLevel() {
    	return maxLevel;
	}
	
	public static class ScrapData{
		private int amount;
		private String name;
		private Material mate;
		
		public ScrapData(int amount,String name,Material material) {
			this.name = name;
			this.amount = amount;
			this.mate = material;
		}
		public Material getItem() {
			return mate;
		}

		public String getName() {
	    	return name;
		}
		public int getAmount() {
			return amount;
		}
	}
	
	public static enum Ench{
		ARROW_DAMAGE(Enchantment.ARROW_DAMAGE),
		ARROW_FIRE(Enchantment.ARROW_FIRE),
		ARROW_INFINITE(Enchantment.ARROW_INFINITE),
		ARROW_KNOCKBACK(Enchantment.ARROW_KNOCKBACK),
		BINDING_CURSE(Enchantment.BINDING_CURSE),
		CHANNELING(Enchantment.CHANNELING),
		DAMAGE_ALL(Enchantment.DAMAGE_ALL),
		DAMAGE_ARTHROPODS(Enchantment.DAMAGE_ARTHROPODS),
		DAMAGE_UNDEAD(Enchantment.DAMAGE_UNDEAD),
		DEPTH_STRIDER(Enchantment.DEPTH_STRIDER),
		DIG_SPEED(Enchantment.DIG_SPEED),
		DURABILITY(Enchantment.DURABILITY),
		FIRE_ASPECT(Enchantment.FIRE_ASPECT),
		FROST_WALKER(Enchantment.FROST_WALKER),
		IMPALING(Enchantment.IMPALING),
		KNOCKBACK(Enchantment.KNOCKBACK),
		LOOT_BONUS_BLOCKS(Enchantment.LOOT_BONUS_BLOCKS),
		LOOT_BONUS_MOBS(Enchantment.LOOT_BONUS_MOBS),
		LOYALTY(Enchantment.LOYALTY),
		LUCK(Enchantment.LUCK),
		LURE(Enchantment.LURE),
		MENDING(Enchantment.MENDING),
		MULTISHOT(Enchantment.MULTISHOT),
		OXYGEN(Enchantment.OXYGEN),
		PIERCING(Enchantment.PIERCING),
		PROTECTION_ENVIRONMENTAL(Enchantment.PROTECTION_ENVIRONMENTAL),
		PROTECTION_EXPLOSIONS(Enchantment.PROTECTION_EXPLOSIONS),
		PROTECTION_FALL(Enchantment.PROTECTION_FALL),
		PROTECTION_FIRE(Enchantment.PROTECTION_FIRE),
		PROTECTION_PROJECTILE(Enchantment.PROTECTION_PROJECTILE),
		QUICK_CHARGE(Enchantment.QUICK_CHARGE),
		RIPTIDE(Enchantment.RIPTIDE),
		SILK_TOUCH(Enchantment.SILK_TOUCH),
		SOUL_SPEED(Enchantment.SOUL_SPEED),
		SWEEPING_EDGE(Enchantment.SWEEPING_EDGE),
		THORNS(Enchantment.THORNS),
		VANISHING_CURSE(Enchantment.VANISHING_CURSE),
		WATER_WORKER(Enchantment.WATER_WORKER);
		
		private String id;
		private Enchantment ench = null;
		@SuppressWarnings("deprecation")
		private Ench(Enchantment ench) {
			id = ench.getName();
			this.ench = ench;
		}
		private Ench(String id) {
			this.id = id;
		}
		public Enchantment getEnch() {
			return ench;
		}
		
		public String getID() {
			return id;
		}
		static public Ench getEnch(String id) {
			for(Ench en : Ench.values()) {
				if(en.getID().equals(id)){
					return en;
				}
			}
			return null;
		}
		static public Ench fromEnchantment(Enchantment ench) {
			for(Ench en : Ench.values()) {
				Enchantment ecm = en.getEnch();
				if(ecm == null){
					continue;
				}
				if(ecm.equals(ench)){
					return en;
				}
			}
			return null;
		}
	}
}
