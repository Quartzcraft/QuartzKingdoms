package uk.co.quartzcraft.kingdoms;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class QuartzKingdoms extends JavaPlugin {

	public void onEnable(){
		
        //Startup notice
		getLogger().info("The QuartzKingdoms Plugin has been enabled!");
	}
	 
	@Override
	public void onDisable() {
    	//Shutdown notice
		getLogger().info("The QuartzKingdoms Plugin has been disabled!");
	}

}
