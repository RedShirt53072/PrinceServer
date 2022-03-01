package com.github.redshirt53072.dimmanager.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.redshirt53072.growthapi.command.ManagementCommand;
import com.github.redshirt53072.growthapi.command.SubCommand;
import com.github.redshirt53072.growthapi.message.MessageManager;
import com.github.redshirt53072.growthapi.message.TextBuilder;
import com.github.redshirt53072.growthapi.message.TextManager;
import com.github.redshirt53072.dimmanager.Teleporter;
import com.github.redshirt53072.dimmanager.data.DimConfig;
import com.github.redshirt53072.dimmanager.data.DimData;
import com.github.redshirt53072.dimmanager.data.DimID;
import com.github.redshirt53072.dimmanager.data.WorldManager;

public class DimSubCommand implements SubCommand{
	private static DimSubCommand sub;

	private static List<OfflinePlayer> offlinePlayers = new ArrayList<OfflinePlayer>();
	
	public static void register(){
		sub = new DimSubCommand();
		ManagementCommand.register("dim", sub);

		for(OfflinePlayer op : Bukkit.getOfflinePlayers()) {
			offlinePlayers.add(op);
		}
	}
	
	@Override
    public void onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if(args.length < 2) {
			MessageManager.sendCommandError("必要な項目が未記入です。", sender);
        	return;
        }
		
		DimData dim = null;
		
