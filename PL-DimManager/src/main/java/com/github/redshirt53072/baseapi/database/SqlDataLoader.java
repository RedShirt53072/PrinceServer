package com.github.redshirt53072.baseapi.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;

import com.github.redshirt53072.baseapi.message.LogManager;
import com.github.redshirt53072.baseapi.server.GrowthApi;

public abstract class SqlDataLoader {
	protected Connection connectData;
	protected GrowthApi plugin;
	
    protected SqlDataLoader(GrowthApi plugin) {
    	this.plugin = plugin;
    	
    }
    
    public boolean connect() {
    	try {
    		connectData = SqlManager.getDataSource().getConnection();
    		return true;
    	}catch(SQLException ex) {
    		logSevere(ex);
    		return false;
    	}
    }
    
    public boolean close() {
    	try {
    		connectData.close();
    		return true;
    	}catch(SQLException ex) {
    		logSevere(ex);
    		return false;
    	}
    }
    
    public void logSevere(Exception ex) {
		LogManager.logError("[database]データベースを読み込めませんでした。", plugin, ex, Level.SEVERE);
	}
    
	public void logEnd() {
		LogManager.logInfo("[database]データベースを読み込みました。", plugin, Level.FINE);
	}
}
