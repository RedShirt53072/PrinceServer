package com.github.redshirt53072.swallowfishing.data;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.github.redshirt53072.growthapi.item.ItemBuilder;
import com.github.redshirt53072.swallowfishing.nbt.FishNBT;

public class FishData {
	private int id;
	private String name;
	private ArrayList<String> lore;
	private ArrayList<BiomeGroup> biome;
	private ArrayList<Time> time;
	private int texture;
	private int size;
	private int rarity;
	private int price;
	
	public FishData(int id, String name,ArrayList<String> lore,ArrayList<BiomeGroup> biome,ArrayList<Time> time,int texture,int size,int rarity,int price) {
		this.id = id;
    	this.name = name;
    	this.biome = biome;
    	this.time = time;
    	this.lore = lore;
    	this.texture = texture;
    	this.size = size;
    	this.price = price;
    	this.rarity = rarity;
    }
	public int getId() {
    	return id;
    }
	public String getName() {
    	return name;
	}
	public ArrayList<Time> getTime() {
    	return time;
    }
	public ArrayList<BiomeGroup> getBiome() {
    	return biome;
    }
	public ArrayList<String> getLore() {
    	return lore;
    }
	public int getTexture() {
    	return texture;
    }
	public int getSize() {
    	return size;
	}
	public int getPrice() {
    	return price;
	}
	public int getrarity() {
    	return rarity;
	}
	
	public ItemStack getNewItem(Player p,Time time,Biome biome){
		BiomeGroup group = FishingBiome.valueOf(biome.toString()).getGroup();
		
		ItemStack item = new ItemStack(Material.TROPICAL_FISH);
		int quality = 1;
		int multiPrice = price;
		int multiSize = size;
		int multiTexture = texture;
		String star = "";
		String newName = name;
		ArrayList<String> newLore = new ArrayList<String>(lore);
		
		int random = new Random().nextInt(86);
		
		if(random < 1) {
			quality = 3;
			multiTexture += 3000;
			multiPrice *= 2;
			multiSize *= 1.5 + new Random().nextDouble() / 2;
			star = "";
		}else if(random < 6) {
			quality = 2;
			multiTexture += 2000;
			multiPrice *= 1.5;
			multiSize *= 2 + new Random().nextDouble() / 2;
			star = "";
		}else {
			multiTexture += 1000;
			multiSize *= 0.5 + new Random().nextDouble();
		}
		ItemMeta meta = item.getItemMeta();
		meta.setCustomModelData(multiTexture);
		
		
		switch(rarity) {
		case 1:
			newLore.add(ChatColor.WHITE + "レア度:");	
			break;
		case 2:
			newLore.add(ChatColor.WHITE + "レア度:");	
			break;
		case 3:
			newLore.add(ChatColor.WHITE + "レア度:");	
			break;
		case 4:
			newLore.add(ChatColor.WHITE + "レア度:");	
			break;
		case 5:
			newLore.add(ChatColor.WHITE + "レア度:");	
			break;
		}
		if(size < 100) {
			newLore.add(ChatColor.WHITE + "大きさ:" + multiSize + "(cm)");	
		}else {
			int si1 = multiSize / 10;
			double si2 = si1;
			si2 /= 10;
			newLore.add(ChatColor.WHITE + "大きさ:" + si2 + "(m)");
		}
		
		newLore.add(ChatColor.WHITE + "釣り人:" + p.getName());
		newLore.add(ChatColor.WHITE + time.getName() + "の" + group.getName() + "にて");
		meta.setLore(newLore);
		meta.setDisplayName(newName + star);
		
		item.setItemMeta(meta);
		new FishNBT(item).init(multiSize, rarity, quality, p, multiPrice, id);
		
		return item;
	}
	
	public static enum FishingBiome{
		FROZEN_OCEAN(BiomeGroup.FROZEN_OCEAN),
		DEEP_FROZEN_OCEAN(BiomeGroup.FROZEN_OCEAN),
		
		COLD_OCEAN(BiomeGroup.OCEAN),
		DEEP_COLD_OCEAN(BiomeGroup.OCEAN),
		OCEAN(BiomeGroup.OCEAN),
		DEEP_OCEAN(BiomeGroup.OCEAN),
		DEEP_LUKEWARM_OCEAN(BiomeGroup.OCEAN),
		LUKEWARM_OCEAN(BiomeGroup.OCEAN),
		
		WARM_OCEAN(BiomeGroup.WARM_OCEAN),
		
		BEACH(BiomeGroup.BEACH),
		SNOWY_BEACH(BiomeGroup.BEACH),
		
		RIVER(BiomeGroup.RIVER),
		FROZEN_RIVER(BiomeGroup.RIVER),
		
		FROZEN_PEAKS(BiomeGroup.SNOWY),
		GROVE(BiomeGroup.SNOWY),
		ICE_SPIKES(BiomeGroup.SNOWY),
		SNOWY_PLAINS(BiomeGroup.SNOWY),
		SNOWY_SLOPES(BiomeGroup.SNOWY),
		SNOWY_TAIGA(BiomeGroup.SNOWY),
		JAGGED_PEAKS(BiomeGroup.SNOWY),
		
