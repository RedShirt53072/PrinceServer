package com.github.redshirt53072.growthapi.player;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

public final class PlayerManager {
	private static List<AsyncLock> asyncLock = new ArrayList<AsyncLock>();
	
	private static List<InitListener> inits = new ArrayList<InitListener>();
	
	public static void initPlayer(Player player) {
		for(InitListener init : inits) {
			init.onInit(player);
		}
	}
	
	public static void registerInit(InitListener listener) {
		inits.add(listener);
	}
	
	public static void removeAsyncLock(String lockID,Player player) {
		for(int i = asyncLock.size() - 1;i >= 0;i--) {
			AsyncLock al = asyncLock.get(i);
			if(al.match(lockID,player)){
				asyncLock.remove(i);
				return;
			}
		}
	}
	
	public static void addAsyncLock(String lockID,Player player) {
		if(isAsyncLocked(lockID,player)) {
			return;
		}
		asyncLock.add(new AsyncLock(lockID,player));
	}
	
	public static boolean isAsyncLocked(Player player) {
		for(AsyncLock al : asyncLock) {
			if(al.match(player)){
				return true;
			}
		}
		return false;
	}
	public static boolean isAsyncLocked(Player player,String lockID) {
		for(AsyncLock al : asyncLock) {
			if(al.match(lockID,player)){
				return true;
			}
		}
		return false;
	}
	
	private static boolean isAsyncLocked(String lockID,Player player) {
		for(AsyncLock al : asyncLock) {
			if(al.match(lockID,player)){
				return true;
			}
		}
		return false;
	}
	
	
	private static class AsyncLock{
		private Player player;
		private String lockID;
		
		public AsyncLock(String lockID,Player player) {
			this.player = player;
			this.lockID = lockID;
		}
		public boolean match(Player player) {
			if(this.player.equals(player)) {
				return true;
			}
			return false;
		}
		public boolean match(String lockID,Player player) {
			if(this.lockID.equals(lockID)) {
				if(this.player.equals(player)) {
					return true;
				}
			}
			return false;
		}
	}
}
