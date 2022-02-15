package com.github.redshirt53072.growthapi.item;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemBuilder {
	private ItemStack item;
	private ItemMeta itemMeta;
	public ItemBuilder(Material material){
		item = new ItemStack(material);
		itemMeta = item.getItemMeta();
	}
	public ItemBuilder(ItemStack item){
		this.item = item;
		itemMeta = item.getItemMeta();
	}
	public ItemBuilder setName(String name) {
		itemMeta.setDisplayName(Color.WHITE + name);
		return this;
	}
	public ItemBuilder setAmount(int amount) {
		item.setAmount(amount);
		return this;
	}
	public ItemBuilder setLore(List<String> lore) {
		itemMeta.setLore(lore);
		return this;
	}
	public ItemBuilder addEnchant(Enchantment ench,int level) {
		itemMeta.addEnchant(ench, level, false);
		return this;
	}
	public ItemBuilder setModelData(int modelNumber) {
		itemMeta.setCustomModelData(modelNumber);
		return this;
	}
	
	public ItemBuilder setHideFlags(ItemFlag...flags) {
		itemMeta.addItemFlags(flags);
		return this;
	}
	
	public ItemStack build(){
		item.setItemMeta(itemMeta);
		return item;
	}
	
	public static List<String> buildLore(String...strings){
		List<String> list = new ArrayList<String>();
		for(String st : strings){
			list.add(st);
		}
		return list;
	}
	
	public static ItemStack quickBuild(Material material,int amount,String name){
		return new ItemBuilder(material).setName(name).setAmount(amount).build();
	}
}