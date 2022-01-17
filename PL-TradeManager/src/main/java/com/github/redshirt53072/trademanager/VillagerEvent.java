package com.github.redshirt53072.trademanager;


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
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.MerchantRecipe;

import com.github.redshirt53072.trademanager.data.VillagerManager;



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
    		return;
    	}
        
        vil.setRecipes(VillagerManager.getNewRecipe(prof, 1));
   	}
    @EventHandler(priority = EventPriority.NORMAL)
    public void levelup(VillagerAcquireTradeEvent event) {
    	event.setCancelled(true);
        Villager vil = (Villager)event.getEntity();
        List<MerchantRecipe> recipes = vil.getRecipes();
        recipes.addAll(VillagerManager.getNewRecipe(vil.getProfession(), vil.getVillagerLevel()));
    	vil.setRecipes(recipes);
   	}
    @EventHandler(priority = EventPriority.NORMAL)
    public void talkVillager(PlayerInteractEntityEvent event) {
    	Entity clicked = event.getRightClicked();
    	if(!clicked.getType().equals(EntityType.VILLAGER)) {
    		return;
    	}
    	Villager vil = (Villager)clicked;
    	Profession prof = vil.getProfession();
    	if(prof.equals(Profession.NONE)) {
    		return;
    	}
    	int vilVersion = new VillagerManager(vil).getVersion();
    	if(vilVersion < 0) {
    		return;
    	}
    	int tableVersion = VillagerManager.getTableVersion(prof);
    	if(vilVersion != tableVersion) {
        	vil.setRecipes(VillagerManager.getAllRecipe(prof, vil.getVillagerLevel()));
        	new VillagerManager(vil).setVersion(tableVersion);
    	}
   	}
}