package uk.co.quartzcraft.kingdoms.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import uk.co.quartzcraft.core.chat.ChatPhrase;
import uk.co.quartzcraft.kingdoms.QuartzKingdoms;
import uk.co.quartzcraft.kingdoms.systems.landclaim.ChunkManager;

public class PlayerMoveListener implements Listener {

	public PlayerMoveListener(QuartzKingdoms plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
	
	@EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		if (event.getFrom().getChunk().equals(event.getTo().getChunk())) {
			//Nothing
		} else {
			if(ChunkManager.isClaimed(event.getTo().getChunk()) && ChunkManager.isClaimed(event.getFrom().getChunk())) {
				if(ChunkManager.getKingdomOwner(event.getFrom().getChunk()) != ChunkManager.getKingdomOwner(event.getTo().getChunk())) {
					player.sendMessage(ChatPhrase.getPhrase("now_leaving_the_land_of") + ChunkManager.getKingdomOwner(event.getFrom().getChunk()));
					player.sendMessage(ChatPhrase.getPhrase("now_entering_the_land_of") + ChunkManager.getKingdomOwner(event.getTo().getChunk()));
				} else {
					//player.sendMessage(ChatPhrase.getPhrase("now_entering_the_land_of") + ChunkManager.getKingdom(event.getTo().getChunk()));
				}
			} else if(ChunkManager.isClaimed(event.getFrom().getChunk())) {
				player.sendMessage(ChatPhrase.getPhrase("now_leaving_the_land_of") + ChunkManager.getKingdomOwner(event.getFrom().getChunk()));
			} else if(ChunkManager.isClaimed(event.getTo().getChunk())) {
				player.sendMessage(ChatPhrase.getPhrase("now_entering_the_land_of") + ChunkManager.getKingdomOwner(event.getTo().getChunk()));
			} else {
				//Nothing
			}
		}
    }
}
