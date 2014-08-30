package uk.co.quartzcraft.kingdoms.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import uk.co.quartzcraft.core.data.QPlayer;
import uk.co.quartzcraft.core.event.QPlayerCreationEvent;
import uk.co.quartzcraft.core.event.QPlayerLoginEvent;
import uk.co.quartzcraft.core.systems.chat.QCChat;
import uk.co.quartzcraft.kingdoms.QuartzKingdoms;
import uk.co.quartzcraft.kingdoms.data.QKPlayer;
import uk.co.quartzcraft.kingdoms.systems.landclaim.ChunkManager;

public class PlayerListener implements Listener {

	private static QuartzKingdoms plugin;
	
	public PlayerListener(QuartzKingdoms plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;
    }
	
	@EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerCreation(QPlayerCreationEvent event) {
        QPlayer player = event.getQPlayer();
        Player bplayer = event.getPlayer();

        if(QKPlayer.createKingdomsPlayer(player)) {
            plugin.log.info("[QC] Kingdoms Player, " + bplayer.getDisplayName() + " was created with UUID of " + bplayer.getUniqueId().toString());
        } else {
            bplayer.kickPlayer(QCChat.getPhrase("database_error_contact") + "\n" + QCChat.getPhrase("could_not_create_kingdoms_player"));
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

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerDeath(PlayerDeathEvent e) {
        Player player = e.getEntity();
        QKPlayer qkPlayer = new QKPlayer(player);
        qkPlayer.takePower(5);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerRespawn(PlayerRespawnEvent e) {
        Player player = e.getPlayer();
        //QKPlayer qkPlayer = new QKPlayer(player);
        //qkPlayer.takePower(5);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (event.getFrom().getChunk().equals(event.getTo().getChunk())) {
            //Nothing
        } else {
            if(ChunkManager.isClaimed(event.getTo().getChunk()) && ChunkManager.isClaimed(event.getFrom().getChunk())) {
                if(!ChunkManager.getKingdomOwner(event.getFrom().getChunk()).equals(ChunkManager.getKingdomOwner(event.getTo().getChunk()))) {
                    player.sendMessage(QCChat.getPhrase("now_leaving_the_land_of") + ChunkManager.getKingdomOwner(event.getFrom().getChunk()));
                    player.sendMessage(QCChat.getPhrase("now_entering_the_land_of") + ChunkManager.getKingdomOwner(event.getTo().getChunk()));
                } else {
                    //player.sendMessage(QCChat.getPhrase("now_entering_the_land_of") + ChunkManager.getKingdom(event.getTo().getChunk()));
                    return;
                }
            } else if(ChunkManager.isClaimed(event.getFrom().getChunk())) {
                player.sendMessage(QCChat.getPhrase("now_leaving_the_land_of") + ChunkManager.getKingdomOwner(event.getFrom().getChunk()));
            } else if(ChunkManager.isClaimed(event.getTo().getChunk())) {
                player.sendMessage(QCChat.getPhrase("now_entering_the_land_of") + ChunkManager.getKingdomOwner(event.getTo().getChunk()));
            } else {
                return;
            }
        }
    }
}
