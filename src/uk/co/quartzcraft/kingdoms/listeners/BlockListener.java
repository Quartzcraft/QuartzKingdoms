package uk.co.quartzcraft.kingdoms.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import uk.co.quartzcraft.core.QuartzCore;
import uk.co.quartzcraft.kingdoms.QuartzKingdoms;

public class BlockListener implements Listener {
	
	public BlockListener(QuartzKingdoms plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
	
	@EventHandler(priority = EventPriority.HIGHEST)
    public void onBreak(BlockBreakEvent event) {
           
    }
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlace(BlockPlaceEvent event) {
		
	}

}
