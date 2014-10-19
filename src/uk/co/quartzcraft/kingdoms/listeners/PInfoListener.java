package uk.co.quartzcraft.kingdoms.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import uk.co.quartzcraft.core.event.PInfoExtraFieldsEvent;
import uk.co.quartzcraft.core.systems.chat.QCChat;
import uk.co.quartzcraft.kingdoms.QuartzKingdoms;
import uk.co.quartzcraft.kingdoms.data.QKPlayer;

public class PInfoListener implements Listener {

    public PInfoListener(QuartzKingdoms plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPInfoExtraFieldsEvent(PInfoExtraFieldsEvent event) {
        QKPlayer target = new QKPlayer(event.getTarget());

        if(target.getKingdom() != null) {
            event.addField(QCChat.getPhrase("player_kingdom_is_X") + target.getKingdom().getName());
        }
        event.addField(QCChat.getPhrase("player_level_is_X") + target.getPower());
    }
}
