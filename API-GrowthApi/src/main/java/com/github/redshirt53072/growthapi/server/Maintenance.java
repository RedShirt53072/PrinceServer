package com.github.redshirt53072.growthapi.server;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import com.github.redshirt53072.growthapi.BaseAPI;
import com.github.redshirt53072.growthapi.config.ConfigManager;
import com.github.redshirt53072.growthapi.message.LogManager;
import com.github.redshirt53072.growthapi.player.PlayerManager;

public final class Maintenance {
	private static boolean onMain = false;
	private static boolean tempMain = false;
	
	private static int playerLimit = 50;
	
	private static Map<String,UUID> opList = new HashMap<String,UUID>();
	private static Map<String,UUID> joinList = new HashMap<String,UUID>();
	
	private static Map<String,UUID> nowOPList = new HashMap<String,UUID>();
	private static Map<String,UUID> nowJoinList = new HashMap<String,UUID>();
	
	public static void reload() {
		ConfigManager manager = new ConfigManager(BaseAPI.getInstance(),"core","maintenance.yml");
		manager.configInit();
		
		opList = new HashMap<String,UUID>();
		for(String key : manager.getKeys("", "op")) {
			String name = manager.getString(key + ".name");
			if(name == null) {
				manager.logWarning(key + ".name", "の値が不正です");
				continue;
			}
			UUID uuid = null;
			try{
				uuid = UUID.fromString(manager.getString(key + ".uuid"));
				Bukkit.getOfflinePlayer(uuid).getName();
			}catch(Exception ex) {
				manager.logWarning(key + ".uuid", "の値が不正です");
				continue;
			}
			opList.put(name,uuid);
		}
		
		joinList = new HashMap<String,UUID>();
		for(String key : manager.getKeys("", "join")) {
			String name = manager.getString(key + ".name");
			if(name == null) {
				manager.logWarning(key + ".name", "の値が不正です");
				continue;
			}
			UUID uuid = null;
			try{
				uuid = UUID.fromString(manager.getString(key + ".uuid"));
				Bukkit.getOfflinePlayer(uuid).getName();
			}catch(Exception ex) {
				manager.logWarning(key + ".uuid", "の値が不正です");
				continue;
			}
			joinList.put(name,uuid);
		}
		Integer maxPlayer = manager.getInt("maxplayer");
		if(maxPlayer == null) {
			manager.logConfig("maxplayer", "の値が未設定です。自動的にデフォルト値の50が設定されました。");
			manager.setData("maxplayer",50);
		}else if(maxPlayer > 150 || maxPlayer < 5) {
			manager.logWarning("maxplayer", "の値が不正です。自動的にデフォルト値の50が設定されました。");
			manager.setData("maxplayer",50);
		}else {
			playerLimit = maxPlayer;
		}
		
		onMain = readOnMain();
		
	}
	private static boolean readOnMain(){
		ConfigManager manager = new ConfigManager(BaseAPI.getInstance(),"core","maintenance.yml");
		
		Integer main = manager.getInt("onmain");
		if(main == null) {
			LogManager.logInfo("初回起動のため、自動的にメンテナンスを開始しました。", BaseAPI.getInstance(), Level.INFO);
			close();
			return false;
		}
		return main == 1;
	}
	
	public static boolean isPlayerLimitOver() {
		return Bukkit.getServer().getOnlinePlayers().size() >= playerLimit;
	}
	
	public static Set<String> getOpList(){
		return opList.keySet();
	}
	public static Set<String> getJoinList(){
		return joinList.keySet();
	}
	public static Set<String> getNowList(){
		Set<String> result = new HashSet<String>();
		result.addAll(nowJoinList.keySet());
		result.addAll(nowOPList.keySet());
		return result;
	}
	
	public static boolean canOPLogin(Player p) {
		if(canOPCommand(p)) {
			PermissionAttachment attach = p.addAttachment(BaseAPI.getInstance());
			attach.setPermission(Bukkit.getPluginManager().getPermission("growth.op"), true);	
			return true;
		}
		return false;
	}
	public static boolean canOPCommand(Player p) {
		if(isOPPlayer(p)) {
			if(nowOPList.containsKey(p.getName())) {
				UUID uuid = nowOPList.get(p.getName());
				return p.getUniqueId().equals(uuid);
			}
		}
		return false;
	}
	
	public static boolean canJoin(Player p) {
		if(isJoinPlayer(p)) {
			if(nowJoinList.containsKey(p.getName())) {
				UUID uuid = nowJoinList.get(p.getName());
				return p.getUniqueId().equals(uuid);
			}	
		}
		return false;
	}
	
	
	public static boolean addOP(String name) {
		if(opList.containsKey(name)){
			UUID uuid = opList.get(name);
			nowOPList.put(name, uuid);
			return true;
		}else {
			return false;
		}
	}
	
	public static boolean addNormal(String name) {
		if(joinList.containsKey(name)){
			UUID uuid = joinList.get(name);
			nowJoinList.put(name, uuid);
			return true;
		}else {
			return false;
		}
	}
	
	public static boolean isOPPlayer(Player p) {
		if(opList.containsKey(p.getName())) {
			UUID uuid = opList.get(p.getName());
			return p.getUniqueId().equals(uuid);
		}
		return false;
	}
	
	public static boolean isJoinPlayer(Player p) {
		if(joinList.containsKey(p.getName())) {
			UUID uuid = joinList.get(p.getName());
			return p.getUniqueId().equals(uuid);
		}
		return false;
	}
	
	public static boolean isMain() {
		return onMain;
	}
	public static boolean isTempMain() {
		return tempMain;
	}
	
	public static boolean removePlayer(String name) {
		UUID uuid = nowJoinList.remove(name);
		if(uuid == null) {	
			uuid = nowOPList.remove(name);
		}else {
			nowOPList.remove(name);
		}
		OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
		if(player == null) {
			return false;
		}
		if(!player.isOnline()) {
			return true;
		}
		Player p = (Player)player;
		if(p.isOp()) {
    		p.setOp(false);
    	}
    	PlayerManager.removePermission(p);
		return true;
	}
	
	public static void open() {
		nowJoinList.clear();
		nowOPList.clear();
		
		ConfigManager manager = new ConfigManager(BaseAPI.getInstance(),"core","maintenance.yml");
		for(Player p : Bukkit.getServer().getOnlinePlayers()){
			PlayerManager.removePermission(p);
			if(p.isOp()) {
	    		p.setOp(false);
	    	}
		}
		tempMain = true;
		manager.setData("onmain",0);

		manager.logConfig("onmain","を0(open)に設定しました。");
	}
	
	public static void close() {
		ConfigManager manager = new ConfigManager(BaseAPI.getInstance(),"core","maintenance.yml");
		tempMain = false;
		onMain = true;
		manager.setData("onmain",1);
		
		manager.logConfig("onmain","を1(close)に設定しました。");
    }
}