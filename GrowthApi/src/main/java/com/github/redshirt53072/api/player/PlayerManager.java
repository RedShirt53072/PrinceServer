package com.github.redshirt53072.api.player;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

public final class PlayerManager {
	private static List<AsyncLock> asyncLock = new ArrayList<AsyncLock>();
	private static List<Player> advLock = new ArrayList<Player>();
	
	private static List<InitListener> inits = new ArrayList<InitListener>();
	private static List<LogoutListener> logouts = new ArrayList<LogoutListener>();
	
	public static void initPlayer(Player player) {
		for(InitListener init : inits) {
			init.onInit(player);
		}
	}
	
	public static void registerInit(InitListener listener) {
		inits.add(listener);
	}
	
	public static void logOutPlayer(Player player) {
		for(LogoutListener logout : logouts) {
			logout.onLogout(player);;
		}
	}
	
	public static void registerLogout(LogoutListener listener) {
		logouts.add(listener);
	}
	
	public static void removePermission(Player p) {
		p.recalculatePermissions();
		for(PermissionAttachmentInfo attach : p.getEffectivePermissions()){
			if(attach.getAttachment() == null) {
				continue;
			}
			if(attach.getPermission().equals("growth.op")){
				attach.getAttachment().remove();
			}
			if(attach.getPermission().equals("growth.console")){
				attach.getAttachment().remove();
			}
		}
	}
	
	public static void removeAdvLock(Player player) {
		if(advLock.contains(player)) {
			advLock.remove(player);
		}
	}
	
	public static void addAdvLock(Player player) {
		if(!advLock.contains(player)) {
			advLock.add(player);
		}
	}
	
	public static boolean isAdvLock(Player player) {
		return advLock.contains(player);
	}
	
	public static void removeAsyncLock(String lockID,OfflinePlayer player) {
		for(int i = asyncLock.size() - 1;i >= 0;i--) {
			AsyncLock al = asyncLock.get(i);
			if(al.match(lockID,player)){
				asyncLock.remove(i);
				return;
			}
		}
	}
	
	public static void addAsyncLock(String lockID,OfflinePlayer player) {
		if(isAsyncLocked(player,lockID)) {
			return;
		}
		asyncLock.add(new AsyncLock(lockID,player));
	}
	
	public static boolean isAsyncLocked(OfflinePlayer player) {
		for(AsyncLock al : asyncLock) {
			if(al.match(player)){
				return true;
			}
		}
		return false;
	}
	public static boolean isAsyncLocked(OfflinePlayer player,String lockID) {
		for(AsyncLock al : asyncLock) {
			if(al.match(lockID,player)){
				return true;
			}
		}
		return false;
	}
	
	private static class AsyncLock{
		private OfflinePlayer player;
		private String lockID;
		
		public AsyncLock(String lockID,OfflinePlayer player) {
			this.player = player;
			this.lockID = lockID;
		}
		public boolean match(OfflinePlayer player) {
			if(this.player.equals(player)) {
				return true;
			}
			return false;
		}
		public boolean match(String lockID,OfflinePlayer player) {
			if(this.lockID.equals(lockID)) {
				if(this.player.equals(player)) {
					return true;
				}
			}
			return false;
		}
	}
}
