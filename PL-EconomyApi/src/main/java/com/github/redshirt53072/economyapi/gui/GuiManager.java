package com.github.redshirt53072.economyapi.gui;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import com.github.redshirt53072.baseapi.message.LogManager;
import com.github.redshirt53072.economyapi.general.EconomyApi;



public class GuiManager {
	private static Map<Player,Gui> data = new HashMap<Player,Gui>();
	
	
	public static void openGui(Gui newGui,Player player) {
		Gui old = getGui(player);
		if(old != null) {
			old.close();	
		}
		data.put(player,newGui);
		return;
	}
	
	private static Gui getGui(Player player) {
		return data.get(player); 
	}
	
	public static void onClick(InventoryClickEvent event) {
		Gui gui = getGui((Player)event.getWhoClicked());
		if(gui != null) {
			boolean cancel = gui.onClick(event);
			if(cancel) {
				event.setCancelled(true);
			}
		}
	}
	
	public static void onClose(Player p) {
		Gui gui = getGui(p);
		if(gui != null) {
			gui.onClose(p);
			remove(p,gui);
		}
	}
	
	public static void close(Player p) {
		Gui gui = getGui(p);
		if(gui != null) {
			gui.close();
			remove(p,gui);
		}else {
			LogManager.logError("[GuiApi]指定されたプレイヤーはGUIを開いていないため、閉じられませんでした。", EconomyApi.getInstance(), new Throwable(), Level.WARNING);
		}
	}
	public static void onEmergency() {
		data.forEach((player,gui) -> {
			gui.close();
		});
		data.clear();
	}
	
	
	public static void remove(Player player,Gui gui) {
		data.remove(player);
	}
	
}
