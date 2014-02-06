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

public class QKPlayer extends QPlayer {

	public ResultSet getDataThisPlugin() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public static String getKingdom(String playername) {
		String kingdom = null;
		Player player = Bukkit.getServer().getPlayer(playername);
		UUID UUID = player.getUniqueId();
		String SUUID = UUID.toString();
		
		try {
			Statement s1 = QuartzCore.MySQLcore.openConnection().createStatement();
			Statement s2 = QuartzKingdoms.MySQLking.openConnection().createStatement();
			
	        ResultSet res1 = s1.executeQuery("SELECT * FROM PlayerData WHERE UUID ='" + SUUID + "';");
	        res1.next();
	        
	        ResultSet res2 = s2.executeQuery("SELECT * FROM KingdomsPlayerData WHERE playerID =" + res1.getInt(1) + ";");
	        res2.next();
	        
		} catch(SQLException e) {
			
		}
		
		return kingdom;
	}

	@Override
	public boolean createPlayerThisPlugin() {
		// TODO Auto-generated method stub
		return false;
	}

}
