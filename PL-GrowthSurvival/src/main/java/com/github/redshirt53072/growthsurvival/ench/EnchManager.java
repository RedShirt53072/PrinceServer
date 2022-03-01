package com.github.redshirt53072.growthsurvival.ench;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import com.github.redshirt53072.growthapi.item.ItemBuilder;
import com.github.redshirt53072.growthapi.item.ItemNBTLoader;
import com.github.redshirt53072.growthapi.message.LogManager;
import com.github.redshirt53072.growthapi.message.TextBuilder;
import com.github.redshirt53072.growthapi.message.TextManager;
import com.github.redshirt53072.growthsurvival.GrowthSurvival;
import com.github.redshirt53072.growthsurvival.config.EnchConfig;
import com.github.redshirt53072.growthsurvival.config.ScrapConfig;
import com.github.redshirt53072.growthsurvival.config.ToolConfig;
import com.github.redshirt53072.growthsurvival.ench.EnchData.Ench;
import com.github.redshirt53072.growthsurvival.ench.EnchData.ScrapData;
import com.github.redshirt53072.growthsurvival.ench.ToolData.ToolGroup;
import com.github.redshirt53072.growthsurvival.ench.ToolData.ToolGroupData;

public class EnchManager extends ItemNBTLoader{
	private static List<EnchData> enchData = new ArrayList<EnchData>();
	private static List<ToolGroupData> toolGroup = new ArrayList<ToolGroupData>();
	private static List<ToolData> toolList = new ArrayList<ToolData>();
	
	private static List<ScrapData> scrapData = new ArrayList<ScrapData>();
	
	public static void reload() {
		enchData = EnchConfig.getAllData();
		scrapData = ScrapConfig.getAllData();
		toolGroup = ToolConfig.getToolGroup();
		toolList = ToolConfig.getToolList();
	}
	
	public static EnchData getEnchData(String id){
		for(EnchData ed : enchData) {
			if(ed.getType().getID().equals(id)){
				return ed;
			}
		}
		return null;
	}
	
	public static ToolData getToolData(Material mate){
		for(ToolData td : toolList) {
			if(td.getType().equals(mate)){
				return td;
			}
		}
		return null;
	}
	
	public static ToolGroupData getToolGroup(ToolGroup group){
		for(ToolGroupData tg : toolGroup) {
			if(tg.getID().equals(group)){
				return tg;
			}
		}
		return null;
	}
	
	public static List<EnchData> getEnchList(){
		return enchData;
	}
	public static List<ToolData> getToolList(){
		return toolList;
	}
	public static List<ToolGroupData> getGroupList(){
		return toolGroup;
	}

	public static List<ScrapData> getScrapList(){
		return scrapData;
	}
	
	//アイテムNBT
	private boolean isVanilla = false;
	private List<Enchant> enchList = new ArrayList<Enchant>();
	
	public EnchManager(ItemStack item) {
		super(item.clone(),GrowthSurvival.getInstance());
		Integer version = super.readInt("version");
		if(version == null) {
			isVanilla = true;
		}
		
		if(isVanilla) {
			Map<Enchantment,Integer> data = meta.getEnchants();
			for(Enchantment ecm : data.keySet()) {
				int level = data.get(ecm);
				EnchData en = EnchManager.getEnchData(ecm.toString());
				if(en != null) {
					enchList.add(new Enchant(en, level));	
				}
			}
		}else {
			List<String> keys = super.getKeys("ench");
			for(String key : keys) {
				String rawData = super.readString(key);
				List<String> data = TextManager.getSections(rawData);
				if(data.size() != 2){
					LogManager.logInfo(TextBuilder.quickBuild(ChatColor.WHITE, "アイテムのエンチャントのNBTの値が不正です。 データ:",rawData) , GrowthSurvival.getInstance(), Level.WARNING);
					continue;
				}
				String rawEnch = data.get(0);
				int level = Integer.valueOf(data.get(1));
				EnchData en = EnchManager.getEnchData(rawEnch);
				if(en != null) {
					enchList.add(new Enchant(en, level));	
				}
			}
		}
		
		
	}
	
	public ItemStack build() {
		super.writeInt("version", 1);
		isVanilla = false;
		List<String> lore = new ArrayList<String>();
		for(Enchant en : enchList){
			lore.add(en.getText());
			int level = en.getLevel();
			Enchantment enc = en.getType().getType().getEnch();
			if(enc != null) {
				item.addEnchantment(enc, level);
			}
		}
		return new ItemBuilder(item).setHideFlags(ItemFlag.HIDE_ENCHANTS).setLore(lore).build();
	}
	public boolean removeEnchant(Ench enchant){
		for(int i = 0;i < enchList.size();i++){
			Enchant e = enchList.get(i);
			if(e.getType().getType().equals(enchant)){
				enchList.remove(i);
				return true;
			}
		}
		return false;
	}
	public boolean addEnchant(Enchant enchant){
		int level = enchant.getLevel();
		EnchData ed = enchant.getType();
		if(ed.getMaxLevel() < level) {
			return false;
		}
		Ench type = ed.getType();
		for(Enchant e : enchList){
			if(e.getType().getType().equals(type)){
				return false;
			}
		}
		for(Ench e : ed.getCantExist()){
			if(e.equals(type)){
				return false;
			}
		}
		enchList.add(enchant);
		return true;
	}
	
	public List<Enchant> getEnchantList(){
		return enchList;
	}
}
