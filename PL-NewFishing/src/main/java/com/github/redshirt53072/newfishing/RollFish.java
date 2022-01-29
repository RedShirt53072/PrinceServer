package com.github.redshirt53072.newfishing;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.github.redshirt53072.newfishing.data.RodLootTable;
import com.github.redshirt53072.newfishing.data.RarityLootData;
import com.github.redshirt53072.newfishing.data.FishData;
import com.github.redshirt53072.newfishing.data.FishLootData;
import com.github.redshirt53072.newfishing.data.TimeLootData;
import com.github.redshirt53072.newfishing.data.YLootData;
import com.github.redshirt53072.newfishing.nbt.FishNBT;

public class RollFish {
	private NewFishing plugin;
	private Player player;
	private int star;
	
	public RollFish(NewFishing plugin,Player p) {
    	this.plugin = plugin;
    	this.player = p;
    	star = 1;
    }
    public void roll() {
    	Location loc = player.getLocation();
    	Environment env = loc.getWorld().getEnvironment();
    	Biome biome = loc.getBlock().getBiome();
    	if(plugin.isError()) {
    		player.sendMessage(ChatColor.RED + "現在不具合が発生しているため、釣りができません");
    		return;
    	}
    	//条件に合うのを探す
    	RodLootTable lootData = plugin.getLootData();
    	String fishid = null;
    	if(env.equals(Environment.THE_END)) {
    		fishid = searchTimeList(lootData.getEnd());
    	}else {
    		boolean match = false;
    		for(RarityLootData biomeData : lootData.getOver().getBiomeData()) {
    			for(Biome b : biomeData.getType()) {
    				if(b.equals(biome)) {
    					match = true;
    				}
    			}
    			if(match) {
        			fishid = searchTimeList(biomeData.getData());
    				break;
    			}
    		}
    		if(!match) {
    			fishid = searchTimeList(lootData.getOver().getDefData());
    		}
    	}
    	if(fishid == null) {
    		plugin.getLogger().warning("[error]loottableで釣りを適切に処理できませんでした");
    		player.sendMessage(ChatColor.RED + "不具合のため、魚が釣れませんでした");
    		return;
    	}
    	ArrayList<FishData> fishData = plugin.getFishList();
    	for(FishData fish : fishData) {
    		if(fish.getId().equals(fishid)) {
    			giveFish(fish);
    			return;
    		}
    	}
    }
    private void giveFish(FishData fishData) {
    	star = fishData.getrarity();
    	//魚用意する
		ItemStack item = new ItemStack(Material.TROPICAL_FISH);
		ItemMeta meta = item.getItemMeta();
		
		//キングサイズ
		int kingRoll = new Random().nextInt(100);
		int isKing = 0;
		if(kingRoll < 5) {
			//ビッグ
			isKing = 1;
		}else if(kingRoll < 6){
			//キング
			isKing = 2;
		}
		//希少種
		int rareType = 0;
		if(1 > new Random().nextInt(100)) {
			rareType = 1;
		}
		//大きさ
		double size;
		double sizeRoll = new Random().nextDouble();
		if(isKing == 2) {
			size = (fishData.getSize() * (2 + sizeRoll));
		}else if (isKing == 1) {
			size = (fishData.getSize() * (1.5 + sizeRoll / 2));
		}else {
			size = (fishData.getSize() * (1 + sizeRoll / 2));
		}
		BigDecimal bd = new BigDecimal(size);
		size = bd.setScale(1, RoundingMode.DOWN).doubleValue();
		
		//name
		String name = fishData.getName();
		if(isKing == 2) {
			name = name + "♕";	
		}else if (isKing == 1) {
			name = name + "♔";	
		}
		if(rareType == 1) {
			name = name + "☄";	
		}
		meta.setDisplayName(ChatColor.RESET + name);
		//lore
		ArrayList<String> lore = new ArrayList<String>();
		lore.add(ChatColor.RESET + "");
		String rerity = "レア度：";
		for(int i = 0;i < star;i++) {
			rerity = rerity + "☆";
		}
		lore.add(ChatColor.RESET + rerity);
		lore.add(ChatColor.RESET + "大きさ:" + size + "(cm)");
		lore.add(ChatColor.RESET + "釣り人:" + player.getName());
		LocalDateTime now = LocalDateTime.now();
		String time = now.getYear() + "年" + now.getMonthValue() + "月" + now.getDayOfMonth() + "日";
		time = time + now.getHour() + "時" + now.getMinute() + "分" + now.getSecond() + "秒";
		lore.add(ChatColor.RESET + time);
		lore.add(ChatColor.RESET + "----------------");
		for(String l : fishData.getLore()) {
			lore.add(ChatColor.RESET + l);	
		}
		meta.setLore(lore);
		//テクスチャ
		int texture = fishData.getTexture();
		if(texture != -1) {
			meta.setCustomModelData(texture);	
		}
		item.setItemMeta(meta);
		//NBTに保存
		new FishNBT(plugin,item).init(sizeRoll, star, isKing, rareType, player.getUniqueId(), fishData.getId(), fishData.getName());
		
		//give item
		player.getInventory().addItem(item);
		
		//メッセージ
		String message;
		if(isKing == 2) {
			message = "キングサイズの";
		}else if (isKing == 1) {
			message = "ビッグサイズの";
		}else {
			message = "";
		}
		message = message + fishData.getName();
		message = message + "(" + size + "cm)";
		if(rareType == 1) {
			message = message + "の希少種";
		}
		message = message + "を釣り上げた!";
		player.sendMessage(message);
		
    }
    private String searchTimeList(ArrayList<TimeLootData> data) {
    	long time = player.getWorld().getTime();
    	for(TimeLootData t : data) {
    		if(t.getStart() <= time && t.getEnd() >= time) {
    			return searchYList(t.getData());
    		}
    	}
    	return null;
    }
    private String searchYList(ArrayList<YLootData> data) {
    	int y = player.getLocation().getBlockY();
    	for(YLootData t : data) {
    		if(t.getStart() <= y && t.getEnd() >= y) {
    			return searchRarityList(t.getData());
    		}
    	}
    	return null;
    }
    private String searchRarityList(ArrayList<ArrayList<FishLootData>> data) {
    	int starRoll = new Random().nextInt(1000);
		if(starRoll < 400) {
			star = 1;
		}else if(starRoll < 700){
			star = 2;
		}else if(starRoll < 880){
			star = 3;
		}else if(starRoll < 970){
			star = 4;
		}else if(starRoll < 990){
			star = 5;
		}else if(starRoll < 998){
			star = 6;
		}else {
			star = 7;
		}
		return searchFishList(data.get(star - 1));	
    }
    private String searchFishList(ArrayList<FishLootData> data) {
    	int total = 0;
    	for(FishLootData t : data) {
    		total += t.getRoll();
    	}
    	int roll = new Random().nextInt(total);
    	int now = 0;
    	for(FishLootData t : data) {
    		now += t.getRoll();
    		if(roll < now) {
    			return t.getId();
    		}
    	}
    	
    	return null;
    }
}
