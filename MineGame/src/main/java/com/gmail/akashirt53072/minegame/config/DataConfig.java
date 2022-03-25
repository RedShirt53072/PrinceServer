package com.gmail.akashirt53072.minegame.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;

public class DataConfig {
	private FileConfiguration config;
	private ConfigLoader configLoader;
	protected final Plugin plugin;

	protected DataConfig(Plugin plugin,String filePath) {
		this.plugin = plugin;
		configLoader = new ConfigLoader(plugin,filePath);
		config = configLoader.getConfig();
	}
	
	protected int getNextIndex(String path){
		int i = 1;
		while(config.contains(path + i)) {
			i ++;
		}
        return i;
    }
	
	protected String getStringData(String path){
		if (config.contains(path)) {
			return config.getString(path);
        }else {
        	plugin.getLogger().warning("[error]" + path + "にデータは存在しません");
        }
        return null;
    }
	protected int getIntData(String path){
		if (config.contains(path)) {
            return config.getInt(path);
        }else {
        	plugin.getLogger().warning("[error]" + path + "にデータは存在しません");
        }
        return -1;
    }
	
	protected float getFloatData(String path){
		if (config.contains(path)) {
            return (float)config.get(path);
        }else {
        	plugin.getLogger().warning("[error]" + path + "にデータは存在しません");
        }
        return -1;
    }
	
	protected Inventory getInventoryData(String path){
		if (config.contains(path)) {
			Object o = config.get(path);
			if(o instanceof Inventory) {
				return (Inventory)o;	
			}else {
				plugin.getLogger().warning("[error]" + path + "のデータはInventory型ではありません");
			}
        }else {
        	plugin.getLogger().warning("[error]" + path + "にデータは存在しません");
        }
        return null;
    }
	
	protected boolean containData(String path){
		return config.contains(path,true);
    }
	
	protected void setData(String path,Object value){
		config.set(path,value);
        configLoader.saveConfig();
    }
}
