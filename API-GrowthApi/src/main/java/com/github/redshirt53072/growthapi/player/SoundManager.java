package com.github.redshirt53072.growthapi.player;


import org.bukkit.Sound;
import org.bukkit.entity.Player;


/**
 * 音を鳴らすクラス
 * @author redshirt
 *
 */
public class SoundManager {
	public static void send(Player player,Sound s,float pitch) {
		player.playSound(player.getLocation(), s, 1, pitch);
	}
	

	/**
	 * ぽっ
	 * @param player
	 */
	public static void sendPickUp(Player player) {
		player.playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1,1);
	}
	/**
	 * かちっ
	 * @param player
	 */
	public static void sendClick(Player player) {
		player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
	}
	
	/**
	 * んぐぉわっ
	 * @param player
	 */
	public static void sendCancel(Player player) {
		player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, (float)0.5);
	}
	/**
	 * ピロリン!
	 * @param player
	 */
	public static void sendLevelUp(Player player) {
		player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 2);
	}
	/**
	 * ピロリン!
	 * @param player
	 */
	public static void sendSuccess(Player player) {
		player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1,0.5F);
	}
	
	
}
