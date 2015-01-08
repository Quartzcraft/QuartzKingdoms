package uk.co.quartzcraft.kingdoms.listeners;

import org.bukkit.ChatColor;
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
import uk.co.quartzcraft.core.util.Util;

import uk.co.quartzcraft.kingdoms.QuartzKingdoms;
import uk.co.quartzcraft.kingdoms.data.QKPlayer;
import uk.co.quartzcraft.kingdoms.features.FancyMessages;
import uk.co.quartzcraft.kingdoms.features.kingdom.Kingdom;
import uk.co.quartzcraft.kingdoms.systems.landclaim.ChunkManager;
import uk.co.quartzcraft.kingdoms.systems.perms.Permissions;
import uk.co.quartzcraft.kingdoms.util.KUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

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
        if(!QKPlayer.exists(player)) {
            QKPlayer.createKingdomsPlayer(player);
        }

        QKPlayer qkPlayer = new QKPlayer(event.getQPlayer());

        Permissions.registerPlayerPerms(qkPlayer);

        player.setDisplayName(qkPlayer.getKingdomGroup().getStyleForName() + player.getDisplayName() + ChatColor.RESET);
        player.setPlayerListName(Util.removeExtraChars(player.getDisplayName(), 16));

        if(qkPlayer.kingdomMember()) {
            if(qkPlayer.isKing()) {
                final QKPlayer qqkPlayer = qkPlayer;
                final Player pplayer = player;
                try {
                    ResultSet pendingWar = qqkPlayer.getKingdom().getProposedEnemy();
                    ResultSet pendingAlly = qqkPlayer.getKingdom().getProposedAlly();
                    if(pendingWar != null) {
                        while(pendingWar.next()) {
                            Kingdom kingdom1 = new Kingdom(pendingWar.getInt("last_update_id"));
                            qkPlayer.getQPlayer().sendMessage(FancyMessages.declaredWar(pplayer, kingdom1.getName()));
                        }
                    }
                    if(pendingAlly != null) {
                        while(pendingAlly.next()) {
                            Kingdom kingdom2 = new Kingdom(pendingAlly.getInt("last_update_id"));
                            qkPlayer.getQPlayer().sendMessage(FancyMessages.proposedAlly(pplayer, kingdom2.getName()));
                        }
                    }
                } catch(SQLException e) {
                    KUtil.printException("Failed to retrieve and loop through pending war and ally relationships", e);
                }

            }
            else {
                KUtil.log(Level.INFO, "The player was not a king of a kingdom");
            }
        } else {
            KUtil.log(Level.INFO, "The player was not a member of a kingdom");
        }
        KUtil.log(Level.INFO, qkPlayer.getQPlayer().getName() + " has successfully joined!");
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerDeath(PlayerDeathEvent e) {
        Player player = e.getEntity();
        QKPlayer qkPlayer = new QKPlayer(player);
        qkPlayer.takePower(3);
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
                        if(ChunkManager.getKingdomOwner(chunkFrom).getID() == ChunkManager.getKingdomOwner(chunkTo).getID()) {
                            return;
                        } else {
                            KUtil.sendMsg(player, QCChat.getPhrase("now_leaving_the_land_of") + ChunkManager.getKingdomOwner(chunkFrom).getName());
                            KUtil.sendMsg(player, QCChat.getPhrase("now_entering_the_land_of") + ChunkManager.getKingdomOwner(chunkTo).getName());
                            return;
                        }
                    } else {
                        if(ChunkManager.isClaimed(chunkFrom)) {
                            KUtil.sendMsg(player, QCChat.getPhrase("now_leaving_the_land_of") + ChunkManager.getKingdomOwner(chunkFrom).getName());
                            return;
                        } else if(ChunkManager.isClaimed(chunkTo)) {
                            KUtil.sendMsg(player, QCChat.getPhrase("now_entering_the_land_of") + ChunkManager.getKingdomOwner(chunkTo).getName());
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
