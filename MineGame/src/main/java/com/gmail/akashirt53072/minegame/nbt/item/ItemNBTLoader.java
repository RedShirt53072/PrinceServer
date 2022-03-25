package com.gmail.akashirt53072.minegame.nbt.item;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import com.gmail.akashirt53072.minegame.Main;



public class ItemNBTLoader {
	protected Main plugin;
	private PersistentDataContainer data;
	private ItemMeta meta;
	private ItemStack item;
	
    protected ItemNBTLoader(Main plugin,ItemStack item) {
    	this.plugin = plugin;
    	this.item = item;
        meta = item.getItemMeta();
        data = meta.getPersistentDataContainer();
    }
    protected void writeInt(String keyword,int value) {
    	NamespacedKey key = new NamespacedKey(plugin,keyword);
    	data.set(key, PersistentDataType.INTEGER, value);
    	confirmChange();
    }
    protected void writeDouble(String keyword,double value) {
    	NamespacedKey key = new NamespacedKey(plugin,keyword);
    	data.set(key, PersistentDataType.DOUBLE, value);
    	confirmChange();
    }
    protected void writeString(String keyword,String value) {
    	NamespacedKey key = new NamespacedKey(plugin,keyword);
    	data.set(key, PersistentDataType.STRING, value);
    	confirmChange();
    }
    protected int readInt(String keyword) {
    	NamespacedKey key = new NamespacedKey(plugin,keyword);
    	return data.get(key, PersistentDataType.INTEGER);
    }
    protected double readDouble(String keyword) {
    	NamespacedKey key = new NamespacedKey(plugin,keyword);
    	return data.get(key, PersistentDataType.DOUBLE);
    }
    protected String readString(String keyword) {
    	NamespacedKey key = new NamespacedKey(plugin,keyword);
    	return data.get(key, PersistentDataType.STRING);
    }
    private void confirmChange() {
    	item.setItemMeta(meta);
    }
}
