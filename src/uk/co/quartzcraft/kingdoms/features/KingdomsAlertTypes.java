package uk.co.quartzcraft.kingdoms.features;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.ChatColor;
import uk.co.quartzcraft.core.systems.notifications.AlertArgs;
import uk.co.quartzcraft.core.systems.notifications.AlertType;
import uk.co.quartzcraft.kingdoms.features.kingdom.Kingdom;

public class KingdomsAlertTypes {

    public KingdomsAlertTypes() {

    }

    @AlertType(name = "kingdom_invite", prefix = "[Kingdoms]", requireArgs = true)
    public TextComponent kingdomInvite(AlertArgs args) {
        int kid = (int) args.getArg("kingdom_id");
        Kingdom kingdom = new Kingdom(kid);

        TextComponent component = new TextComponent("You have ben invited to join the kingdom ");
            component.setColor(ChatColor.GREEN);
            component.addExtra(kingdom.getName());
            component.setColor(ChatColor.GOLD);
            component.addExtra(". ");
            component.setColor(ChatColor.GREEN);
            component.addExtra("Click here to accept the invitation!");
            component.setColor(ChatColor.BLUE);
            component.setUnderlined(true);
            component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/k accept " + kingdom.getName()));

        return component;
    }

    @AlertType(name = "kingdom_war", prefix = "[Kingdoms]", requireArgs = true)
    public TextComponent kingdomWar(AlertArgs args) {
        int kid = (int) args.getArg("kingdom_id");
        Kingdom kingdom = new Kingdom(kid);

        TextComponent component = new TextComponent("The kingdom ");
            component.setColor(ChatColor.RED);
            component.addExtra(kingdom.getName());
            component.setColor(ChatColor.GOLD);
            component.addExtra(" has declared war against your kingdom! ");
            component.setColor(ChatColor.RED);
            component.addExtra("You can ignore this with no consequences or you can ");
            component.setColor(ChatColor.RED);
            component.addExtra(" click to declare war!");
            component.setColor(ChatColor.DARK_RED);
            component.setUnderlined(true);
            component.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/kingdom war " + kingdom.getName()));

        return component;
    }

    @AlertType(name = "kingdom_ally", prefix = "[Kingdoms]", requireArgs = true)
    public TextComponent kingdomAlly(AlertArgs args) {
        int kid = (int) args.getArg("kingdom_id");
        Kingdom kingdom = new Kingdom(kid);

        TextComponent component = new TextComponent("The kingdom ");
            component.setColor(ChatColor.GREEN);
            component.addExtra(kingdom.getName());
            component.setColor(ChatColor.GOLD);
            component.addExtra(" has offered to ");
            component.setColor(ChatColor.GREEN);
            component.addExtra("ally ");
            component.setColor(ChatColor.DARK_GREEN);
            component.addExtra("with your kingdom. ");
            component.setColor(ChatColor.GREEN);
            component.addExtra("You can ignore this with no consequences or you can ");
            component.setColor(ChatColor.GREEN);
            component.addExtra(" click to accept the offer");
            component.setColor(ChatColor.DARK_GREEN);
            component.setUnderlined(true);
            component.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/kingdom ally " + kingdom.getName()));

        return component;
    }
}
