package uk.co.quartzcraft.kingdoms;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import uk.co.quartzcraft.*;
import uk.co.quartzcraft.chat.SetupChatChannel;

public class QuartzKingdoms extends JavaPlugin implements Defaults {

	public void onEnable() {
		
		Logger logger = getLogger();
		
		//ChatChannels
	   	//logger.info("[STARTUP]Registering chat channels...");
	   	
        //Startup notice
		logger.info("The QuartzKingdoms Plugin has been enabled!");
		logger.info("Using QuartzCore version " + release + " " + version);
	}
	 
	@Override
	public void onDisable() {
    	//Shutdown notice
		getLogger().info("The QuartzKingdoms Plugin has been disabled!");
	}

}
