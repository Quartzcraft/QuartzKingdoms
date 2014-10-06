package uk.co.quartzcraft.kingdoms.features;

import org.bukkit.entity.Player;
import uk.co.quartzcraft.core.systems.fancymessage.FancyMessage;

import static org.bukkit.ChatColor.*;
import static org.bukkit.ChatColor.RED;
import static org.bukkit.ChatColor.UNDERLINE;

public class FancyMessages {

    public static String declaredWar(Player player, String kingdomName) {
        return new FancyMessage("The kingdom ").color(GREEN)
                .then(kingdomName).color(WHITE).style(ITALIC)
                .then(" has declared ").color(GREEN)
                .then("war").color(DARK_RED)
                .then(" against your kingdom. ").color(GREEN)
                .then("You can ignore this with no consequences or you can ").color(GREEN)
                .then("declare war.").color(DARK_RED).style(UNDERLINE).suggest("/kingdom war " + kingdomName).tooltip("Declares war against the specified kingdom.")
                .toJSONString();
    }

    public static String proposedAlly(Player player, String kingdomName) {
        return new FancyMessage("The kingdom ").color(GREEN)
                .then(kingdomName).color(WHITE).style(ITALIC)
                .then(" has offered to become an ").color(GREEN)
                .then("ally").color(DARK_GREEN)
                .then(" with your kingdom. ").color(GREEN)
                .then("You can ignore this with no consequences or you can ").color(GREEN)
                .then("accept the offer.").color(DARK_GREEN).style(UNDERLINE).suggest("/kingdom ally " + kingdomName).tooltip("Allies with the specified kingdom.")
                .toJSONString();
    }
}
