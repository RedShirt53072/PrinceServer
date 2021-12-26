package com.github.redshirt53072.economyapi.item;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import com.github.redshirt53072.baseapi.server.GrowthApi;



public class ItemNBTLoader {
	private GrowthApi plugin;
	private PersistentDataContainer data;
	private ItemMeta meta;
	private ItemStack item;
	
    public ItemNBTLoader(ItemStack item,GrowthApi plugin) {
    	this.item = item;
    	this.plugin = plugin;
    	updateMeta();
    }
    public void writeInt(String keyword,int value) {
    	updateMeta();
    	NamespacedKey key = new NamespacedKey(plugin,keyword);
    	data.set(key, PersistentDataType.INTEGER, value);
    	confirmChange();
    }
    public void writeDouble(String keyword,double value) {
    	updateMeta();
    	NamespacedKey key = new NamespacedKey(plugin,keyword);
    	data.set(key, PersistentDataType.DOUBLE, value);
    	confirmChange();
    }
    public void writeString(String keyword,String value) {
    	updateMeta();
    	NamespacedKey key = new NamespacedKey(plugin,keyword);
    	data.set(key, PersistentDataType.STRING, value);
    	confirmChange();
    }
    public Integer readInt(String keyword) {
    	updateMeta();
    	NamespacedKey key = new NamespacedKey(plugin,keyword);
    	return data.get(key, PersistentDataType.INTEGER);
    }
    public Double readDouble(String keyword) {
    	updateMeta();
    	NamespacedKey key = new NamespacedKey(plugin,keyword);
    	return data.get(key, PersistentDataType.DOUBLE);
    }
    public String readString(String keyword) {
    	updateMeta();
    	NamespacedKey key = new NamespacedKey(plugin,keyword);
    	return data.get(key, PersistentDataType.STRING);
    }
    public boolean hasString(String keyword) {
    	updateMeta();
    	NamespacedKey key = new NamespacedKey(plugin,keyword);
    	return data.has(key, PersistentDataType.STRING);
    }
    
    public void remove(String keyword) {
    	updateMeta();
    	NamespacedKey key = new NamespacedKey(plugin,keyword);
    	data.remove(key);	
    }
    public List<String> getAllKey() {
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
