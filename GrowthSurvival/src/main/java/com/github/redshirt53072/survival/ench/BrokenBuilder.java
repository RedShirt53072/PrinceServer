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
import org.bukkit.persistence.PersistentDataType;

import com.github.redshirt53072.api.item.ItemBuilder;
import com.github.redshirt53072.api.item.ItemNBTLoader;
import com.github.redshirt53072.api.message.LogManager;
import com.github.redshirt53072.api.message.TextBuilder;
import com.github.redshirt53072.api.message.TextManager;
import com.github.redshirt53072.survival.GrowthSurvival;
import com.github.redshirt53072.survival.ench.EnchData.Ench;
import com.github.redshirt53072.survival.ench.ToolData.ToolGroupData;

public class BrokenBuilder extends ItemNBTLoader{
	
	private List<Enchant> enchList = new ArrayList<Enchant>();
	private boolean isVanilla = false;
	private Material material = null;
	
	@SuppressWarnings("deprecation")
	public BrokenBuilder(ItemStack item,ToolGroupData tgd) {
		super(item.clone(),GrowthSurvival.getInstance());
		material = item.getType();
		super.item.setType(Material.CHAIN_COMMAND_BLOCK);
		
		Integer version = super.readInt("version");
		if(version == null) {
			isVanilla = true;
		}
		if(isVanilla) {
			Map<Enchantment,Integer> data = meta.getEnchants();
			for(Enchantment ecm : data.keySet()) {
				int level = data.get(ecm);
				EnchData en = EnchManager.getEnchData(ecm.getName());
				if(en != null) {
					Ench type = en.getType();
					for(Ench canEnch : tgd.getEnchData()) {
						if(canEnch.equals(type)){
							enchList.add(new Enchant(en, Math.min(level,en.getMaxLevel())));	
							break;
						}
					}
				}
				meta.removeEnchant(ecm);
			}
			super.item.setItemMeta(meta);
		}else {
			Map<Enchantment,Integer> vanillaData = meta.getEnchants();
			for(Enchantment ecm : vanillaData.keySet()) {
				meta.removeEnchant(ecm);
			}
			super.item.setItemMeta(meta);
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
	}
	
	public ItemStack build() {
		super.writeInt("version", 1);
		super.writeString("brokentooltype", material.name());
		isVanilla = false;
		List<String> lore = new ArrayList<String>();
		for(Enchant en : enchList){
			lore.add(en.getText());
			String rawPath = TextBuilder.plus("ench",String.valueOf(super.getNextIndex("ench", PersistentDataType.STRING)));
			super.writeString(rawPath, en.getRawData());
			
		}
		lore.add(TextBuilder.quickBuild(ChatColor.RED, "<修理が必要です>"));
		if(enchList.isEmpty()) {
			return new ItemBuilder(item).setLore(lore).build();
		}
		
		return new ItemBuilder(item).setHideFlags(ItemFlag.HIDE_ENCHANTS).setLore(lore).build();
	}
	
}
