package uk.co.quartzcraft.kingdoms.managers;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

import uk.co.quartzcraft.core.entity.QPlayer;
import uk.co.quartzcraft.kingdoms.QuartzKingdoms;

public class ChunkManager {

	public static boolean checkClaim(Chunk chunk) {
		return false;
	}
	
	public static boolean claimChunk(Player player) {
		Player chunkClaimer = player;
		Chunk chunk = chunkClaimer.getLocation().getChunk();
		
		if(checkClaim(chunk) == false) {
			//claim chunk
		} else {
			//error
		}
		return false;
	}

	public static boolean unClaimChunk(Player player) {
		// TODO Auto-generated method stub
		return false;
	}
	
	
}
