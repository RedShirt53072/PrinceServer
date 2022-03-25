package com.gmail.akashirt53072.minegame.gui;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.gmail.akashirt53072.minegame.Main;
import com.gmail.akashirt53072.minegame.config.ErrorLog;


public class GuiManager {
	protected  Main plugin;
	protected ArrayList<Gui> data;
	public GuiManager(Main plugin) {
	    this.plugin = plugin;
	    this.data = plugin.getGuiData();
	}
	
	
	
	
	public Gui openPublicGui(GuiID type,Player player) {
		Gui old = getGui(player);
		if(old != null) {
			old.close(player);	
		}
		
		Gui gui = null;
		for(Gui g : data) {
			if(g.getType().equals(type)){
				gui = g;
				break;
			}
		}
		
		if(gui == null) {
			switch(type) {
			case SELECTMAP:
				gui = new SelectMapGui(plugin,false);
				break;
			default:
				//エラー
				new ErrorLog(plugin).writeError("GuiManager.openPrivateGui", type + "は未定義のGUIです。");
				return null;
			}
			data.add(gui);
		}
		
		gui.addPlayer(player);
		return gui;
	}
	
	public Gui openPrivateGui(GuiID type,Player player) {
		Gui old = getGui(player);
		if(old != null) {
			old.close(player);	
		}
		Gui gui = null;
		switch(type) {
		case SELECTMAP:
			gui = new SelectMapGui(plugin,true);
			break;
		default:
			//エラー
			new ErrorLog(plugin).writeError("GuiManager.openPrivateGui", type + "は未定義のGUIです。");
			return null;
		}

		data.add(gui);
		gui.addPlayer(player);
		return gui;
	}
	
	private Gui getGui(Player player) {
		for(Gui g : data) {
			for(Player p : g.getPlayers()) {
				if(p.equals(player)){
					return g;
				}
			}
		}
		return null;
	}
	
	public boolean onClick(Player p,int slot,ItemStack cursor) {
		Gui gui = getGui(p);
		if(gui != null) {
			return gui.onClick(slot,cursor,p);
		}
		return false;
	}
	
	public void onClose(Player p) {
		Gui gui = getGui(p);
		if(gui != null) {
			gui.onClose(p);
		}
	}
	
	public void close(Player p) {
		Gui gui = getGui(p);
		if(gui != null) {
			gui.close(p);
		}else {
			//エラー
			new ErrorLog(plugin).writeError("GuiManager.close", p.getName() + "はGUIを開いていません。");
		}
	}
	public void onEmergency(Gui gui) {
		for(Player p : gui.getPlayers()) {
			close(p);
		}
	}
	
	
	public void remove(Gui gui) {
		gui.onRemove();
		data.remove(gui);
	}
	
}
