package uk.co.quartzcraft.kingdoms.listeners;

import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import uk.co.quartzcraft.core.data.QPlayer;
import uk.co.quartzcraft.core.event.QPlayerCreationEvent;
import uk.co.quartzcraft.core.event.QPlayerJoinEvent;
import uk.co.quartzcraft.core.systems.chat.QCChat;
import uk.co.quartzcraft.core.util.TaskChain;
import uk.co.quartzcraft.kingdoms.QuartzKingdoms;
import uk.co.quartzcraft.kingdoms.data.QKPlayer;
import uk.co.quartzcraft.kingdoms.systems.landclaim.ChunkManager;
import uk.co.quartzcraft.kingdoms.systems.perms.Permissions;

public class PlayerListener implements Listener {

	private static QuartzKingdoms plugin;
	
	public PlayerListener(QuartzKingdoms plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;
    }
	
	@EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerCreation(QPlayerCreationEvent event) {
        QPlayer player = event.getQPlayer();
        Player bplayer = event.getPlayer();

        if(QKPlayer.createKingdomsPlayer(player)) {
            plugin.log.info("[QC] Kingdoms Player, " + bplayer.getDisplayName() + " was created with UUID of " + bplayer.getUniqueId().toString());
        } else {
            bplayer.kickPlayer(QCChat.getPhrase("database_error_contact") + "\n" + QCChat.getPhrase("could_not_create_kingdoms_player"));
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onQPlayerJoin(QPlayerJoinEvent event) {
        Player player = event.getPlayer();
        if(QKPlayer.exists(player)) {
            Permissions.registerPlayerPerms(new QKPlayer(event.getQPlayer()));
        } else {
            QKPlayer.createKingdomsPlayer(player);
            Permissions.registerPlayerPerms(new QKPlayer(event.getQPlayer()));
        }

        QKPlayer qkPlayer = new QKPlayer(player);

        if(qkPlayer.kingdomMember()) {
            if(qkPlayer.isKing()) {
                //TODO Add pending war checks
            }
        }
        plugin.logg.info("[QK] " + qkPlayer.getQPlayer().getName() + " has successfully joined!");
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
        final Player player = event.getPlayer();
        final Chunk chunkFrom = event.getFrom().getChunk();
        final Chunk chunkTo = event.getTo().getChunk();

        TaskChain.newChain().add(new TaskChain.AsyncGenericTask() {
            @Override
            protected void run() {
                if(chunkFrom.equals(chunkTo)) {
                    return;
                } else {
                    if(ChunkManager.isClaimed(chunkTo) && ChunkManager.isClaimed(chunkFrom)) {
                        if(ChunkManager.getKingdomOwner(chunkFrom).equals(ChunkManager.getKingdomOwner(chunkTo))) {
                            return;
                        } else {
                            player.sendMessage(QCChat.getPhrase("now_leaving_the_land_of") + ChunkManager.getKingdomOwner(chunkFrom).getName());
                            player.sendMessage(QCChat.getPhrase("now_entering_the_land_of") + ChunkManager.getKingdomOwner(chunkTo).getName());
                            return;
                        }
                    } else {
                        if(ChunkManager.isClaimed(chunkFrom)) {
                            player.sendMessage(QCChat.getPhrase("now_leaving_the_land_of") + ChunkManager.getKingdomOwner(chunkFrom).getName());
                            return;
                        } else if(ChunkManager.isClaimed(chunkTo)) {
                            player.sendMessage(QCChat.getPhrase("now_entering_the_land_of") + ChunkManager.getKingdomOwner(chunkTo).getName());
                            return;
                        } else {
                            return;
                        }
                    }
                }
            }
        }).execute();
    }
}
