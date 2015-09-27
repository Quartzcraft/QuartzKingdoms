package uk.co.quartzcraft.kingdoms.features;

import org.bukkit.ChatColor;
import uk.co.quartzcraft.core.systems.fancymessage.FancyMessage;
import uk.co.quartzcraft.core.systems.notifications.AlertArgs;
import uk.co.quartzcraft.core.systems.notifications.AlertType;
import uk.co.quartzcraft.kingdoms.features.kingdom.Kingdom;

import static org.bukkit.ChatColor.*;

public class KingdomsAlertTypes {

    public KingdomsAlertTypes() {

    }

    @AlertType(name = "kingdom_invite", prefix = "[Kingdoms]", permission = "QCK.everyone")
    public String kingdomInvite(AlertArgs args) {
        int kid = (int) args.getArg("kingdom_id");
        Kingdom kingdom = new Kingdom(kid);
        return new FancyMessage("You have been invited to join the kingdom ").color(GREEN)
                .then(kingdom.getName() + ".").color(GOLD)
                .then(" ")
                .then("Click here to accept the invitation!").color(BLUE).style(UNDERLINE).command("/k accept " + kingdom.getName()).tooltip("/k accept " + kingdom.getName())
                .toJSONString();
    }

    @AlertType(name = "kingdom_war", prefix = "[Kingdoms]", permission = "QCK.king")
    public String kingdomWar(AlertArgs args) {
        int kid = (int) args.getArg("kingdom_id");
        Kingdom kingdom = new Kingdom(kid);
        return new FancyMessage("The kingdom ").color(RED)
                .then(kingdom.getName()).color(GOLD).style(ITALIC)
                .then(" has declared war").color(DARK_RED)
                .then(" against your kingdom. ").color(GREEN)
                .then("You can ignore this with no consequences or you can ").color(GREEN)
                .then("declare war.").color(DARK_RED).style(UNDERLINE).suggest("/kingdom war " + kingdom.getName()).tooltip("Click to declare war against the specified kingdom.")
                .toJSONString();
    }
}
