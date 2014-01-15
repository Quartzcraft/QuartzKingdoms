package uk.co.quartzcraft.kingdoms;

import java.sql.Connection;
import java.util.Arrays;
import java.util.logging.Logger;

import com.sun.tools.javac.util.List;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import uk.co.quartzcraft.core.QuartzCore;
import uk.co.quartzcraft.core.database.MySQL;
import uk.co.quartzcraft.kingdoms.command.*;
import uk.co.quartzcraft.kingdoms.listeners.*; 

public class QuartzKingdoms extends JavaPlugin {
	
	public Plugin plugin = this.plugin;
	
	public static final Logger log = Logger.getLogger("Minecraft");
	
	public static String releaseVersion = QuartzCore.displayReleaseVersion();

	public static Connection DBKing = null;

	public static MySQL MySQLking = null;
	
	@Override
	public void onDisable() {
		
		//Database
		//log.info("[SHUTDOWN]Terminating Connection to Database");
		//DBKing = MySQL.closeConnection();
				
    	//Shutdown notice
		log.info("[QK]The QuartzKingdoms Plugin has been disabled!");
	}
	
	@Override
	public void onEnable() {
		
		log.info("[QC]Running plugin configuration");
		this.saveDefaultConfig();
		
		String SQLKingHost = this.getConfig().getString("database.kingdoms.host");
		String SQLKingDatabase = this.getConfig().getString("database.kingdoms.database");
		String SQLKingUser = this.getConfig().getString("database.kingdoms.username");
		String SQLKingPassword = this.getConfig().getString("database.kingdoms.password");
		MySQLking = new MySQL(plugin, SQLKingHost, "3306", SQLKingDatabase, SQLKingUser, SQLKingPassword);
		
		//Phrases
		log.info("[QK][STARTUP]Creating Phrases");
		
		//Database
		//logger.info("[STARTUP]Connecting to Database");
		DBKing = MySQLking.openConnection();
		
		//Listeners
		log.info("[QK][STARTUP]Registering Listeners");
		//getServer().getPluginManager().registerEvents(new BlockListener(), this);
		new BlockListener(this);
		
		//Commands
		log.info("[QK][STARTUP]Registering Commands");
		getCommand("kingdom").setExecutor(new CommandKingdom());
		CommandKingdom.addCommand(Arrays.asList("info"), new KingdomInfoSubCommand());
		CommandKingdom.addCommand(Arrays.asList("create"), new KingdomCreateSubCommand());
	   	
        //Startup notice
		log.info("[QK]The QuartzKingdoms Plugin has been enabled!");
		log.info("[QK]Compiled using QuartzCore version " + releaseVersion);
	}

}
