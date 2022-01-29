package com.github.redshirt53072.growthapi.item;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import com.github.redshirt53072.growthapi.server.GrowthPlugin;

public class ItemNBTLoader {
	private GrowthPlugin plugin;
	private PersistentDataContainer data;
	private ItemMeta meta;
	private ItemStack item;
	
    protected ItemNBTLoader(ItemStack item,GrowthPlugin plugin) {
    	this.item = item;
    	this.plugin = plugin;
    	updateMeta();
    }
    protected void writeInt(String keyword,int value) {
    	updateMeta();
    	NamespacedKey key = new NamespacedKey(plugin,keyword);
    	data.set(key, PersistentDataType.INTEGER, value);
    	confirmChange();
    }
    protected void writeDouble(String keyword,double value) {
    	updateMeta();
    	NamespacedKey key = new NamespacedKey(plugin,keyword);
    	data.set(key, PersistentDataType.DOUBLE, value);
    	confirmChange();
    }
    protected void writeString(String keyword,String value) {
    	updateMeta();
    	NamespacedKey key = new NamespacedKey(plugin,keyword);
    	data.set(key, PersistentDataType.STRING, value);
    	confirmChange();
    }
    protected Integer readInt(String keyword) {
    	updateMeta();
    	NamespacedKey key = new NamespacedKey(plugin,keyword);
    	return data.get(key, PersistentDataType.INTEGER);
    }
    protected Double readDouble(String keyword) {
    	updateMeta();
    	NamespacedKey key = new NamespacedKey(plugin,keyword);
    	return data.get(key, PersistentDataType.DOUBLE);
    }
    protected String readString(String keyword) {
    	updateMeta();
    	NamespacedKey key = new NamespacedKey(plugin,keyword);
    	return data.get(key, PersistentDataType.STRING);
    }
    protected boolean hasString(String keyword) {
    	updateMeta();
    	NamespacedKey key = new NamespacedKey(plugin,keyword);
    	return data.has(key, PersistentDataType.STRING);
    }
    
    protected void remove(String keyword) {
    	updateMeta();
    	NamespacedKey key = new NamespacedKey(plugin,keyword);
    	data.remove(key);	
    }
    protected List<String> getAllKey() {
    	updateMeta();
    	Set<NamespacedKey> keys = data.getKeys();
    	List<String> keyList = new ArrayList<String>();
    	keys.forEach(key ->{
    		keyList.add(key.getKey());
    	});
    	return keyList;
    }
    
    private void confirmChange() {
    	item.setItemMeta(meta);
    }
    private void updateMeta() {
        meta = item.getItemMeta();
        data = meta.getPersistentDataContainer();
    }
    
}
