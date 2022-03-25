package com.github.redshirt53072.shulker.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import com.github.redshirt53072.api.database.SQLSender;
import com.github.redshirt53072.shulker.UsefulShulker;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

class EnderChestSqlSender extends SQLSender{
	public EnderChestSqlSender(Connection connect) {
    	super(connect,"player_enderchest",UsefulShulker.getInstance());
    }
	
	public void init(UUID player) {
		String data = new Gson().toJson(new JsonArray());
		try (PreparedStatement statement = connectData.prepareStatement(
        		"INSERT IGNORE INTO player_enderchest (uuid, page, data) VALUES (?,1,?),(?,2,?),(?,3,?),(?,4,?),(?,5,?),(?,6,?),(?,7,?),(?,8,?),(?,9,?);"
        )){
            statement.setString(1, player.toString());
            statement.setString(3, player.toString());
            statement.setString(5, player.toString());
            statement.setString(7, player.toString());
            statement.setString(9, player.toString());
            statement.setString(11, player.toString());
            statement.setString(13, player.toString());
            statement.setString(15, player.toString());
            statement.setString(17, player.toString());
            statement.setString(2, data);
            statement.setString(4, data);
            statement.setString(6, data);
            statement.setString(8, data);
            statement.setString(10, data);
            statement.setString(12, data);
            statement.setString(14, data);
            statement.setString(16, data);
            statement.setString(18, data);
            
            int result = statement.executeUpdate();
            
            if(result > 0) {
            	super.logInfo("INSERTALL " + player + "; result : " + result);
            }
        } catch (SQLException e) {
            super.logSevere("INSERTALL " + player,e);
        }
	}
	
	public void insert(UUID player,int page,String data) {
        try (PreparedStatement statement = connectData.prepareStatement(
        		"INSERT INTO player_enderchest (uuid, page, data) VALUES (?,?,?);"
        )){
            statement.setString(1, player.toString());
            statement.setInt(2, page);
            statement.setString(3, data);
            
            int result = statement.executeUpdate();
            if(result != 1) {
            	super.logWarning("INSERT " + player);
            }
        } catch (SQLException e) {
            super.logSevere("INSERT " + player,e);
        }
	}
	public void delete(UUID player,int page) {
		try (PreparedStatement statement = connectData.prepareStatement(
        		"DELETE FROM player_enderchest WHERE uuid = ? AND page = ?;"
        )){
            statement.setString(1, player.toString());
            statement.setInt(2, page);
            
            statement.executeUpdate();
        } catch (SQLException e) {
        	super.logWarning("DELETE FROM player_enderchest WHERE uuid = " + player.toString() + " AND page = " + page, e);
        }
	}
    public String read(UUID player,int page) {
    	try (PreparedStatement statement = connectData.prepareStatement(
        		"SELECT data FROM player_enderchest WHERE uuid = ? AND page = ?;"
        )){
            statement.setString(1, player.toString());
            statement.setInt(2, page);
            
            ResultSet result = statement.executeQuery();
            
            if(result.next()) {
            	String resultData = result.getString("data");
            	if(resultData == null) {
            		super.logWarning("SELECT data FROM player_enderchest WHERE uuid = " + player.toString() + " AND page = " + page, new Exception());
            	}
            	return resultData;
            }else {
            	super.logWarning("SELECT data FROM player_enderchest WHERE uuid = " + player.toString() + " AND page = " + page, new Exception("結果はnullでした。"));
            }
        } catch (SQLException e) {
        	super.logWarning("SELECT data FROM player_enderchest WHERE uuid = " + player.toString() + " AND page = " + page, e);
        }
    	return null;
    }
    
}
