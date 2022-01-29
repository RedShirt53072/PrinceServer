package com.github.redshirt53072.newfishing;
//playerの行動をトリガーに様々な処理クラスへと取り次ぐクラス
import org.bukkit.event.Listener;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.github.redshirt53072.newfishing.nbt.FishingRodNBT;

public final class PlayerAction implements Listener {
    public PlayerAction() {
    	NewFishing plugin = NewFishing.getInstance();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    //釣ったとき
    @EventHandler(priority = EventPriority.NORMAL)
    public void onSpawn(PlayerFishEvent event) {
    	if(!event.getState().equals(PlayerFishEvent.State.CAUGHT_FISH)) {
    		return;
    	}
    	Player player = event.getPlayer();
    	ItemStack mainItem = player.getInventory().getItemInMainHand();
    	ItemStack offItem = player.getInventory().getItemInOffHand();
    	if(mainItem != null) {
    		if(isFishingRod(mainItem)) {
    			Item itemEntity = (Item)event.getCaught();
    			itemEntity.setItemStack(new RollFish(player).roll());	
    		}
    	}else {
    		if(offItem == null) {
    			return;
    		}
    		if(isFishingRod(offItem)) {
    			Item itemEntity = (Item)event.getCaught();
    			itemEntity.setItemStack(new RollFish(player).roll());
    		}
    	}
    }
    private boolean isFishingRod(ItemStack item){
    	String rodName = new FishingRodNBT(item).getFishName();
    	
    	return rodName != null;
    }
}