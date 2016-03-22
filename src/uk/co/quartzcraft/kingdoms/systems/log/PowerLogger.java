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

    }

    /**
     * Logs the change in power to be retrieved later.
     *
     * @param player Player object of a player
     * @param power The number of power taken
     */
    public static void logTake(QKPlayer player, int power) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

    }
}
