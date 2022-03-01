package com.github.redshirt53072.trademanager.data;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;


public class TradeData implements Comparable<TradeData>{
	private ItemStack buy1 = null;
	private ItemStack buy2 = null;
	private ItemStack sell = null;
	private int level = 1;
	private int vilExp = 0;
	
	public TradeData(int level) {
		this.level = level;
	}
	public TradeData(int level,MerchantRecipe recipe) {
		this.level = level;
		List<ItemStack> buys = recipe.getIngredients();
		int size = buys.size();
		if(size > 0){
			buy1 = buys.get(0);
		}
		if(size > 1){
			buy2 = buys.get(1);
		}
		
		sell = recipe.getResult();
		vilExp = recipe.getVillagerExperience();
		
	}
	
	
	public void setSell(ItemStack sellItem) {
		if(sellItem != null) {
			if(sellItem.getType().equals(Material.AIR)) {
				sell = null;
				return;
			}
		}
		sell = sellItem;
	}
	public void setBuy1(ItemStack buy) {
		if(buy != null) {
			if(buy.getType().equals(Material.AIR)) {
				buy1 = null;
				return;
			}
		}
		buy1 = buy;
	}
	
	public void setBuy2(ItemStack buy) {
		if(buy != null) {
			if(buy.getType().equals(Material.AIR)) {
				buy2 = null;
				return;
			}
		}
		buy2 = buy;
	}
	
	public void setVilExp(int exp) {
		vilExp = exp;
	}
	
	public ItemStack getSell() {
		return sell;
	}
	public ItemStack getBuy1() {
		return buy1;
	}
	
	public ItemStack getBuy2() {
		return buy2;
	}
	
	public int getVilExp() {
		return vilExp;
	}
	
	public int getLevel() {
		return level;
	}
	
	public MerchantRecipe convert(){
		try {
			MerchantRecipe recipe = new MerchantRecipe(sell,2000000000);
			List<ItemStack> buy = new ArrayList<ItemStack>();
			buy.add(buy1);
			if(buy2 != null) {
				buy.add(buy2);
			}
			recipe.setIngredients(buy);
			recipe.setVillagerExperience(vilExp);
			recipe.setExperienceReward(true);
			recipe.setPriceMultiplier(0);
			return recipe;
		}catch(Exception ex) {
			return null;
		}
	}
	@Override
	public int compareTo(TradeData o) {
		if(this.getLevel() < o.getLevel()){
			return -1;
		}
		if(this.getLevel() > o.getLevel()){
			return 1;
		}
		return 0;
	}
}
