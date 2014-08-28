package uk.co.quartzcraft.kingdoms.listeners;

import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import uk.co.quartzcraft.kingdoms.QuartzKingdoms;
import uk.co.quartzcraft.kingdoms.data.QKPlayer;
import uk.co.quartzcraft.kingdoms.systems.landclaim.ChunkManager;

public class BlockListener implements Listener {
	
	public BlockListener(QuartzKingdoms plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
	
	@EventHandler(priority = EventPriority.LOWEST)
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if(permission(event.getBlock().getChunk(), player)) {

        } else {
            event.setCancelled(true);
        }
    }
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if(permission(event.getBlock().getChunk(), player)) {

        } else {
            event.setCancelled(true);
        }
	}

    @EventHandler(priority = EventPriority.LOWEST)
    public void onIgnite(BlockIgniteEvent event) {
        Player player = event.getPlayer();
        if(permission(event.getBlock().getChunk(), player)) {

        } else {
            event.setCancelled(true);
        }
    }

    public boolean permission(Chunk chunk, Player player) {
        if(ChunkManager.isClaimed(chunk)) {
            if(QKPlayer.getKingdom(player) == ChunkManager.getKingdomOwner(chunk)) {
               return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

}
