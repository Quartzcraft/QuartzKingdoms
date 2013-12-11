package uk.co.quartzcraft.kingdoms;

import java.sql.Connection;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import uk.co.quartzcraft.QuartzCore;

public class QuartzKingdoms extends JavaPlugin {
	
	public Plugin plugin = this.plugin;
	
	public static final Logger log = Logger.getLogger("Minecraft");
	
	//public static Connection DBCore = null;
	//public static Connection DBKing = null;

	//MySQL MySQLcore = new MySQL(plugin, "localhost", "3306", "Quartz", "root", "database1");
	//MySQL MySQLking = new MySQL(plugin, "localhost", "3306", "Kingdoms", "root", "database1");
	
	@Override
	public void onEnable() {
		
		//Database
		//logger.info("[STARTUP]Connecting to Database");
		//DBKing = MySQLking.openConnection();
		//DBCore = MySQLcore.openConnection();
		
		//Listeners
		log.info("[STARTUP]Registering Listeners");
		
		//Commands
		log.info("[STARTUP]Registering Commands");
	   	
        //Startup notice
		log.info("The QuartzKingdoms Plugin has been enabled!");
		log.info("Compiled using QuartzCore version " + QuartzCore.release + " " + QuartzCore.version);
	}
	 
	@Override
	public void onDisable() {
		
		//Database
		//log.info("[SHUTDOWN]Terminating Connection to Database");
		//DBKing = MySQL.closeConnection();
				
    	//Shutdown notice
		log.info("The QuartzKingdoms Plugin has been disabled!");
	}

}
