package uk.co.quartzcraft.kingdoms.systems.perms;

import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import uk.co.quartzcraft.kingdoms.QuartzKingdoms;
import uk.co.quartzcraft.kingdoms.util.KUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

public class Group {

    private Plugin plugin = QuartzKingdoms.plugin;

    private int id;
    private String name;
    private String fullName;
    private String prefix;
    private ChatColor colour = null;

    public Group(int id) {
        this.id = id;

        try {
            PreparedStatement s = QuartzKingdoms.DBKing.prepareStatement("SELECT * FROM Groups WHERE id=?;");
            s.setInt(1, id);
            ResultSet res = s.executeQuery();
            if(res.next()) {
                if(res.getInt("id") == id) {
                    this.name = res.getString("group_name");
                    this.fullName = res.getString("full_group_name");
                    this.prefix = res.getString("group_prefix");
                    this.colour = ChatColor.getByChar(res.getString("group_colour"));
                } else {
                    KUtil.log(Level.SEVERE, "Group id not equal");
                }
            }

        } catch(SQLException e) {
            KUtil.printException("Failed to retrieve Group from database", e);
        }
    }

    public Group(String groupName) {
        this.name = groupName;

        try {
            PreparedStatement s = QuartzKingdoms.DBKing.prepareStatement("SELECT * FROM Groups WHERE group_name=?;");
            s.setString(1, groupName);
            ResultSet res = s.executeQuery();
            if(res.next()) {
                if(res.getString("group_name").equalsIgnoreCase(groupName)) {
                    this.id = res.getInt("id");
                    this.fullName = res.getString("full_group_name");
                    this.prefix = res.getString("group_prefix");
                    this.colour = ChatColor.getByChar(res.getString("group_colour"));
                } else {
                    KUtil.log(Level.SEVERE, "Group names not equal");
                }
            }

        } catch(SQLException e) {
            KUtil.printException("Failed to retrieve Group from database", e);
        }
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getFullName() {
        return this.fullName;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public ChatColor getColour() {
        return this.colour;
    }

    public String getFancyName() {
        if(this.prefix == null) {
            if(this.colour == null) {
                return "[" + this.prefix + "]" + this.fullName + ChatColor.RESET;
            }
            return this.colour + this.fullName + ChatColor.RESET;
        }
        if(this.colour == null) {
            return "[" + this.prefix + "]" + this.fullName + ChatColor.RESET;
        }
        return this.colour + "[" + this.prefix + "]" + this.fullName + ChatColor.RESET;
    }

    public String getStyleForName() {
        if(this.colour == null) {
            return "[" + this.prefix + "]";
        }
        if(this.prefix == null) {
            return "";
        }
        return this.colour + "[" + this.prefix + "]" + ChatColor.RESET;
    }
}

