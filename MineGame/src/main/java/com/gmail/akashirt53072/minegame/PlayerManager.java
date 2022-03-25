package com.gmail.akashirt53072.minegame;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.gmail.akashirt53072.minegame.config.cache.LocationManager;
import com.gmail.akashirt53072.minegame.enums.MessageType;
import com.gmail.akashirt53072.minegame.enums.PlayerStatus;
import com.gmail.akashirt53072.minegame.match.MatchManager;
import com.gmail.akashirt53072.minegame.nbt.NBTPlayerStatus;

public class PlayerManager {
	private Main plugin;
	private Player player;
	public PlayerManager(Main plugin,Player player) {
		this.plugin = plugin;
		this.player = player;
	}
	
	public void onLogout() {
		//状態チェックして
		PlayerStatus status = new NBTPlayerStatus(plugin,player).getType();
		switch(status) {
		case lOGOUT:
		case BREAKING:
			return;
		case LOBBY:
			goToServerLobby();
			break;
		case WAITING:
			//待機離脱
			new MatchManager(plugin).logoutWaitMatch(player);
			
			goToServerLobby();
			break;
		case ONMATCH:
			//試合保存
			new NBTPlayerStatus(plugin,player).setType(PlayerStatus.BREAKING);
			
			new MatchManager(plugin).breakMatch(player);
			
			break;
		}
		
		
	}
	
	
	public void onLogin() {
		//ログアウト(break)→試合
		//試合復帰を試みる
		new MatchManager(plugin).returnMatch(player);
		
	}
	
	public void onJoin() {
		
		goToLobby();
		
	}
	public void onLeave() {
		//状態チェック
		PlayerStatus status = new NBTPlayerStatus(plugin,player).getType();
		switch(status) {
		case lOGOUT:
		case BREAKING:
			return;
		case LOBBY:
			break;
		case WAITING:
			new MatchManager(plugin).logoutWaitMatch(player);
			
			break;
		case ONMATCH:
			new MatchManager(plugin).leaveMatch(player);
			
			break;
		}
		
		MessageManager.sendAllPlayer(player.getName() + "が退出しました。", MessageType.INFO, plugin);
		
		//ミニゲームロビーへtp
		goToServerLobby();
	}
	public void goToServerLobby() {
		//ミニゲームロビーへtp
		player.teleport(new LocationManager(plugin).getServerLobby());
		
		new NBTPlayerStatus(plugin,player).setType(PlayerStatus.lOGOUT);
		
		resetPlayer();
		
		MessageManager.sendInfo("ミニゲームサーバーロビーに移動しました。", player);
	}
	
	
	public void kick(String reason) {
		PlayerStatus status = new NBTPlayerStatus(plugin,player).getType();
		switch(status) {
		case lOGOUT:
		case BREAKING:
			return;
		case LOBBY:
			break;
		case WAITING:
			new MatchManager(plugin).logoutWaitMatch(player);
			
			break;
		case ONMATCH:
			new MatchManager(plugin).kickMatch(player, reason);
			
			break;
		}
		goToServerLobby();
		
	}
	
	
	
	public void goToLobby() {
		//tp
		player.teleport(new LocationManager(plugin).getMainLobby());
		
		new NBTPlayerStatus(plugin,player).setType(PlayerStatus.LOBBY);
		
		resetPlayer();
		
		MessageManager.sendInfo("マインゲームロビーに移動しました。", player);
	}
	
	private void resetPlayer() {
		//持ち物やステータスリセット
		player.getInventory().clear();
		for(PotionEffect e : player.getActivePotionEffects()){
			player.removePotionEffect(e.getType());
		}
		player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION,100000,0,false,false,false));
		player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,100000,0,false,false,false));
		player.setHealth(20);
		player.setFoodLevel(20);
		player.sendExperienceChange(0, 0);
		player.setGameMode(GameMode.ADVENTURE);
		player.setAllowFlight(false);
	}
}
