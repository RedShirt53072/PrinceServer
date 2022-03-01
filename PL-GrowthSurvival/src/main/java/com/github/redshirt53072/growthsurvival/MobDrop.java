package com.github.redshirt53072.growthsurvival;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;

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

import com.github.redshirt53072.growthapi.BaseAPI;
import com.github.redshirt53072.growthapi.message.LogManager;
import com.github.redshirt53072.growthapi.message.MessageManager;
import com.github.redshirt53072.growthapi.message.MessageManager.MessageLevel;
import com.github.redshirt53072.growthapi.message.TextBuilder;

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
        item.setTicksLived(4800);
   	}
    @EventHandler(priority = EventPriority.NORMAL)
    public void spwan(EntitySpawnEvent event) {
    	Location loc = event.getLocation();
    	World world = loc.getWorld();
    	Collection<Entity> entities = world.getNearbyEntities(loc, 40, 40, 40);
    	int size = entities.size();
    	if(size > 400) {
    		boolean newRegister = true;
    		for(SpawnLocation ld : locData) {
    			if(ld.loc400(loc)) {
    				newRegister = false;
    				break;
    			}
    		}
    		if(newRegister){
    			locData.add(new SpawnLocation(loc,LocLevel.LOC400));
    		}
    	}else if(size > 200) {
    		boolean newRegister = true;
    		for(SpawnLocation ld : locData) {
    			if(ld.loc200(loc)) {
    				newRegister = false;
    				break;
    			}
    		}
    		if(newRegister){
    			locData.add(new SpawnLocation(loc,LocLevel.LOC200));
    		}
    	}else {
    		for(int i = 0;locData.size() > i;i++) {
    			SpawnLocation ld = locData.get(i);
    			if(ld.normal(loc)) {
    				locData.remove(i);
    				break;
    			}
    		}
    	}
   	}
    
    private static enum LocLevel{
    	NORMAL,
    	LOC200,
    	LOC400
    }
    
    private static class SpawnLocation{
    	private LocLevel level = LocLevel.NORMAL;
    	private LocLevel tempLevel;
    	private Location loc;
    	private World world;
    	
    	public SpawnLocation(Location loc,LocLevel tempLevel) {
    		this.loc = loc;
    		this.tempLevel = tempLevel;
    		world = loc.getWorld();
    	}
    	
    	public boolean normal(Location newLoc) {
    		if(newLoc.getWorld().equals(world)){
    			if(loc.distanceSquared(newLoc) < 50) {
    				if(tempLevel.equals(LocLevel.NORMAL)) {
    					LogManager.logInfo(TextBuilder.plus("エンティティの密集は200体未満まで改善しました。(","world:",world.getName(),"x:",String.valueOf(loc.getBlockX()),"y:",String.valueOf(loc.getBlockY()),"z:",String.valueOf(loc.getBlockZ()),")付近"), BaseAPI.getInstance(), Level.INFO);
    					MessageManager.sendNearPlayer(MessageLevel.IMPORTANT,loc , 50,"あなたの周囲でのエンティティの密集が一定程度改善されました。");
    					return true;
    				}
    				tempLevel = LocLevel.NORMAL;
    				return false;
    			}
    		}
    		return false;
    	}
    	public boolean loc200(Location newLoc) {
    		if(newLoc.getWorld().equals(world)){
    			if(loc.distanceSquared(newLoc) < 40) {
    				if(!level.equals(LocLevel.LOC200)) {
    					if(tempLevel.equals(LocLevel.LOC200)) {
    						if(level.equals(LocLevel.LOC400)) {
    							LogManager.logInfo(TextBuilder.plus("エンティティの密集は400体未満まで改善しました。(","world:",world.getName(),"x:",String.valueOf(loc.getBlockX()),"y:",String.valueOf(loc.getBlockY()),"z:",String.valueOf(loc.getBlockZ()),")付近"), BaseAPI.getInstance(), Level.INFO);
    						}else {
    							LogManager.logInfo(TextBuilder.plus("エンティティが200体以上密集しています。(","world:",world.getName(),"x:",String.valueOf(loc.getBlockX()),"y:",String.valueOf(loc.getBlockY()),"z:",String.valueOf(loc.getBlockZ()),")付近"), BaseAPI.getInstance(), Level.WARNING);
    							MessageManager.sendNearPlayer(MessageLevel.IMPORTANT,loc , 50,"あなたの周囲にエンティティが密集している場所があります。","何度も続く場合、運営による監査が行われることがありますのでご注意ください。");
    						}
        					level = LocLevel.LOC200;
        					return true;
        				}
        				tempLevel = LocLevel.LOC200;
    				}
    				return true;
    			}
    		}
    		return false;
    	}
    	public boolean loc400(Location newLoc) {
    		if(newLoc.getWorld().equals(world)){
    			if(loc.distanceSquared(newLoc) < 40) {
    				if(!level.equals(LocLevel.LOC400)) {
    					if(tempLevel.equals(LocLevel.LOC400)) {
    						LogManager.logInfo(TextBuilder.plus("エンティティが400体以上密集しています。(","world:",world.getName(),"x:",String.valueOf(loc.getBlockX()),"y:",String.valueOf(loc.getBlockY()),"z:",String.valueOf(loc.getBlockZ()),")付近"), BaseAPI.getInstance(), Level.SEVERE);
    						MessageManager.sendNearPlayer(MessageLevel.WARNING,loc , 100,"あなたの周囲にエンティティが異常に密集している場所があります。","原因に心当たりがある場合はできる限り早急にエンティティを減らしてください。","改善が見られない場合は運営による監査が行われることがあります。");
    						level = LocLevel.LOC400;
        					return true;
        				}
        				tempLevel = LocLevel.LOC400;
    				}
    				return true;
    			}
    		}
    		return false;
    	}
    }
}