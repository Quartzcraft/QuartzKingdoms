package uk.co.quartzcraft.kingdoms.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import uk.co.quartzcraft.core.QuartzCore;
import uk.co.quartzcraft.core.entity.QPlayer;
import uk.co.quartzcraft.kingdoms.QuartzKingdoms;
import uk.co.quartzcraft.kingdoms.kingdom.Kingdom;

public class QKPlayer extends QPlayer {

	public ResultSet getDataThisPlugin() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public static String getKingdom(Player player) {
		String error = "error";
		String kingdom = null;
		//Player player = Bukkit.getServer().getPlayer(playername);
		UUID UUID = player.getUniqueId();
		String SUUID = UUID.toString();
		
		try {
			Statement s1 = QuartzCore.MySQLcore.openConnection().createStatement();
			Statement s2 = QuartzKingdoms.MySQLking.openConnection().createStatement();
			Statement s3 = QuartzKingdoms.MySQLking.openConnection().createStatement();
			
	        ResultSet res1 = s1.executeQuery("SELECT * FROM PlayerData WHERE UUID ='" + SUUID + "';");
	        
	        if(res1.next()) {
		        ResultSet res2 = s2.executeQuery("SELECT * FROM KingdomsPlayerData WHERE playerID =" + res1.getInt(1) + ";");
		        if(res2.next()) {
		        	int kingdomID = res2.getInt(4);
		        	ResultSet res3 = s3.executeQuery("SELECT * FROM Kingdoms WHERE id =" + kingdomID + ";");
		        	if(res3.next()) {
		        		kingdom = res3.getString("KingdomName");
		        		return kingdom;
		        	}
		        } else {
		        	return error;
		        }
	        } else {
	        	return error;
	        }
	        
		} catch(SQLException e) {
			
		}
		
		return kingdom;
	}

	@Override
	public boolean createPlayerThisPlugin() {
		// TODO Auto-generated method stub
		return false;
	}

	public static boolean isKing(String kingdomName, int userID) {
		// TODO Auto-generated method stub
		return false;
	}

	public static int getCoreID(int id) {
		Statement s;
		
		try {
			s = QuartzKingdoms.MySQLking.openConnection().createStatement();
			ResultSet res = s.executeQuery("SELECT * FROM KingdomsPlayerData WHERE id ='" + id + "';");
	        if(res.next()) {
	        	return res.getInt(2);
	        } else {
	        	return 0;
	        }
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	public static int getID(Player player) {
		Statement s;
		int userID = QKPlayer.getUserID(player);
		
		try {
			s = QuartzKingdoms.MySQLking.openConnection().createStatement();
			ResultSet res = s.executeQuery("SELECT * FROM KingdomsPlayerData WHERE PlayerID ='" + userID + "';");
	        if(res.next()) {
	        	return res.getInt(1);
	        } else {
	        	return 0;
	        }
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	public static boolean joinKingdom(Player player, String kingdomName) {
		int userID = getID(player);
		int kingdomID = Kingdom.getID(kingdomName);
		
		try {
			java.sql.Connection connection = QuartzKingdoms.MySQLking.openConnection();
			java.sql.PreparedStatement s = connection.prepareStatement("UPDATE KingdomsPlayerData SET KingdomID=" + kingdomID + " WHERE id=" + userID + ");");
			if(s.executeUpdate() == 1) {
				if(Kingdom.addUser(player)) {
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		} catch (SQLException e) {
			
		}
		return false;
	}
	
	public static boolean leaveKingdom(Player player, String kingdomName) {
		
		return false;
	}
	
	public static int getKingdomID(String playername) {
		String kingdom = null;
		Player player = Bukkit.getServer().getPlayer(playername);
		UUID UUID = player.getUniqueId();
		String SUUID = UUID.toString();
		
		try {
			Statement s1 = QuartzCore.MySQLcore.openConnection().createStatement();
			Statement s2 = QuartzKingdoms.MySQLking.openConnection().createStatement();
			Statement s3 = QuartzKingdoms.MySQLking.openConnection().createStatement();
			
	        ResultSet res1 = s1.executeQuery("SELECT * FROM PlayerData WHERE UUID ='" + SUUID + "';");
	        
	        if(res1.next()) {
		        ResultSet res2 = s2.executeQuery("SELECT * FROM KingdomsPlayerData WHERE playerID =" + res1.getInt(1) + ";");
		        if(res2.next()) {
		        	int kingdomID = res2.getInt(4);
		        	return kingdomID;
		        } else {
		        	return 0;
		        }
	        } else {
	        	return 0;
	        }
	        
		} catch(SQLException e) {
			
		}
		return 0;
	}

}
