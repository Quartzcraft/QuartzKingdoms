package uk.co.quartzcraft.kingdoms;

import java.sql.Connection;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import uk.co.quartzcraft.QuartzCore;
import uk.co.quartzcraft.Defaults;
import uk.co.quartzcraft.database.MySQL;

public class QuartzKingdoms extends JavaPlugin implements Defaults {
	
	public Plugin plugin = this.plugin;
	
	public static Connection DBCore = null;
	public static Connection DBKing = null;

	MySQL MySQL = new MySQL(plugin, "localhost", "3306", "Quartz", "root", "database1");
	MySQL MySQLking = new MySQL(plugin, "localhost", "3306", "Kingdoms", "root", "database1");
	
	public void onEnable() {
		
		Logger logger = getLogger();
		
		//Database
		logger.info("[STARTUP]Connecting to Database");
		DBKing = MySQL.openConnection();
		DBCore = MySQL.openConnection();
		
		//Listeners
		logger.info("[STARTUP]Registering Listeners");
		
		//Commands
		logger.info("[STARTUP]Registering Commands");
	   	
        //Startup notice
		logger.info("The QuartzKingdoms Plugin has been enabled!");
		logger.info("Compiled using QuartzCore version " + release + " " + version);
	}
	 
	@Override
	public void onDisable() {
		
		Logger logger = getLogger();
		
		//Database
		logger.info("[SHUTDOWN]Terminating Connection to Database");
		//DBKing = MySQL.closeConnection();
				
    	//Shutdown notice
		getLogger().info("The QuartzKingdoms Plugin has been disabled!");
	}

}
