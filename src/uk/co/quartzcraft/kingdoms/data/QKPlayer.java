package uk.co.quartzcraft.kingdoms.data;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

import net.minecraft.util.io.netty.handler.codec.http.HttpContentEncoder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import org.bukkit.plugin.Plugin;
import uk.co.quartzcraft.core.data.QPlayer;
import uk.co.quartzcraft.kingdoms.systems.log.PowerLogger;
import uk.co.quartzcraft.kingdoms.systems.perms.Group;
import uk.co.quartzcraft.kingdoms.systems.perms.Permissions;
import uk.co.quartzcraft.kingdoms.util.KUtil;
import uk.co.quartzcraft.kingdoms.QuartzKingdoms;
import uk.co.quartzcraft.kingdoms.features.kingdom.Kingdom;
import uk.co.quartzcraft.kingdoms.features.clan.Clan;

public class QKPlayer {

    private Plugin plugin = QuartzKingdoms.plugin;

    private QPlayer qplayer;

    private int id;
    private UUID uuid;
    private int power;
    private int clanid;
    private Clan clan;
    private int kingdomid;
    private Kingdom kingdom;
    private Player player;
    private Group kingdomGroup;

    /**
     * Creates QKPlayer object using the specified id
     *
     * @param id
     */
    public QKPlayer(int id) {
        this.id = id;

        try {
            PreparedStatement s = QuartzKingdoms.DBKing.prepareStatement("SELECT * FROM KingdomsPlayerData WHERE id=?;");
            s.setInt(1, id);
            ResultSet res = s.executeQuery();
            if(res.next()) {
                this.qplayer = new QPlayer(res.getInt("PlayerID"));
                this.power = res.getInt("Power");
                this.kingdomid = res.getInt("KingdomID");
                this.clanid = res.getInt("ClanID");
                this.kingdomGroup = new Group(res.getInt("GroupID"));
            } else {
                return;
            }

        } catch(SQLException e) {
            KUtil.printException("Could not retrieve players data", e);
            return;
        }

        if(this.kingdomid != 0) {
            this.kingdom = new Kingdom(this.kingdomid);
        } else {
            this.kingdom = null;
        }
        if(this.clanid != 0) {
            //this.clan = new Clan(this.clanid);
            this.clan = null;
        } else {
            this.clan = null;
        }

        this.player = this.qplayer.getPlayer();
        this.uuid = this.qplayer.getUniqueId();

    }

    /**
     * Creates a QKPlayer object using the player id
     *
     * @param iplayer
     */
    public QKPlayer(Player iplayer) {
        QPlayer qPlayer = new QPlayer(iplayer);
        this.qplayer = qPlayer;
        try {
            PreparedStatement s = QuartzKingdoms.DBKing.prepareStatement("SELECT * FROM KingdomsPlayerData WHERE PlayerID=?;");
            s.setInt(1, qPlayer.getID());
            ResultSet res = s.executeQuery();
            if(res.next()) {
                this.id = res.getInt("id");
                this.power = res.getInt("Power");
                this.kingdomid = res.getInt("KingdomID");
                this.clanid = res.getInt("ClanID");
                this.kingdomGroup = new Group(res.getInt("GroupID"));
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
        this.player = iplayer;
    }

    /**
     * Creates a QKPlayer object using the specified QPlayer
     *
     * @param qPlayer
     */
    public QKPlayer(QPlayer qPlayer) {

        try {
            PreparedStatement s = QuartzKingdoms.DBKing.prepareStatement("SELECT * FROM KingdomsPlayerData WHERE PlayerID=?;");
            s.setInt(1, qPlayer.getID());
            ResultSet res = s.executeQuery();
            if(res.next()) {
                this.id = res.getInt("id");
                this.power = res.getInt("Power");
                this.kingdomid = res.getInt("KingdomID");
                this.clanid = res.getInt("ClanID");
                this.kingdomGroup = new Group(res.getInt("GroupID"));
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
     * Creates a new QKPlayer (not object)
     * 
     * @param player
     * @return
     */
    public static boolean createKingdomsPlayer(Player player) {
        QPlayer lqplayer = new QPlayer(player);

        try {
            PreparedStatement s = QuartzKingdoms.DBKing.prepareStatement("INSERT INTO KingdomsPlayerData (PlayerID) VALUES (?);");
            s.setInt(1, lqplayer.getID());
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
            PreparedStatement s = QuartzKingdoms.DBKing.prepareStatement("INSERT INTO KingdomsPlayerData (PlayerID) VALUES (?);");
            s.setInt(1, player.getID());
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
            PreparedStatement s = QuartzKingdoms.DBKing.prepareStatement("SELECT * FROM KingdomsPlayerData WHERE PlayerID=?;");
            s.setInt(1, qplayer.getID());
            ResultSet res = s.executeQuery();
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
     * Find out whether a QKPlayer object exists for the specified QPlayer
     *
     * @param player
     * @return
     */
    public static boolean exists(QPlayer player) {

        try {
            PreparedStatement s = QuartzKingdoms.DBKing.prepareStatement("SELECT * FROM KingdomsPlayerData WHERE PlayerID=?;");
            s.setInt(1, player.getID());
            ResultSet res = s.executeQuery();
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
     * Returns the Player object associated with this player.
     *
     * @return
     */
    public Player getPlayer() {
        return this.player;
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
    public Group getKingdomGroup() {
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
                java.sql.PreparedStatement s = QuartzKingdoms.DBKing.prepareStatement("SELECT * FROM Kingdoms WHERE id=?;");
                s.setInt(1, kingdom.getID());
                ResultSet res = s.executeQuery();
                if(res.next()) {
                    int kingID = res.getInt("KingID");
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
     * Finds out whether a player is the king of their kingdom
     *
     * @return boolean true if king, false if not.
     */
    public boolean isKing() {
        if(this.kingdom.getKing().getID() == this.id) {
            return true;
        } else {
            return false;
        }
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
                PowerLogger.logAdd(this, newa);
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
                PowerLogger.logTake(this, newa);
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
        if(rank > 6 | rank == 0 | rank == this.kingdomGroup.getId()) {
            return this;
        }

        try {
            java.sql.PreparedStatement s = QuartzKingdoms.DBKing.prepareStatement("UPDATE KingdomsPlayerData SET GroupID=? WHERE id=?;");
            s.setInt(1, rank);
            s.setInt(2, this.id);
            if(s.executeUpdate() == 1) {
                this.kingdomGroup = new Group(rank);
                if(this.player != null) {
                    Permissions.registerPlayerPerms(this);
                }
                return this;
            } else {
                return this;
            }
        } catch (SQLException e) {
            KUtil.printException("Could not update players group", e);
            return this;
        }
    }

    public void sendMessage(String msg) {
        if(this.qplayer.getPlayer() != null) {
            this.qplayer.sendMessage(msg);
        }
    }

}
