package uk.co.quartzcraft.kingdoms.listeners;

import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import uk.co.quartzcraft.core.systems.chat.QCChat;
import uk.co.quartzcraft.kingdoms.QuartzKingdoms;
import uk.co.quartzcraft.kingdoms.data.QKPlayer;
import uk.co.quartzcraft.kingdoms.systems.landclaim.ChunkManager;
import uk.co.quartzcraft.kingdoms.util.KUtil;

public class BlockListener implements Listener {
	
	public BlockListener(QuartzKingdoms plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
	
	@EventHandler(priority = EventPriority.LOWEST)
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if(permission(event.getBlock().getChunk(), player)) {
            return;
        } else {
            event.setCancelled(true);
            KUtil.sendMsg(player, QCChat.getPhrase("you_do_not_have_permission_to_build_here"));
        }
    }
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if(permission(event.getBlock().getChunk(), player)) {
            return;
        } else {
            event.setCancelled(true);
            KUtil.sendMsg(player, QCChat.getPhrase("you_do_not_have_permission_to_build_here"));
        }
	}

    @EventHandler(priority = EventPriority.LOWEST)
    public void onIgnite(BlockIgniteEvent event) {
        Player player = event.getPlayer();
        if(permission(event.getBlock().getChunk(), player)) {
            return;
        } else {
            event.setCancelled(true);
            KUtil.sendMsg(player, QCChat.getPhrase("you_do_not_have_permission_to_build_here"));
        }
    }

    public boolean permission(Chunk chunk, Player player) {
        QKPlayer qkPlayer = new QKPlayer(player);
        if(ChunkManager.isClaimed(chunk)) {
            if(qkPlayer.getKingdom().getID() == ChunkManager.getKingdomOwner(chunk).getID() || ChunkManager.getKingdomOwner(chunk).isAlly(qkPlayer.getKingdom())) {
               return true;
            }
            return false;
        } else {
            return true;
        }
    }

}