		switch(args[1]) {
		case "delete":
			if(args.length < 3) {
				MessageManager.sendCommandError("必要な項目が未記入です。", sender);
	        	return;
	        }
			if(args[2].equals("normal")) {
				MessageManager.sendCommandError("normalの削除はできません。", sender);
	        	return;
			}
			
			for(DimData dd : WorldManager.getAllDims()) {
				if(dd.getName().equals(args[2])) {
					dim = dd;
					break;
				}
			}
			if(dim == null) {
				MessageManager.sendCommandError("無効なディメンション名です。", sender);
	        	return;
			}

			DimConfig.delete(args[2]);
			
			WorldManager.reload();
			
			MessageManager.sendSpecial(TextBuilder.plus("ディメンションを削除しました。(", args[2] ,")"), sender);
	        
			return;
		case "login":
			if(args.length < 3) {
				MessageManager.sendCommandError("必要な項目が未記入です。", sender);
	        	return;
	        }
			if(args[2].equals("normal")) {
				MessageManager.sendCommandError("normalのスポーン設定はできません。", sender);
	        	return;
			}
			
			for(DimData dd : WorldManager.getAllDims()) {
				if(dd.getName().equals(args[2])) {
					dim = dd;
					break;
				}
			}
			
			if(dim == null) {
				if(!args[2].equals("NONE")) {
					MessageManager.sendCommandError("無効なディメンション名です。", sender);
		        	return;	
				}
			}
			DimConfig.setStart(args[2]);

			WorldManager.reload();
			MessageManager.sendSpecial(TextBuilder.plus("ログイン時のディメンションを設定しました。(", args[2],")"), sender);
	        
			return;
		case "reload":
			WorldManager.reload();
			MessageManager.sendSpecial("ディメンション設定を再読み込みしました。", sender);
	        
			return;
		case "addhomedim":
			if(args.length < 3) {
				MessageManager.sendCommandError("必要な項目が未記入です。",sender);
	        	return;
	        }
			boolean isMatch3 = false;
			
			for(DimData dd : WorldManager.getAllDims()) {
    			if(dd.getName().equals(args[2])) {
    				isMatch3 = true;
    			}
			}
			
			for(DimData dd : WorldManager.getHomeData()) {
    			if(dd.getName().equals(args[2])) {
    				isMatch3 = false;
    			}
			}
			
			if(!isMatch3) {
				MessageManager.sendCommandError("無効なディメンション名です。",sender);
				return;	
			}
			DimConfig.setHome(args[2]);
			
			WorldManager.reload();
			
			MessageManager.sendSpecial(TextBuilder.plus("ホームを追加しました。(",args[2], ")"),sender);
			
			return;
		case "delhomedim":
			if(args.length < 3) {
				MessageManager.sendCommandError("必要な項目が未記入です。", sender);
	        	return;
	        }
			
			boolean isMatch4 = false;
			
			for(DimData dd : WorldManager.getHomeData()) {
    			if(dd.getName().equals(args[2])) {
    				isMatch4 = true;
    			}
			}
			if(!isMatch4) {
				MessageManager.sendCommandError("無効なディメンション名です。", sender);
				return;	
			}
			if(DimConfig.deleteHome(args[2])) {
				WorldManager.reload();
				MessageManager.sendSpecial(TextBuilder.plus("ホームを削除しました。(" ,args[2], ")"), sender);	
			}
			MessageManager.sendCommandError(TextBuilder.plus("ホームの削除ができませんでした。(",args[2],")"), sender);
			return;
		}
		if(!(sender instanceof Player)) {
			MessageManager.sendCommandError("無効なサブコマンドです。", sender);
        	return;
		}
		Player p = (Player)sender;
		switch(args[1]) {
		case "register":
			if(args.length < 10) {
				MessageManager.sendCommandError("必要な項目が未記入です。", sender);
	        	return;
	        }
			
			try {
				DimID.valueOf(args[2]).getName();
			}catch(Exception ex) {
				MessageManager.sendCommandError("ディメンション名は事前に定義されたIDのみ使用が許可されています。", sender);
	        	return;
			}
			
			for(DimData dd : WorldManager.getAllDims()) {
				if(dd.getName().equals(args[2])) {
					dim = dd;
					break;
				}
			}
			if(dim != null) {
				MessageManager.sendCommandError("既に使用されているディメンション名です。", sender);
	        	return;
			}
			boolean visible;
			if(args[3].equals("visible")) {
				visible = true;
			}else if(args[3].equals("invisible")) {
				visible = false;
			}else {
				MessageManager.sendCommandError("無効なパーミッション区分です。", sender);
	        	return;
			}
			GameMode mode;
			try {
				mode = GameMode.valueOf(args[4]);
			}catch(IllegalArgumentException ex) {
				MessageManager.sendCommandError("無効なゲームモードです。", sender);
				return;
			}
			int x = TextManager.toNumber(args[5]);
			int y = TextManager.toNumber(args[6]);
			int z = TextManager.toNumber(args[7]);
			int yaw = TextManager.toNumber(args[8]);
			int pitch = TextManager.toNumber(args[9]);
			
			if(x > 1000000000 || y > 1000000000 || z > 1000000000 || yaw > 1000000000 || pitch > 1000000000) {
				MessageManager.sendCommandError(TextBuilder.plus("無効な座標値です。(",",",String.valueOf(x), ",", String.valueOf(y),"," ,String.valueOf(z),",", String.valueOf(yaw), ",", String.valueOf(pitch), ")"), sender);
	        	return;
			}
			boolean result = DimConfig.addDimension(new DimData(args[2], new Location(p.getWorld(), x, y, z, yaw, pitch), mode, p.getWorld().getUID(),visible));
			
			WorldManager.reload();
			if(result) {
				MessageManager.sendSpecial(TextBuilder.plus("ディメンションを登録しました。(",args[2],",",String.valueOf(x),",",String.valueOf(y),",",String.valueOf(z)+ ",", String.valueOf(yaw), ",", String.valueOf(pitch),")"), sender);	
			}else {
				MessageManager.sendCommandError("このディメンションは既に登録されています", sender);
			}
			
			return;
		
		case "dimall":
			if(args.length < 3) {
				MessageManager.sendCommandError("必要な項目が未記入です。", sender);
	        	return;
	        }
			
			for(DimData dd : WorldManager.getAllDims()) {
				if(dd.getName().equals(args[2])) {
					dim = dd;
					break;
				}
			}
			
			if(dim == null) {
				MessageManager.sendCommandError("無効なディメンション名です。", sender);
				return;
			}
			if(args[2].equals("normal")) {
				Teleporter.teleportSavedLocation(p, dim);
			}else {
				Teleporter.teleportDefault(p, dim);
			}
			
			return;
		case "teleport":
			if(args.length < 4) {
				MessageManager.sendCommandError("必要な項目が未記入です。",sender);
	        	return;
	        }
			
			String name = args[2];
			OfflinePlayer target = null;
			
			for(OfflinePlayer op : offlinePlayers) {
				if(op.getName().equals(name)){
					target = op;
					break;
				}
			}
			if(target == null) {
				MessageManager.sendCommandError("無効なプレイヤー名です。", sender);	
				return;
			}
			
			if(args[3].equals("normal")) {
				dim = WorldManager.getNormal();
			}else {
				for(DimData dd : WorldManager.getHomeData()) {
					if(dd.getName().equals(args[3])) {
						dim = dd;
						break;
					}
				}
			}
			if(dim == null) {
				MessageManager.sendCommandError("無効なディメンション名です。",sender);
				return;
			}
			
			Teleporter.teleportSavedLocation(p, target, dim);
			
			return;
		
		}
		MessageManager.sendCommandError("無効なサブコマンドです。", sender);
    	
    }
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args){
		List<String> tab = new ArrayList<String>();
		if(args.length == 2) {
        	tab.add("reload");
        	tab.add("login");
        	tab.add("delete");
        	tab.add("addhomedim");
        	tab.add("delhomedim");
        	if(sender instanceof Player) {
                tab.add("register");
                tab.add("dimall");
            	tab.add("teleport");
        	}else {
        		tab.add("※コンソールからは一部のサブコマンドが利用できません");	
        	}
        }else {
        	String text1 = args[1];
        	switch(text1) {
        	case "register":
        		if(sender instanceof Player) {
        			if(args.length == 3) {
        				tab.add("<英字ディメンション名>");
        				for(DimID di : DimID.values()) {
        					tab.add(di.toString());
        				}
            			
            		}else if(args.length == 4) {
            			tab.add("権限なしでも使えるか");
            			tab.add("visible");
            			tab.add("invisible");
            		}else if(args.length == 5) {
            			for(GameMode gm : GameMode.values()){
                			tab.add(gm.toString());	
            			}
            		}else if(args.length == 6) {
            			tab.add("X座標");
            		}else if(args.length == 7) {
            			tab.add("Y座標");
            		}else if(args.length == 8) {
            			tab.add("Z座標");
            		}else if(args.length == 9) {
            			tab.add("横回転");
            		}else if(args.length == 10) {
            			tab.add("上下回転");
            		}
        		}
        		break;
        	case "delete":
        		if(args.length == 3) {
        			for(DimData dd : WorldManager.getAllDims()) {
    					tab.add(dd.getName());
    				}
    				if(tab.isEmpty()) {
        				tab.add("登録されたディメンションがありません");
        			}
        		}
        		break;
        	case "login":
        		if(args.length == 3) {
        			for(DimData dd : WorldManager.getWorlds()) {
        				tab.add(dd.getName());
        			}
        			
            		tab.add("NONE");	
        		}
        		break;
        	case "addhomedim":
        		if(args.length == 3) {
        			List<DimData> worlds = WorldManager.getWorlds();
        			for(DimData dd : WorldManager.getHomeData()) {
            			worlds.remove(dd);
        			}
        			for(DimData dd : worlds) {
        				tab.add(dd.getName());
        			}
        			if(tab.isEmpty()) {
        				tab.add("ホームを追加できるディメンションがありません");
        			}
        		}
        		break;
        	case "delhomedim":
        		if(args.length == 3) {
        			for(DimData dd : WorldManager.getHomeData()) {
            			tab.add(dd.getName());	
        			}
        			if(tab.isEmpty()) {
        				tab.add("ホームが設定されたディメンションがありません");
        			}
        		}
        		break;
        	case "dimall":
        		if(sender instanceof Player) {
        			if(args.length == 3) {
        				for(DimData dd : WorldManager.getAllDims()) {
        					tab.add(dd.getName());
        				}
        				if(tab.isEmpty()) {
            				tab.add("登録されたディメンションがありません");
            			}
        			}
        		}
        		break;
        	case "teleport":
        		if(sender instanceof Player) {
        			if(args.length == 3) {
        				List<OfflinePlayer> offPlayers = new ArrayList<OfflinePlayer>();
						for(OfflinePlayer op : Bukkit.getOfflinePlayers()) {
							tab.add(op.getName());
		        			offPlayers.add(op);
						}
						offlinePlayers = offPlayers;
        			}
        			if(args.length == 4) {
        				for(DimData dd : WorldManager.getHomeData()) {
        					tab.add(dd.getName());
        				}
        				if(tab.isEmpty()) {
            				tab.add("ホームが設定されたディメンションがありません");
            			}
        			}
        		}
        		break;
        	}
        }
        return tab;
	}
	
}

