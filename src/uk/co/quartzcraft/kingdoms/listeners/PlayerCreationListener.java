package uk.co.quartzcraft.kingdoms.listeners;

import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import uk.co.quartzcraft.core.chat.ChatPhrase;
import uk.co.quartzcraft.core.event.QPlayerCreationEvent;
import uk.co.quartzcraft.kingdoms.QuartzKingdoms;
import uk.co.quartzcraft.kingdoms.entity.QKPlayer;

public class PlayerCreationListener implements Listener {

	private static QuartzKingdoms plugin;
	
	public PlayerCreationListener(QuartzKingdoms plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;
    }
	
	@EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerCreation(QPlayerCreationEvent event) {
           Player player = event.getPlayer();
           if(QKPlayer.createKingdomsPlayer(player)) {
        	   plugin.log.info("[QC] Player, " + player.getDisplayName() + " was created with UUID of " + player.getUniqueId().toString());
           } else {
        	   player.kickPlayer(ChatPhrase.getPhrase("database_error_contact") + "\n" + ChatPhrase.getPhrase("could_not_create_kingdoms_player"));
           }
    }
}
