package uk.co.quartzcraft.kingdoms.kingdom;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import uk.co.quartzcraft.core.QuartzCore;
import uk.co.quartzcraft.core.chat.ChatPhrase;
import uk.co.quartzcraft.core.entity.QPlayer;
import uk.co.quartzcraft.kingdoms.QuartzKingdoms;
import uk.co.quartzcraft.kingdoms.entity.QKPlayer;

public class Kingdom {
	
	private static QuartzKingdoms plugin;
	
	public void QuartzKingdomsConfig(QuartzKingdoms plugin) {
		this.plugin = plugin;
	}
	
	public static String createKingdom(String kingdomName, CommandSender sender) {
		String name_error = "name_error";
		String error = "error";
		Player player = (Player) sender;
		int userID = QPlayer.getUserID(player);
		if(exists(kingdomName)) {
			return name_error;
		}
		
		if(userID == 0) {
			return error;
		} 
		
		try {
			java.sql.Connection connection = QuartzKingdoms.MySQLking.openConnection();
			java.sql.PreparedStatement s = connection.prepareStatement("INSERT INTO Kingdoms (KingdomName, KingID, MemberIDs) VALUES ('" + kingdomName + "', '" + userID + "', '" + userID + "');");
			if(s.executeUpdate() == 1) {
				return kingdomName;
			} else {
				return error;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return error;
		}
	}

	public static String deleteKingdom(String kingdomName, CommandSender sender) {
		String name_error = "name_error";
		String king_error = "error";
		String error = "error";
		Player player = (Player) sender;
		int userID = QPlayer.getUserID(player);
		
		if(exists(kingdomName)) {
			
		} else {
			return error;
		}
		
		if(QKPlayer.isKing(kingdomName, userID)) {
			
		} else {
			return king_error;
		}
		
		try {
			java.sql.Connection connection = QuartzKingdoms.MySQLking.openConnection();
			java.sql.PreparedStatement s = connection.prepareStatement("DELETE FROM Kingdoms WHERE KingdomName = '" + kingdomName + "' AND KingID = '" + userID + "';");
			if(s.executeUpdate() == 1) {
				return kingdomName;
			} else {
				return error;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return error;
		}
		
	}
	
	public static boolean promotePlayer(String kingdomName, CommandSender sender, String playerToPromote, String group) {
		String[] ranks = null;
		ranks[0] = "Citizen";
		ranks[1] = "Knight";
		ranks[2] = "Nobel";
		
		int i = 1;
		int a = 1;
		int current = 0;
		if(i == 1) {
			for(String rank : ranks) {
				if(group.equalsIgnoreCase(rank)) {
					if(QPlayer.addSecondaryGroup(sender, playerToPromote, rank)) {
						return true;
					} else {
						return false;
					}
				}
				current++;
			}
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean exists(String kingdomName) {
		java.sql.Connection connection = QuartzKingdoms.MySQLking.openConnection();
		try {
			java.sql.PreparedStatement s = connection.prepareStatement("SELECT * FROM Kingdoms WHERE KingdomName =" + kingdomName + ";");
			ResultSet res2 = s.executeQuery();
			if(res2.next()) {
		    	 return true;
		     } else {
		    	 return false;
		     }
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static Map getInfo(String kingdomName) {
		HashMap<String, String> info = new HashMap<String, String>();
		
		java.sql.Connection connection = QuartzKingdoms.MySQLking.openConnection();
		try {
			java.sql.PreparedStatement s = connection.prepareStatement("SELECT * FROM Kingdoms WHERE KingdomName =" + kingdomName + ";");
			ResultSet res2 = s.executeQuery();
			if(res2.next()) {
				info.put("id", res2.getString(1));
				info.put("Name", res2.getString(2));
				info.put("Invite Only", res2.getString(3));
				info.put("King", QPlayer.getDisplayName(QKPlayer.getCoreID(res2.getInt(4))));
				info.put("Members", res2.getString(5));
		    	 return info;
		     } else {
		    	 return null;
		     }
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String getName(int id) {
		java.sql.Connection connection = QuartzKingdoms.MySQLking.openConnection();
		try {
			Statement s = QuartzKingdoms.MySQLking.openConnection().createStatement();
			ResultSet res2 = s.executeQuery("SELECT * FROM Kingdoms WHERE id =" + id + ";");
			if(res2.next()) {
		    	 String kingdomName = res2.getString("KingdomName");
		    	 return kingdomName;
		     } else {
		    	 return null;
		     }
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	        	
	}
	
	public static boolean setRelationshipStatus(String kingdom, String relatingKingdom, int status) {
		switch(status) {
			case 1:
				//Neutral
				//Update
			case 2:
				//Ally
				//Update
			case 3:
				//War
				//Update
			default:
				//Do nothing
				return false;
		}
		
	}
	
	public boolean setOpen(String kingdomName, boolean status) {
		
		if(status = true) {
			return true;
		} else {
			return false;
		}
	}

	public static int getID(String kingdomName) {
		java.sql.Connection connection = QuartzKingdoms.MySQLking.openConnection();
		try {
			Statement s = connection.createStatement();
			ResultSet res2 = s.executeQuery("SELECT * FROM Kingdoms WHERE KingdomName =" + kingdomName + ";");
			if(res2.next()) {
		    	 int id = res2.getInt("id");
		    	 return id;
		     } else {
		    	 return 0;
		     }
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}

	public static boolean addUser(Player player) {
		// TODO Auto-generated method stub
		return true;
	}

	public static String getKing(String kingdomName) {
		int id = Kingdom.getID(kingdomName);
		java.sql.Connection connection = QuartzKingdoms.MySQLking.openConnection();
		try {
			Statement s = QuartzKingdoms.MySQLking.openConnection().createStatement();
			ResultSet res2 = s.executeQuery("SELECT * FROM Kingdoms WHERE id =" + id + ";");
			if(res2.next()) {
		    	 int kingID = res2.getInt("KingID");
		    	 int coreKingID = QKPlayer.getCoreID(kingID);
		    	 return QPlayer.getDisplayName(coreKingID);
		     } else {
		    	 return null;
		     }
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static boolean compareKingdom(Player p1, Player p2) {
		if(QKPlayer.getKingdom(p1) == QKPlayer.getKingdom(p2)) {
			return true;
		} else {
			return false;
		}
	}
}
