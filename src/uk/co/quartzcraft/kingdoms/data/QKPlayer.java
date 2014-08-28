package uk.co.quartzcraft.kingdoms.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import org.bukkit.plugin.Plugin;
import uk.co.quartzcraft.core.data.QPlayer;
import uk.co.quartzcraft.kingdoms.util.KUtil;
import uk.co.quartzcraft.kingdoms.QuartzKingdoms;
import uk.co.quartzcraft.kingdoms.features.kingdom.Kingdom;
import uk.co.quartzcraft.kingdoms.features.clan.Clan;

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
    private static int kingdomGroup;

    public QKPlayer(int id) {
        this.plugin = QuartzKingdoms.plugin;
        this.id = id;

        try {
            Statement s = QuartzKingdoms.MySQLking.openConnection().createStatement();
            ResultSet res = s.executeQuery("SELECT * FROM KingdomsPlayerData WHERE id=" + id + ";");
            if(res.next()) {
                this.qplayer = new QPlayer(res.getInt("PlayerID"));
                this.power = res.getInt("Power");
                this.kingdomid = res.getInt("KingdomID");
                this.clanid = res.getInt("ClanID");
                this.kingdomGroup = res.getInt("GroupID");
            } else {
                return;
            }

        } catch(SQLException e) {
            KUtil.printException("Could not retrieve players data", e);
            return;
        }

        if(this.kingdomid >= 1) {
            this.kingdom = new Kingdom(this.kingdomid);
        } else {
            this.kingdom = null;
        }
        if(this.clanid >= 1) {
            //this.clan = new Clan(this.clanid);
            this.clan = null;
        } else {
            this.clan = null;
        }

        this.player = this.qplayer.getPlayer();
        this.uuid = this.qplayer.getUniqueId();

    }

    public QKPlayer(Player iplayer) {
        this.plugin = QuartzKingdoms.plugin;
        this.uuid = iplayer.getUniqueId();
        this.player = iplayer;

        String SUUID = uuid.toString();
        try {
            Statement s = QuartzKingdoms.MySQLking.openConnection().createStatement();
            ResultSet res = s.executeQuery("SELECT * FROM KingdomsPlayerData WHERE UUID='" + SUUID + "';");
            if(res.next()) {
                this.id = res.getInt("id");
                this.power = res.getInt("Power");
                this.kingdomid = res.getInt("KingdomID");
                this.clanid = res.getInt("ClanID");
                this.kingdomGroup = res.getInt("GroupID");
            } else {
                return;
            }

        } catch(SQLException e) {
            KUtil.printException("Could not retrieve players data", e);
            return;
        }

        if(this.kingdomid >= 1) {
            this.kingdom = new Kingdom(this.kingdomid);
        } else {
            this.kingdom = null;
        }
        if(this.clanid >= 1) {
            //this.clan = new Clan(this.clanid);
            this.clan = null;
        } else {
            this.clan = null;
        }

        this.qplayer = new QPlayer(iplayer);
    }

    public QKPlayer(QPlayer qPlayer) {
        this.plugin = QuartzKingdoms.plugin;

        try {
            Statement s = QuartzKingdoms.MySQLking.openConnection().createStatement();
            ResultSet res = s.executeQuery("SELECT * FROM KingdomsPlayerData WHERE id=" + qplayer.getID() + ";");
            if(res.next()) {
                this.id = res.getInt("id");
                this.power = res.getInt("Power");
                this.kingdomid = res.getInt("KingdomID");
                this.clanid = res.getInt("ClanID");
                this.kingdomGroup = res.getInt("GroupID");
            } else {
                return;
            }

        } catch(SQLException e) {
            KUtil.printException("Could not retrieve players data", e);
            return;
        }

        if(this.kingdomid >= 1) {
            this.kingdom = new Kingdom(this.kingdomid);
        } else {
            this.kingdom = null;
        }
        if(this.clanid >= 1) {
            //this.clan = new Clan(this.clanid);
            this.clan = null;
        } else {
            this.clan = null;
        }

        this.qplayer = qPlayer;
        this.uuid = qPlayer.getUniqueId();
        this.player = qPlayer.getPlayer();
    }

    /**
     * Creates a new QKPlayer (not object
     * )
     * @param player
     * @return
     */
    public static boolean createKingdomsPlayer(Player player) {

        QPlayer lqplayer = new QPlayer(player);

        try {
            java.sql.Connection connection = QuartzKingdoms.MySQLking.openConnection();
            java.sql.PreparedStatement s = connection.prepareStatement("INSERT INTO KingdomsPlayerData (PlayerID) VALUES (" + lqplayer.getID() +");");
            if(s.executeUpdate() == 1) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            KUtil.printException("Could not create kingdoms player", e);
            return false;
        }
    }

    /**
     * Creates a new QKPlayer (not object
     *
     * @param player
     * @return
     */
    public static boolean createKingdomsPlayer(QPlayer player) {

        try {
            java.sql.Connection connection = QuartzKingdoms.MySQLking.openConnection();
            java.sql.PreparedStatement s = connection.prepareStatement("INSERT INTO KingdomsPlayerData (PlayerID) VALUES (" + player.getID() +");");
            if(s.executeUpdate() == 1) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            KUtil.printException("Could not create kingdoms player", e);
            return false;
        }
    }

    /**
     * Find out whether a QKPlayer object exists for the specified player
     *
     * @param player
     * @return
     */
    public static boolean exists(Player player) {
        QPlayer qplayer = new QPlayer(player);

        try {
            Statement s = QuartzKingdoms.MySQLking.openConnection().createStatement();
            ResultSet res = s.executeQuery("SELECT * FROM KingdomsPlayerData WHERE PlayerID='" + qplayer.getID() + "';");
            if(res.next()) {
                return true;
            } else {
                return false;
            }

        } catch(SQLException e) {
            KUtil.printException("Could not retrieve player data", e);
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
     * Returns the QPlayer object associated with this player.
     *
     * @return
     */
    public QPlayer getQPlayer() {
        return this.qplayer;
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
     * Returns the amount of power a player has.
     * @return
     */
    public int getPower() {
        return this.power;
    }

    /**
     * Returns the players kingdoms group.
     *
     * @return
     */
    public int getKingdomGroup() {
        return this.kingdomGroup;
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
                KUtil.printException("Could not retrieve player data", e);
                return false;
            }
        }
        return false;
	}

    /**
     * Increases a players power by the specified amount.
     *
     * @param am
     * @return
     */
    public QKPlayer addPower(int am) {
        int newa = this.power + am;

        try {
            java.sql.PreparedStatement s = QuartzKingdoms.DBKing.prepareStatement("UPDATE KingdomsPlayerData SET Power=? WHERE id=?);");
            s.setInt(1, newa);
            s.setInt(2, this.id);
            if(s.executeUpdate() == 1) {
                this.power = newa;
                return this;
            } else {
                return this;
            }
        } catch (SQLException e) {
            KUtil.printException("Could not add players power", e);
            return this;
        }
    }

    /**
     * Decreases a players power by the specified amount.
     *
     * @param am
     * @return
     */
    public QKPlayer takePower(int am) {
        int newa = this.power - am;

        try {
            java.sql.PreparedStatement s = QuartzKingdoms.DBKing.prepareStatement("UPDATE KingdomsPlayerData SET Power=? WHERE id=?);");
            s.setInt(1, newa);
            s.setInt(2, this.id);
            if(s.executeUpdate() == 1) {
                this.power = newa;
                return this;
            } else {
                return this;
            }
        } catch (SQLException e) {
            KUtil.printException("Could not take players power", e);
            return this;
        }
    }

    /**
     * Sets a players kingdom to the specified kingdom.
     *
     * @param kingdom
     * @return
     */
    public QKPlayer setKingdom(Kingdom kingdom) {
        if(kingdom != null) {
            try {
                java.sql.PreparedStatement s = QuartzKingdoms.DBKing.prepareStatement("UPDATE KingdomsPlayerData SET KingdomID=? WHERE id=?;");
                s.setInt(1, kingdom.getID());
                s.setInt(2, this.id);
                if(s.executeUpdate() == 1) {
                    this.kingdom = kingdom;
                    return this;
                } else {
                    return this;
                }
            } catch (SQLException e) {
                KUtil.printException("Could not update players kingdom", e);
                return this;
            }
        } else {
            try {
                java.sql.PreparedStatement s = QuartzKingdoms.DBKing.prepareStatement("UPDATE KingdomsPlayerData SET KingdomID=0 WHERE id=?;");
                s.setInt(1, this.id);
                if(s.executeUpdate() == 1) {
                    this.kingdom = null;
                    return this;
                } else {
                    return this;
                }
            } catch (SQLException e) {
                KUtil.printException("Could not update players kingdom", e);
                return this;
            }
        }
    }

    public QKPlayer setKingdomGroup(int rank) {
        //1 - no kingdom
        //2 - citizen
        //3 - upper class
        //4 - knight
        //5 - noble
        //6 - king
        if(rank >= 6 | rank == 0) {
            return this;
        }

        try {
            java.sql.PreparedStatement s = QuartzKingdoms.DBKing.prepareStatement("UPDATE KingdomsPlayerData SET GroupID=? WHERE id=?;");
            s.setInt(1, this.id);
            s.setInt(2, rank);
            if(s.executeUpdate() == 1) {
                this.kingdomGroup = rank;
                return this;
            } else {
                return this;
            }
        } catch (SQLException e) {
            KUtil.printException("Could not update players group", e);
            return this;
        }
    }

}
