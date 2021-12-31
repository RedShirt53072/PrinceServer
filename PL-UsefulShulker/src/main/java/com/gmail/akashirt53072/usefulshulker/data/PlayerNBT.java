package com.gmail.akashirt53072.usefulshulker.data;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import com.gmail.akashirt53072.usefulshulker.UsefulShulker;



public class PlayerNBT {
	private UsefulShulker plugin;
	private PersistentDataContainer data;
	
    public PlayerNBT(Entity entity) {
    	this.plugin = UsefulShulker.getInstance();
    	data = entity.getPersistentDataContainer();
    }
    protected void writeInt(String keyword,int value) {
    	NamespacedKey key = new NamespacedKey(plugin,keyword);
    	data.set(key, PersistentDataType.INTEGER, value);
    }
    protected void writeString(String keyword,String value) {
    	NamespacedKey key = new NamespacedKey(plugin,keyword);
    	data.set(key, PersistentDataType.STRING, value);
    }
    protected Integer readInt(String keyword) {
    	NamespacedKey key = new NamespacedKey(plugin,keyword);
    	return data.get(key, PersistentDataType.INTEGER);
    }
    protected String readString(String keyword) {
    	NamespacedKey key = new NamespacedKey(plugin,keyword);
    	return data.get(key, PersistentDataType.STRING);
    }
    

    public void setInvID(InvIDType type) {
    	writeString("invID",type.toString());
    }
    
    public InvIDType getInvID() {
    	String invID = readString("invID");
    	if(invID == null) {
    		return InvIDType.NULLINV;
    	}
    	return InvIDType.valueOf(invID);
    }
    
    public void setPage(int page) {
    	writeInt("endpage",page);
    }
    
    public int getPage() {
    	return readInt("endpage");
    }
    
    public void setUnlockedPage(int page) {
    	writeInt("unlockpage",page);
    }
    
    public int getUnlockedPage() {
    	Integer page = readInt("unlockpage");
    	if(page == null) {
    		setUnlockedPage(1);
        	return 1;
    	}
    	return page;
    }
    public void setPayPage(int page) {
    	writeInt("paypage",page);
    }
    
    public int getPayPage() {
    	return readInt("paypage");
    }
    public void setSafeClose(boolean safe) {
    	if(safe) {
    		writeInt("safeclose",1);	
    	}else {
    		writeInt("safeclose",0);
    	}
    }
    
    public boolean getSafeClose() {
    	Integer safe = readInt("safeclose");
    	if(safe == null) {
    		writeInt("safeclose",0);
    		safe = 0;
    	}
    	
    	return 1 == safe;
    }
    
    
}
