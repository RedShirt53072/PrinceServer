package com.github.redshirt53072.trade.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.inventory.MerchantRecipe;

import com.github.redshirt53072.api.player.PlayerNBTLoader;
import com.github.redshirt53072.trade.TradeManager;
import com.github.redshirt53072.trade.data.TradeConfig.ProfessionData;

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
			recipes.addAll(profData.getLevelData(i).getRandomRecipe());
		}			
		return recipes;
	}
	
	public static ProfessionData getProfessionData(Profession prof){
		return data.get(prof);
	}
	
	public static int setProfessionData(Profession prof,ProfessionData profData){
		int version = data.get(prof).getVersion() + 1;
		profData.setVersion(version);
		data.put(prof, profData);
		TradeConfig.saveProfession(prof, profData);
		return version;
	}
	
	public static int getTableVersion(Profession prof){
		return data.get(prof).getVersion();
	}
	
	public VillagerManager(Villager entity) {
    	super(entity,TradeManager.getInstance());
    }

	public void setLevel(int level) {
    	writeInt("level",level);
    }
    
    public int getLevel() {
    	Integer level = readInt("level");
    	if(level == null) {
        	return 0;
    	}
    	return level;
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
