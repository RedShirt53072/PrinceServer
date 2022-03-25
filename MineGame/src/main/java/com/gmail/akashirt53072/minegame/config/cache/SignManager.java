package com.gmail.akashirt53072.minegame.config.cache;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;

import com.gmail.akashirt53072.minegame.Main;
import com.gmail.akashirt53072.minegame.config.ErrorLog;
import com.gmail.akashirt53072.minegame.config.datatype.LocationData;
import com.gmail.akashirt53072.minegame.enums.SignType;

public class SignManager {
	private Main plugin;
	public SignManager(Main plugin) {
		this.plugin = plugin;
	}
	
	public void showSignList(CommandSender sender) {
		ArrayList<LocationData> signData = plugin.getSignData();
		sender.sendMessage("--以下登録された看板のリスト--");
		for(LocationData sd : signData) {
			Location loc = sd.getLocation();
			sender.sendMessage("タイプ:" + sd.getId() + ",座標:" + loc.getX() + "," + loc.getY() + "," + loc.getZ());
		}
		sender.sendMessage("--以上登録された看板のリスト--");	
	}
	
	
	public SignType getSignType(int x,int y,int z) {
		ArrayList<LocationData> signData = plugin.getSignData();
		for(LocationData sd : signData) {
			Location loc = sd.getLocation();
			
			if(loc.getBlockX() == x && loc.getBlockY() == y && loc.getBlockZ() == z) {
				try{
					return SignType.valueOf(sd.getId());
				} catch(IllegalArgumentException ex) {
					new ErrorLog(plugin).writeError("SignManager.getSignType", "signのconfigのx=" + x + ",y=" + y + ",z=" + z + "で種類識別名が不正です");
					continue;
				}
			}
		}
		return SignType.NOTFOUND;
	}
}
