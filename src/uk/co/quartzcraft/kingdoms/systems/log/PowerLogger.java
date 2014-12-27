package uk.co.quartzcraft.kingdoms.systems.log;

import org.bukkit.entity.Player;
import uk.co.quartzcraft.core.QuartzCore;
import uk.co.quartzcraft.core.data.QPlayer;
import uk.co.quartzcraft.core.util.Util;
import uk.co.quartzcraft.kingdoms.data.QKPlayer;

import java.sql.SQLException;
import java.sql.Timestamp;

public class PowerLogger {

    /**
     * Logs the change in power to be retrieved later.
     *
     * @param player QPlayer object of a player
     * @param power The number of power added
     */
    public static void logAdd(QKPlayer player, int power) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        try{
            java.sql.PreparedStatement s = QuartzCore.DBLog.prepareStatement("INSERT INTO PowerLog (server_id, timestamp, player_id, taken, power) VALUES (?, ?, ?, 0, ?);");
            s.setString(1, QuartzCore.getServerName());
            s.setTimestamp(2, timestamp);
            s.setInt(3, player.getQPlayer().getID());
            s.setInt(4, power);
            s.executeUpdate();
        } catch(SQLException e) {
            Util.printException("Failed to log addition of power", e);
        }
    }

    /**
     * Logs the change in power to be retrieved later.
     *
     * @param player Player object of a player
     * @param power The number of power taken
     */
    public static void logTake(QKPlayer player, int power) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        try{
            java.sql.PreparedStatement s = QuartzCore.DBLog.prepareStatement("INSERT INTO PowerLog (server_id, timestamp, player_id, taken, power) VALUES (?, ?, ?, 1, ?);");
            s.setString(1, QuartzCore.getServerName());
            s.setTimestamp(2, timestamp);
            s.setInt(3, player.getQPlayer().getID());
            s.setInt(4, power);
            s.executeUpdate();
        } catch(SQLException e) {
            Util.printException("Failed to log taking of power", e);
        }
    }
}
