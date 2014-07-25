package uk.co.quartzcraft.kingdoms.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import org.bukkit.plugin.Plugin;
import uk.co.quartzcraft.core.QuartzCore;
import uk.co.quartzcraft.core.data.QPlayer;
import uk.co.quartzcraft.kingdoms.QuartzKingdoms;
import uk.co.quartzcraft.kingdoms.kingdom.Kingdom;
import uk.co.quartzcraft.kingdoms.clan.Clan;

public class QKPlayer {

    private static Plugin plugin;

    private static QPlayer qplayer;

    private static int id;
    private static UUID uuid;
    private static int power;
    private static int clanid;
    private static Clan clan;
    private static int kingdomid;
    private static Kingdom kingdom;
    private static Player player;

    public QKPlayer(Plugin plugin, UUID uuid) {
        this.plugin = plugin;
        this.uuid = uuid;

        String SUUID = uuid.toString();
        try {
            Statement s = QuartzKingdoms.MySQLking.openConnection().createStatement();
            ResultSet res = s.executeQuery("SELECT * FROM KingdomsPlayerData WHERE UUID='" + SUUID + "';");
            if(res.next()) {
                this.id = res.getInt("id");
                this.power = res.getInt("KingdomID");
                this.kingdomid = res.getInt("KingdomID");
                this.clanid = res.getInt("KingdomID");
            } else {
                return;
            }

        } catch(SQLException e) {
            e.printStackTrace();
            return;
        }

        if(this.kingdomid >= 1) {
            this.kingdom = new Kingdom(this.kingdomid);
        } else {
            this.kingdom = null;
        }
        if(this.clanid >= 1) {
            this.clan = new Clan(this.clanid);
        } else {
            this.clan = null;
        }

        this.qplayer = new QPlayer(QuartzCore.plugin, uuid);
        this.player = Bukkit.getPlayer(this.qplayer.getName());
    }

    /**
     *
     * @param plugin
     * @param id QuartzKingdoms id
     */
    public QKPlayer(Plugin plugin, int id) {
        this.plugin = plugin;
        this.id = id;

        try {
            Statement s = QuartzKingdoms.MySQLking.openConnection().createStatement();
            ResultSet res = s.executeQuery("SELECT * FROM KingdomsPlayerData WHERE id='" + id + "';");
            if(res.next()) {
                this.power = res.getInt("KingdomID");
                this.kingdomid = res.getInt("KingdomID");
                this.clanid = res.getInt("KingdomID");
            } else {
                return;
            }

        } catch(SQLException e) {
            e.printStackTrace();
            return;
        }

        if(this.kingdomid >= 1) {
            this.kingdom = new Kingdom(this.kingdomid);
        } else {
            this.kingdom = null;
        }
        if(this.clanid >= 1) {
            this.clan = new Clan(this.clanid);
        } else {
            this.clan = null;
        }

        this.qplayer = new QPlayer(QuartzCore.plugin, uuid);
        this.uuid = this.qplayer.getUniqueId();
        this.player = Bukkit.getPlayer(this.qplayer.getName());
    }

    public static boolean createKingdomsPlayer(Player player) {

        QPlayer lqplayer = new QPlayer(QuartzCore.plugin, player.getUniqueId());

        try {
            java.sql.Connection connection = QuartzKingdoms.MySQLking.openConnection();
            java.sql.PreparedStatement s = connection.prepareStatement("INSERT INTO KingdomsPlayerData (PlayerID) VALUES (" + lqplayer.getID() +");");
            if(s.executeUpdate() == 1) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Gets the users kingdoms database ID.
     *
     * @return The ID of the player.
     */
    public int getID() {
        return this.id;
    }

    /**
     * Retrieves the name of the kingdom that the specified player is in.
     *
     * @return The kingdom object
     */
    public Kingdom getKingdom() {
        return this.kingdom;
    }

    /**
     * Find out whether a player is a member of a kingdom
     *
     * @return true if member, false if not.
     */
    public boolean kingdomMember() {
        if(this.kingdom == null) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Finds out whether a player is a king, can check one kingdom.
     *
     * @return boolean true if king of specified kingdom, false if not.
     */
	public boolean isKing(Kingdom kingdom) {
        if(kingdom != null) {
            try {
                Statement s = QuartzKingdoms.MySQLking.openConnection().createStatement();
                ResultSet res2 = s.executeQuery("SELECT * FROM Kingdoms WHERE id=" + kingdom.getID() + ";");
                if(res2.next()) {
                    int kingID = res2.getInt("KingID");
                    if(this.id == kingID) {
                        return true;
                    }
                } else {
                    return false;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
	}

    /**
     * Sets the users kingodm to the specified kingdom.
     *
     * @param kingdom
     * @return
     */
    public boolean joinKingdom(Kingdom kingdom) {

        try {
            java.sql.Connection connection = QuartzKingdoms.MySQLking.openConnection();
            java.sql.PreparedStatement s = connection.prepareStatement("UPDATE KingdomsPlayerData SET KingdomID=" + kingdom.getID() + " WHERE id=" + this.id + ";");
            s.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Removes the player from the specified kingdom
     *
     * @param kingdom
     * @return
     */
	public boolean leaveKingdom(Kingdom kingdom) {
        if(this.getKingdom().getID() == kingdom.getID()) {
            try {
                java.sql.Connection connection = QuartzKingdoms.MySQLking.openConnection();
                java.sql.PreparedStatement s = connection.prepareStatement("UPDATE KingdomsPlayerData SET KingdomID=0 WHERE id=" + this.id + ";");
                if(s.executeUpdate() == 1) {
                    return true;
                } else {
                    return false;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
	}

    /**
     *
     * @return
     */
    public int getPower() {
        return this.power;
    }

    /**
     * Increases a players power by the specified amount.
     *
     * @param am
     * @return
     */
    public int addPower(int am) {
        int newa = this.power + am;

        try {
            java.sql.Connection connection = QuartzKingdoms.MySQLking.openConnection();
            java.sql.PreparedStatement s = connection.prepareStatement("UPDATE KingdomsPlayerData SET Power=" + newa + " WHERE id=" + this.id + ");");
            if(s.executeUpdate() == 1) {
                this.power = newa;
                return newa;
            } else {
                return this.power;
            }
        } catch (SQLException e) {
            return this.power;
        }
    }

    /**
     * Decreases a players power by the specified amount.
     *
     * @param am
     * @return
     */
    public int takePower(int am) {
        int newa = this.power - am;

        try {
            java.sql.Connection connection = QuartzKingdoms.MySQLking.openConnection();
            java.sql.PreparedStatement s = connection.prepareStatement("UPDATE KingdomsPlayerData SET Power=" + newa + " WHERE id=" + this.id + ");");
            if(s.executeUpdate() == 1) {
                this.power = newa;
                return newa;
            } else {
                return this.power;
            }
        } catch (SQLException e) {
            return this.power;
        }
    }

}
