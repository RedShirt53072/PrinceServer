package com.github.redshirt53072.dimmanager.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;

import com.github.redshirt53072.growthapi.database.SQLSender;
import com.github.redshirt53072.growthapi.message.TextBuilder;
import com.github.redshirt53072.growthapi.BaseAPI;

class WorldSqlSender extends SQLSender{
	
	public WorldSqlSender(Connection connect) {
    	super(connect,"player_loc",BaseAPI.getInstance());
    }
	
	public void insert(String name,UUID player,Location loc) {
        try (PreparedStatement statement = connectData.prepareStatement(
        		"INSERT IGNORE INTO player_loc (dim, player, world, x, y, z, yaw, pitch) VALUES (?, ?,?,?,?,?,?,?);"
        )){
            statement.setString(1, name);
            statement.setString(2, player.toString());
            statement.setString(3, loc.getWorld().getUID().toString());
            statement.setDouble(4, loc.getX());
            statement.setDouble(5, loc.getY());
            statement.setDouble(6, loc.getZ());
            statement.setDouble(7, loc.getYaw());
            statement.setDouble(8, loc.getPitch());
            
            int result = statement.executeUpdate();
            if(result == 1) {
            	super.logInfo(new TextBuilder(ChatColor.WHITE)
            			.addText("INSERT ",name,",",player.toString(),",",loc.getWorld().getUID().toString()
            					,",x=",String.valueOf(loc.getBlockX())
            					,",y=",String.valueOf(loc.getBlockY())
            					,",z=",String.valueOf(loc.getBlockZ()))
            			.build());
            }
        } catch (SQLException e) {
            super.logSevere(new TextBuilder(ChatColor.WHITE)
        			.addText("INSERT ",name,",",player.toString(),",",loc.getWorld().getUID().toString()
        					,",x=",String.valueOf(loc.getBlockX())
        					,",y=",String.valueOf(loc.getBlockY())
        					,",z=",String.valueOf(loc.getBlockZ()))
        			.build(),e);
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
            	super.logWarning(new TextBuilder(ChatColor.WHITE)
            			.addText("UPDATE world = ",loc.getWorld().getUID().toString()
            					,",x=",String.valueOf(loc.getBlockX())
            					,",y=",String.valueOf(loc.getBlockY())
            					,",z=",String.valueOf(loc.getBlockZ())
            					," (dim=",loc.getWorld().getName()
            					,",player=",player.toString(),")")
            			.build());
            }
        } catch (SQLException e) {
            super.logSevere(new TextBuilder(ChatColor.WHITE)
        			.addText("UPDATE world = ",loc.getWorld().getUID().toString()
        					,",x=",String.valueOf(loc.getBlockX())
        					,",y=",String.valueOf(loc.getBlockY())
        					,",z=",String.valueOf(loc.getBlockZ())
        					," (dim=",loc.getWorld().getName()
        					,",player=",player.toString(),")")
        			.build(), e);
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
            		super.logWarning(new TextBuilder(ChatColor.WHITE)
                			.addText("SELECT FROM player_loc WHERE dim = ",dim
                					," AND player = " + player.toString())
                			.build(), ex);
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
            super.logSevere(new TextBuilder(ChatColor.WHITE)
        			.addText("SELECT FROM player_loc WHERE dim = ",dim
        					," AND player = " + player.toString())
        			.build(),e);
        }
    	return null;
    }
}
