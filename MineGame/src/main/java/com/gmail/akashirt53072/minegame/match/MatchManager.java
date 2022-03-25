package com.gmail.akashirt53072.minegame.match;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import com.gmail.akashirt53072.minegame.Main;
import com.gmail.akashirt53072.minegame.MessageManager;
import com.gmail.akashirt53072.minegame.PlayerManager;
import com.gmail.akashirt53072.minegame.config.ErrorLog;
import com.gmail.akashirt53072.minegame.config.MatchLog;
import com.gmail.akashirt53072.minegame.config.cache.LocationManager;
import com.gmail.akashirt53072.minegame.config.datatype.MapLocData;
import com.gmail.akashirt53072.minegame.config.datatype.MatchPlayerData;
import com.gmail.akashirt53072.minegame.config.datatype.MatchTeamData;
import com.gmail.akashirt53072.minegame.enums.GameType;
import com.gmail.akashirt53072.minegame.enums.MatchPlayerStatus;
import com.gmail.akashirt53072.minegame.enums.MatchStatus;
import com.gmail.akashirt53072.minegame.enums.MessageType;
import com.gmail.akashirt53072.minegame.enums.PlayerStatus;
import com.gmail.akashirt53072.minegame.gui.GuiID;
import com.gmail.akashirt53072.minegame.gui.GuiManager;
import com.gmail.akashirt53072.minegame.gui.SelectMapGui;
import com.gmail.akashirt53072.minegame.nbt.NBTPlayerStatus;

public class MatchManager {
	private Main plugin;
	private ArrayList<Match> allData;
	public MatchManager(Main plugin) {
		this.plugin = plugin;
		allData = plugin.getMatchData();
	}
	/*
	id: 1
	match1:
  		id: 0
  		type: battleroyale8
  		status: STARTED
  		wait:
  			name:
      		player1:
      			etc...
  		team1:
      		name:
      		player1:
        		name: aaa
        		uuid: xxx
        		status: PLAYING
	*/
	
	public Match getMatch(Player player){
		for(Match m : allData) {
			for(MatchPlayerData p : m.getData().getWait().getPlayers()){

				if(p.getUUID().equals(player.getUniqueId())){
					return m;
				}	
			}
		}
		return null;
	}
	private MatchPlayerData getPlayerData(Match match,Player player){
		for(MatchTeamData t : match.getData().getTeams()){
			for(MatchPlayerData p : t.getPlayers()){
				if(p.getUUID().equals(player.getUniqueId())){
					return p;
				}	
			}
		}
		return null;
	}
	private MatchPlayerData getWaitPlayerData(Match match,Player player){
		for(MatchPlayerData p : match.getData().getWait().getPlayers()){
			if(p.getUUID().equals(player.getUniqueId())){
				return p;
			}	
		}
		return null;
	}
	
	
	
	//プレイヤーがログアウト/コマンド退出し、waitから退出
	public void logoutWaitMatch(Player player) {
		Match match = getMatch(player);
		if(match == null) {
			new ErrorLog(plugin).writeError("MatchManager.logoutWaitMatch", player.getName() + "は試合に参加していません");
			return;
		}
		match.getData().getWait().getPlayers().remove(getWaitPlayerData(match,player));
		
		MessageMatchMenber(match,player.getName() + "がマッチングから退出しました。",MessageType.INFO);
		
		new MatchLog(plugin,match).onUpdate("マッチング退出", player);

		match.onWaitPlayerLeave(player);

		new PlayerManager(plugin,player).goToLobby();		
	}
			
	//プレイヤーが試合から落ちた時(回線落ちとか)
	public void breakMatch(Player player) {
		//マッチを持ってくる、ないならreturn
		Match match = getMatch(player);
		if(match == null) {
			new ErrorLog(plugin).writeError("MatchManager.breakMatch", player.getName() + "は試合に参加していません");
			return;
		}
		//状態更新
		MatchPlayerData playerData = getPlayerData(match,player);
		if(playerData != null) {
			playerData.setStatus(MatchPlayerStatus.BREAKING);
		}
		MatchPlayerData waitPlayerData = getWaitPlayerData(match,player);
		waitPlayerData.setStatus(MatchPlayerStatus.BREAKING);

		new NBTPlayerStatus(plugin,player).setType(PlayerStatus.BREAKING);
		
		new MatchLog(plugin,match).onUpdate("ログアウト", player);
		
		
		//個別処理
		match.onPlayerBreak(player);
		
		//試合終わらないかチェック
		match.checkGameEnd();		
	}
	
