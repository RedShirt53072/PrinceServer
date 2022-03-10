package com.github.redshirt53072.api.message;


import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.github.redshirt53072.api.BaseAPI;
import com.github.redshirt53072.api.server.Maintenance;


/**
 * 音を鳴らすクラス
 * @author redshirt
 *
 */
public class SoundManager {
	public static void send(Player player,Sound s,float pitch) {
		player.playSound(player.getLocation(), s, 1, pitch);
	}
	
	public static enum CustomSound{
		PickUp,
		Click,
		Cancel,
		LevelUp,
		Smith,
		Success,
		Notice,
		Alert,
		Chime;
	}
	
	public static void send(Player player,CustomSound sound) {
		switch(sound) {
		case PickUp :
	        sendPickUp(player);
	        break;
		case Click :
	        sendClick(player);
	        break;
		case Cancel :
	        sendCancel(player);
	        break;
		case LevelUp :
	        sendLevelUp(player);
	        break;
		case Smith :
	        sendSmith(player);
	        break;    
		case Success :
	        sendSuccess(player);
	        break;
		case Notice :
	        sendNotice(player);
	        break;
		case Alert :
	        sendAlert(player);
	        break;
		case Chime:
			sendChime(player);
			break;
		}
	}
	
	public static void sendOP(CustomSound sound) {
		for(Player p : BaseAPI.getInstance().getServer().getOnlinePlayers()){
			if(Maintenance.isOPPlayer(p)) {
				send(p,sound);
			}
		}
	}
	
	public static void sendAllPlayer(CustomSound sound) {
		for(Player p : BaseAPI.getInstance().getServer().getOnlinePlayers()){
			send(p,sound);
		}
	}
	
	public static void sendNearPlayer(CustomSound sound,Location loc,int maxDistance) {
		World world = loc.getWorld();
		for(Player p : BaseAPI.getInstance().getServer().getOnlinePlayers()){
			if(world.equals(p.getWorld())) {
				if(loc.distanceSquared(p.getLocation()) < maxDistance) {
					send(p,sound);	
				}
			}
		}
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
	 * カンカンカンッ！
	 * @param player
	 */
	public static void sendSmith(Player player) {
		player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_USE, 1, 1);
	}
	
