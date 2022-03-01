package com.github.redshirt53072.growthapi.player;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import com.github.redshirt53072.growthapi.server.GrowthPlugin;

/**
 * @see できればプレイヤーNBTよりMySQLデータベース使ってください
 * @author akash
 */
public class PlayerNBTLoader {
	private GrowthPlugin plugin;
	private PersistentDataContainer data;
	
    protected PlayerNBTLoader(Entity entity,GrowthPlugin plugin) {
    	this.plugin = plugin;
    	data = entity.getPersistentDataContainer();
    }
    
    protected void writeInt(String keyword,int value) {
    	NamespacedKey key = new NamespacedKey(plugin,keyword);
    	data.set(key, PersistentDataType.INTEGER, value);
    }
    protected void writeIntArray(String keyword,int[] value) {
    	NamespacedKey key = new NamespacedKey(plugin,keyword);
    	data.set(key, PersistentDataType.INTEGER_ARRAY,value);
    }
    protected void writeDouble(String keyword,double value) {
    	NamespacedKey key = new NamespacedKey(plugin,keyword);
    	data.set(key, PersistentDataType.DOUBLE, value);
    }
    protected void writeString(String keyword,String value) {
    	NamespacedKey key = new NamespacedKey(plugin,keyword);
    	data.set(key, PersistentDataType.STRING, value);
    }
    protected Integer readInt(String keyword) {
    	NamespacedKey key = new NamespacedKey(plugin,keyword);
    	return data.get(key, PersistentDataType.INTEGER);
    }
    protected int[] readIntArray(String keyword) {
    	NamespacedKey key = new NamespacedKey(plugin,keyword);
    	return data.get(key, PersistentDataType.INTEGER_ARRAY);
    }
    
    protected Double readDouble(String keyword) {
    	NamespacedKey key = new NamespacedKey(plugin,keyword);
    	return data.get(key, PersistentDataType.DOUBLE);
    }
    protected String readString(String keyword) {
    	NamespacedKey key = new NamespacedKey(plugin,keyword);
    	return data.get(key, PersistentDataType.STRING);
    }
    protected boolean hasString(String keyword) {
    	NamespacedKey key = new NamespacedKey(plugin,keyword);
    	return data.has(key, PersistentDataType.STRING);
    }
    
    protected void remove(String keyword) {
    	NamespacedKey key = new NamespacedKey(plugin,keyword);
    	data.remove(key);	
    }
    protected List<String> getAllKey() {
    	Set<NamespacedKey> keys = data.getKeys();
    	List<String> keyList = new ArrayList<String>();
    	keys.forEach(key ->{
    		keyList.add(key.getKey());
    	});
    	return keyList;
    }
}
