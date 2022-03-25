package com.gmail.akashirt53072.minegame.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import com.gmail.akashirt53072.minegame.Main;
import com.gmail.akashirt53072.minegame.MessageManager;
import com.gmail.akashirt53072.minegame.config.PointData;
import com.gmail.akashirt53072.minegame.config.cache.SignManager;
import com.gmail.akashirt53072.minegame.enums.GameType;
import com.gmail.akashirt53072.minegame.enums.SignType;
import com.gmail.akashirt53072.minegame.match.MatchManager;


public final class PlayerClick implements Listener{
	Main plugin;
	public PlayerClick(Main plugin) {
    	this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    //左右クリック
	@EventHandler(priority = EventPriority.NORMAL)
	public void onClick(PlayerInteractEvent event) {
    	Action action = event.getAction();
    	Player player = event.getPlayer();
    	//blockクリック
    	if (action == Action.LEFT_CLICK_BLOCK || action == Action.RIGHT_CLICK_BLOCK){
    		Block block = event.getClickedBlock();
			if(block.getType().equals(Material.BIRCH_SIGN)
        			|| block.getType().equals(Material.OAK_SIGN)
        			|| block.getType().equals(Material.OAK_WALL_SIGN)
        			|| block.getType().equals(Material.BIRCH_WALL_SIGN)
        			) {
        		if(onSignClick(player,block)) {
            		return;	
        		}
        	}
        }
    	//その他
    	if (action == Action.LEFT_CLICK_BLOCK || action == Action.LEFT_CLICK_AIR){
    		if(player.getInventory().getItemInMainHand() == null) {
    			return;
    		}
    		onLItemClick(player);
        }else if (action == Action.RIGHT_CLICK_BLOCK || action == Action.RIGHT_CLICK_AIR){
        	if(player.getInventory().getItemInMainHand() == null) {
        		return;
    		}
        	onRItemClick(player);
    	}
    }
	private boolean onSignClick(Player p,Block b) {
		//config行ってブロック座標でid持ってきてクラス分岐
		Location loc = b.getLocation();
		SignType type = new SignManager(plugin).getSignType(loc.getBlockX(),loc.getBlockY(),loc.getBlockZ());
		switch(type) {
		case NOTFOUND:
			return false;
		case JOIN:
			new MatchManager(plugin).joinMatch(GameType.SINGLE,p);
			break;
		case JOINBATTLE:
			new MatchManager(plugin).joinMatch(GameType.BATTLE,p);
			break;
		case QUIT:
			new MatchManager(plugin).logoutWaitMatch(p);
			break;
		case START:
			new MatchManager(plugin).startGame(p);
			break;
		case CHANGEMAP:
			new MatchManager(plugin).openSelectMap(p);
			break;
		case TEXT:
			MessageManager.sendImportant("ゲームが始まると以下のような様々な効果を持った鉱石が生成されます。", p);
			MessageManager.sendInfo("・すぐに得点になる石炭", p);
			MessageManager.sendInfo("・一定時間後に得点になる鉄と金", p);
			MessageManager.sendInfo("・一定時間採掘速度が上がるレッドストーン", p);
			MessageManager.sendInfo("・一定時間獲得ポイントが2倍になるラピスラズリ", p);
			MessageManager.sendInfo("・獲得ポイントが最高のダイヤモンド", p);
			MessageManager.sendImportant("制限時間内にできるだけ多くの鉱石を掘って高得点を目指しましょう!", p);
			break;
		case RANK:
			new PointData(plugin).sendTopPoint(p);
			break;
		}
		return true;
	}
	private void onRItemClick(Player p) {
		//NBT行ってアイテム種でid持ってきてクラス分岐
	}
	private void onLItemClick(Player p) {
		//NBT行ってアイテム種でid持ってきてクラス分岐
	}
}