	/**
	 * ピロリン!
	 * @param player
	 */
	public static void sendLevelUp(Player player) {
		player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 2);
	}
	/**
	 * パロローン!
	 * @param player
	 */
	public static void sendSuccess(Player player) {
		player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1,0.5F);
	}
	
	/**
	 * ピンポーン！
	 * @param player
	 */
	public static void sendNotice(Player player) {
		List<Float> data1 = buildNoteSound(new ArrayList<Float>(),16,20);
		
		sendLoop(player,Sound.BLOCK_NOTE_BLOCK_HARP,data1,8);
	}
	/**
	 * 防災行政無線の始まりの音
	 * @param player
	 */
	public static void sendChime2(Player player){
		List<Float> data1 = buildNoteSound(new ArrayList<Float>(),10,14,17,22);
		
		sendLoop(player,Sound.BLOCK_NOTE_BLOCK_HARP,data1,10);
	}
	/**
	 * 学校のチャイムの音
	 * @param player
	 */
	public static void sendChime(Player player){

		List<Float> data1 = buildNoteSound(new ArrayList<Float>(),10,14,12,5);
		
		List<Float> data2 = buildNoteSound(new ArrayList<Float>(),5,12,14,10);
		
		List<Float> data3 = buildNoteSound(new ArrayList<Float>(),14,10,12,5);
		
		List<Float> data4 = buildNoteSound(new ArrayList<Float>(),5,12,14,10);
		
		
		sendLoop(player,Sound.BLOCK_NOTE_BLOCK_HARP,data1,10);
		
		
		Bukkit.getScheduler().runTaskLater(BaseAPI.getInstance(),new Runnable() {
			@Override
			public void run() {
				sendLoop(player,Sound.BLOCK_NOTE_BLOCK_HARP,data2,10);
			}
		}, 60);
		
		Bukkit.getScheduler().runTaskLater(BaseAPI.getInstance(),new Runnable() {
			@Override
			public void run() {
				sendLoop(player,Sound.BLOCK_NOTE_BLOCK_HARP,data3,10);
			}
		}, 120);
		
		Bukkit.getScheduler().runTaskLater(BaseAPI.getInstance(),new Runnable() {
			@Override
			public void run() {
				sendLoop(player,Sound.BLOCK_NOTE_BLOCK_HARP,data4,10);
			}
		}, 180);
	}
	
	/**
	 * @param player
	 */
	public static void sendAlert(Player player) {
		List<Float> data1 = buildNoteSound(new ArrayList<Float>(),3,9,13);
		
		List<Float> data2 = buildNoteSound(new ArrayList<Float>(),4,10,14);
		
		List<Float> data3 = buildNoteSound(new ArrayList<Float>(),3,9,13);
		
		List<Float> data4 = buildNoteSound(new ArrayList<Float>(),4,10,14);
		
		
		sendLoop(player,Sound.BLOCK_NOTE_BLOCK_HARP,data1,3);
		
		
		Bukkit.getScheduler().runTaskLater(BaseAPI.getInstance(),new Runnable() {
			@Override
			public void run() {
				sendLoop(player,Sound.BLOCK_NOTE_BLOCK_HARP,data2,3);
			}
		}, 15);
		Bukkit.getScheduler().runTaskLater(BaseAPI.getInstance(),new Runnable() {
			@Override
			public void run() {
				sendLoop(player,Sound.BLOCK_NOTE_BLOCK_HARP,data3,3);
			}
		}, 35);
		
		Bukkit.getScheduler().runTaskLater(BaseAPI.getInstance(),new Runnable() {
			@Override
			public void run() {
				sendLoop(player,Sound.BLOCK_NOTE_BLOCK_HARP,data4,3);
			}
		}, 50);
		
	}
	
	
	public static List<Float> buildNoteSound(List<Float> data,int... notes) {
		for(int note : notes) {
			switch(note) {
			case 0://ファ#
				data.add(0.5F);
				break;
			case 1://ソ
				data.add(0.529732F);
				break;
			case 2://ソ#
				data.add(0.561231F);
				break;
			case 3://ラ
				data.add(0.594604F);
				break;
			case 4://ラ#
				data.add(0.629961F);
				break;
			case 5://シ
				data.add(0.667420F);
				break;
			case 6://ド
				data.add(0.707107F);
				break;
			case 7://ド#
				data.add(0.749154F);
				break;
			case 8://レ
				data.add(0.793701F);
				break;
			case 9://レ#
				data.add(0.840896F);
				break;
			case 10://ミ
				data.add(0.890899F);
				break;
			case 11://ファ
				data.add(0.943874F);
				break;
			case 12://ファ#
				data.add(1.0F);
				break;
			case 13://ソ
				data.add(1.059463F);
				break;
			case 14://ソ#
				data.add(1.122462F);
				break;
			case 15://ラ
				data.add(1.189207F);
				break;
			case 16://ラ#
				data.add(1.259921F);
				break;
			case 17://シ
				data.add(1.334840F);
				break;
			case 18://ド
				data.add(1.414214F);
				break;
			case 19://ド#
				data.add(1.498307F);
				break;
			case 20://レ
				data.add(1.587401F);
				break;
			case 21://レ#
				data.add(1.681793F);
				break;
			case 22://ミ
				data.add(1.781797F);
				break;
			case 23://ファ
				data.add(1.887749F);
				break;
			case 24://ファ#
				data.add(2.0F);
				break;
			default:
				
			}
		}
		
		return data;
	}
	
	public static void sendLoop(Player p,Sound sound,List<Float> data,int interval) {
		Bukkit.getScheduler().runTaskLater(BaseAPI.getInstance(),new Runnable() {
			@Override
			public void run() {
				if(data.isEmpty()) {
					return;
				}
				p.playSound(p.getLocation(), sound, 1,data.get(0));
				data.remove(0);
				sendLoop(p,sound,data,interval);
			}
		}, interval);
	}
}