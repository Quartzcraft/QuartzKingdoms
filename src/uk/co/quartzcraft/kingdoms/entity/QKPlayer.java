package uk.co.quartzcraft.kingdoms.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import org.bukkit.entity.Player;

import uk.co.quartzcraft.core.QuartzCore;
import uk.co.quartzcraft.core.entity.QPlayer;
import uk.co.quartzcraft.kingdoms.QuartzKingdoms;

public class QKPlayer extends QPlayer {

	public ResultSet getDataThisPlugin() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public String getKingdom(Player player) {
		String kingdom = null;
		
		String playername = player.toString();
		
		try {
			Statement s1 = QuartzCore.MySQLcore.openConnection().createStatement();
			Statement s2 = QuartzKingdoms.MySQLking.openConnection().createStatement();
			
	        ResultSet res1 = s1.executeQuery("SELECT * FROM PlayerData WHERE DisplayName ='" + playername + "';");
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
