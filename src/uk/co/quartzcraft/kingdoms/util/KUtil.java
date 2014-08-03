package uk.co.quartzcraft.kingdoms.util;

import uk.co.quartzcraft.kingdoms.QuartzKingdoms;

import java.util.logging.Level;

public class KUtil {

    public static void printException(String message, Exception e) {
        QuartzKingdoms.log.log(Level.WARNING, "[QK] " + message);
        e.printStackTrace();
    }

    public static void log(Level level, String message) {
        QuartzKingdoms.log.log(level, message);
    }
}
