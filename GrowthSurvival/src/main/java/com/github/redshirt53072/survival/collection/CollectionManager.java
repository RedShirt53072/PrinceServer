package com.github.redshirt53072.survival.collection;

import java.util.ArrayList;
import java.util.List;

public final class CollectionManager {
	private static List<ItemCollection> data = new ArrayList<ItemCollection>();
	
	public static void register(ItemCollection itemCol) {
		data.add(itemCol);
	}
	public static List<ItemCollection> getColList(){
		return data;
	}
}
