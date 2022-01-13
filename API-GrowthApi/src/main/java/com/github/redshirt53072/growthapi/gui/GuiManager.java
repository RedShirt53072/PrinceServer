package com.github.redshirt53072.growthapi.gui;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import com.github.redshirt53072.growthapi.BaseAPI;
import com.github.redshirt53072.growthapi.message.LogManager;
import com.github.redshirt53072.growthapi.server.EmergencyListener;



public final class GuiManager implements EmergencyListener{
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
			remove(p);
		}
	}
	
	public static void close(Player p) {
		Gui gui = getGui(p);
		if(gui != null) {
			gui.close();
			remove(p);
		}else {
			LogManager.logError("[GuiApi]指定されたプレイヤーはGUIを開いていないため、閉じられませんでした。", BaseAPI.getInstance(), new Throwable(), Level.WARNING);
		}
	}
	@Override
	public void onEmergency() {
		data.forEach((player,gui) -> {
			gui.close();
		});
		data.clear();
	}
	
	
	public static void remove(Player player) {
		data.remove(player);
	}
	
}
