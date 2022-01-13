package com.github.redshirt53072.growthapi.util;

import java.util.ArrayList;


/**
 * テキストを加工するクラス
 * @author redshirt
 *
 */
public class TextManager {
	/**String->int変換(整数)
	 * Stringのテキストから数字を抽出し、10進数でintに直す
	 * テキスト中に"-"があると最後にマイナスになる
	 * テキストが10文字以上か数字がなければnullを返す
	 * サンプル
	 * "10s5" -> 15
	 * "aje-" -> null
	 * "24-a" -> -24
	 * 
	 * @param text 処理をするテキスト
	 * @return 抽出結果の数(Integer)
	 */
	public static Integer toNumber(String text) {
		int length = text.length();
        if(length == 0) {
        	return null;
        }
        if(length > 9) {
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
	/**String->int変換(自然数)
	 * Stringのテキストから数字を抽出し、10進数でintに直す
	 * マイナスにはならないというだけで厳密には自然数ではない
	 * テキストが10文字以上か数字がなければnullを返す
	 * サンプル
	 * "01s5" -> 15
	 * "aje-" -> null
	 * "24-a" -> 24
	 * 
	 * @param text 処理をするテキスト
	 * @return 抽出結果の数(Integer)
	 */
	public static Integer toNaturalNumber(String text) {
		int length = text.length();
        if(length == 0) {
        	return null;
        }
        if(length > 9) {
        	return null;
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
        	return null;
        }
        int result = 0;
        int size = numberData.size();
        for(int i = 0;i < size;i ++) {
        	int m = (int)Math.pow(10, size - 1 - i);
        	result += Integer.valueOf(numberData.get(i)) * m;
        }
        if(result < 1) {
        	return null;
        }
        return result;
	}
	
	
}
