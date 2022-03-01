package com.github.redshirt53072.swallowfishing.collection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.github.redshirt53072.growthapi.database.SQLSender;
import com.github.redshirt53072.swallowfishing.SwallowFishing;

class FishCollectionSqlSender extends SQLSender{
	public FishCollectionSqlSender(Connection connect) {
    	super(connect,"player_fish_collection",SwallowFishing.getInstance());
    }
	
	public void init(UUID player,List<Integer> fishIDList) {
		String sql = "INSERT IGNORE INTO player_fish_collection (uuid, fishid, collection) VALUES ";
		boolean first = true;
		for(Integer fishID : fishIDList) {
			if(first) {
				sql = sql + "(?," + fishID + ",0)";
				first = false;
			}else {
				sql = sql + ",(?," + fishID + ",0)";		
			}
		}
		
		sql = sql + ";";
		
		try (PreparedStatement statement = connectData.prepareStatement(sql)){
            for(int i = 0;i < fishIDList.size();i++) {
            	statement.setString(i, player.toString());	
            }
			
            
            int result = statement.executeUpdate();
            super.logInfo("INSERT ALL + " + player.toString() + "; result:" + result);
            
        } catch (SQLException e) {
            super.logSevere("INSERT ALL" + player,e);
        }
	}
	
	
	
	public void update(UUID player,int fishID,int collection) {
        try (PreparedStatement statement = connectData.prepareStatement(
        		"UPDATE player_fish_collection SET collection = ? WHERE uuid = ? AND fishid = ?;"
        )){
            statement.setString(1, player.toString());
            statement.setInt(2, fishID);
            statement.setInt(3, collection);
            
            int result = statement.executeUpdate();
            if(result != 1) {
            	super.logWarning("UPDATE player_fish_collection SET collection = " + collection + " WHERE " + player.toString() + " AND " + fishID);
            }
        } catch (SQLException e) {
            super.logSevere("UPDATE player_fish_collection SET collection = " + collection + " WHERE " + player.toString() + " AND " + fishID,e);
        }
	}
	public Integer read(UUID player,int fishID) {
		try (PreparedStatement statement = connectData.prepareStatement(
        		"SELECT collection FROM player_fish_collection WHERE uuid = ?,fishid = ?;"
        )){
			statement.setString(1, player.toString());
			statement.setInt(2, fishID);
			
            ResultSet result = statement.executeQuery();
            
            if(result.next()) {
            	return result.getInt("collection");
            }
            return null;
        } catch (SQLException e) {
        	super.logWarning("SELECT collection FROM player_fish_collection WHERE uuid = " + player.toString() + ",fishid = " + fishID, e);
        }
    	return null;
	}
	
    public Map<Integer,Integer> readAll(UUID player) {
    	try (PreparedStatement statement = connectData.prepareStatement(
        		"SELECT * FROM player_fish_collection WHERE uuid = ?;"
        )){
    		statement.setString(1, player.toString());
			
    		ResultSet result = statement.executeQuery();
            
            Map<Integer,Integer> resultMap = new HashMap<Integer,Integer>();
            while(result.next()) {
            	try {
            		int fishid = result.getInt("fishid");
            		int collection = result.getInt("collection");
            		resultMap.put(fishid, collection);
            	}catch(Exception ex) {
            		super.logWarning("SELECT * FROM player_fish_collection WHRER uuid = " + player.toString(), ex);		
            	}
            }
            return resultMap;
        } catch (SQLException e) {
        	super.logWarning("SELECT * FROM player_fish_collection WHRER uuid = " + player.toString(), e);
        }
    	return new HashMap<Integer,Integer>();
    }
}
