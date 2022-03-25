package com.gmail.akashirt53072.minegame.match;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import com.gmail.akashirt53072.minegame.Main;
import com.gmail.akashirt53072.minegame.MessageManager;
import com.gmail.akashirt53072.minegame.SoundManager;
import com.gmail.akashirt53072.minegame.config.MatchLog;
import com.gmail.akashirt53072.minegame.config.PointData;
import com.gmail.akashirt53072.minegame.config.cache.LocationManager;
import com.gmail.akashirt53072.minegame.config.datatype.MatchPlayerData;
import com.gmail.akashirt53072.minegame.config.datatype.PlayerScoreData;
import com.gmail.akashirt53072.minegame.enums.GameType;
import com.gmail.akashirt53072.minegame.enums.MatchPlayerStatus;
import com.gmail.akashirt53072.minegame.enums.MatchStatus;
import com.gmail.akashirt53072.minegame.enums.MessageType;

import net.minecraft.server.v1_16_R3.NBTTagCompound;
import net.minecraft.server.v1_16_R3.NBTTagList;
import net.minecraft.server.v1_16_R3.NBTTagString;

public class Battle extends Match{
	ArrayList<BukkitRunnable> runnables = new ArrayList<BukkitRunnable>();
	ArrayList<PlayerScoreData> scoreData = new ArrayList<PlayerScoreData>();
	
	public Battle(Main plugin,int id ,GameType type) {
		super(plugin,id,type);
		onCreate();
	}
	
