package uk.co.quartzcraft.kingdoms.listeners;

import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import uk.co.quartzcraft.core.QuartzCore;
import uk.co.quartzcraft.core.event.QPlayerCreationEvent;
import uk.co.quartzcraft.kingdoms.QuartzKingdoms;

public class PlayerCreationListener implements Listener {

	public PlayerCreationListener(QuartzKingdoms plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
	
	@EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerCreation(QPlayerCreationEvent event) {
           Player player = event.getPlayer();
           
    }
}
