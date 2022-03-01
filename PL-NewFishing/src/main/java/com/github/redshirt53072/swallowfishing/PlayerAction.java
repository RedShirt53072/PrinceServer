package com.github.redshirt53072.swallowfishing;
//playerの行動をトリガーに様々な処理クラスへと取り次ぐクラス
import org.bukkit.event.Listener;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;

import com.github.redshirt53072.swallowfishing.data.FishManager;
import com.github.redshirt53072.swallowfishing.nbt.FishingRodNBT;

public final class PlayerAction implements Listener {
    public PlayerAction() {
    	SwallowFishing plugin = SwallowFishing.getInstance();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    //釣ったとき
    @EventHandler(priority = EventPriority.NORMAL)
    public void onFishing(PlayerFishEvent event) {
    	if(!event.getState().equals(PlayerFishEvent.State.CAUGHT_FISH)) {
    		return;
    	}
    	Player player = event.getPlayer();
    	ItemStack mainItem = player.getInventory().getItemInMainHand();
    	ItemStack offItem = player.getInventory().getItemInOffHand();
    	ItemStack item;
    	if(mainItem != null) {
    		item = mainItem;
    	}else {
    		if(offItem == null) {
    			return;
    		}
    		item = offItem;
    	}
    	
    	String rodName = new FishingRodNBT(item).getRodID();
    	if(rodName == null) {
    		return;
    	}
    	Item itemEntity = (Item)event.getCaught();
    	ItemStack fish = FishManager.lootNewFish(player, rodName);
    	if(fish == null) {
    		itemEntity.remove();
    		return;
    	}
		itemEntity.setItemStack(fish);
    }
}