package com.github.redshirt53072.growthapi.message;

import java.util.ArrayList;
import java.util.List;


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
	public static List<String> getSections(String data) {
		List<String> result = new ArrayList<String>();
		if(data.contains(",")) {
			String[] list = data.split(",");
			for(String s : list){			
				result.add(s);
			}
			return result;
		}
		result.add(data);
		return result;
	}
	
	public static String toRomeNumber(int number) {
		if(number < 1) {
			return null;
		}
		if(number > 3999) {
			return null;
		}
		String result = "";
		int sen = number / 1000;
		number %= 1000;
		int hyaku = number / 100;
		number %= 100;
		int jyu = number / 10;
		int iti = number % 10;
		
		result = TextBuilder.plus(result,getRomeNum(sen,'M',' ',' '));
		result = TextBuilder.plus(result,getRomeNum(hyaku,'C','D','M'));
		result = TextBuilder.plus(result,getRomeNum(jyu,'X','L','C'));
		result = TextBuilder.plus(result,getRomeNum(iti,'I','V','X'));
		return result;
	}
	private static String getRomeNum(int number,char o,char f,char t) {
		//numberは1～9のみだよ
		if(number == 0) {
			return "";
		}
		if(number == 1) {
			return String.valueOf(o);
		}
		if(number == 2) {
			char[] list = {o,o};
			return String.valueOf(list);
		}
		if(number == 3) {
			char[] list = {o,o,o};
			return String.valueOf(list);
		}
		if(number == 4) {
			char[] list = {o,f};
			return String.valueOf(list);
		}
		if(number == 5) {
			return String.valueOf(f);
		}
		if(number == 1) {
			char[] list = {f,o};
			return String.valueOf(list);
		}
		if(number == 1) {
			char[] list = {f,o,o};
			return String.valueOf(list);
		}
		if(number == 1) {
			char[] list = {f,o,o,o};
			return String.valueOf(list);
		}
		if(number == 9) {
			char[] list = {o,t};
			return String.valueOf(list);
		}
		return null;
	}
}