	//ゲーム開始
	@Override
	public void startGame() {
		
		//カウントダウン等イベントをセッティング
		
		BukkitRunnable e1 = new BukkitRunnable() {
            @Override
            public void run() {
            	ArrayList<Player> pls = getPlayers();
        		for(Player p : pls) {
        			p.sendTitle("開始まで", "2秒...", 0, 25, 0);
	        		SoundManager.send(p, Sound.BLOCK_NOTE_BLOCK_BIT, 1);
        		}
        	}
		};
		e1.runTaskLater(plugin, 20);
		runnables.add(e1);
		
		BukkitRunnable e2 = new BukkitRunnable() {
            @Override
            public void run() {
            	ArrayList<Player> pls = getPlayers();
        		for(Player p : pls) {
        			p.sendTitle("開始まで", "1秒...", 0, 25, 0);
        			SoundManager.send(p, Sound.BLOCK_NOTE_BLOCK_BIT, 1);
            	}
            }
		};
		e2.runTaskLater(plugin, 40);
		runnables.add(e2);
				
		
		BukkitRunnable e3 = new BukkitRunnable() {
            @Override
            public void run() {
            	ArrayList<Player> pls = getPlayers();
        		for(Player p : pls) {
        			p.removePotionEffect(PotionEffectType.SLOW_DIGGING);
        			p.removePotionEffect(PotionEffectType.SLOW);
        			p.removePotionEffect(PotionEffectType.JUMP);
        			SoundManager.send(p, Sound.BLOCK_ANVIL_DESTROY, (float)1.2);
            		
        			p.sendTitle("スタート!", "", 0, 10, 0);
        		}
            }
		};
		e3.runTaskLater(plugin, 60);
		runnables.add(e3);
		
		BukkitRunnable e51 = new BukkitRunnable() {
            @Override
            public void run() {
            	ArrayList<Player> pls = getPlayers();
        		for(Player p : pls) {
        			p.sendTitle("終了まで", "30秒...", 0, 20, 0);
        			SoundManager.send(p, Sound.BLOCK_NOTE_BLOCK_BIT, 1);
        		}
        	}
		};
		e51.runTaskLater(plugin, 660);
		runnables.add(e51);
		
		
		BukkitRunnable e101 = new BukkitRunnable() {
            @Override
            public void run() {
            	ArrayList<Player> pls = getPlayers();
        		for(Player p : pls) {
        			p.sendTitle("終了まで", "3秒...", 0, 25, 0);
        		}
        	}
		};
		e101.runTaskLater(plugin, 1200);
		runnables.add(e101);
		
		BukkitRunnable e102 = new BukkitRunnable() {
            @Override
            public void run() {
            	ArrayList<Player> pls = getPlayers();
        		for(Player p : pls) {
        			p.sendTitle("終了まで", "2秒...", 0, 25, 0);
        		}
            }
		};
		e102.runTaskLater(plugin, 1220);
		runnables.add(e102);
		
		BukkitRunnable e103 = new BukkitRunnable() {
            @Override
            public void run() {
            	ArrayList<Player> pls = getPlayers();
        		for(Player p : pls) {
        			p.sendTitle("終了まで", "1秒...", 0, 25, 0);
            	}
            }
		};
		e103.runTaskLater(plugin, 1240);
		runnables.add(e103);
		
		Match match = this;
		BukkitRunnable e104 = new BukkitRunnable() {
            @Override
            public void run() {
            	new MatchManager(plugin).endGame(match);
            }
		};
		e104.runTaskLater(plugin, 1300);
		runnables.add(e104);
		
		
		
		//鉱石類の生成
		Location oreStart = new LocationManager(plugin).getLocation(this, data.getMap().getType(), "orestart",data.getMap().getID());
		
		BukkitRunnable r1 = new BukkitRunnable() {
            @Override
            public void run() {
            	generateOre(oreStart,Material.STONE,10000);  	
        	}
		};
		r1.runTaskLater(plugin, 4);
		runnables.add(r1);
		
		BukkitRunnable r2 = new BukkitRunnable() {
            @Override
            public void run() {
        		generateOre(oreStart,Material.COAL_ORE,155); 	
        	}
		};
		r2.runTaskLater(plugin, 8);
		runnables.add(r2);
		
		BukkitRunnable r3 = new BukkitRunnable() {
            @Override
            public void run() {
        		generateOre(oreStart,Material.IRON_ORE,95);	
        	}
		};
		r3.runTaskLater(plugin, 12);
		runnables.add(r3);
		
		BukkitRunnable r4 = new BukkitRunnable() {
            @Override
            public void run() {
        		generateOre(oreStart,Material.GOLD_ORE,54); 	
        	}
		};
		r4.runTaskLater(plugin, 16);
		runnables.add(r4);
		
		BukkitRunnable r5 = new BukkitRunnable() {
            @Override
            public void run() {
        		generateOre(oreStart,Material.REDSTONE_ORE,65);	
        	}
		};
		r5.runTaskLater(plugin, 20);
		runnables.add(r5);

		BukkitRunnable r6 = new BukkitRunnable() {
            @Override
            public void run() {
        		generateOre(oreStart,Material.LAPIS_ORE,65);
        	}
		};
		r6.runTaskLater(plugin, 24);
		runnables.add(r6);

		BukkitRunnable r7 = new BukkitRunnable() {
            @Override
            public void run() {
        		generateOre(oreStart,Material.DIAMOND_ORE,36);
        	}
		};
		r7.runTaskLater(plugin, 28);
		runnables.add(r7);
		
		
		//鉱石リセット
		BukkitRunnable r11 = new BukkitRunnable() {
            @Override
            public void run() {
            	resetOre(oreStart,Material.STONE);  	
        	}
		};
		r11.runTaskLater(plugin, 1264);
		runnables.add(r11);
		
		BukkitRunnable r12 = new BukkitRunnable() {
            @Override
            public void run() {
        		resetOre(oreStart,Material.COAL_ORE); 	
        	}
		};
		r12.runTaskLater(plugin, 1268);
		runnables.add(r12);
		
		BukkitRunnable r13 = new BukkitRunnable() {
            @Override
            public void run() {
        		resetOre(oreStart,Material.IRON_ORE);	
        	}
		};
		r13.runTaskLater(plugin, 1272);
		runnables.add(r13);
		
		BukkitRunnable r14 = new BukkitRunnable() {
            @Override
            public void run() {
        		resetOre(oreStart,Material.GOLD_ORE); 	
        	}
		};
		r14.runTaskLater(plugin, 1276);
		runnables.add(r14);
		
		BukkitRunnable r15 = new BukkitRunnable() {
            @Override
            public void run() {
        		resetOre(oreStart,Material.REDSTONE_ORE);	
        	}
		};
		r15.runTaskLater(plugin, 1280);
		runnables.add(r15);

		BukkitRunnable r16 = new BukkitRunnable() {
            @Override
            public void run() {
        		resetOre(oreStart,Material.LAPIS_ORE);
        	}
		};
		r16.runTaskLater(plugin, 1284);
		runnables.add(r16);

		BukkitRunnable r17 = new BukkitRunnable() {
            @Override
            public void run() {
        		resetOre(oreStart,Material.DIAMOND_ORE);
        	}
		};
		r17.runTaskLater(plugin, 1288);
		runnables.add(r17);
		
		
		
		//プレイヤー
		
		ArrayList<Player> pls = getPlayers();
		for(int i = 0;i < pls.size();i ++ ) {
			Player p = pls.get(i);
			//メッセージ
			MessageManager.sendInfo("マインゲーム-バトルモード-", p);
			MessageManager.sendInfo("時間内にたくさんの鉱石を掘って他のプレイヤーよりも高い得点を目指しましょう!", p);
			int i1 = i + 1;
			Location loc = new LocationManager(plugin).getLocation(this, data.getMap().getType(), "spawn" + i1,data.getMap().getID());
			
			//tp
			p.teleport(loc);
			//ポーション
			p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING,1000,10,false,false,false));
			p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,1000,10,false,false,false));
			p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP,1000,255,false,false,false));
			
			//アイテム
			net.minecraft.server.v1_16_R3.ItemStack stack = CraftItemStack.asNMSCopy(new ItemStack(Material.DIAMOND_PICKAXE, 1));
		    
	        NBTTagList idsTag = new NBTTagList();
	        idsTag.add(NBTTagString.a("stone"));
	        idsTag.add(NBTTagString.a("coal_ore"));
	        idsTag.add(NBTTagString.a("diamond_ore"));
	        idsTag.add(NBTTagString.a("redstone_ore"));
	        idsTag.add(NBTTagString.a("lapis_ore"));
	        idsTag.add(NBTTagString.a("iron_ore"));
	        idsTag.add(NBTTagString.a("gold_ore"));
	        
	        NBTTagCompound tag = stack.hasTag() ? stack.getTag() : new NBTTagCompound();
	        
	        tag.set("CanDestroy", idsTag);
	        
	        stack.setTag(tag);
	        ItemStack item = CraftItemStack.asBukkitCopy(stack);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName("採掘ピッケル");
			meta.setUnbreakable(true);
			meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE,ItemFlag.HIDE_ENCHANTS,ItemFlag.HIDE_DESTROYS);
			meta.addEnchant(Enchantment.DIG_SPEED, 4, true);
			item.setItemMeta(meta);
			
			p.getInventory().addItem(item);
			
			//タイトル
			p.sendTitle("開始まで", "3秒...", 0, 25, 0);
			
			
			//スコア
	    	ScoreboardManager manager = Bukkit.getScoreboardManager();
			Scoreboard board = manager.getNewScoreboard();
			Objective obj = board.registerNewObjective("mg_single", "dummy", "-得点-");
			obj.setDisplaySlot(DisplaySlot.SIDEBAR);
			
			PointData pd = new PointData(plugin);
			
			obj.getScore("サーバー記録").setScore(pd.getAllTopPoint());
			
			obj.getScore("自己ベスト").setScore(pd.getMyTopPoint(p.getUniqueId()));
			
			for(Player p1 : pls) {
				obj.getScore(p1.getName()).setScore(0);
			}
			
			scoreData.add(new PlayerScoreData(p.getUniqueId(),p.getName(),board));
			p.setScoreboard(board);
		}
	}
	
	private void generateOre(Location oreStart,Material mate,int roll) {
		for(int i = 0;i < 41;i++) {
			Location oreLoc = oreStart.clone();
			oreLoc.setX(i + oreStart.getBlockX());
			for(int j = 0;j < 41;j++) {
				oreLoc.setZ(j + oreStart.getBlockZ());
				for(int k = 0;k < 4;k++) {
					oreLoc.setY(k + oreStart.getBlockY());
					int result = new Random().nextInt(10000);
					if(result < roll) {
						oreLoc.getBlock().setType(mate);
					}
				}
			}
		}
	}
	
	private void resetOre(Location oreStart,Material mate) {
		for(int i = 0;i < 41;i++) {
			Location oreLoc = oreStart.clone();
			oreLoc.setX(i + oreStart.getBlockX());
			for(int j = 0;j < 41;j++) {
				oreLoc.setZ(j + oreStart.getBlockZ());
				for(int k = 0;k < 4;k++) {
					oreLoc.setY(k + oreStart.getBlockY());
					if(oreLoc.getBlock().getType().equals(mate)) {
						oreLoc.getBlock().setType(Material.AIR);	
					}
				}
			}
		}
	}
	
	public void onMine(Player player,Material mate) {
		switch(mate) {
		case STONE:

        	addScore(10,player);
			break;
		case DIAMOND_ORE:

        	addScore(10000,player);
			player.getInventory().addItem(new ItemStack(Material.DIAMOND,1));
			break;
		case COAL_ORE:

        	addScore(500,player);
			
			player.getInventory().addItem(new ItemStack(Material.COAL,1));
			break;
		case GOLD_ORE:
			BukkitRunnable e1 = new BukkitRunnable() {
	            @Override
	            public void run() {
	            	if(!player.isOnline()){
	            		return;
	            	}
	            	for(MatchPlayerData pd : data.getWait().getPlayers()){
	        			if(pd.getUUID().equals(player.getUniqueId())) {
	        				if(pd.getStatus().equals(MatchPlayerStatus.PLAYING)) {
		        				break;
		        			}
	        				return;
	        			}
	        		}
	            	
	            	addScore(5000,player);
	            	
        			
	            	int index = player.getInventory().first(Material.GOLD_ORE);
	            	ItemStack i = player.getInventory().getItem(index);
	        		int a = i.getAmount() - 1;
	        		if(a == 0) {
	        			player.getInventory().clear(index);
	        		}else {
	        			i.setAmount(a);
	        			player.getInventory().setItem(index, i);
	        		}
	        		SoundManager.send(player, Sound.BLOCK_LAVA_EXTINGUISH, 1);
	        		player.getInventory().addItem(new ItemStack(Material.GOLD_INGOT,1));
	        	}
			};
			e1.runTaskLater(plugin, 400);
			runnables.add(e1);

			player.getInventory().addItem(new ItemStack(Material.GOLD_ORE,1));
			break;
		case IRON_ORE:
			BukkitRunnable e2 = new BukkitRunnable() {
	            @Override
	            public void run() {
	            	if(!player.isOnline()){
	            		return;
	            	}
	            	for(MatchPlayerData pd : data.getWait().getPlayers()){
	        			if(pd.getUUID().equals(player.getUniqueId())) {
	        				if(pd.getStatus().equals(MatchPlayerStatus.PLAYING)) {
		        				break;
		        			}
	        				return;
	        			}
	        		}
	            	
	            	
	            	addScore(1000,player);
	            	
	            	int index = player.getInventory().first(Material.IRON_ORE);
	            	ItemStack i = player.getInventory().getItem(index);
	        		int a = i.getAmount() - 1;
	        		if(a == 0) {
	        			player.getInventory().clear(index);
	        		}else {
	        			i.setAmount(a);
	        			player.getInventory().setItem(index, i);
	        		}
	        		SoundManager.send(player, Sound.BLOCK_LAVA_EXTINGUISH, 1);
	        		player.getInventory().addItem(new ItemStack(Material.IRON_INGOT,1));            	
	        	}
			};
			e2.runTaskLater(plugin, 200);
			runnables.add(e2);

			player.getInventory().addItem(new ItemStack(Material.IRON_ORE,1));
			break;
		case LAPIS_ORE:
			SoundManager.send(player, Sound.ENTITY_PLAYER_LEVELUP, 2);
			player.addPotionEffect(new PotionEffect(PotionEffectType.LUCK,400,0,false,false,true));
			addScore(100,player);
			break;
		case REDSTONE_ORE:
			
    		player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING,100,21,false,false,true));

    		SoundManager.send(player, Sound.BLOCK_ANVIL_PLACE, 2);

			player.getInventory().addItem(new ItemStack(Material.REDSTONE,1));
			addScore(300,player);
			
			
			break;
		default:
		}
	}
	
	private void addScore(int score,Player p) {
		int now = 0;
		String name = "";
		for(PlayerScoreData sd : scoreData) {
			if(sd.getUUID().equals(p.getUniqueId())){
				now = sd.getPoint();
				name = sd.getName();
				if(p.hasPotionEffect(PotionEffectType.LUCK)) {
					score *= 2;
				}
				now += score;
				sd.setPoint(now);
				break;
			}
		}
		for(int i = 0 ;i < scoreData.size();i++ ) {
			PlayerScoreData sd = scoreData.get(i);
			Scoreboard board = sd.getBoard();
			Objective obj = board.getObjective("mg_single");
			
			obj.getScore(name).setScore(now);
			int p1 = sd.getPoint();
			
			int pos = 1;
			for(PlayerScoreData sd2 : scoreData) {
				int p2 = sd2.getPoint();
				if(p1 < p2){
					pos ++;
				}
			}
			
			sd.setPosition(pos);
			Player player1 = Bukkit.getPlayer(sd.getUUID());
			if(player1 == null) {
				break;
			}
			player1.setScoreboard(board);
			
		}
		
	}
	
	//ゲームとしての終了処理
	@Override
	public void endGame() {
		
		//片付け
		for(BukkitRunnable r : runnables){
			r.cancel();
		}
		//鉱石リセット
		Location oreStart = new LocationManager(plugin).getLocation(this, data.getMap().getType(), "orestart",data.getMap().getID());
		generateOre(oreStart,Material.AIR,10000);
		
	}
	
	//プレイヤーとしての終了の処理
	@Override
	public void onPlayerEnd(Player player) {
		//点数保存
		int point = 0;
		int position = 0;
		for(PlayerScoreData sd : scoreData) {
			if(sd.getUUID().equals(player.getUniqueId())) {
				point = sd.getPoint();
				position = sd.getPosition();
				break;
			}
		}
		//成績発表
		MessageManager.sendInfo("マインゲーム-バトルモード-が終了しました!", player);		
		MessageManager.sendInfo("獲得ポイントは" + ChatColor.GOLD + point + "ポイント" + ChatColor.WHITE + "で" + ChatColor.LIGHT_PURPLE + position + "位" + ChatColor.WHITE + "でした!", player);
		
		if(new PointData(plugin).getMyTopPoint(player.getUniqueId()) < point) {
			MessageManager.sendImportant("自己ベスト更新!", player);
		}
		if(new PointData(plugin).getAllTopPoint() < point) {
			MessageManager.sendAllPlayer(player.getName() + "が" + ChatColor.GOLD + point + "ポイント" + ChatColor.WHITE + "でサーバー記録を更新!", MessageType.IMPORTANT, plugin);			
		}
		new PointData(plugin).updatePoint(player, point);
		resetScore(player);
		
		int price = 15;
		for(int i = position; i < 5;i++) {
			price += 5;	
		}
		
		price += point / 10000;
		
		Bukkit.getScoreboardManager().getMainScoreboard().getObjective("vil_score").getScore(player.getName()).setScore(price);
		new MatchLog(plugin,this).onUpdate("[バトル]" + point + "ポイントで" + position + "位で" + price + "vilゲット", player);
		
	}
	

	//scoreリセット
	private void resetScore(Player p) {
		for(PlayerScoreData sd : scoreData) {
			if(sd.getUUID().equals(p.getUniqueId())) {
				scoreData.remove(sd);
				break;
			}
		}
		p.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
	}
	
	//プレイヤーのマッチング参加
	@Override
	public void onPlayerJoin(Player player) {
		if(getPlayers().size() == 8) {
			data.setStatus(MatchStatus.FULL);	
		}
		
		//マップに直接TP
		Location loc = new LocationManager(plugin).getLocation(this, data.getMap().getType(), "wait",data.getMap().getID());
		player.teleport(loc);
		
		player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION,100000,0,false,false,false));
		player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,100000,0,false,false,false));
		
		MessageManager.sendImportant("マインゲーム-バトルモード-", player);
		MessageManager.sendInfo("時間内にたくさんの鉱石を掘って他のプレイヤーよりも高い得点を目指しましょう!", player);
		
	}
		
	//マッチングからの退出(コマンド/ログアウト/看板)
	@Override
	public void onWaitPlayerLeave(Player player) {
		MessageManager.sendInfo("マインゲーム-バトルモード-から退出しました。", player);
		data.setStatus(MatchStatus.WAITING);
	}
	
	//試合からの退出(コマンド)
	@Override
	public void onPlayerLeave(Player player) {
		MessageManager.sendInfo("マインゲーム-バトルモード-の試合を途中で終了しました。", player);
		resetScore(player);
	}
		
	//試合からのキック時
	@Override
	public void onPlayerKick(Player player) {
		resetScore(player);
	}
		
	//試合からの退出(ログアウト)
	@Override
	public void onPlayerBreak(Player player) {
		for(Player p : getPlayers()){
			MessageManager.sendImportant(player.getName() + "がログアウトし、試合を一時退出しました。", p);
		}
	}
		
	//試合への復帰
	@Override
	public void onPlayerReturn(Player player) {
		for(Player p : getPlayers()){
			MessageManager.sendImportant(player.getName() + "が試合に復帰しました。", p);
		}
	}
		
	@Override
	public void onChangeMap() {
		Location loc = new LocationManager(plugin).getLocation(this, data.getMap().getType(), "wait",data.getMap().getID());
		ArrayList<Player> pls = getPlayers();
		for(Player p : pls) {
			//マップに直接TP
			p.teleport(loc);
		}
		
	}
		
	//非常時全体処理
	@Override
	public void onEmergency() {
		for(BukkitRunnable r : runnables){
			r.cancel();
		}
		//鉱石リセット
		Location oreStart = new LocationManager(plugin).getLocation(this, data.getMap().getType(), "orestart",data.getMap().getID());
		generateOre(oreStart,Material.AIR,10000);
		
	}
		
	//非常時のプレイヤー処理
	@Override
	public void onEmergency(Player player) {
		
	}
	//プレイヤーが途中で減る場合
	public void checkGameEnd() {
		//playingが残り0人の時にゲーム終了へ
		int playing = 0;
		for(MatchPlayerData pd : data.getWait().getPlayers()){
			if(pd.getStatus().equals(MatchPlayerStatus.PLAYING)) {
				playing ++;
			}
		}
		if(playing < 1){
			new MatchManager(plugin).endGame(this);
		}
	}
	
	public boolean checkGameStart() {
		//playingが1人~の時にゲーム開始へ
		int playing = 0;
		for(MatchPlayerData pd : data.getWait().getPlayers()){
			if(pd.getStatus().equals(MatchPlayerStatus.WAITING)) {
				playing ++;
			}
		}
		if(playing > 1){
			return true;
		
		}
		return false;
	}
	
	//チーム決めるときに使うと良き
	@Override
	protected void decideTeam() {
	}
		
	//コンストラクタの追記に使うと良き
	@Override
	protected void onCreate() {
	}
	
}
