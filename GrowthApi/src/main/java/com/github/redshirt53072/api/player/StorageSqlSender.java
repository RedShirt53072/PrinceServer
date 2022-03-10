package com.github.redshirt53072.api.player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import com.github.redshirt53072.api.BaseAPI;
import com.github.redshirt53072.api.database.SQLSender;
import com.github.redshirt53072.api.message.TextBuilder;

final class StorageSqlSender extends SQLSender{
	
	public StorageSqlSender(Connection connect) {
    	super(connect,"player_storage",BaseAPI.getInstance());
    }
	
	public void insert(UUID player,String data) {
        try (PreparedStatement statement = connectData.prepareStatement(
        		"INSERT INTO player_storage (uuid, data) VALUES (?,?);"
        )){
            statement.setString(1, player.toString());
            statement.setString(2, data);
            
            int result = statement.executeUpdate();
            if(result != 1) {
            	super.logWarning(TextBuilder.plus("INSERT " ,player.toString()));
            }
        } catch (SQLException e) {
            super.logSevere(TextBuilder.plus("INSERT " ,player.toString()),e);
        }
	}
	public void delete(UUID player) {
		try (PreparedStatement statement = connectData.prepareStatement(
        		"DELETE FROM player_storage WHERE uuid = ?;"
        )){
            statement.setString(1, player.toString());
            
            statement.executeUpdate();
        } catch (SQLException e) {
        	super.logWarning(TextBuilder.plus("DELETE FROM player_storage WHERE uuid = ",player.toString()), e);
        }
	}
    public String read(UUID player) {
    	try (PreparedStatement statement = connectData.prepareStatement(
        		"SELECT data FROM player_storage WHERE uuid = ?;"
        )){
            statement.setString(1, player.toString()); 
            
            ResultSet result = statement.executeQuery();
            
            if(result.next()) {
            	String resultData = result.getString("data");
            	if(resultData == null) {
            		super.logWarning(TextBuilder.plus("SELECT data FROM player_storage WHERE uuid = " + player.toString()), new Exception());
            	}
            	return resultData;
            }else {
            	super.logWarning(TextBuilder.plus("SELECT data FROM player_storage WHERE uuid = " + player.toString()), new Exception("結果はnullでした。"));
            }
        } catch (SQLException e) {
        	super.logWarning(TextBuilder.plus("SELECT data FROM player_storage WHERE uuid = ",player.toString()), e);
        }
    	return null;
    }
}