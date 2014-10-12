package uk.co.quartzcraft.kingdoms.features.kingdom;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.plugin.Plugin;

import uk.co.quartzcraft.kingdoms.QuartzKingdoms;
import uk.co.quartzcraft.kingdoms.data.QKPlayer;
import uk.co.quartzcraft.kingdoms.util.KUtil;

public class Kingdom {
	
	private Plugin plugin = QuartzKingdoms.plugin;

    private int id;
    private String name;
    private QKPlayer king;
    private int power;
    private int level;
    private boolean open;

    /**
     * Creates a kingdom object using the specified id
     *
     * @param id
     */
    public Kingdom(int id) {
        this.id = id;

        try {
            PreparedStatement s = QuartzKingdoms.DBKing.prepareStatement("SELECT * FROM Kingdoms WHERE id=?;");
            s.setInt(1, id);
            ResultSet res = s.executeQuery();
            if(res.next()) {
                this.id = res.getInt("id");
                this.power = res.getInt("Power");
                this.open = res.getBoolean("invite_only");
                this.king = new QKPlayer(res.getInt("KingID"));
                this.name = res.getString("KingdomName");
                this.level = res.getInt("Level");
            } else {
                return;
            }

        } catch(SQLException e) {
            KUtil.printException("Could not retrieve Kingdoms data", e);
            return;
        }
    }

    /**
     * Creates a kingdom object using the specified name
     *
     * @param name
     */
    public Kingdom(String name) {
        this.name = name;

        try {
            PreparedStatement s = QuartzKingdoms.DBKing.prepareStatement("SELECT * FROM Kingdoms WHERE KingdomName=?;");
            s.setString(1, name);
            ResultSet res = s.executeQuery();
            if(res.next()) {
                this.id = res.getInt("id");
                this.power = res.getInt("Power");
                this.open = res.getBoolean("invite_only");
                this.king = new QKPlayer(res.getInt("KingID"));
                this.level = res.getInt("Level");
            } else {
                return;
            }

        } catch(SQLException e) {
            KUtil.printException("Could not retrieve Kingdoms data", e);
            return;
        }
    }