	//プレイヤーが試合に戻った時
	public void returnMatch(Player player) {
		
		Match match = getMatch(player);
		
		if(match == null) {
			//ログアウト(break)→ロビー
			//リセット+ロビーへ
			MessageManager.sendImportant("試合が終了しているため、再ログイン時の試合復帰はできませんでした。", player);
			
			new PlayerManager(plugin,player).goToLobby();
			return;
		}
		//ログアウト(break)→試合
		
		//状態更新
		MatchPlayerData playerData = getPlayerData(match,player);
		if(playerData != null) {
			playerData.setStatus(MatchPlayerStatus.PLAYING);
		}
		MatchPlayerData waitPlayerData = getWaitPlayerData(match,player);
		waitPlayerData.setStatus(MatchPlayerStatus.PLAYING);
				
		new NBTPlayerStatus(plugin,player).setType(PlayerStatus.ONMATCH);
		
		new MatchLog(plugin,match).onUpdate("リログして復帰", player);
		
		//個別処理
		match.onPlayerReturn(player);
	}
	
	//プレイヤーが試合から抜けた時(コマンドでサーバーロビー行ったとか)
	public void leaveMatch(Player player) {
		Match match = getMatch(player);
		if(match == null) {
			new ErrorLog(plugin).writeError("MatchManager.leaveMatch", player.getName() + "は試合に参加していません");
			return;
		}
		
		
		//状態更新
		MatchPlayerData playerData = getPlayerData(match,player);
		if(playerData != null) {
			playerData.setStatus(MatchPlayerStatus.LEFT);	
		}
			
		MatchPlayerData waitPlayerData = getWaitPlayerData(match,player);
		waitPlayerData.setStatus(MatchPlayerStatus.LEFT);
		
		new NBTPlayerStatus(plugin,player).setType(PlayerStatus.WAITING);
		
		new MatchLog(plugin,match).onUpdate("自主離脱", player);
		
		
		//個別処理
		match.onPlayerLeave(player);
		
		//試合終わらないかチェック
		match.checkGameEnd();
		
	}
	
	public void changeMap(Player player,MapLocData map) {
		Match match = getMatch(player);
		if(match == null) {
			new ErrorLog(plugin).writeError("MatchManager.changeMap", player.getName() + "は試合に参加していません");
			return;
		}
		match.getData().setMap(map);
		match.onChangeMap();
		Bukkit.getScheduler().runTask(plugin, new Runnable() {
            @Override
            public void run() {
        		match.getData().getGui().updateMapData(new LocationManager(plugin).getUsableMap(match.getData().getType()), map);
        	}
		});
	}
	
	public void openSelectMap(Player player) {
		Match match = getMatch(player);
		if(match == null) {
			new ErrorLog(plugin).writeError("MatchManager.openSelectMap", player.getName() + "は試合に参加していません");
			return;
		}
		SelectMapGui gui = (SelectMapGui) new GuiManager(plugin).openPublicGui(GuiID.SELECTMAP, player);
		ArrayList<MapLocData> data = new LocationManager(plugin).getUsableMap(match.getData().getType());
		gui.updateMapData(data,match.getData().getMap());
		match.onOpenGui(gui);
	}
	
	
	public void joinMatch(GameType type,Player player) {
		//waitのマッチを探し、なければ作る
		Match match = getWaitMatch(type);
		
		if(match == null) {
			match = createMatch(type);
		}
		
		if(match == null) {
			//マップがいっぱいなどの問題が発生
			MessageManager.sendImportant("マップの確保ができなかったため、マッチングを始められませんでした。", player);
			MessageManager.sendImportant("時間を置いてからもう一度試してみてください。", player);
			return ;
		}
		
		//マッチにwaitとして入れる
		ArrayList<PotionEffect> effects = new ArrayList<PotionEffect>();
		for(PotionEffect e : player.getActivePotionEffects()) {
			effects.add(e);
		}
		
		MessageMatchMenber(match,player.getName() + "がマッチングに参加しました。",MessageType.INFO);
		
		//状態更新
		MatchPlayerData playerData = new MatchPlayerData(player.getUniqueId(),player.getName(),MatchPlayerStatus.WAITING);
		match.getData().getWait().getPlayers().add(playerData);
		new NBTPlayerStatus(plugin,player).setType(PlayerStatus.WAITING);
		
		//tp
		player.teleport(new LocationManager(plugin).getWaitingRoom(type));
		
		new MatchLog(plugin,match).onUpdate("マッチング参加", player);
		
		//個別処理
		match.onPlayerJoin(player);
		
	}
	
	private Match getWaitMatch(GameType type){
		for(Match m : allData) {
			if(m.getData().getType().equals(type)){
				if(m.getData().getStatus().equals(MatchStatus.WAITING)) {
					return m;
				}
			}
		}
		return null;
	}
	
	private Match createMatch(GameType type) {
		Match match = null;
		if(new LocationManager(plugin).getUsableMap(type).isEmpty()){
			return null;
		}
		switch(type) {
		case SINGLE:
			match = new Single(plugin,plugin.getNextMatchID(),type);
			break;
		case BATTLE:
			match = new Battle(plugin,plugin.getNextMatchID(),type);
			break;
		}
		if(match == null) {
			new ErrorLog(plugin).writeError("MatchManager.createMatch", type + "は未定義です。");
			return null;
		}
		new MatchLog(plugin,match).onCreate();
		allData.add(match);
		return match;
		
	}
	
