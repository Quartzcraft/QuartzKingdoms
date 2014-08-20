package uk.co.quartzcraft.kingdoms.systems.perms;

import uk.co.quartzcraft.core.data.QPlayer;
import uk.co.quartzcraft.core.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import uk.co.quartzcraft.core.QuartzCore;
import uk.co.quartzcraft.kingdoms.data.QKPlayer;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Permissions {
    public static HashMap<String, PermissionAttachment> permissions = new HashMap<>();

    public static void registerPlayerPerms(QKPlayer kplayer) {
        Player player = kplayer.getQPlayer().getPlayer();

        Integer kingdomGroup = kplayer.getKingdomGroup();

        if (permissions.containsKey(player.getName())) {
            unregisterPlayerPerms(kplayer);
        }
        PermissionAttachment attachmentPrimary = player.addAttachment(QuartzCore.plugin);

        attachmentPrimary.setPermission("QCK.everyone", true);

        //
        if (kingdomGroup > 0) {
            attachmentPrimary.setPermission("QCK.normal", true);
        }

        //citizens
        if (kingdomGroup > 1) {
            attachmentPrimary.setPermission("empire.supporter.iron", true);
        }

        //upper class
        if (kingdomGroup > 2) {
            attachmentPrimary.setPermission("empire.supporter.gold", true);
        }

        //noble
        if (kingdomGroup > 3) {
            attachmentPrimary.setPermission("empire.supporter.diamond", true);
        }

        //king
        if (kingdomGroup > 5) {
            attachmentPrimary.setPermission("empire.staff", true);
        }

        permissions.put(player.getName(), attachmentPrimary);
    }

    public static void unregisterPlayerPerms(QKPlayer kplayer) {
        if (permissions.containsKey(kplayer.getQPlayer().getPlayer().getName())) {
            try {
                kplayer.getQPlayer().getPlayer().removeAttachment(permissions.get(kplayer.getQPlayer().getPlayer().getName()));
            } catch (IllegalArgumentException ex) {
            }
            permissions.remove(kplayer.getQPlayer().getPlayer().getName());
        }
    }
}
