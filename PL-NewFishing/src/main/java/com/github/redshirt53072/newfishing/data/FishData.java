package com.github.redshirt53072.newfishing.data;

import java.util.ArrayList;

public class FishData {
	private String id;
	private String name;
	private ArrayList<String> lore;
	private int texture;
	private int size;
	private int rarity;
	private int price;
	
	public FishData(String id, String name,ArrayList<String> lore,int texture,int size,int rarity,int price) {
		this.id = id;
    	this.name = name;
    	this.lore = lore;
    	this.texture = texture;
    	this.size = size;
    	this.price = price;
    	this.rarity = rarity;
    }
	public String getId() {
    	return id;
    }
	public String getName() {
    	return name;
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
		public BiomeGroup getName() {
			return group;
		}
	}
	
	public static enum BiomeGroup{
        FROZEN_OCEAN("凍った海"),
        DEEP_OCEAN("遠洋"),
        OCEAN("近海"),
        WARM_OCEAN("サンゴ礁"),
        BEACH("浜辺"),
        RIVER("川"),
        SNOWY("雪原"),
        PLAINS("草原"),
        SWAMP("沼"),
        MUSHROOM_FIELDS("キノコ島"),
        JUNGLE("ジャングル"),
        MOUNTAIN("山"),
        FOREST("森"),
        BADLANDS("メサ"),
        SAVANNA("サバンナ"),
        DESERT("砂漠"),
        CAVES("洞窟"),
        THE_END("エンド"),
        NETHER("ネザー"),
        OTHER("その他");
		
		private String savedName;
		
		private BiomeGroup(String name) {
			savedName = name;
		}
		public String getName() {
			return savedName;
		}
	}
	
	
	
	public static enum Time{
		ALL("一日中"),
		NIGHT("夜のみ"),
		DAY("昼のみ");
		
		private String savedName;
		
		private Time(String name) {
			savedName = name;
		}
		public String getName() {
			return savedName;
		}
	}
}