	public void startGame(Player player) {
		Match match = getMatch(player);
		
		if(match == null) {
			new ErrorLog(plugin).writeError("MatchManager.startGame", player.getName() + "は試合に参加していません");
			return ;
		}

		if(!match.checkGameStart()) {
			MessageMatchMenber(match,"人数が足りないなどの理由で試合を始められません。",MessageType.IMPORTANT);
			return;
		}
		
		match.getData().setStatus(MatchStatus.STARTED);
		
		new MatchLog(plugin,match).onUpdate("試合開始", player);
		
		for(Player p : match.getPlayers()){
			new NBTPlayerStatus(plugin,p).setType(PlayerStatus.ONMATCH);
			MatchPlayerData playerData = getPlayerData(match,player);
			if(playerData != null) {
				playerData.setStatus(MatchPlayerStatus.PLAYING);
			}
			MatchPlayerData waitPlayerData = getWaitPlayerData(match,player);
			waitPlayerData.setStatus(MatchPlayerStatus.PLAYING);
			
		}
		
		//個別処理
		match.startGame();
		
	}
	
	public void endGame(Match match) {
		for(Player p : match.getPlayers()){
			playerEnd(p);
		}
		
		//個別処理
		match.endGame();

		//削除
		removeMatch(match,"正常");
	}
	
	//プレイヤーが正常に試合を終了した時(バトロワで負けたとか、最後まで行ってから終わったとか)	
	public void playerEnd(Player player) {
		Match match = getMatch(player);
		if(match == null) {
			new ErrorLog(plugin).writeError("MatchManager.playerEnd", player.getName() + "は試合に参加していません");
			return;
		}
		//状態更新
		MatchPlayerData playerData = getPlayerData(match,player);
		if(playerData != null) {
			playerData.setStatus(MatchPlayerStatus.LEFT);
		}
		MatchPlayerData waitPlayerData = getWaitPlayerData(match,player);
		waitPlayerData.setStatus(MatchPlayerStatus.LEFT);
				
		
		
		new MatchLog(plugin,match).onUpdate("通常終了", player);
		
		//個別処理
		match.onPlayerEnd(player);
		
		new PlayerManager(plugin,player).goToLobby();
		
		
		
	}
	
	public void kickMatch(Player player,String reason) {
		Match match = getMatch(player);
		if(match == null) {
			new ErrorLog(plugin).writeError("MatchManager.kickMatch", player.getName() + "は試合に参加していません");
			return;
		}
		
		//tp
		new PlayerManager(plugin,player).goToLobby();
		
		MessageMatchMenber(match,player.getName() + "が" + reason + "により試合からキックされました。",MessageType.IMPORTANT);
		
		MessageManager.sendWarning(reason + "により試合から退出させられました。", player);
		
		new MatchLog(plugin,match).onUpdate(reason + "によりキック", player);
		
		MatchPlayerData playerData = getPlayerData(match,player);
		if(playerData != null) {
			playerData.setStatus(MatchPlayerStatus.LEFT);
		}
		MatchPlayerData waitPlayerData = getWaitPlayerData(match,player);
		waitPlayerData.setStatus(MatchPlayerStatus.LEFT);
				
		
		//個別処理
		match.onPlayerKick(player);
		
		//試合終わらないかチェック
		match.checkGameEnd();
	}
	
	
	//緊急停止
	public void emergencyStop(Match match,String reason) {
		if(match.getData().getStatus().equals(MatchStatus.WAITING)){
			//プレイヤーごと
			for(Player p : match.getPlayers()){
				MessageManager.sendWarning(reason + "により緊急的にマッチングが終了しました。", p);
				new PlayerManager(plugin,p).goToLobby();
			}
		}else {
			//プレイヤーごと
			for(Player p : match.getPlayers()){
				match.onEmergency(p);
				MessageManager.sendWarning(reason + "により緊急的に試合が終了しました。", p);
				new PlayerManager(plugin,p).goToLobby();
			}
			//個別処理
			match.onEmergency();
		}
		
		//削除
		removeMatch(match,reason + "による緊急停止");
	}
		

	private void MessageMatchMenber(Match match,String text,MessageType type) {
		for(Player p : match.getPlayers()){
			MessageManager.send(p, text, type);
		}
	}
	
	public void MessageMatchMenber(Player player,String text) {
		Match match = getMatch(player);
		if(match == null) {
			new ErrorLog(plugin).writeError("MatchManager.MessageMatchMenber", player.getName() + "は試合に参加していません");
			return;
		}
		for(Player p : match.getPlayers()){
			p.sendMessage(text);
		}
	}
	
	public void MessageTeamMenber(Player player,String text) {
		Match match = getMatch(player);
		if(match == null) {
			new ErrorLog(plugin).writeError("MatchManager.MessageTeamMenber", player.getName() + "は試合に参加していません");
			return;
		}
		match.chatTeamPlayer(text,player);
	}
	
	
	private void removeMatch(Match match,String reason) {

		new MatchLog(plugin,match).onEnd(reason);
		
		new LocationManager(plugin).returnMap(match.getData().getMap());
		
		allData.remove(match);
	}	
	
}
