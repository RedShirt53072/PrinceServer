package com.github.redshirt53072.dimmanager.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.redshirt53072.growthapi.command.ManagementCommand;
import com.github.redshirt53072.growthapi.command.SubCommand;
import com.github.redshirt53072.growthapi.message.MessageManager;
import com.github.redshirt53072.growthapi.util.Flag;
import com.github.redshirt53072.growthapi.util.TextManager;
import com.github.redshirt53072.dimmanager.data.DimData;
import com.github.redshirt53072.dimmanager.general.WorldManager;

public class DimSubCommand implements SubCommand{
	private static DimSubCommand sub;
	
	public static void register(){
		sub = new DimSubCommand();
		ManagementCommand.register("dim", sub);
		
	}
	
	@Override
    public void onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage("[error]コンソールからは実行できません。");
        	return;
		}
		Player p = (Player)sender;
		if(args.length < 2) {
			MessageManager.sendSpecial(ChatColor.RED + "[error]必要な項目が未記入です。", p);
        	return;
        }
		
		switch(args[1]) {
		case "register":
			if(args.length < 10) {
				MessageManager.sendSpecial(ChatColor.RED + "[error]必要な項目が未記入です。", p);
	        	return;
	        }
			if(!args[2].matches("[a-z_]+")) {
				MessageManager.sendSpecial(ChatColor.RED + "[error]ディメンション名は英字と_のみ使用が許可されています。", p);
	        	return;
			}
			Flag match = new Flag(); 
			WorldManager.getAllDims().forEach(dim -> {if(args[2].equals(dim)) {
				match.setTrue();
			}});
			if(match.getFlag()) {
				MessageManager.sendSpecial(ChatColor.RED + "[error]既に使用されているディメンション名です。", p);
	        	return;
			}
			boolean visible;
			if(args[3].equals("visible")) {
				visible = true;
			}else if(args[3].equals("invisible")) {
				visible = false;
			}else {
				MessageManager.sendSpecial(ChatColor.RED + "[error]無効なパーミッション区分です。", p);
	        	return;
			}
			GameMode mode;
			try {
				mode = GameMode.valueOf(args[4]);
			}catch(IllegalArgumentException ex) {
				MessageManager.sendSpecial(ChatColor.RED + "[error]無効なゲームモードです。", p);
				return;
			}
			int x = TextManager.toNumber(args[5]);
			int y = TextManager.toNumber(args[6]);
			int z = TextManager.toNumber(args[7]);
			int yaw = TextManager.toNumber(args[8]);
			int pitch = TextManager.toNumber(args[9]);
			
			if(x > 1000000000 || y > 1000000000 || z > 1000000000 || yaw > 1000000000 || pitch > 1000000000) {
				MessageManager.sendSpecial(ChatColor.RED + "[error]無効な座標値です。(" + "," + x+ "," + y+ "," + z+ "," + yaw+ "," + pitch + ")", p);
	        	return;
			}
			boolean result = DimData.addDimension(p.getWorld(), args[2], mode, visible, x, y, z, yaw, pitch);
			
			WorldManager.reload();
			if(result) {
				MessageManager.sendSpecial("ディメンションを登録しました。(" + args[2] + "," + x+ "," + y+ "," + z+ "," + yaw+ "," + pitch + ")", p);	
			}else {
				MessageManager.sendSpecial(ChatColor.RED + "[error]このディメンションは既に登録されています", p);
			}
			
			return;
		case "delete":
			if(args.length < 3) {
				MessageManager.sendSpecial(ChatColor.RED + "[error]必要な項目が未記入です。", p);
	        	return;
	        }
			if(args[2].equals("normal")) {
				MessageManager.sendSpecial(ChatColor.RED + "[error]normalの削除はできません。", p);
	        	return;
			}
			Flag match1 = new Flag(); 
			WorldManager.getAllDims().forEach(dim -> {if(args[2].equals(dim)) {
				match1.setTrue();
			}});
			if(!match1.getFlag()) {
				MessageManager.sendSpecial(ChatColor.RED + "[error]無効なディメンション名です。", p);
	        	return;
			}

			DimData.delete(args[2]);
			
			WorldManager.reload();
			
			MessageManager.sendSpecial("ディメンションを削除しました。(" + args[2] + ")", p);
	        
			return;
		case "login":
			if(args.length < 3) {
				MessageManager.sendSpecial(ChatColor.RED + "[error]必要な項目が未記入です。", p);
	        	return;
	        }
			if(args[2].equals("normal")) {
				MessageManager.sendSpecial(ChatColor.RED + "[error]normalのスポーン設定はできません。", p);
	        	return;
			}
			Flag match2 = new Flag(); 
			WorldManager.getAllDims().forEach(dim -> {if(args[2].equals(dim)) {
				match2.setTrue();
			}});
			if(!match2.getFlag()) {
				if(!args[2].equals("NONE")) {
					MessageManager.sendSpecial(ChatColor.RED + "[error]無効なディメンション名です。", p);
		        	return;	
				}
			}
			DimData.setStart(args[2]);
			
			MessageManager.sendSpecial("ログイン時のディメンションを設定しました。(" + args[2] + ")", p);
	        
			return;
		case "reload":
			WorldManager.reload();
			MessageManager.sendSpecial("ディメンション設定を再読み込みしました。", p);
	        
			return;
		}
		MessageManager.sendSpecial(ChatColor.RED + "[error]無効なサブコマンドです。", p);
    	
    }
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args){
		List<String> tab = new ArrayList<String>();
		if(args.length == 2) {
            tab.add("register");
            tab.add("delete");
        	tab.add("login");
        	tab.add("reload");
        }else {
        	String text1 = args[1];
        	switch(text1) {
        	case "register":
        		if(args.length == 3) {
        			tab.add("<英字ディメンション名>");
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
        		break;
        	case "delete":
        		if(args.length == 3) {
        			tab.addAll(WorldManager.getAllDims());
        		}
        		break;
        	case "login":
        		if(args.length == 3) {
        			tab.addAll(WorldManager.getWorlds());
            		tab.add("NONE");		
        		}
        		break;
        	}
        }
        return tab;
	}
	
}

