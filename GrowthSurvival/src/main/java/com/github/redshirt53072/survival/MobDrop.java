package com.github.redshirt53072.survival;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.event.entity.EntitySpawnEvent;

import com.github.redshirt53072.api.BaseAPI;
import com.github.redshirt53072.api.message.LogManager;
import com.github.redshirt53072.api.message.MessageManager;
import com.github.redshirt53072.api.message.TextBuilder;
import com.github.redshirt53072.api.message.MessageManager.MessageLevel;

public final class MobDrop implements Listener {
	private GrowthSurvival plugin;
	
	private static List<SpawnLocation> locData = new ArrayList<SpawnLocation>();
	
	
    public MobDrop() {
    	this.plugin = GrowthSurvival.getInstance();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void itemDrop(EntityDropItemEvent event) {
    	if(event.getEntityType().equals(EntityType.PLAYER)) {
    		return;
    	}
        Item item = event.getItemDrop();
        Bukkit.getScheduler().runTask(GrowthSurvival.getInstance(), new Runnable() {
            @Override
            public void run() {
                item.setTicksLived(4800);
            }
        });
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void spwan(EntitySpawnEvent event) {
    	Location loc = event.getLocation();
    	World world = loc.getWorld();
    	Collection<Entity> entities = world.getNearbyEntities(loc, 40, 40, 40);
    	int size = entities.size();
    	if(size > 400) {
    		for(SpawnLocation ld : locData) {
    			if(ld.isMatch(loc)) {
    				return;
    			}
    		}
    		LogManager.logInfo(TextBuilder.plus("エンティティが400体以上密集しています。(","world:",world.getName(),",x:",String.valueOf(loc.getBlockX()),",y:",String.valueOf(loc.getBlockY()),",z:",String.valueOf(loc.getBlockZ()),")付近"), BaseAPI.getInstance(), Level.SEVERE);
			MessageManager.sendNearPlayer(MessageLevel.WARNING,loc , 100,"あなたの周囲にエンティティが異常に密集している場所があります。","原因に心当たりがある場合はできる限り早急にエンティティを減らしてください。","改善が見られない場合は運営による監査が行われることがあります。");
			
			SpawnLocation spLoc = new SpawnLocation(loc,LocLevel.LOC400);
    		locData.add(spLoc);

			Bukkit.getScheduler().runTaskLater(GrowthSurvival.getInstance(), spLoc, 300);
    	}else if(size > 200) {
    		for(SpawnLocation ld : locData) {
    			if(ld.isMatch(loc)) {
    				return;
    			}
    		}
    		LogManager.logInfo(TextBuilder.plus("エンティティが200体以上密集しています。(","world:",world.getName(),",x:",String.valueOf(loc.getBlockX()),",y:",String.valueOf(loc.getBlockY()),",z:",String.valueOf(loc.getBlockZ()),")付近"), BaseAPI.getInstance(), Level.WARNING);
			MessageManager.sendNearPlayer(MessageLevel.IMPORTANT,loc , 50,"あなたの周囲にエンティティが密集している場所があります。","何度も続く場合、運営による監査が行われることがありますのでご注意ください。");
		
			SpawnLocation spLoc = new SpawnLocation(loc,LocLevel.LOC200);
    		locData.add(spLoc);

			Bukkit.getScheduler().runTaskLater(GrowthSurvival.getInstance(), spLoc, 300);
    	}
   	}
    
    private static enum LocLevel{
    	NORMAL,
    	LOC200,
    	LOC400
    }
    
    private static class SpawnLocation implements Runnable{
    	private LocLevel level = LocLevel.NORMAL;
    	private Location loc;
    	private World world;
    	
    	public SpawnLocation(Location loc,LocLevel level) {
    		this.loc = loc;
    		this.level = level;
    		world = loc.getWorld();
    	}
    	@Override
    	public void run() {
    		Collection<Entity> entities = world.getNearbyEntities(loc, 40, 40, 40);
    		int size = entities.size();
        	if(size > 400) {
        		if(level.equals(LocLevel.LOC200)) {
					LogManager.logInfo(TextBuilder.plus("エンティティが400体以上密集しています。(","world:",world.getName(),",x:",String.valueOf(loc.getBlockX()),",y:",String.valueOf(loc.getBlockY()),",z:",String.valueOf(loc.getBlockZ()),")付近"), BaseAPI.getInstance(), Level.SEVERE);
					MessageManager.sendNearPlayer(MessageLevel.WARNING,loc , 100,"あなたの周囲にエンティティが異常に密集している場所があります。","原因に心当たりがある場合はできる限り早急にエンティティを減らしてください。","改善が見られない場合は運営による監査が行われることがあります。");
					level = LocLevel.LOC400;
				}
				Bukkit.getScheduler().runTaskLater(GrowthSurvival.getInstance(), this, 300);
        	}else if(size > 200){
        		if(level.equals(LocLevel.LOC400)) {
					LogManager.logInfo(TextBuilder.plus("エンティティの密集は400体未満まで改善しました。(","world:",world.getName(),",x:",String.valueOf(loc.getBlockX()),",y:",String.valueOf(loc.getBlockY()),",z:",String.valueOf(loc.getBlockZ()),")付近"), BaseAPI.getInstance(), Level.INFO);
					level = LocLevel.LOC200;
        		}
				Bukkit.getScheduler().runTaskLater(GrowthSurvival.getInstance(), this, 300);
        	}else {
        		LogManager.logInfo(TextBuilder.plus("エンティティの密集は200体未満まで改善しました。(","world:",world.getName(),",x:",String.valueOf(loc.getBlockX()),",y:",String.valueOf(loc.getBlockY()),",z:",String.valueOf(loc.getBlockZ()),")付近"), BaseAPI.getInstance(), Level.INFO);
				MessageManager.sendNearPlayer(MessageLevel.IMPORTANT,loc , 50,"あなたの周囲でのエンティティの密集が一定程度改善されました。");
				locData.remove(this);
        	}
        	
    	}
    	public boolean isMatch(Location newLoc) {
    		if(!newLoc.getWorld().equals(world)){
    			return false;
    		}
    		return newLoc.distanceSquared(loc) < 50;
    	}
    }
}