package com.github.redshirt53072.trademanager.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.Villager.Profession;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;

import com.github.redshirt53072.growthapi.config.ConfigManager;
import com.github.redshirt53072.trademanager.TradeManager;

public final class TradeConfig {
	
	/*
	NITWIT:
	  version: 1
  	  level1:
    	roll: 1
    	trade1:
      	  buy1:
      	  buy2:
      	  sell:
      	  villagerexp: 1
    	trade2:
  	  level2:
  	  level3:
  	  level4:
  	  level5:
	ARMORER:
	 */
	
	
	//configの書き込み
	public static void save(Map<Profession,ProfessionData> data) {
		ConfigManager manager = new ConfigManager(TradeManager.getInstance(),"trades","data.yml");
		data.forEach((pro,proData) -> {
			writeProfession(manager,pro.toString(),proData);
		});
		manager.logConfig("", "を更新しました。");
	}
	
	private static void writeProfession(ConfigManager manager,String path,ProfessionData data) {
		List<LevelData> levels = data.getAllLevel();
		manager.setData(path + ".version",data.getVersion());
		for(int i = 0;i < levels.size() ;i++){
			writeLevelData(manager,path + ".level" + (i + 1),levels.get(i));
		}
	}
	
	private static void writeLevelData(ConfigManager manager,String path,LevelData data) {
		List<MerchantRecipe> recipes = data.getAllRecipe();
		for(int i = 0;i < recipes.size() ;i++){
			writeRecipe(manager,path + ".trade" + (i + 1),recipes.get(i));
		}
		manager.setData(path + ".roll", data.getRoll());
	}
	
	private static void writeRecipe(ConfigManager manager,String path,MerchantRecipe recipe) {
		List<ItemStack> buyItems = recipe.getIngredients();
		for(int i = 0;i < Math.min(2,buyItems.size());i++) {
			manager.setData(path + ".buy" + (i + 1), buyItems.get(i));	
		}
		manager.setData(path + ".sell", recipe.getResult());
		manager.setData(path + ".villagerexp", recipe.getVillagerExperience());
	}
	//config書き込み終了
	
	//configの読み取り
	public static Map<Profession,ProfessionData> reload() {
		Map<Profession,ProfessionData> data = new HashMap<Profession,ProfessionData>();
		ConfigManager manager = new ConfigManager(TradeManager.getInstance(),"trades","data.yml");
		manager.configInit();
		
		for(Profession prof : Profession.values()) {
			if(prof == Profession.NONE) {
				continue;
			}
			data.put(prof, readProfession(manager,prof.toString()));
		}
		
		manager.logConfig("", "を読み込みました。");
		return data;
    }
	
	
	private static ProfessionData readProfession(ConfigManager manager,String path) {
		ProfessionData data = new ProfessionData(manager.getInt(path + ".version"));
		for(int i = 1;i < 6;i++) {
			data.addLevel(i,readLevelData(manager,path + ".level" + i));
		}
		return data;
	}
	
	private static LevelData readLevelData(ConfigManager manager,String path) {
		LevelData data = new LevelData(manager.getInt(path + ".roll"));
		for(int i = 1;manager.containData(path + ".trade" + i);i++) {
			MerchantRecipe recipe = readRecipe(manager,path + ".trade" + i);
			if(recipe != null) {
				data.addRecipe(recipe);	
			}
		}
		return data;
	}
	
	private static MerchantRecipe readRecipe(ConfigManager manager,String path) {
		try {
			List<ItemStack> buyItems = manager.getItemArray(path, "buy");
			ItemStack sellItem = manager.getItemStack(path + ".sell");
			MerchantRecipe recipe = new MerchantRecipe(sellItem,2000000000);
			recipe.setIngredients(buyItems);
			recipe.setVillagerExperience(manager.getInt(path + ".villagerexp"));
			recipe.setExperienceReward(true);
			recipe.setPriceMultiplier(0);
			return recipe;
		}catch(Exception ex) {
			manager.logException(path, "を読み込みましたが、正しいデータが入っていません。", ex);
		}
		return null;
	}
	//以上読み取り
	
	public static class LevelData{
		private List<MerchantRecipe> data = new ArrayList<MerchantRecipe>();
		private int roll = 0;
		
		LevelData(Integer roll){
			if(roll != null) {
				this.roll = roll;	
			}
		}
		
		
		public int getRoll() {
			return roll;
		}
		
		public void addRecipe(MerchantRecipe recipe) {
			data.add(recipe);
		}
		
		public List<MerchantRecipe> getAllRecipe(){
			return data;
		}
		
		public List<MerchantRecipe> getRandomRecipe(){
			List<MerchantRecipe> recipes =  new ArrayList<MerchantRecipe>();
			if(data.isEmpty()) {
				return recipes;
			}
			recipes.addAll(data);
			Collections.shuffle(recipes);
			int size = data.size();
			for(int i = size;i > roll;i--) {
				recipes.remove(i - 1);
			}
			return recipes;
		}
		public void setRoll(int roll) {
			this.roll = roll;
		}
	}
	//implements Cloneable
	public static class ProfessionData {
		private Map<Integer,LevelData> data = new HashMap<Integer,LevelData>();
		private int version = 1;
		
		public ProfessionData(int version){
			this.version = version;
			for(int i = 1;i < 6;i++) {
				data.put(i, new LevelData(0));
			}
		}
		
		public void addLevel(int level,LevelData levelData) {
			data.put(level, levelData);
		}

		public void setVersion(int version) {
			this.version = version;
		}

		public int getVersion() {
			return version;
		}
		
		public List<LevelData> getAllLevel(){
			List<LevelData> result = new ArrayList<LevelData>();
			for(int i = 1;i < 6;i++) {
				LevelData ld = data.get(Integer.valueOf(i));
				result.add(ld);
			}
			return result;
		}
		
		public LevelData getLevelData(int level){
			LevelData ld = data.get(Integer.valueOf(level));
			return ld;
		}
		/*
		@Override
		public ProfessionData clone() {
			ProfessionData newData = new ProfessionData(version);
			for(int i = 1;i < 6;i++) {
				LevelData ld = data.get(Integer.valueOf(i));
				if(ld == null) {
					ld = new LevelData(0);
				}
				newData.addLevel(i,ld);
			}
			return newData;
		}*/
	}
}
