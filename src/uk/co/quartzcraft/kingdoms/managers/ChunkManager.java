package uk.co.quartzcraft.kingdoms.managers;

import org.bukkit.Chunk;
import org.bukkit.entity.Player;

import uk.co.quartzcraft.core.entity.QPlayer;
import uk.co.quartzcraft.kingdoms.QuartzKingdoms;

public class ChunkManager {

	public boolean checkClaim(Chunk chunk) {
		return false;
	}
	
	public void claimChunk(Player chunkClaimer) {
		Chunk chunk = chunkClaimer.getLocation().getChunk();
		
		if(checkClaim(chunk) == false) {
			//claim chunk
		} else {
			//error
		}
	}
	
	
}
