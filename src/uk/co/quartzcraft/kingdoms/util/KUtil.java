package uk.co.quartzcraft.kingdoms.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import uk.co.quartzcraft.core.systems.chat.QCChat;
import uk.co.quartzcraft.core.util.Util;
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

    public static void sendMsg(Player player, String msg) {
        String finalmsg = Util.colour(msg);
        player.sendMessage(QCChat.getPhrase("kingdoms_prefix") + finalmsg);
    }

    public static void sendPhrase(Player player, String phrase) {
        String finalmsg = QCChat.getPhrase(phrase);
        player.sendMessage(QCChat.getPhrase("kingdoms_prefix") + finalmsg);
    }

    public static void broadcastMsg(String msg) {
        String finalmsg = Util.colour(msg);
        Bukkit.broadcastMessage(QCChat.getPhrase("kingdoms_prefix") + finalmsg);
    }
}
