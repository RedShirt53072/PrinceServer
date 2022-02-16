package com.github.redshirt53072.usefulshulker.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.github.redshirt53072.growthapi.database.SQLSender;
import com.github.redshirt53072.growthapi.BaseAPI;

class ECLockSqlSender extends SQLSender{
	public ECLockSqlSender(Connection connect) {
    	super(connect,"pl_ec_unlocked_page",BaseAPI.getInstance());
    }
	
	public void insert(UUID player,int page) {
        try (PreparedStatement statement = connectData.prepareStatement(
        		"INSERT INTO pl_ec_unlocked_page (uuid, page) VALUES (?,?);"
        )){
            statement.setString(1, player.toString());
            statement.setInt(2, page);
            
            int result = statement.executeUpdate();
            if(result != 1) {
            	super.logWarning("INSERT " + player);
            }
        } catch (SQLException e) {
            super.logSevere("INSERT " + player,e);
        }
	}
	public void delete(UUID player) {
		try (PreparedStatement statement = connectData.prepareStatement(
        		"DELETE FROM pl_ec_unlocked_page WHERE uuid = ?;"
        )){
            statement.setString(1, player.toString());
            
            statement.executeUpdate();
        } catch (SQLException e) {
        	super.logWarning("DELETE FROM pl_ec_unlocked_page WHERE uuid = " + player.toString(), e);
        }
	}
    public Map<UUID,Integer> readAll() {
    	try (PreparedStatement statement = connectData.prepareStatement(
        		"SELECT * FROM pl_ec_unlocked_page;"
        )){
            ResultSet result = statement.executeQuery();
            
            Map<UUID,Integer> resultMap = new HashMap<UUID,Integer>();
            while(result.next()) {
            	try {
            		int page = result.getInt("page");
            		String uuid = result.getString("uuid");
            		resultMap.put(UUID.fromString(uuid), page);
            	}catch(Exception ex) {
            		super.logWarning("SELECT * FROM pl_ec_unlocked_page", ex);		
            	}
            }
            return resultMap;
        } catch (SQLException e) {
        	super.logWarning("SELECT * FROM pl_ec_unlocked_page", e);
        }
    	return new HashMap<UUID,Integer>();
    }
}