		PLAINS(BiomeGroup.PLAINS),
		SUNFLOWER_PLAINS(BiomeGroup.PLAINS),
		MEADOW(BiomeGroup.PLAINS),
		
		SWAMP(BiomeGroup.SWAMP),
		
		MUSHROOM_FIELDS(BiomeGroup.MUSHROOM_FIELDS),
		
		BAMBOO_JUNGLE(BiomeGroup.JUNGLE),
		SPARSE_JUNGLE(BiomeGroup.JUNGLE),
		JUNGLE(BiomeGroup.JUNGLE),

		STONY_PEAKS(BiomeGroup.MOUNTAIN),
		STONY_SHORE(BiomeGroup.MOUNTAIN),
		WINDSWEPT_FOREST(BiomeGroup.MOUNTAIN),
		WINDSWEPT_GRAVELLY_HILLS(BiomeGroup.MOUNTAIN),
		WINDSWEPT_HILLS(BiomeGroup.MOUNTAIN),
		
		FLOWER_FOREST(BiomeGroup.FOREST),
		FOREST(BiomeGroup.FOREST),
		BIRCH_FOREST(BiomeGroup.FOREST),
		OLD_GROWTH_BIRCH_FOREST(BiomeGroup.FOREST),
		OLD_GROWTH_PINE_TAIGA(BiomeGroup.FOREST),
		OLD_GROWTH_SPRUCE_TAIGA(BiomeGroup.FOREST),
		TAIGA(BiomeGroup.FOREST),
		DARK_FOREST(BiomeGroup.FOREST),
		
		BADLANDS(BiomeGroup.BADLANDS),
		ERODED_BADLANDS(BiomeGroup.BADLANDS),
		WOODED_BADLANDS(BiomeGroup.BADLANDS),
		
		WINDSWEPT_SAVANNA(BiomeGroup.SAVANNA),
		SAVANNA(BiomeGroup.SAVANNA),
		SAVANNA_PLATEAU(BiomeGroup.SAVANNA),
		
		DESERT(BiomeGroup.DESERT),
		
		LUSH_CAVES(BiomeGroup.CAVES),
		DRIPSTONE_CAVES(BiomeGroup.CAVES),
		
		END_BARRENS(BiomeGroup.THE_END),
		END_HIGHLANDS(BiomeGroup.THE_END),
		END_MIDLANDS(BiomeGroup.THE_END),
		SMALL_END_ISLANDS(BiomeGroup.THE_END),
		THE_END(BiomeGroup.THE_END),
		
		BASALT_DELTAS(BiomeGroup.NETHER),
		CRIMSON_FOREST(BiomeGroup.NETHER),
		NETHER_WASTES(BiomeGroup.NETHER),
		SOUL_SAND_VALLEY(BiomeGroup.NETHER),
		WARPED_FOREST(BiomeGroup.NETHER),

		THE_VOID(BiomeGroup.OTHER),
		CUSTOM(BiomeGroup.OTHER);
		private BiomeGroup group;
		
		private FishingBiome(BiomeGroup group) {
			this.group = group;
		}
		public BiomeGroup getGroup() {
			return group;
		}
	}
	
	public static enum BiomeGroup{
        FROZEN_OCEAN("凍った海",100),
        DEEP_OCEAN("遠洋",101),
        OCEAN("近海",102),
        WARM_OCEAN("サンゴ礁",103),
        BEACH("浜辺",104),
        RIVER("川",105),
        SNOWY("雪原",106),
        PLAINS("草原",107),
        SWAMP("沼",108),
        MUSHROOM_FIELDS("キノコ島",109),
        JUNGLE("ジャングル",110),
        MOUNTAIN("山",111),
        FOREST("森",112),
        BADLANDS("メサ",113),
        SAVANNA("サバンナ",114),
        DESERT("砂漠",115),
        CAVES("洞窟",116),
        THE_END("エンド",117),
        NETHER("ネザー",118),
        OTHER("辺境の地",119);
		
		private String savedName;
		private int model;
		
		
		private BiomeGroup(String name,int modelNum) {
			savedName = name;
			model = modelNum;
		}
		
		public ItemStack getIconItem() {
			return new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setModelData(model).build();
		}
		
		public ItemStack getSelectedIconItem() {
			return new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setModelData(model + 100).build();
		}
		
		public String getName() {
			return savedName;
		}
	}
	
	public static enum Time{
		MIDNIGHT("真夜中",300),
		MORNING("夜明け",301),
		NIGHT("夕暮れ",302),
		DAY("昼頃",303);
		
		private String savedName;
		private int model;
		
		private Time(String name,int modelNum) {
			savedName = name;
			model = modelNum;
		}
		public String getName() {
			return savedName;
		}
		
		public ItemStack getIconItem() {
			return new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setModelData(model).build();
		}
		
		public static Time getTime(long time){
			if(time > 22000) {
				return Time.MORNING;
			}
			if(time > 16000) {
				return Time.MIDNIGHT;
			}
			if(time > 12000) {
				return Time.NIGHT;
			}
			if(time > 2000) {
				return Time.DAY;
			}
			return Time.MORNING;
		}
	}
}
