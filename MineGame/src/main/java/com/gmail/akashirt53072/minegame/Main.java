package com.gmail.akashirt53072.minegame;

import java.util.ArrayList;

import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.gmail.akashirt53072.minegame.command.JoinCommand;
import com.gmail.akashirt53072.minegame.command.SettingCommand;
import com.gmail.akashirt53072.minegame.config.ConfigLoader;
import com.gmail.akashirt53072.minegame.config.GeneralData;
import com.gmail.akashirt53072.minegame.config.LocationConfig;
import com.gmail.akashirt53072.minegame.config.SignConfig;
import com.gmail.akashirt53072.minegame.config.datatype.LocResorceData;
import com.gmail.akashirt53072.minegame.config.datatype.LocationData;
import com.gmail.akashirt53072.minegame.enums.PlayerStatus;
import com.gmail.akashirt53072.minegame.gui.Gui;
import com.gmail.akashirt53072.minegame.gui.GuiManager;
import com.gmail.akashirt53072.minegame.listener.BlockEvent;
import com.gmail.akashirt53072.minegame.listener.PlayerChat;
import com.gmail.akashirt53072.minegame.listener.PlayerClick;
import com.gmail.akashirt53072.minegame.listener.PlayerInvAction;
import com.gmail.akashirt53072.minegame.listener.PlayerLogin;
import com.gmail.akashirt53072.minegame.match.Match;
import com.gmail.akashirt53072.minegame.match.MatchManager;
import com.gmail.akashirt53072.minegame.nbt.NBTPlayerStatus;



public class Main extends JavaPlugin {
	
	private ArrayList<Match> matchList = new ArrayList<Match>();
	private ArrayList<Gui> guiList = new ArrayList<Gui>();
	private int nowMatchID = 0;
	
	private ArrayList<LocationData> signData;
	private LocResorceData locData;
	
	
	@Override
	public void onEnable() {
		//config
		new ConfigLoader(this,"config_sign.yml").saveDefaultConfig();
		new ConfigLoader(this,"config_loc.yml").saveDefaultConfig();

		new ConfigLoader(this,"data_general.yml").saveDefaultConfig();
		new ConfigLoader(this,"data_point.yml").saveDefaultConfig();
		

		new ConfigLoader(this,"log_error.yml").saveDefaultConfig();
		new ConfigLoader(this,"log_match.yml").saveDefaultConfig();
		
		nowMatchID = new GeneralData(this).getMatchID();
		
		//listener
		new PlayerLogin(this);
		new PlayerClick(this);
		new PlayerChat(this);
		new PlayerInvAction(this);
		new BlockEvent(this);
		
		//command
		this.getCommand("stellamine").setExecutor(new JoinCommand(this));
		this.getCommand("opmine").setExecutor(new SettingCommand(this));
		loadConfig();
	}
	@Override
	public void onDisable() {
		//試合終了と全プレイヤーkick
		for(int i =  matchList.size() - 1;i > -1 ;i--) {
			new MatchManager(this).emergencyStop(matchList.get(i) ,"システムの終了");
		}
		for(Player p : getPlayers()) {
			new PlayerManager(this,p).kick("システムの終了");
		}
		
		new GeneralData(this).setMatchID(nowMatchID);
		
		
		//GUIのリセット
		for(int i =  guiList.size() - 1;i > -1 ;i--) {
			ArrayList<Player> pls = guiList.get(i).getPlayers();
			ArrayList<Player> pls2 = new ArrayList<Player>();
			for(Player p : pls) {
				pls2.add(p);
			}
			for(Player p : pls2){
				new GuiManager(this).close(p);
			}
		}
	
	}
	
	public void loadConfig(){
		//試合終了と全プレイヤーkick
		for(int i =  matchList.size() - 1;i > -1 ;i--) {
			new MatchManager(this).emergencyStop(matchList.get(i) ,"システムの再読み込み");
		}
		for(Player p : getPlayers()) {
			new PlayerManager(this,p).kick("システムの再読み込み");
		}
		
		//GUIのリセット
		for(int i =  guiList.size() - 1;i > -1 ;i--) {
			ArrayList<Player> pls = guiList.get(i).getPlayers();
			ArrayList<Player> pls2 = new ArrayList<Player>();
			for(Player p : pls) {
				pls2.add(p);
			}
			for(Player p : pls2){
				new GuiManager(this).close(p);
			}
		}
		
		//configデータベース読み込み
		
		
		//config読み込み
		signData = new SignConfig(this).getAllData();
		locData = new LocationConfig(this).getAllData();
		
		
		return;
	}
	
	public void loadSign() {
		signData = new SignConfig(this).getAllData();
	}
	
	public World getMainWorld() {
		for(World w : getServer().getWorlds()) {
			if(w.getEnvironment().equals(Environment.NORMAL)){
				return w;
			}
		}
		return null;
	}
	
	public ArrayList<Player> getPlayers() {
		World w = getMainWorld();
		if(w == null) {
			return new ArrayList<Player>();
		}
		ArrayList<Player> data = new ArrayList<Player>();
		for(Player p : w.getPlayers()) {
			PlayerStatus type = new NBTPlayerStatus(this,p).getType();
			if(type.equals(PlayerStatus.lOGOUT) || type.equals(PlayerStatus.BREAKING)) {
				continue;
			}
			data.add(p);
		}
		return data;
		
	}
	
	public Player getPlayer(String name) {
		Player p = getServer().getPlayer(name);
		if(p == null) {
			return null;
		}
		PlayerStatus type = new NBTPlayerStatus(this,p).getType();
		if(type.equals(PlayerStatus.lOGOUT) || type.equals(PlayerStatus.BREAKING)) {
			return null;
		}
		return p;
	}
	
	public ArrayList<Gui> getGuiData(){
		return guiList;
	}
	
	public ArrayList<Match> getMatchData(){
		return matchList;
	}
	
	public int getNextMatchID(){
		nowMatchID ++;
		return nowMatchID;
	}
	
	public ArrayList<LocationData> getSignData(){
		return signData;
	}
	public LocResorceData getLocationData(){
		return locData;
	}
	
	
}
