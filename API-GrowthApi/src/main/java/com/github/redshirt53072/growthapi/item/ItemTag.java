package com.github.redshirt53072.growthapi.item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.Tag;

public enum ItemTag {
	NONE("無",getList(Material.AIR)),
	CONCRETE("コンクリート",getList(
			Material.BLACK_CONCRETE,
			Material.WHITE_CONCRETE,
			Material.BLUE_CONCRETE,
			Material.BROWN_CONCRETE,
			Material.CYAN_CONCRETE,
			Material.GRAY_CONCRETE,
			Material.GREEN_CONCRETE,
			Material.LIGHT_BLUE_CONCRETE,
			Material.YELLOW_CONCRETE,
			Material.RED_CONCRETE,
			Material.PURPLE_CONCRETE,
			Material.PINK_CONCRETE,
			Material.ORANGE_CONCRETE,
			Material.MAGENTA_CONCRETE,
			Material.LIME_CONCRETE,
			Material.LIGHT_GRAY_CONCRETE)),
	CONCRETE_POWDER("コンクリートパウダー",getList(
			Material.BLACK_CONCRETE_POWDER,
			Material.WHITE_CONCRETE_POWDER,
			Material.BLUE_CONCRETE_POWDER,
			Material.BROWN_CONCRETE_POWDER,
			Material.CYAN_CONCRETE_POWDER,
			Material.GRAY_CONCRETE_POWDER,
			Material.GREEN_CONCRETE_POWDER,
			Material.LIGHT_BLUE_CONCRETE_POWDER,
			Material.YELLOW_CONCRETE_POWDER,
			Material.RED_CONCRETE_POWDER,
			Material.PURPLE_CONCRETE_POWDER,
			Material.PINK_CONCRETE_POWDER,
			Material.ORANGE_CONCRETE_POWDER,
			Material.MAGENTA_CONCRETE_POWDER,
			Material.LIME_CONCRETE_POWDER,
			Material.LIGHT_GRAY_CONCRETE_POWDER)),
	GLASS_PANE("ガラス板",getList(
			Material.BLACK_STAINED_GLASS_PANE,
			Material.WHITE_STAINED_GLASS_PANE,
			Material.BLUE_STAINED_GLASS_PANE,
			Material.BROWN_STAINED_GLASS_PANE,
			Material.CYAN_STAINED_GLASS_PANE,
			Material.GRAY_STAINED_GLASS_PANE,
			Material.GREEN_STAINED_GLASS_PANE,
			Material.LIGHT_BLUE_STAINED_GLASS_PANE,
			Material.YELLOW_STAINED_GLASS_PANE,
			Material.RED_STAINED_GLASS_PANE,
			Material.PURPLE_STAINED_GLASS_PANE,
			Material.PINK_STAINED_GLASS_PANE,
			Material.ORANGE_STAINED_GLASS_PANE,
			Material.MAGENTA_STAINED_GLASS_PANE,
			Material.LIME_STAINED_GLASS_PANE,
			Material.LIGHT_GRAY_STAINED_GLASS_PANE,
			Material.GLASS)),
	GLASS("ガラス",getList(
			Material.BLACK_STAINED_GLASS,
			Material.WHITE_STAINED_GLASS,
			Material.BLUE_STAINED_GLASS,
			Material.BROWN_STAINED_GLASS,
			Material.CYAN_STAINED_GLASS,
			Material.GRAY_STAINED_GLASS,
			Material.GREEN_STAINED_GLASS,
			Material.LIGHT_BLUE_STAINED_GLASS,
			Material.YELLOW_STAINED_GLASS,
			Material.RED_STAINED_GLASS,
			Material.PURPLE_STAINED_GLASS,
			Material.PINK_STAINED_GLASS,
			Material.ORANGE_STAINED_GLASS,
			Material.MAGENTA_STAINED_GLASS,
			Material.LIME_STAINED_GLASS,
			Material.LIGHT_GRAY_STAINED_GLASS,
			Material.GLASS)),
	TERRACOTTA("テラコッタ",new ArrayList<Material>(Tag.TERRACOTTA.getValues())),
	GLAZED_TERRACOTTA("彩釉テラコッタ",getList(
			Material.BLACK_GLAZED_TERRACOTTA,
			Material.WHITE_GLAZED_TERRACOTTA,
			Material.BLUE_GLAZED_TERRACOTTA,
			Material.BROWN_GLAZED_TERRACOTTA,
			Material.CYAN_GLAZED_TERRACOTTA,
			Material.GRAY_GLAZED_TERRACOTTA,
			Material.GREEN_GLAZED_TERRACOTTA,
			Material.LIGHT_BLUE_GLAZED_TERRACOTTA,
			Material.YELLOW_GLAZED_TERRACOTTA,
			Material.RED_GLAZED_TERRACOTTA,
			Material.PURPLE_GLAZED_TERRACOTTA,
			Material.PINK_GLAZED_TERRACOTTA,
			Material.ORANGE_GLAZED_TERRACOTTA,
			Material.MAGENTA_GLAZED_TERRACOTTA,
			Material.LIME_GLAZED_TERRACOTTA,
			Material.LIGHT_GRAY_GLAZED_TERRACOTTA)),
	WOOL("羊毛",new ArrayList<Material>(Tag.WOOL.getValues())),
	FLOWER("花",new ArrayList<Material>(Tag.FLOWERS.getValues())),
	CORAL("サンゴ",getList(
			Material.BRAIN_CORAL,
			Material.BUBBLE_CORAL,
			Material.HORN_CORAL,
			Material.TUBE_CORAL,
			Material.FIRE_CORAL)),
	CORAL_FAN("ウチワサンゴ",getList(
			Material.BRAIN_CORAL_FAN,
			Material.BUBBLE_CORAL_FAN,
			Material.HORN_CORAL_FAN,
			Material.TUBE_CORAL_FAN,
			Material.FIRE_CORAL_FAN)),
	CORAL_BLOCK("サンゴブロック",getList(
			Material.BRAIN_CORAL_BLOCK,
			Material.BUBBLE_CORAL_BLOCK,
			Material.HORN_CORAL_BLOCK,
			Material.TUBE_CORAL_BLOCK,
			Material.FIRE_CORAL_BLOCK)),
	CANDLE("ロウソク",new ArrayList<Material>(Tag.CANDLES.getValues()));
	
	List<Material> items;
	String name;
	
	private ItemTag(String name,List<Material> items) {
		this.name = name;
		this.items = items;
	}
	
	public String getName() {
		return name;
	}
	public List<Material> getItems() {
		return items;
	}
	
	private static List<Material> getList(Material... materials){
		ArrayList<Material> list = new ArrayList<Material>();
		Collections.addAll(list, materials);
		return list;
	}
}
