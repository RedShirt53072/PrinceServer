package com.github.redshirt53072.trade;


import java.util.ArrayList;
import java.util.List;

import org.bukkit.event.Listener;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.VillagerAcquireTradeEvent;
import org.bukkit.event.entity.VillagerCareerChangeEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.MerchantInventory;
import org.bukkit.inventory.MerchantRecipe;

import com.github.redshirt53072.api.npc.NpcManager;
import com.github.redshirt53072.trade.data.VillagerManager;

public final class VillagerEvent implements Listener {
	TradeManager plugin;
    public VillagerEvent() {
    	this.plugin = TradeManager.getInstance();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void careerChange(VillagerCareerChangeEvent event) {
        Villager vil = event.getEntity();
    	Profession prof = event.getProfession();
        if(prof.equals(Profession.NONE)){
        	VillagerManager manager = new VillagerManager(vil);
        	manager.setLevel(0);
        	manager.setVersion(0);
        	
    		return;
    	}
   	}
    @EventHandler(priority = EventPriority.NORMAL)
    public void levelup(VillagerAcquireTradeEvent event) {
    	if(!event.getEntity().getType().equals(EntityType.VILLAGER)) {
    		return;
    	}
    	event.setCancelled(true);
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void openVillager(InventoryOpenEvent event) {
    	Inventory inv = event.getInventory();
    	if(!inv.getType().equals(InventoryType.MERCHANT)) {
    		return;
    	}
    	MerchantInventory minv = (MerchantInventory)inv;
    	Merchant m = minv.getMerchant();
    	for(MerchantRecipe mr : m.getRecipes()){
    		int sp = mr.getSpecialPrice();
    		if(sp != 0) {
    			mr.setSpecialPrice(0);
    		}
    	}
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void talkVillager(PlayerInteractEntityEvent event) {
    	Entity clicked = event.getRightClicked();
    	if(!clicked.getType().equals(EntityType.VILLAGER)) {
    		return;
    	}
    	Villager vil = (Villager)clicked;
    	if(NpcManager.getNowType(vil) != null) {
    		return;
    	}
    	
    	Profession prof = vil.getProfession();
    	if(prof.equals(Profession.NONE)) {
    		return;
    	}
		update(vil,prof);
		event.getPlayer().openMerchant(vil, true);
		event.setCancelled(true);
    	
   	}
    private void update(Villager vil,Profession prof) {
    	VillagerManager manager = new VillagerManager(vil);
    	int vilVersion = manager.getVersion();
    	if(vilVersion < 0) {
    		return;
    	}
    	int tableVersion = VillagerManager.getTableVersion(prof);
    	if(vilVersion == 0) {
    		manager.setVersion(tableVersion);
    	}else if(vilVersion != tableVersion) {
        	vil.setRecipes(VillagerManager.getAllRecipe(prof, vil.getVillagerLevel()));
        	manager.setVersion(tableVersion);
        	return;
    	}
    	
        int oldLevel = manager.getLevel();
    	int nowLevel = vil.getVillagerLevel();
        if(oldLevel == nowLevel) {
        	return;
        }
        List<MerchantRecipe> recipes = new ArrayList<MerchantRecipe>();
        recipes.addAll(vil.getRecipes());
        for(int i = nowLevel;i > oldLevel;i--) {
        	recipes.addAll(VillagerManager.getNewRecipe(vil.getProfession(), i));	
        }
        vil.setRecipes(recipes);
    	
    	manager.setLevel(nowLevel);
    }
}