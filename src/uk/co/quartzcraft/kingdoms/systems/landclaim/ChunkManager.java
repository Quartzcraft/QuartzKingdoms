package uk.co.quartzcraft.kingdoms.systems.landclaim;

import org.bukkit.Chunk;
import org.bukkit.entity.Player;

import uk.co.quartzcraft.core.data.QPlayer;
import uk.co.quartzcraft.core.systems.chat.QCChat;
import uk.co.quartzcraft.kingdoms.QuartzKingdoms;
import uk.co.quartzcraft.kingdoms.data.QKPlayer;
import uk.co.quartzcraft.kingdoms.features.kingdom.Kingdom;
import uk.co.quartzcraft.kingdoms.util.KUtil;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ChunkManager {
	
	public static boolean claimChunk(Kingdom kingdom, Player player) {
		Chunk chunk = player.getLocation().getChunk();
		
		if(!isClaimed(chunk)) {
			//claim chunk
            try {
                java.sql.PreparedStatement s = QuartzKingdoms.DBKing.prepareStatement("INSERT INTO Chunks (kingdom_id, X, Z) VALUES (?, ?, ?);");
                s.setInt(1, kingdom.getID());
                s.setInt(2, chunk.getX());
                s.setInt(3, chunk.getZ());
                s.executeUpdate();
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
		} else {
            return false;
		}
	}

	public static boolean unClaimChunk(Kingdom kingdom, Player player) {
        Chunk chunk = player.getLocation().getChunk();

        if(isClaimed(chunk)) {
            try {
                java.sql.PreparedStatement s = QuartzKingdoms.DBKing.prepareStatement("DELETE * FROM Chunks WHERE X=? AND Z=? AND kingdom_id=?;");
                s.setInt(1, chunk.getX());
                s.setInt(2, chunk.getZ());
                s.setInt(3, kingdom.getID());
                s.executeUpdate();
                return true;
            } catch (SQLException e) {
                KUtil.printException("Failed to delete chunks from database", e);
                return false;
            }
        } else {
            return false;
        }
	}

	public static boolean isClaimed(Chunk chunk) {
        int chunkX = chunk.getX();
        int chunkZ = chunk.getZ();

        try {
            java.sql.PreparedStatement s = QuartzKingdoms.DBKing.prepareStatement("SELECT * FROM Chunks WHERE X=? AND Z=?;");
            s.setInt(1, chunk.getX());
            s.setInt(2, chunk.getZ());
            ResultSet res = s.executeQuery();
            if(res.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
	}

	public static Kingdom getKingdomOwner(Chunk chunk) {
        int chunkX = chunk.getX();
        int chunkZ = chunk.getZ();

        try {
            java.sql.PreparedStatement s = QuartzKingdoms.DBKing.prepareStatement("SELECT * FROM Chunks WHERE X=? AND Z=?;");
            s.setInt(1, chunk.getX());
            s.setInt(2, chunk.getZ());
            ResultSet res = s.executeQuery();
            if(res.next()) {
                int kingdomID = res.getInt("kingdom_id");
                return new Kingdom(kingdomID);
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
	}
	
	
}
