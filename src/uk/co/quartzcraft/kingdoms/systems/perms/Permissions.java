package uk.co.quartzcraft.kingdoms.systems.perms;

import uk.co.quartzcraft.core.data.QPlayer;
import uk.co.quartzcraft.core.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import uk.co.quartzcraft.core.QuartzCore;
import uk.co.quartzcraft.kingdoms.QuartzKingdoms;
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

        Integer group = kplayer.getKingdomGroup();

        if (permissions.containsKey(player.getName())) {
            unregisterPlayerPerms(kplayer);
        }

        PermissionAttachment attachmentPrimary = player.addAttachment(QuartzKingdoms.plugin);

        attachmentPrimary.setPermission("QCK.everyone", true);

        // everyone (not including guests)
        if (group == 1) {
            attachmentPrimary.setPermission("QCK.normal", true);
        }

        //Members of a kingdom
        if (group == 2) {
            attachmentPrimary.setPermission("QCK.citizen", true);
        }

        //Upper class
        if (group == 3) {
            attachmentPrimary.setPermission("QCK.upper", true);
        }

        //Knights
        if (group == 4) {
            attachmentPrimary.setPermission("QCK.knight", true);
        }

        //Nobles
        if (group == 5) {
            attachmentPrimary.setPermission("QCK.noble", true);
        }

        //Kings
        if (group == 6) {
            attachmentPrimary.setPermission("QCK.king", true);
        }

        registerNamePrefixPerm(kplayer);


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

    public static void registerNamePrefixPerm(QKPlayer qplayer) {
        Player player = qplayer.getPlayer();

        PermissionAttachment attachmentColour = player.addAttachment(QuartzKingdoms.plugin);

        switch(qplayer.getKingdomGroup()) {
            default:
                attachmentColour.setPermission("QCC.nameprefix.null", true);
        }

        permissions.put(player.getName(), attachmentColour);
    }
}
