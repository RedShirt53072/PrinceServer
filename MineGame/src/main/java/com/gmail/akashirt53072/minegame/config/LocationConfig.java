package com.gmail.akashirt53072.minegame.config;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Material;

import com.gmail.akashirt53072.minegame.Main;
import com.gmail.akashirt53072.minegame.config.datatype.LocResorceData;
import com.gmail.akashirt53072.minegame.config.datatype.LocationData;
import com.gmail.akashirt53072.minegame.config.datatype.MapLocData;
import com.gmail.akashirt53072.minegame.enums.GameType;
import com.gmail.akashirt53072.minegame.enums.MapType;

public class LocationConfig extends DataConfig{
	private Main plugin;
	public LocationConfig(Main plugin) {
		super(plugin,"config_loc.yml");
		this.plugin = plugin;
	}
	
	public LocResorceData getAllData() {
		
		LocationData serverlobby = readSign("serverlobby");
		
		LocationData mainlobby = readSign("mainlobby");
		
		ArrayList<LocationData> waitingRoom = new ArrayList<LocationData>();
		for(int i = 1;containData("waitingroom" + i);i ++) {
			waitingRoom.add(readSign("waitingroom" + i));
		}
		if(waitingRoom.isEmpty()) {
			plugin.getLogger().warning("[error]config_loc.yml内にwaitingRoomがひとつもありません");
		}
		
		
		ArrayList<MapLocData> maps = new ArrayList<MapLocData>();
		for(int i = 1;containData("map" + i);i ++) {
			ArrayList<LocationData> map = new ArrayList<LocationData>();
			for(int j = 1;containData("map" + i + ".location" + j);j ++) {
				map.add(readSign("map" + i + ".location" + j));
			}
			if(map.isEmpty()) {
				plugin.getLogger().warning("[error]config_loc.yml内、" + "map" + i + "にlocationがひとつもありません");
			}
			MapType type = MapType.LOBBY;
			if(containData("map" + i + ".type")) {
				try{
					type = MapType.valueOf(getStringData("map" + i + ".type"));
				} catch(IllegalArgumentException ex){
					plugin.getLogger().warning("[error]config_loc.yml内、map" + i + "のtypeが無効な値です");
				}
			}else {
				plugin.getLogger().warning("[error]config_loc.yml内、map" + i + "にtype(種類識別名)がありません");
			}
			
			String name = type.toString();
			if(containData("map" + i + ".name")) {
				name = getStringData("map" + i + ".name");
			}else {
				plugin.getLogger().warning("[error]config_loc.yml内、map" + i + "にname(一般名)がありません");
			}
			
			Material icon = Material.BARRIER;
			if(containData("map" + i + ".icon")) {
				try{
					icon = Material.valueOf(getStringData("map" + i + ".icon"));
				} catch(IllegalArgumentException ex){
					plugin.getLogger().warning("[error]config_loc.yml内、map" + i + "のiconが無効な値です");
				}
			}else {
				plugin.getLogger().warning("[error]config_loc.yml内、map" + i + "にicon(表示アイテム)がありません");
			}
			
			
			int id = 0;
			if(containData("map" + i + ".id")) {
				id = getIntData("map" + i + ".id");
			}else {
				plugin.getLogger().warning("[error]config_loc.yml内、map" + i + "にid(種類別番号)がありません");
			}
			
			
			ArrayList<GameType> game = new ArrayList<GameType>();
			
			for(int j = 1;containData("map" + i + ".game" + j);j ++) {
				try{
					GameType ty = GameType.valueOf(getStringData("map" + i + ".game" + j));
					game.add(ty);
				} catch(IllegalArgumentException ex){
					plugin.getLogger().warning("[error]config_loc.yml内、map" + i + "のgame" + j + "が無効な値です");
				}
			}
			if(game.isEmpty()) {
				plugin.getLogger().warning("[error]config_loc.yml内、map" + i + "にgame(使用可能試合タイプ)がひとつもありません");
			}
			
			
			maps.add(new MapLocData(map,type,name,icon,game,id));
		}
		if(maps.isEmpty()) {
			plugin.getLogger().warning("[error]config_loc.yml内にmapがひとつもありません");
		}
		return new LocResorceData(serverlobby,mainlobby,waitingRoom,maps);
	}
	
	private LocationData readSign(String path) {
		String id = "temp";
		if(containData(path + ".name")) {
			id = getStringData(path + ".name");
		}else {
			plugin.getLogger().warning("[error]config_loc.yml内、" + path + "にname(種類識別名)がありません");
		}
		int x = 0;
		if(containData(path + ".x")) {
			x = getIntData(path + ".x");
		}else {
			plugin.getLogger().warning("[error]config_loc.yml内、" + path + "にx(X座標)がありません");
		}
		int y = 0;
		if(containData(path + ".y")) {
			y = getIntData(path + ".y");
		}else {
			plugin.getLogger().warning("[error]config_loc.yml内、" + path + "にy(y座標)がありません");
		}
		int z = 0;
		if(containData(path + ".z")) {
			z = getIntData(path + ".z");
		}else {
			plugin.getLogger().warning("[error]config_loc.yml内、" + path + "にz(z座標)がありません");
		}
		int yaw = 0;
		if(containData(path + ".yaw")) {
			yaw = getIntData(path + ".yaw");
		}else {
			plugin.getLogger().warning("[error]config_loc.yml内、" + path + "にyaw(左右回転角度)がありません");
		}
		int pitch = 0;
		if(containData(path + ".pitch")) {
			pitch = getIntData(path + ".pitch");
		}else {
			plugin.getLogger().warning("[error]config_loc.yml内、" + path + "にpitch(上下回転角度)がありません");
		}
		
		return new LocationData(id,new Location(plugin.getMainWorld(),x,y,z,yaw,pitch));
	}
}