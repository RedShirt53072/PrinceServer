package com.github.redshirt53072.trademanager.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;

import com.github.redshirt53072.growthapi.util.PlayerNBTLoader;
import com.github.redshirt53072.trademanager.TradeManager;
import com.github.redshirt53072.trademanager.data.TradeConfig.ProfessionData;

public final class VillagerManager extends PlayerNBTLoader{
	private static Map<Profession,ProfessionData> data = new HashMap<Profession,ProfessionData>();
	
	public static void reload() {
		data = TradeConfig.reload();
		
	}
	public static List<MerchantRecipe> getNewRecipe(Profession prof,int level){
		return data.get(prof).getLevelData(level).getRandomRecipe();
	}
	
	public static List<MerchantRecipe> getAllRecipe(Profession prof,int level){
		List<MerchantRecipe> recipes = new ArrayList<MerchantRecipe>();
		ProfessionData profData = data.get(prof);
		for(int i = 1; i <= level;i++) {
			recipes.addAll(profData.getLevelData(level).getRandomRecipe());
		}			
		return recipes;
	}
	
	public static ProfessionData getProfessionData(Profession prof){
		return data.get(prof);
	}
	
	public static void setProfessionData(Profession prof,ProfessionData profData){
		profData.setVersion(data.get(prof).getVersion() + 1);
		data.replace(prof, profData);
		TradeConfig.save(data);
	}
	
	
	public static int getTableVersion(Profession prof){
		return data.get(prof).getVersion();
	}
	
	public VillagerManager(Villager entity) {
    	super(entity,TradeManager.getInstance());
    }

	
	
	public void setVersion(int ver) {
    	writeInt("version",ver);
    }
    
    public int getVersion() {
    	Integer ver = readInt("version");
    	if(ver == null) {
        	return 0;
    	}
    	return ver;
    }
	
	
    public static class TradeData implements Comparable<TradeData>{
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
    	
		
		public void setSell(ItemStack sellItems) {
			sell = sellItems;
		}
		public void setBuy1(ItemStack buy) {
			buy1 = buy;
		}
		
		public void setBuy2(ItemStack buy) {
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
	
	public enum ProfData{
		//NONE("求職者",Material.GLASS,Profession.NONE),
		NITWIT("ニート",Material.GREEN_BED,Profession.NITWIT),
		ARMORER("防具鍛冶",Material.IRON_CHESTPLATE,Profession.ARMORER),
		BUTCHER("肉屋",Material.SMOKER,Profession.BUTCHER),
		CARTOGRAPHER("製図家",Material.CARTOGRAPHY_TABLE,Profession.CARTOGRAPHER),
		CLERIC("聖職者",Material.BREWING_STAND,Profession.CLERIC),
		FARMER("農家",Material.COMPOSTER,Profession.FARMER),
		FISHERMAN("釣り人",Material.BARREL,Profession.FISHERMAN),
		FLETCHER("矢細工師",Material.FLETCHING_TABLE,Profession.FLETCHER),
		LEATHERWORKER("革細工師",Material.CAULDRON,Profession.LEATHERWORKER),
		LIBRARIAN("司書",Material.LECTERN,Profession.LIBRARIAN),
		MASON("石工",Material.STONECUTTER,Profession.MASON),
		SHEPHERD("羊飼い",Material.LOOM,Profession.SHEPHERD),
		TOOLSMITH("道具鍛冶",Material.SMITHING_TABLE,Profession.TOOLSMITH),
		WEAPONSMITH("武器鍛冶",Material.GRINDSTONE,Profession.WEAPONSMITH);
		
		private final String name;
		private final Material icon;
		private final Profession prof;
		
		private ProfData(String name,Material icon,Profession prof) {
			this.name = name;
			this.icon = icon;
			this.prof = prof;
		}
		
		public String getName(){
			return name;
		}
		public Material getIconItem(){
			return icon;
		}
		public Profession getProfession() {
			return prof;
		}
	}
	
	
}
