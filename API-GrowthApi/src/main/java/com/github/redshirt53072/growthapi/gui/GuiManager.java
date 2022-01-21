package com.github.redshirt53072.growthapi.gui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import com.github.redshirt53072.growthapi.BaseAPI;
import com.github.redshirt53072.growthapi.server.EmergencyListener;



public final class GuiManager implements EmergencyListener{
	private static Map<Player,Gui> data = new HashMap<Player,Gui>();
	private static List<Player> notClosePlayers = new ArrayList<Player>();
	
	
	public static void openGui(Gui newGui,Player player) {
		Gui old = getGui(player);
		if(old != null) {
			//BaseAPI.getInstance().getLogger().log(Level.INFO,"close old");
			old.close();
		}
		//BaseAPI.getInstance().getLogger().log(Level.INFO,"add");
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
		if(notClosePlayers.contains(p)){
			notClosePlayers.remove(p);
			//BaseAPI.getInstance().getLogger().log(Level.INFO,"block");
			return;
		}
		Gui gui = getGui(p);
		if(gui != null) {
			//BaseAPI.getInstance().getLogger().log(Level.INFO,"onclose1");
			remove(p);
			gui.onClose();

			//BaseAPI.getInstance().getLogger().log(Level.INFO,"onclose2");
		}
	}
	
	public static void addNotClose(Player p) {
		notClosePlayers.add(p);
	}
	
	@Override
	public void onEmergency() {
		Collection<Gui> guiCols = data.values();
		List<Gui> guis = new ArrayList<Gui>();
		guis.addAll(guiCols);
		for(int i = guis.size() - 1;i > -1;i--) {
			guis.get(i).onEmergency();
		}
		data.clear();
	}
	
	
	public static void remove(Player player) {
		//BaseAPI.getInstance().getLogger().log(Level.INFO,"remove");
		
		data.remove(player);
	}
	
}
