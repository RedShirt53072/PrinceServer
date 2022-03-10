package com.github.redshirt53072.api.util;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.logging.Level;

import javax.xml.bind.DatatypeConverter;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import com.github.redshirt53072.api.BaseAPI;
import com.github.redshirt53072.api.message.LogManager;


public class Serializer {
	public static ItemStack toItem(JsonObject data) {
		YamlConfiguration config = new YamlConfiguration();
	    ItemStack item = null;
		try {
		    String raw = data.get("data").getAsString();
	        config.loadFromString(new String(DatatypeConverter.parseBase64Binary(raw), StandardCharsets.UTF_8));
	        item = config.getItemStack("i", null);
	    } catch (Exception e) {
	        LogManager.logError("アイテムのデシリアライズに失敗しました。", BaseAPI.getInstance(), e, Level.WARNING);
	    }
	    return item;
	}
	
	public static Integer getIndex(JsonObject data) {
		try {
		    return data.get("index").getAsInt();
		} catch (Exception e) {
	        LogManager.logError("アイテムのデシリアライズに失敗しました。", BaseAPI.getInstance(), e, Level.WARNING);
	    }
		return null;
	}
	public static void toInventory(JsonArray data,PlayerInventory inv) {
		inv.clear();
		
        for (int i = 0; i < data.size(); i++) {
        	try {
            	JsonObject jo = data.get(i).getAsJsonObject();
                ItemStack item = toItem(jo);
                Integer index = getIndex(jo);
                inv.setItem(index, item);
    		}catch(Exception ex) {
    			LogManager.logError("インベントリデータのデシリアライズに失敗しました。", BaseAPI.getInstance(),ex, Level.WARNING);
    		}
        }
	}
	public static void toGui(String rawData,Inventory inv,int min,int max) {
		JsonArray data = null;
		try {
			data = new Gson().fromJson(rawData,JsonArray.class);
		}catch(Exception ex) {
			LogManager.logError("GUIデータのデシリアライズに失敗しました。", BaseAPI.getInstance(),ex, Level.WARNING);
			return;
		}
		for (int i = 0; i < data.size(); i++) {
        	try {
            	JsonObject jo = data.get(i).getAsJsonObject();
                ItemStack item = toItem(jo);
                Integer index = getIndex(jo);
                if(min > index) {
                	continue;
                }
                if(max < index) {
                	continue;
                }
                inv.setItem(index, item);
    		}catch(Exception ex) {
    			LogManager.logError("GUIデータのデシリアライズに失敗しました。", BaseAPI.getInstance(),ex, Level.WARNING);
    		}
        }
	}
	
	public static PotionEffect toEffect(JsonObject data) {
		PotionEffect effect = null;
		try {
		    PotionEffectType type = PotionEffectType.getByName(data.get("type").getAsString());
		    int duration = data.get("duration").getAsInt();
		    int amplifier = data.get("amplifier").getAsInt();
		    
		    return new PotionEffect(type,duration , amplifier);
		} catch (Exception e) {
	        LogManager.logError("アイテムのデシリアライズに失敗しました。", BaseAPI.getInstance(), e, Level.WARNING);
	    }
	    return effect;
	}
	
	public static void toPotion(JsonArray data,Player player) {
        for (int i = 0; i < data.size(); i++) {
        	try {
            	JsonObject jo = data.get(i).getAsJsonObject();
                PotionEffect effect = toEffect(jo);
                player.addPotionEffect(effect);
    		}catch(Exception ex) {
    			LogManager.logError("ポーションエフェクトデータのデシリアライズに失敗しました。", BaseAPI.getInstance(),ex, Level.WARNING);
    		}
        }
	}
	
	public static void toPlayer(String data,Player player) {
		JsonObject jo = new JsonObject();
		try {
			jo = new Gson().fromJson(data,JsonObject.class);
			toInventory(jo.get("inv").getAsJsonArray(),player.getInventory());
			toPotion(jo.get("potion").getAsJsonArray(),player);
			
			player.setHealth(jo.get("health").getAsDouble());
			player.setFoodLevel(jo.get("food").getAsInt());
			player.setTotalExperience(jo.get("exp").getAsInt());
			player.setSaturation(jo.get("satur").getAsInt());
		}catch(Exception ex) {
			LogManager.logError("プレイヤーデータのデシリアライズに失敗しました。", BaseAPI.getInstance(),ex, Level.WARNING);
		}
	}
	
	
	public static JsonObject toJson(ItemStack itemStack,int index) {
	    YamlConfiguration config = new YamlConfiguration();
	    config.set("i", itemStack);
	    String str = DatatypeConverter.printBase64Binary(config.saveToString().getBytes(StandardCharsets.UTF_8));
	    JsonObject jo = new JsonObject();
	    jo.addProperty("index", index);
	    jo.addProperty("data", str);
	    return jo;
	}
	public static String toJson(Inventory inv,int min,int max) {
        JsonArray inventory = new JsonArray();
        for (int i = 0; i < inv.getSize(); i++) {
        	if(min > i) {
            	continue;
            }
            if(max < i) {
            	break;
            }
        	ItemStack item = inv.getItem(i);
        	if (item != null) {
        		JsonObject values = toJson(item, i);
                inventory.add(values);
            }
        }
        return new Gson().toJson(inventory);
    }
	public static JsonArray toJson(PlayerInventory inv) {
        JsonArray inventory = new JsonArray();
        for (int i = 0; i < inv.getSize(); i++) {
            ItemStack item = inv.getItem(i);
        	if (item != null) {
        		JsonObject values = toJson(item, i);
                inventory.add(values);
            }
        }
        return inventory;
    }
	
	public static JsonObject toJson(PotionEffect effect) {
		JsonObject jo = new JsonObject();
		jo.addProperty("type",effect.getType().toString());
		jo.addProperty("duration",effect.getDuration());
	    jo.addProperty("amplifier",effect.getAmplifier());
	    return jo;
	}
	
	public static JsonArray toJson(Collection<PotionEffect> potions) {
		JsonArray ja = new JsonArray();
		for(PotionEffect pe : potions){
    		ja.add(toJson(pe));
    	}
		return ja;
	}

	public static String toJson(Player player) {
		JsonObject jo = new JsonObject();
		jo.add("inv", toJson(player.getInventory()));
		jo.add("potion",toJson(player.getActivePotionEffects()));
		
		jo.addProperty("health",player.getHealth());
		jo.addProperty("food",player.getFoodLevel());
		jo.addProperty("exp",player.getTotalExperience());
		jo.addProperty("satur",player.getSaturation());
	
		return new Gson().toJson(jo);
	}
}
