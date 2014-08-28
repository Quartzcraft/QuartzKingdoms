package uk.co.quartzcraft.kingdoms.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import org.bukkit.event.player.PlayerJoinEvent;
import uk.co.quartzcraft.core.event.QPlayerCreationEvent;
import uk.co.quartzcraft.core.event.QPlayerLoginEvent;
import uk.co.quartzcraft.core.systems.chat.QCChat;
import uk.co.quartzcraft.kingdoms.QuartzKingdoms;
import uk.co.quartzcraft.kingdoms.data.QKPlayer;

public class PlayerListener implements Listener {

	private static QuartzKingdoms plugin;
	
	public PlayerListener(QuartzKingdoms plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;
    }
	
	@EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerCreation(QPlayerCreationEvent event) {
           Player player = event.getPlayer();

           if(QKPlayer.createKingdomsPlayer(player)) {
        	   plugin.log.info("[QC] Kingdoms Player, " + player.getDisplayName() + " was created with UUID of " + player.getUniqueId().toString());
           } else {
        	   player.kickPlayer(QCChat.getPhrase("database_error_contact") + "\n" + QCChat.getPhrase("could_not_create_kingdoms_player"));
           }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onQPlayerLogin(QPlayerLoginEvent event) {
        Player player = event.getPlayer();
        if(QKPlayer.exists(player)) {
            //something else
        } else {
            QKPlayer.createKingdomsPlayer(player);
        }

        QKPlayer qkPlayer = new QKPlayer(player);

        //TODO add kingdom rank
        player.setDisplayName("[" + qkPlayer.getKingdom().getName() + "]" + player.getDisplayName());
    }
}
