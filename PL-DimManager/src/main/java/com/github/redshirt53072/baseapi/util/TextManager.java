package com.github.redshirt53072.baseapi.util;

import java.util.ArrayList;



public class TextManager {
	
	public static int toNumber(String text) {
		int length = text.length();
        if(length == 0) {
        	return 1000000001;
        }
        if(length > 9) {
        	return 1000000002;
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
        	return 1000000003;
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
	public static int toNaturalNumber(String text) {
		int length = text.length();
        if(length == 0) {
        	return 1000000001;
        }
        if(length > 9) {
        	return 1000000002;
        }
        
        ArrayList<String> numberData = new ArrayList<String>();
        for(int i = 0;i < text.length();i ++) {
         	char c = text.charAt(i);
         	String ca = String.valueOf(c);
         	if(ca.matches("[0-9]")) {
         		numberData.add(ca);
         	}
        }
        if(numberData.isEmpty()) {
        	return 1000000003;
        }
        int result = 0;
        int size = numberData.size();
        for(int i = 0;i < size;i ++) {
        	int m = (int)Math.pow(10, size - 1 - i);
        	result += Integer.valueOf(numberData.get(i)) * m;
        }
        if(result < 1) {
        	return 1000000010;
        }
        return result;
	}
	
	
}
