package uk.co.quartzcraft.kingdoms;

import java.sql.Connection;
import java.util.Arrays;
import java.util.logging.Logger;

import com.sun.tools.javac.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import uk.co.quartzcraft.core.QuartzCore;
import uk.co.quartzcraft.core.systems.chat.QCChat;
import uk.co.quartzcraft.core.command.framework.QCommandFramework;
import uk.co.quartzcraft.core.database.MySQL;
import uk.co.quartzcraft.core.command.framework.QCommand;
import uk.co.quartzcraft.kingdoms.command.*;
import uk.co.quartzcraft.kingdoms.features.chat.ChatPhrases;
import uk.co.quartzcraft.kingdoms.listeners.*;

public class QuartzKingdoms extends JavaPlugin {
	
	public static Plugin plugin;
    public static Plugin core;
    public static final Logger log = Logger.getLogger("Minecraft");
    public static FileConfiguration config;

    public FileConfiguration pluginConfig;
	
	public static String releaseVersion = QuartzCore.version;
    public static String version;

	public static Connection DBKing = null;
	public static MySQL MySQLking = null;

    public QCommandFramework commandFramework;
	
	@Override
	public void onDisable() {
				
    	//Shutdown notice
		log.info("[QK] The QuartzKingdoms Plugin has been disabled!");
	}
	
	@Override
	public void onEnable() {

        version = this.getDescription().getVersion();
        plugin = this;
        core = QuartzCore.plugin;

        //Plugin config
		log.info("[QK] Running plugin configuration");
		this.saveDefaultConfig();
        config = plugin.getConfig();
        this.pluginConfig = plugin.getConfig();
		
		String SQLKingHost = this.pluginConfig.getString("database.kingdoms.host");
		String SQLKingDatabase = this.pluginConfig.getString("database.kingdoms.database");
		String SQLKingUser = this.pluginConfig.getString("database.kingdoms.username");
		String SQLKingPassword = this.pluginConfig.getString("database.kingdoms.password");
		MySQLking = new MySQL(plugin, SQLKingHost, "3306", SQLKingDatabase, SQLKingUser, SQLKingPassword);
		
		//Phrases
		log.info("[QK][STARTUP]Creating Phrases");
        ChatPhrases.phrases();
		
		//Database
		log.info("[QK][STARTUP] Connecting to Database");
		DBKing = MySQLking.openConnection();
		
		//Listeners
		log.info("[QK][STARTUP] Registering Listeners");
		new BlockListener(this);
		new PlayerListener(this);
        new PInfoListener(this);
		
		//Commands
		log.info("[QK][STARTUP] Registering Commands");
        commandFramework = new QCommandFramework(this);
        commandFramework.registerCommands(new CommandKingdom(this, core));
        commandFramework.registerCommands(new CommandKAdmin(this, core));
	   	
        //Startup notice
		log.info("[QK] The QuartzKingdoms Plugin has been enabled!");
		log.info("[QK] QuartzKingdoms version " + version + " compiled with QuartzCore version " + releaseVersion);
        //log.info("[QK] QuartzKingdoms version " + version + " compiled with QuartzCore version " + QuartzCore.getVersion());
	}

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        return commandFramework.handleCommand(sender, label, command, args);
    }

}
