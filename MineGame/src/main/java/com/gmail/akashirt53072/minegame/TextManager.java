package com.gmail.akashirt53072.minegame;


import java.util.ArrayList;

import org.bukkit.entity.Player;


public class TextManager {
	
	public static Integer toNumber(Player player,String text) {
		int length = text.length();
        if(length == 0 || length > 10) {
        	MessageManager.sendImportant(text + "は長すぎます", player);
        	return null;
        }
        
        ArrayList<String> numberData = new ArrayList<String>();
        boolean isMinus = false;
        for(int i = 0;i < text.length();i ++) {
         	char c = text.charAt(i);
         	String ca = String.valueOf(c);
         	if(ca.equals("-")) {
         		isMinus = true;	
         	}
         	if(ca.matches("[0-9]")) {
         		numberData.add(ca);
         	}
        }
        if(numberData.isEmpty()) {
        	MessageManager.sendImportant( text + "には数字がありません", player);
        	return null;
        }
        int result = 0;
        int size = numberData.size();
        for(int i = 0;i < size;i ++) {
        	int m = (int)Math.pow(10, size - 1 - i);
        	result += Integer.valueOf(numberData.get(i)) * m;
        }
        if(isMinus) {
        	result = 0 - result;
        }
        return result;
	}
	public static Integer toNumber(String text) {
		int length = text.length();
        if(length == 0 || length > 10) {
        	return null;
        }
        ArrayList<String> numberData = new ArrayList<String>();
        boolean isMinus = false;
        for(int i = 0;i < text.length();i ++) {
         	char c = text.charAt(i);
         	String ca = String.valueOf(c);
        	if(ca.equals("-")) {
         		isMinus = true;	
         	}
         	if(ca.matches("[0-9]")) {
         		numberData.add(ca);
         	}
        }
        if(numberData.isEmpty()) {
        	return null;
        }
        int result = 0;
        int size = numberData.size();
        for(int i = 0;i < size;i ++) {
        	int m = (int)Math.pow(10, size - 1 - i);
        	 result += Integer.valueOf(numberData.get(i)) * m;
        }
        if(isMinus) {
        	result = 0 - result;
        }
        return result;
	}
	
}
