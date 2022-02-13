package com.github.redshirt53072.dimmanager.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import com.github.redshirt53072.growthapi.database.SQLSender;
import com.github.redshirt53072.growthapi.BaseAPI;

class WorldSqlSender extends SQLSender{
	
	public WorldSqlSender(Connection connect) {
    	super(connect,"player_loc",BaseAPI.getInstance());
    }
	
	public void insert(String name,UUID player,UUID world) {
        try (PreparedStatement statement = connectData.prepareStatement(
        		"INSERT INTO player_loc (dim, player, world, x, y, z, yaw, pitch) VALUES (?, ?,?,?,?,?,?,?);"
        )){
            statement.setString(1, name);
            statement.setString(2, player.toString());
            statement.setString(3, world.toString());
            statement.setDouble(4, 0);
            statement.setDouble(5, 70);
            statement.setDouble(6, 0);  
            statement.setDouble(7, 0);
            statement.setDouble(8, 0);
            
            int result = statement.executeUpdate();
            if(result != 1) {
            	super.logWarning("INSERT " + name + "," + player + "," + world + ",0,70,0,0,0");
            }
        } catch (SQLException e) {
            super.logSevere("INSERT " + name + "," + player + "," + world + ",0,70,0,0,0",e);
        }
    }
	public void update(String name,UUID player,Location loc) {
        try (PreparedStatement statement = connectData.prepareStatement(
        		"UPDATE player_loc SET world = ?, x = ?, y = ?, z = ?, yaw = ?, pitch = ? WHERE dim = ? AND player = ?;"
        )){
        	statement.setString(1, loc.getWorld().getUID().toString());
            statement.setDouble(2, loc.getX());
            statement.setDouble(3, loc.getY());
            statement.setDouble(4, loc.getZ());
            statement.setDouble(5, loc.getYaw());
            statement.setDouble(6, loc.getPitch());
            statement.setString(7, name);
            statement.setString(8, player.toString());
            
            int result = statement.executeUpdate();
            if(result != 1) {
            	super.logWarning("UPDATE SET world = "+ loc.getWorld().getUID() + ", x = "+ loc.getBlockX() + ", y = "+ loc.getBlockY() + ", z = " + loc.getBlockZ() + ", yaw = "+ loc.getYaw() + ", pitch = " + loc.getPitch() + "(dim = " + name + ",player = " + player.toString() + ")");
            }
        } catch (SQLException e) {
            super.logSevere("UPDATE SET world = "+ loc.getWorld().getUID() + ", x = "+ loc.getBlockX() + ", y = "+ loc.getBlockY() + ", z = " + loc.getBlockZ() + ", yaw = "+ loc.getYaw() + ", pitch = " + loc.getPitch() + "(dim = " + name + ",player = " + player.toString() + ")", e);
        }
    }
    public Location read(UUID player,String dim) {
    	try (PreparedStatement statement = connectData.prepareStatement(
        		"SELECT world, x, y, z, yaw, pitch FROM player_loc WHERE dim = ? AND player = ?;"
        )){

            statement.setString(1, dim);
            statement.setString(2, player.toString());
            
            ResultSet result = statement.executeQuery();
            Location data = null;
            
            if(result.next()) {
            	String uuid = result.getString("world");
            	UUID id = null;
            	try {
            		id = UUID.fromString(uuid);
            	}catch(IllegalArgumentException ex) {
            		super.logWarning("SELECT world, x, y, z, yaw, pitch FROM player_loc WHERE dim = " + dim + " AND player = " + player.toString(), ex);
            	}
            	World world = Bukkit.getWorld(id);
            	double x = result.getDouble("x");
            	double y = result.getDouble("y");
            	double z = result.getDouble("z");
            	double yaw = result.getDouble("yaw");
            	double pitch = result.getDouble("pitch");
            	data = new Location(world,x,y,z,(float)yaw,(float)pitch);
            }
            
            return data;
            
        } catch (SQLException e) {
            super.logSevere("SELECT world, x, y, z, yaw, pitch FROM player_loc WHERE dim = " + dim + " AND player = " + player.toString(),e);
        }
    	return null;
    }
}
