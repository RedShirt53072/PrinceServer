package com.github.redshirt53072.growthapi.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import com.github.redshirt53072.growthapi.server.GrowthPlugin;




public class PlayerNBTLoader {
	private GrowthPlugin plugin;
	private PersistentDataContainer data;
	
    public PlayerNBTLoader(Entity entity,GrowthPlugin plugin) {
    	this.plugin = plugin;
    	data = entity.getPersistentDataContainer();
    }
    
    public void writeInt(String keyword,int value) {
    	NamespacedKey key = new NamespacedKey(plugin,keyword);
    	data.set(key, PersistentDataType.INTEGER, value);
    }
    public void writeDouble(String keyword,double value) {
    	NamespacedKey key = new NamespacedKey(plugin,keyword);
    	data.set(key, PersistentDataType.DOUBLE, value);
    }
    public void writeString(String keyword,String value) {
    	NamespacedKey key = new NamespacedKey(plugin,keyword);
    	data.set(key, PersistentDataType.STRING, value);
    }
    public Integer readInt(String keyword) {
    	NamespacedKey key = new NamespacedKey(plugin,keyword);
    	return data.get(key, PersistentDataType.INTEGER);
    }
    public Double readDouble(String keyword) {
    	NamespacedKey key = new NamespacedKey(plugin,keyword);
    	return data.get(key, PersistentDataType.DOUBLE);
    }
    public String readString(String keyword) {
    	NamespacedKey key = new NamespacedKey(plugin,keyword);
    	return data.get(key, PersistentDataType.STRING);
    }
    public boolean hasString(String keyword) {
    	NamespacedKey key = new NamespacedKey(plugin,keyword);
    	return data.has(key, PersistentDataType.STRING);
    }
    
    public void remove(String keyword) {
    	NamespacedKey key = new NamespacedKey(plugin,keyword);
    	data.remove(key);	
    }
    public List<String> getAllKey() {
    	Set<NamespacedKey> keys = data.getKeys();
    	List<String> keyList = new ArrayList<String>();
    	keys.forEach(key ->{
    		keyList.add(key.getKey());
    	});
    	return keyList;
    }
}
