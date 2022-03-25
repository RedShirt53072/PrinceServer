package com.gmail.akashirt53072.minegame.config;

import java.util.ArrayList;

import org.bukkit.Location;

import com.gmail.akashirt53072.minegame.Main;
import com.gmail.akashirt53072.minegame.config.datatype.LocationData;

public class SignConfig extends DataConfig{
	private Main plugin;
	public SignConfig(Main plugin) {
		super(plugin,"config_sign.yml");
		this.plugin = plugin;
	}
	
	public ArrayList<LocationData> getAllData() {
		ArrayList<LocationData> listData = new ArrayList<LocationData>();
		for(int i = 1;i < 1000;i ++) {
			if(containData("sign" + i)) {
				listData.add(readSign(i));
			}
		}
		return listData;		
	}
	private LocationData readSign(int i) {
		String path = "sign" + i;
		String id = "temp";
		if(containData(path + ".type")) {
			id = getStringData(path + ".type");
		}else {
			plugin.getLogger().warning("[error]config_sign.yml内、sign" + i + "にtype(種類識別名)がありません");
		}
		
		int x = 0;
		if(containData(path + ".x")) {
			x = getIntData(path + ".x");
		}else {
			plugin.getLogger().warning("[error]config_sign.yml内、sign" + i + "にx(X座標)がありません");
		}
		int y = 0;
		if(containData(path + ".y")) {
			y = getIntData(path + ".y");
		}else {
			plugin.getLogger().warning("[error]config_sign.yml内、sign" + i + "にy(y座標)がありません");
		}
		int z = 0;
		if(containData(path + ".z")) {
			z = getIntData(path + ".z");
		}else {
			plugin.getLogger().warning("[error]config_sign.yml内、sign" + i + "にz(z座標)がありません");
		}
		return new LocationData(id,new Location(plugin.getMainWorld(),x,y,z));
		
	}
	public void writeSign(LocationData data) {
		int index = super.getNextIndex("sign");
		int x = data.getLocation().getBlockX();
		int y = data.getLocation().getBlockY();
		int z = data.getLocation().getBlockZ();
		setData("sign" + index + ".x",x);
		setData("sign" + index + ".y",y);
		setData("sign" + index + ".z",z);
		setData("sign" + index + ".type",data.getId());
		
		plugin.loadSign();
	}
	public void deleteSign(Location loc) {
		for(int i = 1;i < 1000;i ++) {
			if(containData("sign" + i)) {
				if(checkSign(i,loc)) {
					plugin.loadSign();
					return;
				}
			}
		}
		
	}
	private boolean checkSign(int i,Location loc) {
		String path = "sign" + i;
		int x = getIntData(path + ".x");
		int y = getIntData(path + ".y");
		int z = getIntData(path + ".z");
		if(x == loc.getBlockX() && y == loc.getBlockY() && z == loc.getBlockZ()) {
			setData(path,null);
			return true;
		}
		return false;
	}
	
}