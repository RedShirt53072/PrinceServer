package com.github.redshirt53072.survival.ench;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.persistence.PersistentDataType;

import com.github.redshirt53072.api.item.ItemBuilder;
import com.github.redshirt53072.api.item.ItemNBTLoader;
import com.github.redshirt53072.api.message.LogManager;
import com.github.redshirt53072.api.message.TextBuilder;
import com.github.redshirt53072.api.message.TextManager;
import com.github.redshirt53072.survival.GrowthSurvival;
import com.github.redshirt53072.survival.ench.EnchData.Ench;

public class EnchBookBuilder extends ItemNBTLoader implements EnchantBuilder{
	public static EnchBookBuilder getBuilder(ItemStack bookItem){
		if(bookItem.getType().equals(Material.ENCHANTED_BOOK)){
			return new EnchBookBuilder(bookItem);
		}
		return null;
	}
	
	private boolean isVanilla = false;
	private boolean isLock = false;
	private List<Enchant> enchList = new ArrayList<Enchant>();
	
	@SuppressWarnings("deprecation")
	private EnchBookBuilder(ItemStack item) {
		super(item.clone(),GrowthSurvival.getInstance());
		Integer version = super.readInt("version");
		if(version == null) {
			isVanilla = true;
		}
		Integer lock = super.readInt("enlock");
		if(lock != null) {
			if(lock == 1) {
				isLock = true;
			}
		}
		if(isVanilla) {
			EnchantmentStorageMeta enchMeta = (EnchantmentStorageMeta)meta;
			
			Map<Enchantment,Integer> data = enchMeta.getStoredEnchants();
			for(Enchantment ecm : data.keySet()) {
				int level = data.get(ecm);
				EnchData en = EnchManager.getEnchData(ecm.getName());
				if(en != null) {
					enchList.add(new Enchant(en, Math.min(level,en.getMaxLevel())));	
				}
				enchMeta.removeStoredEnchant(ecm);
			}
			super.item.setItemMeta(enchMeta);
		}else {
			List<String> keys = super.getKeys("bookench");
			for(String key : keys) {
				String rawData = super.readString(key);
				super.remove(key);
				List<String> data = TextManager.getSections(rawData);
				if(data.size() != 2){
					LogManager.logInfo(TextBuilder.quickBuild(ChatColor.WHITE, "アイテムのエンチャントのNBTの値が不正です。 データ:",rawData) , GrowthSurvival.getInstance(), Level.WARNING);
					continue;
				}
				String rawEnch = data.get(0);
				int level = Integer.valueOf(data.get(1));
				EnchData en = EnchManager.getEnchData(rawEnch);
				if(en != null) {
					enchList.add(new Enchant(en, Math.min(level,en.getMaxLevel())));	
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
			String rawPath = TextBuilder.plus("bookench",String.valueOf(super.getNextIndex("bookench", PersistentDataType.STRING)));
			super.writeString(rawPath, en.getRawData());
		}
		if(enchList.isEmpty()) {
			return new ItemBuilder(Material.BOOK).build();	
		}
		return new ItemBuilder(item).setHideFlags(ItemFlag.HIDE_ENCHANTS).setLore(lore).build();
	}

	public int removeEnchant(EnchData enchant){
		int index = this.getIndex(enchant.getType());
		if(index == -1) {
			return 0;		
		}
		
		int cost = enchList.get(index).getExpCost();
		enchList.remove(index);
		return cost;
	}

	public int addEnchant(Enchant enchant){
		
		EnchData ed = enchant.getType();
		int newLevel = Math.min(enchant.getLevel(), ed.getMaxLevel());
		
		int index = this.getIndex(ed.getType());
		if(index == -1) {
			//新しいエンチャの場合
			if(isExclusive(ed)){		
				return 0;
			}
			enchList.add(enchant);
			
			return enchant.getExpCost();
		}
		//既にあった場合
		Enchant origin = enchList.get(index);
		int oldLevel = origin.getLevel();
		if(oldLevel > newLevel) {
			
			return 0;
		}
		if(oldLevel == newLevel) {
			if(ed.getMaxLevel() <= newLevel) {
				return 0;
			}
			enchList.remove(index);
			deleteExclusive(ed);
			enchList.add(new Enchant(ed,oldLevel + 1));
			
			return ed.getExpCost(newLevel);
		}
		enchList.remove(index);
		deleteExclusive(ed);
		enchList.add(new Enchant(ed,newLevel));
		return ed.getExpCost(newLevel);	
	}
	private void deleteExclusive(EnchData ed) {
		for(Ench en : ed.getExclusive()) {
			int index = getIndex(en);
			if(index != -1) {
				enchList.remove(index);		
			}
		}
	}
	private boolean isExclusive(EnchData ed) {
		for(Ench en : ed.getExclusive()) {
			int index = getIndex(en);
			if(index != -1) {
				return true;
			}
		}
		return false;
	}
	
	private int getIndex(Ench enchant){
		for(int i = 0;i < enchList.size();i++){
			Enchant e = enchList.get(i);
			if(e.getType().getType().equals(enchant)){
				return i;
			}
		}
		return -1;
	}
	public List<Enchant> getEnchantList(){
		return enchList;
	}

	@Override
	public ItemStack getItem() {
		return item;
	}

	@Override
	public boolean isLocked() {
		return isLock;
	}
	@Override
	public void removeAll() {
		enchList.clear();
	}
}
