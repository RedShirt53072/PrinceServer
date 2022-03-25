package com.gmail.akashirt53072.minegame;


import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;



public class PlayerTick extends BukkitRunnable  {
	private Player player;
	private Main plugin;
	
	public PlayerTick(Main plugin,Player player) {
	    this.player = player;
	    this.plugin = plugin;
	}

	@Override
	public void run() {
		
		new PlayerTick(plugin,player).runTaskLater(plugin,1);
	}
}