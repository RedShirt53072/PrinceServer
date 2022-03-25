package com.github.redshirt53072.survival.ench;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import com.github.redshirt53072.api.item.ItemBuilder;
import com.github.redshirt53072.api.item.ItemNBTLoader;
import com.github.redshirt53072.api.message.LogManager;
import com.github.redshirt53072.api.message.TextBuilder;
import com.github.redshirt53072.api.message.TextManager;
import com.github.redshirt53072.survival.GrowthSurvival;
import com.github.redshirt53072.survival.ench.EnchData.Ench;
import com.github.redshirt53072.survival.ench.ToolData.ToolGroupData;

public class BrokenReader extends ItemNBTLoader implements EnchantBuilder{
	
	public static BrokenReader getBuilder(ItemStack item,boolean isBroken) {
		BrokenReader beb = new BrokenReader(item,isBroken);
		if(beb.tgd == null){
			return null;
		}
		return beb;
	}
	
	private List<Enchant> enchList = new ArrayList<Enchant>();
	private ToolGroupData tgd = null;
	private ToolData td = null;
	private Material material = null;
	private boolean isBroken;
	private boolean isLock = false;
	
	private BrokenReader(ItemStack item,boolean isBroken) {
		super(item.clone(),GrowthSurvival.getInstance());
		this.isBroken = isBroken;
		String rawTool = super.readString("brokentooltype");
		if(rawTool == null) {
			return;
		}
		material = Material.getMaterial(rawTool);
		if(material == null) {
			return;
		}
		ToolData td = EnchManager.getToolData(material);
		if(td == null) {
			return;
		}
		this.td = td;
		ToolGroupData readTgd = td.getToolGroupData();
		if(readTgd == null) {
			return;
		}
		Integer version = super.readInt("version");
		if(version == null) {
			return;
		}

		Integer lock = super.readInt("enlock");
		if(lock != null) {
			if(lock == 1) {
				isLock = true;
			}
		}
		
		this.tgd = readTgd;
		
		List<String> keys = super.getKeys("ench");
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
				Ench type = en.getType();
				for(Ench canEnch : tgd.getEnchData()) {
					if(canEnch.equals(type)){
						enchList.add(new Enchant(en, Math.min(level,en.getMaxLevel())));	
						break;
					}
				}
			}
		}
	}
	
	public ItemStack build() {
		if(isBroken) {
			return buildBroken();
		}else {
			return buildRepaired();
		}
	}
	
	public ItemStack buildRepaired() {
		item.setType(material);
		super.remove("brokentooltype");
		List<String> lore = new ArrayList<String>();
		for(Enchant en : enchList){
			lore.add(en.getText());
			String rawPath = TextBuilder.plus("ench",String.valueOf(super.getNextIndex("ench", PersistentDataType.STRING)));
			super.writeString(rawPath, en.getRawData());
			int level = en.getLevel();
			Enchantment enc = en.getType().getType().getEnch();
			if(enc != null) {
				item.addUnsafeEnchantment(enc, level);
			}
		}
		if(isLock) {
			lore.add(TextBuilder.quickBuild(ChatColor.RED, "<エンチャント編集不可アイテム>"));
		}
		if(enchList.isEmpty()) {
			super.remove("version");
			return new ItemBuilder(item).setLore(lore).removeHideFlags(ItemFlag.HIDE_ENCHANTS).build();		
		}
		super.writeInt("version", 1);
		return new ItemBuilder(item).setHideFlags(ItemFlag.HIDE_ENCHANTS).setLore(lore).build();		
	}
	public ItemStack buildBroken() {
		super.writeInt("version", 1);
		List<String> lore = new ArrayList<String>();
		for(Enchant en : enchList){
			lore.add(en.getText());
			String rawPath = TextBuilder.plus("ench",String.valueOf(super.getNextIndex("ench", PersistentDataType.STRING)));
			super.writeString(rawPath, en.getRawData());
		}
		lore.add(TextBuilder.quickBuild(ChatColor.RED, "<修理が必要です>"));
		if(isLock) {
			lore.add(TextBuilder.quickBuild(ChatColor.RED, "<エンチャント編集不可アイテム>"));
		}
		if(enchList.isEmpty()) {
			return new ItemBuilder(item).setLore(lore).build();
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
		Ench type = ed.getType();
		
		
		int index = this.getIndex(ed.getType());
		if(index == -1) {
			//新しいエンチャの場合
			
			for(Ench canEnch : tgd.getEnchData()) {
				if(canEnch.equals(type)){
					if(isExclusive(ed)){
						return 0;
					}
					enchList.add(enchant);
					
					return enchant.getExpCost();
				}
			}
			return 0;
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
	
	public ToolData getToolData() {
		return td;
	}
	
	public ItemStack getItem() {
		return item;
	}
	
	public List<Enchant> getEnchantList(){
		return enchList;
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
