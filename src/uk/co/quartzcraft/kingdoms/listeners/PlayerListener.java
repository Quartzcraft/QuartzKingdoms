package uk.co.quartzcraft.kingdoms.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import org.bukkit.event.player.PlayerJoinEvent;
import uk.co.quartzcraft.core.chat.ChatPhrase;
import uk.co.quartzcraft.core.event.QPlayerCreationEvent;
import uk.co.quartzcraft.core.event.QPlayerLoginEvent;
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
        /*
           if(QKPlayer.createKingdomsPlayer(player)) {
        	   plugin.log.info("[QC] Player, " + player.getDisplayName() + " was created with UUID of " + player.getUniqueId().toString());
           } else {
        	   player.kickPlayer(ChatPhrase.getPhrase("database_error_contact") + "\n" + ChatPhrase.getPhrase("could_not_create_kingdoms_player"));
           }
        */
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onQPlayerLogin(QPlayerLoginEvent event) {
        Player player = event.getPlayer();
        if(QKPlayer.getID(player) != 0) {
            //something else
        } else {
            QKPlayer.createKingdomsPlayer(player);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        String playerName = player.getDisplayName();
        String kingdomPrefix = "[" + QKPlayer.getKingdom(player) + "]";
        player.setDisplayName(kingdomPrefix + playerName);
    }
}
