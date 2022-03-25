package com.github.redshirt53072.trade.bundle;

import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;

import com.github.redshirt53072.api.item.ItemNBTLoader;
import com.github.redshirt53072.api.item.ItemUtil;
import com.github.redshirt53072.api.message.TextBuilder;
import com.github.redshirt53072.api.util.LootRoller;
import com.github.redshirt53072.trade.TradeManager;

public class SpecialItem extends ItemNBTLoader {

	public enum SpecialItems{
		NONE("無"),
		ENCHANTED_BOOK("エンチャ本"),
		FIREWORK("花火");
		
		String name;
		
		private SpecialItems(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
	}
	
	public SpecialItem(ItemStack box) {
    	super(box,TradeManager.getInstance());
    }
	
	public void setType(SpecialItems type) {
    	writeString("sptype",type.toString());
    }
    
    public SpecialItems getType() {
    	String type = readString("sptype");
    	SpecialItems tag;
    	try{
    		tag = SpecialItems.valueOf(type);
    	}catch(Exception ex) {
    		writeString("sptype","NONE");
    		return SpecialItems.NONE;
    	}
    	return tag;
    }
    public static void doRandom(Player p,ItemStack item) {
    	SpecialItems type = new SpecialItem(item).getType();
    	switch(type) {
    	case ENCHANTED_BOOK:
    		ItemStack ench = new ItemStack(Material.ENCHANTED_BOOK);
    		EnchantmentStorageMeta enchMeta = (EnchantmentStorageMeta)ench.getItemMeta();
    		LootRoller<Enchantment> loot = new LootRoller<Enchantment>();
    		loot.addData(Enchantment.ARROW_DAMAGE, 10);
    		loot.addData(Enchantment.DIG_SPEED, 10);
    		loot.addData(Enchantment.DAMAGE_ALL, 10);
    		loot.addData(Enchantment.PROTECTION_ENVIRONMENTAL, 10);
    		loot.addData(Enchantment.PIERCING, 10);
    		loot.addData(Enchantment.KNOCKBACK, 5);
    		loot.addData(Enchantment.DAMAGE_ARTHROPODS, 5);
    		loot.addData(Enchantment.DAMAGE_UNDEAD, 5);
    		loot.addData(Enchantment.QUICK_CHARGE, 5);
    		loot.addData(Enchantment.LOYALTY, 5);
    		loot.addData(Enchantment.DURABILITY, 5);
    		loot.addData(Enchantment.PROTECTION_FALL, 5);
    		loot.addData(Enchantment.PROTECTION_FIRE, 5);
    		loot.addData(Enchantment.PROTECTION_PROJECTILE, 5);
    		loot.addData(Enchantment.FIRE_ASPECT, 2);
    		loot.addData(Enchantment.LOOT_BONUS_MOBS, 2);
    		loot.addData(Enchantment.SWEEPING_EDGE, 2);
    		loot.addData(Enchantment.MULTISHOT, 2);
    		loot.addData(Enchantment.IMPALING, 2);
    		loot.addData(Enchantment.RIPTIDE, 2);
    		loot.addData(Enchantment.LOOT_BONUS_BLOCKS, 2);
    		loot.addData(Enchantment.LUCK, 2);
    		loot.addData(Enchantment.LURE, 2);
    		loot.addData(Enchantment.WATER_WORKER, 2);
    		loot.addData(Enchantment.DEPTH_STRIDER, 2);
    		loot.addData(Enchantment.OXYGEN, 2);
    		loot.addData(Enchantment.PROTECTION_EXPLOSIONS, 2);
    		loot.addData(Enchantment.ARROW_FIRE, 2);
    		loot.addData(Enchantment.ARROW_KNOCKBACK, 2);
    		loot.addData(Enchantment.CHANNELING, 1);
    		loot.addData(Enchantment.SILK_TOUCH, 1);
    		loot.addData(Enchantment.ARROW_INFINITE, 1);
    		loot.addData(Enchantment.THORNS, 1);
    		enchMeta.addStoredEnchant(loot.getRandom(), 1, false);
    		if(new Random().nextInt(20) == 0) {
    			enchMeta.addStoredEnchant(Enchantment.VANISHING_CURSE, 1, false);	
    		}
    		if(new Random().nextInt(20) == 0) {
    			enchMeta.addStoredEnchant(Enchantment.BINDING_CURSE, 1, false);	
    		}
    		
    		ench.setItemMeta(enchMeta);

    		ItemUtil.removeItem(p, item, 1);
    		ItemUtil.giveItem(p, ench);
    		p.playSound(p.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1,1);
    		return;
    	case FIREWORK:
    		ItemStack firework = new ItemStack(Material.FIREWORK_ROCKET);
        	FireworkMeta meta = (FireworkMeta)firework.getItemMeta();
    		meta.setPower(new Random().nextInt(3) + 1);
    		
    		meta.addEffect(FireworkEffect.builder()
    				.flicker(new Random().nextBoolean())
    				.trail(new Random().nextBoolean())
    				.with(Type.values()[new Random().nextInt(5)])
    				.withColor(Color.fromRGB(new Random().nextInt(256),new Random().nextInt(256),new Random().nextInt(256)))
    				.withFade(Color.fromRGB(new Random().nextInt(256),new Random().nextInt(256),new Random().nextInt(256)))
    				.build());
    		firework.setItemMeta(meta);
    		ItemUtil.removeItem(p, item, 1);
    		ItemUtil.giveItem(p, firework);
    		p.playSound(p.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1,1);
    		return;
		default:
			break;
    	}
	}
    
    public static ItemStack getNewBox(SpecialItems tag) {
    	ItemStack item = new ItemStack(Material.COMMAND_BLOCK);
    	ItemMeta meta = item.getItemMeta();
    	meta.setCustomModelData(3001);
    	meta.setDisplayName(TextBuilder.quickBuild(ChatColor.WHITE,"ランダムな",tag.getName()));
    	item.setItemMeta(meta);
    	new SpecialItem(item).setType(tag);
    	return item;
    }
}
