package com.github.redshirt53072.growthapi.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 好きなデータ配列とその抽選割合を入力し、ランダムで1つのオブジェクトを抽選できる
 * @author redshirt
 *
 * @param <D> データ型
 */
public class LootRoller<D> {
	/**
	 * データ
	 */
	private List<D> data = new ArrayList<D>();
	private List<Integer> rolls = new ArrayList<Integer>();
	int maxIndex = 0;
	int maxRoll = 0;
	
	public void addData(D newData,int roll) {
		data.add(newData);
		rolls.add(roll);
		maxIndex ++;
		maxRoll += roll;
	}
	
	/**
	 * ランダムでデータを1つ取り出す
	 * @return データ
	 */
	public D getRandom() {
		int rolled = new Random().nextInt(maxRoll);
		int sum = 0;
		for(int i = 0;i < maxIndex;i++) {
			int now = rolls.get(i);
			sum += now;
			if(sum > rolled) {
				
				return data.get(i);
			}
		}
		return null;
	}
	
	/**
	 * 被りなしでランダムでデータをn個取り出す
	 * @param amount
	 * @return
	 */
	public List<D> getNonDuplication(int amount) {
		List<D> data2 = new ArrayList<D>(data);
		List<Integer> rolls2 = new ArrayList<Integer>(rolls);
		
		List<D> result = new ArrayList<D>(); 
		int maxRoll2 = maxRoll;
		int maxIndex2 = maxIndex;
		amount = Math.min(amount,maxIndex);
		
		for(int i = 0;i < amount;i++) {
			
			int rolled = new Random().nextInt(maxRoll2);
			int sum = 0;
			for(int j = 0;j < maxIndex2;j++) {
				int now = rolls2.get(j);
				sum += now;
				if(sum > rolled) {
					
					result.add(data2.get(i));
					maxRoll2 -= now;
					data2.remove(j);
					rolls2.remove(j);
					break;
				}
			}
			maxIndex2 --;
		}
		return result;
	}
}
