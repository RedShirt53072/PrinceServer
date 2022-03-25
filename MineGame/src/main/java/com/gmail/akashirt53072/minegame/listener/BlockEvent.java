package com.gmail.akashirt53072.minegame.listener;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

import com.gmail.akashirt53072.minegame.Main;
import com.gmail.akashirt53072.minegame.config.ErrorLog;
import com.gmail.akashirt53072.minegame.config.SignConfig;
import com.gmail.akashirt53072.minegame.enums.GameType;
import com.gmail.akashirt53072.minegame.enums.PlayerStatus;
import com.gmail.akashirt53072.minegame.match.Battle;
import com.gmail.akashirt53072.minegame.match.Match;
import com.gmail.akashirt53072.minegame.match.MatchManager;
import com.gmail.akashirt53072.minegame.match.Single;
import com.gmail.akashirt53072.minegame.nbt.NBTPlayerStatus;


public final class BlockEvent implements Listener{
	Main plugin;
	public BlockEvent(Main plugin) {
    	this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
	
	@EventHandler
    public void onBlockBreak(final BlockBreakEvent event) {
		Block block = event.getBlock();
    	if(block.getType().equals(Material.BIRCH_SIGN) 
    	|| block.getType().equals(Material.BIRCH_WALL_SIGN)
    	|| block.getType().equals(Material.OAK_WALL_SIGN)
    	|| block.getType().equals(Material.OAK_SIGN)){
        	new SignConfig(plugin).deleteSign(block.getLocation());
    		return;
    	}
    	Player player = event.getPlayer();
    	if(!new NBTPlayerStatus(plugin,player).getType().equals(PlayerStatus.ONMATCH)) {
    		
    		return;
    	}
    	
    	if(block.getType().equals(Material.DIAMOND_ORE)
    	|| block.getType().equals(Material.COAL_ORE)
    	|| block.getType().equals(Material.GOLD_ORE)
    	|| block.getType().equals(Material.IRON_ORE)
    	|| block.getType().equals(Material.LAPIS_ORE)
    	|| block.getType().equals(Material.REDSTONE_ORE)
    	|| block.getType().equals(Material.STONE)
    		) {
    		Match match = new MatchManager(plugin).getMatch(player);
    		if(match == null) {
    			new ErrorLog(plugin).writeError("BlockEvent.onBlockBreak", player.getName() + "は試合に参加していません。");
    			
    			return;
    		}
    		event.setDropItems(false);
    		if(match.getData().getType().equals(GameType.SINGLE)){
    			Single s = (Single)match;
    			s.onMine(player,block.getType());
    		}else if(match.getData().getType().equals(GameType.BATTLE)) {
    			Battle b = (Battle)match;
    			b.onMine(player,block.getType());
    		}
    		return;
    	}
    	
	}
	@EventHandler
	public void onDrop(PlayerDropItemEvent event) {
		Player player = event.getPlayer();
    	if(!new NBTPlayerStatus(plugin,player).getType().equals(PlayerStatus.ONMATCH)) {
    		return;
    	}
    	event.setCancelled(true);
    	
    	
	}
}