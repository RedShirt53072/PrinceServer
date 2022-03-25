package com.gmail.akashirt53072.minegame.match;


import java.util.ArrayList;

import org.bukkit.entity.Player;

import com.gmail.akashirt53072.minegame.Main;
import com.gmail.akashirt53072.minegame.config.cache.LocationManager;
import com.gmail.akashirt53072.minegame.config.datatype.MatchData;
import com.gmail.akashirt53072.minegame.config.datatype.MatchPlayerData;
import com.gmail.akashirt53072.minegame.enums.GameType;
import com.gmail.akashirt53072.minegame.enums.MatchPlayerStatus;
import com.gmail.akashirt53072.minegame.gui.GuiManager;
import com.gmail.akashirt53072.minegame.gui.SelectMapGui;

public abstract class Match {
	protected Main plugin;
	protected MatchData data;
	
	public Match(Main plugin,int id ,GameType type) {
		this.plugin = plugin;
		this.data = new MatchData(id,type,plugin);
		onCreate();
	}
	public MatchData getData() {
		return data;
	}
	
	//ゲーム開始
	abstract public void startGame();
	
	//ゲームが完全に終了する場合
	abstract public void endGame();
	
	//プレイヤーのマッチング参加
	abstract public void onPlayerJoin(Player player);
	
	//マッチングからの退出(コマンド/ログアウト/看板)
	abstract public void onWaitPlayerLeave(Player player);
	
	//試合からの退出(コマンド)
	abstract public void onPlayerLeave(Player player);
	
	//試合からのキック時
	abstract public void onPlayerKick(Player player);
	
	//試合からの退出(ログアウト)
	abstract public void onPlayerBreak(Player player);
	
	//試合への復帰
	abstract public void onPlayerReturn(Player player);
	
	//プレイヤーが途中で終了する場合
	abstract public void onPlayerEnd(Player player);
	
	//マップ変更
	abstract public void onChangeMap();
		
	//非常時全体処理
	abstract public void onEmergency();
	
	//非常時のプレイヤー処理
	abstract public void onEmergency(Player player);
	
	
	public ArrayList<Player> getPlayers() {
		ArrayList<Player> players = new ArrayList<Player>();
		for(MatchPlayerData pd : data.getWait().getPlayers()){
			if(pd.getStatus().equals(MatchPlayerStatus.PLAYING) || pd.getStatus().equals(MatchPlayerStatus.WAITING)) {
				Player p = plugin.getServer().getPlayer(pd.getUUID());
				if(p == null) {
					continue;
				}
				players.add(p);
			}
		}
		return players;
	}
	
	
	//プレイヤーが途中で減る場合
	public void checkGameEnd() {
		//playingが残り1人の時にゲーム終了へ
		int playing = 0;
		for(MatchPlayerData pd : data.getWait().getPlayers()){
			if(pd.getStatus().equals(MatchPlayerStatus.PLAYING)) {
				playing ++;
			}
		}
		if(playing < 2){
			new MatchManager(plugin).endGame(this);
		}
	}
	
	public boolean checkGameStart() {
		return true;
	}
	
	
	//同じチームのプレイヤーにチャットを送る
	public void chatTeamPlayer(String text,Player player) {
		player.sendMessage(text);
	}
	
	
	//マップ選択開いた時
	public void onOpenGui(SelectMapGui gui) {
		data.setGui(gui);
	}
	
	//インスタンス削除前
	public void onremove() {
		if(data.getGui() != null) {
			new GuiManager(plugin).onEmergency(data.getGui());
		}
		new LocationManager(plugin).returnMap(data.getMap());
	}
	
	//チーム決めるときに使うと良き
	abstract protected void decideTeam();
	
	//コンストラクタの追記に使うと良き
	abstract protected void onCreate();
	
}