    /**
     * Creates a kingdom using the specified name and setting the specified player as king.
     *
     * @param kingdomName
     * @param player
     * @return
     */
	public static Kingdom createKingdom(String kingdomName, QKPlayer player) {
		if(exists(kingdomName)) {
			return null;
		}
		
		if(player.getID() == 0 | player.getQPlayer().getID() == 0) {
			return null;
		} 
		
		try {
			java.sql.PreparedStatement s = QuartzKingdoms.DBKing.prepareStatement("INSERT INTO Kingdoms (KingdomName, KingID) VALUES (?, ?);");
            s.setString(1, kingdomName);
            s.setInt(2, player.getID());
            if(s.executeUpdate() == 1) {
                Kingdom kkingdom = new Kingdom(kingdomName);
                player.setKingdom(kkingdom);
                player.setKingdomGroup(6);
                return kkingdom;
            } else {
                return null;
            }
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

    /**
     * Determines whether a kingdom with the specified name exists.
     *
     * @param kingdomName
     * @return
     */
    public static boolean exists(String kingdomName) {
        try {
            java.sql.PreparedStatement s = QuartzKingdoms.DBKing.prepareStatement("SELECT * FROM Kingdoms WHERE KingdomName=?;");
            s.setString(1, kingdomName);
            ResultSet res2 = s.executeQuery();
            if(res2.next()) {
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
     * Deletes the kingdom
     *
     * @param player
     * @return
     */
	public boolean delete(QKPlayer player) {
        //TODO All players must leave kingdom
		if(!player.isKing(this)) {
			return false;
		}
		
		try {
			java.sql.PreparedStatement s = QuartzKingdoms.DBKing.prepareStatement("DELETE FROM Kingdoms WHERE id=? AND KingID=?;");
            s.setInt(1, this.id);
            s.setInt(2, player.getID());
            player.setKingdom(null);
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
     * Returns a kingdoms id.
     *
     * @return
     */
    public int getID() {
        return this.id;
    }

    /**
     * Returns a kingdom name.
     *
     * @return
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns a kingdoms king.
     *
     * @return
     */
    public QKPlayer getKing() {
        return this.king;
    }

    /**
     * Find out whether the kingdom is open for new members or not.
     *
     * @return True if open for new players, false if closed to new players.
     */
    public boolean isOpen() {
        return this.open;
    }

    /**
     * Returns the kingdoms level.
     *
     * @return
     */
    public int getLevel() {
        return this.level;
    }

    /**
     * Returns the amount of power a kingdom has.
     *
     * @return
     */
    public int getPower() {
        return this.power;
    }

    /**
     * Increases a kingdoms power by the specified amount.
     *
     * @param am
     * @return
     */
    public Kingdom addPower(int am) {
        int newa = this.power + am;

        try {
            java.sql.PreparedStatement s = QuartzKingdoms.DBKing.prepareStatement("UPDATE KingdomsPlayerData SET Power=? WHERE id=?);");
            s.setInt(1, newa);
            s.setInt(2, this.id);
            if(s.executeUpdate() == 1) {
                this.power = newa;
                this.setLevel(newa);
                return this;
            } else {
                return this;
            }
        } catch (SQLException e) {
            return this;
        }
    }

    /**
     * Decreases a kingdoms power by the specified amount.
     *
     * @param am
     * @return
     */
    public Kingdom takePower(int am) {
        int newa = this.power - am;

        try {
            java.sql.PreparedStatement s = QuartzKingdoms.DBKing.prepareStatement("UPDATE KingdomsPlayerData SET Power=? WHERE id=?);");
            s.setInt(1, newa);
            s.setInt(2, this.id);
            if(s.executeUpdate() == 1) {
                this.power = newa;
                this.setLevel(newa);
                return this;
            } else {
                return this;
            }
        } catch (SQLException e) {
            return this;
        }
    }

    public int setLevel(int power) {
        double rlevel = (double)power/10;
        int level = (int) rlevel;
        try {
            java.sql.PreparedStatement s = QuartzKingdoms.DBKing.prepareStatement("UPDATE Kingdoms SET level=? WHERE id=?;");
            s.setInt(1, level);
            s.setInt(1, this.id);
            if(s.executeUpdate() == 1) {
                this.level = level;
                return level;
            } else {
                return 0;
            }
        } catch (SQLException e) {
            return 0;
        }
    }

    /**
     * Set the kingdom open or closed to new members.
     *
     * @param status
     * @return
     */
    public Kingdom setOpen(boolean status) {
        //TODO
        if(status && !this.open) {
            try {
                java.sql.PreparedStatement s = QuartzKingdoms.DBKing.prepareStatement("UPDATE Kingdoms SET invite_only=0 WHERE id=?;");
                s.setInt(1, this.id);
                if(s.executeUpdate() == 1) {
                    this.open = true;
                    return this;
                } else {
                    return this;
                }
            } catch (SQLException e) {
                return this;
            }
        } else if(!status && this.open) {
            try {
                java.sql.PreparedStatement s = QuartzKingdoms.DBKing.prepareStatement("UPDATE Kingdoms SET invite_only=1 WHERE id=?;");
                s.setInt(1, this.id);
                if(s.executeUpdate() == 1) {
                    this.open = false;
                    return this;
                } else {
                    return this;
                }
            } catch (SQLException e) {
                return this;
            }
        } else {
            return this;
        }
    }

    /**
     * Sets the king to the specified player.
     *
     * @param player
     * @return
     */
    public Kingdom setKing(QKPlayer player) {
        try {
            java.sql.PreparedStatement s = QuartzKingdoms.DBKing.prepareStatement("UPDATE Kingdoms SET kingID=? WHERE id=?;");
            s.setInt(1, player.getID());
            s.setInt(2, this.id);
            if(s.executeUpdate() == 1) {
                this.king = player;
                return this;
            } else {
                return this;
            }
        } catch (SQLException e) {
            return this;
        }
    }

    /**
     * Makes the specified player a noble for this kingdom.
     *
     * @param player
     * @return
     */
    public boolean makeNoble(QKPlayer player) {
        if(player.getKingdom() != this) {
            return false;
        }

        return true;
    }

    /**
     * Makes the specified player a knight for this kingdom.
     *
     * @param player
     * @return
     */
    public boolean makeKnight(QKPlayer player) {
        if(player.getKingdom() != this) {
            return false;
        }

        return true;
    }

    /**
     * Invites the specified player to the kingdom.
     *
     * @param player
     */
    public void invitePlayer(QKPlayer player) {
        
    }

    /**
     * Sets the relationships between this kingdom and the specified kingdom to neutral.
     *
     * @param relatingKingdom
     */
    public void setAtNeutral(Kingdom relatingKingdom) {
        try {
            java.sql.PreparedStatement s = QuartzKingdoms.DBKing.prepareStatement("SELECT FROM relationships WHERE kingdom_id=? AND sec_kingdom_id=?;");
            s.setInt(1, this.id);
            s.setInt(2, relatingKingdom.getID());
            ResultSet res = s.executeQuery();
            if(res.next()) {
                java.sql.PreparedStatement s1 = QuartzKingdoms.DBKing.prepareStatement("DELETE FROM relationships WHERE kingdom_id=? AND sec_kingdom_id=?;");
                s.setInt(1, this.id);
                s.setInt(2, relatingKingdom.getID());
                s1.executeUpdate();
            } else {
                return;
            }
        }  catch (SQLException e) {
            KUtil.printException("Could not retrieve relationships", e);
        }
    }

    /**
     * Sets the relationships between this kingdom and the specified kingdom to war.
     *
     * @param relatingKingdom
     */
    public int setAtWar(Kingdom relatingKingdom) {
        try {
            java.sql.PreparedStatement s = QuartzKingdoms.DBKing.prepareStatement("SELECT FROM relationships WHERE kingdom_id=? AND sec_kingdom_id=?;");
            s.setInt(1, this.id);
            s.setInt(2, relatingKingdom.getID());
            ResultSet res = s.executeQuery();
            if(isEnemy(relatingKingdom)) {
                return 3;
            } else if(res.next()) {
                if(res.getString("status") == "22") {
                    java.sql.PreparedStatement s1 = QuartzKingdoms.DBKing.prepareStatement("UPDATE relationships SET status=3 WHERE kingdom_id=? AND sec_kingdom_id=?;");
                    s1.setInt(1, this.id);
                    s1.setInt(2, relatingKingdom.getID());
                    s1.executeUpdate();
                    return 3;
                } else {
                    java.sql.PreparedStatement s2 = QuartzKingdoms.DBKing.prepareStatement("UPDATE relationships SET status=33 WHERE kingdom_id=? AND sec_kingdom_id=?;");
                    s2.setInt(1, this.id);
                    s2.setInt(2, relatingKingdom.getID());
                    s2.executeUpdate();
                    return 33;
                }
            } else {
                java.sql.PreparedStatement s3 = QuartzKingdoms.DBKing.prepareStatement("INSERT INTO relationships (kingdom_id, sec_kingdom_id, status) VALUES (?, ?, 33);");
                s3.setInt(1, this.id);
                s3.setInt(2, relatingKingdom.getID());
                s3.executeUpdate();
                return 33;
            }
        }  catch (SQLException e) {
            KUtil.printException("Could not modify relationships", e);
            return 0;
        }
    }

    /**
     * Sets the relationships between this kingdom and the specified kingdom to ally.
     *
     * @param relatingKingdom
     */
    public int setAtAlly(Kingdom relatingKingdom) {
        try {
            java.sql.PreparedStatement s = QuartzKingdoms.DBKing.prepareStatement("SELECT FROM relationships WHERE kingdom_id=? AND sec_kingdom_id=?;");
            s.setInt(1, this.id);
            s.setInt(2, relatingKingdom.getID());
            ResultSet res = s.executeQuery();
            if(isAlly(relatingKingdom)) {
                return 2;
            } else if(res.next()) {
                if(res.getString("status") == "22") {
                    java.sql.PreparedStatement s1 = QuartzKingdoms.DBKing.prepareStatement("UPDATE relationships SET status=2 WHERE kingdom_id=? AND sec_kingdom_id=?;");
                    s1.setInt(1, this.id);
                    s1.setInt(2, relatingKingdom.getID());
                    s1.executeUpdate();
                    return 2;
                } else {
                    java.sql.PreparedStatement s2 = QuartzKingdoms.DBKing.prepareStatement("UPDATE relationships SET status=22 WHERE kingdom_id=? AND sec_kingdom_id=?;");
                    s2.setInt(1, this.id);
                    s2.setInt(2, relatingKingdom.getID());
                    s2.executeUpdate();
                    return 22;
                }
            } else {
                java.sql.PreparedStatement s3 = QuartzKingdoms.DBKing.prepareStatement("INSERT INTO relationships (kingdom_id, sec_kingdom_id, status) VALUES (?, ?, 22);");
                s3.setInt(1, this.id);
                s3.setInt(2, relatingKingdom.getID());
                s3.executeUpdate();
                return 22;
            }
        }  catch (SQLException e) {
            KUtil.printException("Could not modify relationships", e);
            return 0;
        }
    }

    public boolean isNeutral(Kingdom relatingKingdom) {
        if(!isEnemy(relatingKingdom) && !isAlly(relatingKingdom)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isEnemy(Kingdom relatingKingdom) {
        try {
            java.sql.PreparedStatement s = QuartzKingdoms.DBKing.prepareStatement("SELECT * FROM relationships WHERE kingdom_id=? AND sec_kingdom_id=? AND status=3;");
            s.setInt(1, this.id);
            s.setInt(2, relatingKingdom.getID());
            ResultSet res = s.executeQuery();
            if(res.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean isAlly(Kingdom relatingKingdom) {
        try {
            java.sql.PreparedStatement s = QuartzKingdoms.DBKing.prepareStatement("SELECT * FROM relationships WHERE kingdom_id=? AND sec_kingdom_id=? AND status=2;");
            s.setInt(1, this.id);
            s.setInt(2, relatingKingdom.getID());
            ResultSet res = s.executeQuery();
            if (res.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            return false;
        }
    }

    public ResultSet getProposedEnemy() {
        try {
            java.sql.PreparedStatement s = QuartzKingdoms.DBKing.prepareStatement("SELECT * FROM relationships WHERE sec_kingdom_id=? AND status=33;");
            s.setInt(1, this.id);
            ResultSet res = s.executeQuery();
            if(res.next()) {
                return res;
            } else {
                return null;
            }
        } catch (SQLException e) {
            return null;
        }
    }

    public ResultSet getProposedAlly() {
        try {
            java.sql.PreparedStatement s = QuartzKingdoms.DBKing.prepareStatement("SELECT * FROM relationships WHERE sec_kingdom_id=? AND status=22;");
            s.setInt(1, this.id);
            ResultSet res = s.executeQuery();
            if(res.next()) {
                return res;
            } else {
                return null;
            }
        } catch (SQLException e) {
            return null;
        }
    }
}
