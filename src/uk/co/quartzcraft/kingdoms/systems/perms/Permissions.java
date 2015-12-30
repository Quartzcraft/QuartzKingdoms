package uk.co.quartzcraft.kingdoms.systems.perms;

import uk.co.quartzcraft.core.data.QPlayer;
import uk.co.quartzcraft.core.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import uk.co.quartzcraft.core.QuartzCore;
import uk.co.quartzcraft.kingdoms.QuartzKingdoms;
import uk.co.quartzcraft.kingdoms.data.QKPlayer;
import uk.co.quartzcraft.kingdoms.util.KUtil;

import java.sql.ResultSet;
import java.util.*;

public class Permissions {
    public static HashMap<UUID, PermissionAttachment> permissions = new HashMap<>();

    public static void registerPlayerPerms(QKPlayer kplayer) {
        Player player = kplayer.getPlayer();

        Integer group = kplayer.getKingdomGroup().getId();

        if (permissions.containsKey(player.getUniqueId())) {
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
        if (group == 10) {
            attachmentPrimary.setPermission("QCK.knight", true);
        }

        //Nobles
        if (group == 15) {
            attachmentPrimary.setPermission("QCK.noble", true);
        }

        //Kings
        if (group == 20) {
            attachmentPrimary.setPermission("QCK.king", true);
        }

        permissions.put(player.getUniqueId(), attachmentPrimary);
    }

    public static void unregisterPlayerPerms(QKPlayer kplayer) {
        if (permissions.containsKey(kplayer.getQPlayer().getPlayer().getUniqueId())) {
            try {
                kplayer.getQPlayer().getPlayer().removeAttachment(permissions.get(kplayer.getQPlayer().getPlayer().getUniqueId()));
            } catch (IllegalArgumentException ex) {
                KUtil.printException("Failed to remove permission attachments", ex);
            }
            permissions.remove(kplayer.getQPlayer().getPlayer().getUniqueId());
        }
    }
}
