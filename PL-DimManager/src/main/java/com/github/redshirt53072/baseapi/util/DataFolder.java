package com.github.redshirt53072.baseapi.util;

public class DataFolder<D> {
	private D data = null;
	
	public void setData(D d) {
		data = d;
	}
	public D getData() {
		return data;
	}
	
}
