package uk.co.quartzcraft.kingdoms.landprotection;

import org.bukkit.Chunk;
import org.bukkit.entity.Player;

import uk.co.quartzcraft.core.entity.QPlayer;
import uk.co.quartzcraft.kingdoms.QuartzKingdoms;



public class ChunkManager {

	public void claimChunk(Player chunkClaimer) {
		Chunk chunk = chunkClaimer.getLocation().getChunk();
	}
	
	
}
