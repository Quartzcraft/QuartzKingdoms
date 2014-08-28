package uk.co.quartzcraft.kingdoms.features.kingdom;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.plugin.Plugin;

import uk.co.quartzcraft.kingdoms.QuartzKingdoms;
import uk.co.quartzcraft.kingdoms.data.QKPlayer;
import uk.co.quartzcraft.kingdoms.util.KUtil;

public class Kingdom {
	
	private static Plugin plugin = QuartzKingdoms.plugin;

    private static int id;
    private static String name;
    private static QKPlayer king;
    private static int power;
    private static boolean open;

    /**
     * Creates a kingdom object using the specified id
     *
     * @param id
     */
    public Kingdom(int id) {
        this.id = id;

        try {
            Statement s = QuartzKingdoms.MySQLking.openConnection().createStatement();
            ResultSet res = s.executeQuery("SELECT * FROM Kingdoms WHERE id='" + id + "';");
            if(res.next()) {
                this.id = res.getInt("id");
                this.power = res.getInt("Power");
                this.open = res.getBoolean("invite_only");
                this.king = new QKPlayer(res.getInt("KingID"));
                this.name = res.getString("KingdomName");
            } else {
                return;
            }

        } catch(SQLException e) {
            KUtil.printException("Could not retrieve Kingdoms data", e);
            return;
        }
    }

    /**
     * Creates a kingdom object using the spcified name
     *
     * @param name
     */
    public Kingdom(String name) {
        this.name = name;

        try {
            Statement s = QuartzKingdoms.MySQLking.openConnection().createStatement();
            ResultSet res = s.executeQuery("SELECT * FROM Kingdoms WHERE KingdomName='" + name + "';");
            if(res.next()) {
                this.id = res.getInt("id");
                this.power = res.getInt("Power");
                this.open = res.getBoolean("invite_only");
                this.king = new QKPlayer(res.getInt("KingID"));
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
			java.sql.PreparedStatement s = QuartzKingdoms.DBKing.prepareStatement("INSERT INTO Kingdoms (KingdomName) VALUES ('?');");
            s.setString(1, kingdomName);
            if(s.executeUpdate() == 1) {
                Kingdom kkingdom = new Kingdom(player.getKingdom().getID());
                player.setKingdom(kkingdom);
                kkingdom.setKing(player);
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
     * Determines whether a kingdom with the specifed name exisits.
     *
     * @param kingdomName
     * @return
     */
    public static boolean exists(String kingdomName) {
        java.sql.Connection connection = QuartzKingdoms.MySQLking.openConnection();
        try {
            java.sql.PreparedStatement s = QuartzKingdoms.DBKing.prepareStatement("SELECT * FROM Kingdoms WHERE KingdomName='?';");
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
                return this;
            } else {
                return this;
            }
        } catch (SQLException e) {
            return this;
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
	
	public static int setRelationshipStatus(String kingdom, String relatingKingdom, int status) {
		//TODO
		switch(status) {
			case 1:
                //Neutral
                try {
                    java.sql.PreparedStatement s = QuartzKingdoms.MySQLking.openConnection().prepareStatement("SELECT FROM relationships WHERE kingdom_id=" + getID(kingdom) + " AND sec_kingdom_id=" + getID(relatingKingdom) + ";");
                    ResultSet res = s.executeQuery();
                    if(isNeutral(kingdom, relatingKingdom)) {
                        return 0;
                    } else if(res.next() && res.getInt("kingdom_id") == getID(kingdom)) {
                        if(res.getString("status") == "11") {
                            java.sql.PreparedStatement s1 = QuartzKingdoms.MySQLking.openConnection().prepareStatement("UPDATE relationships SET status=1 WHERE kingdom_id=" + getID(kingdom) + " AND sec_kingdom_id=" + getID(relatingKingdom) + ";");
                            if(s1.executeUpdate() == 1) {
                                return 2;
                            } else {
                                return 0;
                            }
                        } else {
                            java.sql.PreparedStatement s1 = QuartzKingdoms.MySQLking.openConnection().prepareStatement("UPDATE relationships SET status=11 WHERE kingdom_id=" + getID(kingdom) + " AND sec_kingdom_id=" + getID(relatingKingdom) + ";");
                            if(s1.executeUpdate() == 1) {
                                return 1;
                            } else {
                                return 0;
                            }
                        }
                    } else {
                        java.sql.PreparedStatement s1 = QuartzKingdoms.MySQLking.openConnection().prepareStatement("INSERT INTO relationships (kingdom_id, sec_kingdom_id, status) VALUES (" + getID(kingdom) + ", " + getID(relatingKingdom) + ", 11);");
                        if(s1.executeUpdate() == 1) {
                            return 1;
                        } else {
                            return 0;
                        }
                    }
                }  catch (SQLException e) {
                    e.printStackTrace();
                    return 0;
                }
			case 2:
				//Ally
                try {
                    java.sql.PreparedStatement s = QuartzKingdoms.MySQLking.openConnection().prepareStatement("SELECT FROM relationships WHERE kingdom_id=" + getID(kingdom) + " AND sec_kingdom_id=" + getID(relatingKingdom) + ";");
                    ResultSet res = s.executeQuery();
                    if(isAlly(kingdom, relatingKingdom)) {
                        return 0;
                    } else if(res.next()) {
                        if(res.getString("status") == "11") {
                            java.sql.PreparedStatement s1 = QuartzKingdoms.MySQLking.openConnection().prepareStatement("UPDATE relationships SET status=2 WHERE kingdom_id=" + getID(kingdom) + " AND sec_kingdom_id=" + getID(relatingKingdom) + ";");
                            if(s1.executeUpdate() == 1) {
                                return 2;
                            } else {
                                return 0;
                            }
                        } else {
                            java.sql.PreparedStatement s1 = QuartzKingdoms.MySQLking.openConnection().prepareStatement("UPDATE relationships SET status=22 WHERE kingdom_id=" + getID(kingdom) + " AND sec_kingdom_id=" + getID(relatingKingdom) + ";");
                            if(s1.executeUpdate() == 1) {
                                return 1;
                            } else {
                                return 0;
                            }
                        }
                    } else {
                        java.sql.PreparedStatement s1 = QuartzKingdoms.MySQLking.openConnection().prepareStatement("INSERT INTO relationships (kingdom_id, sec_kingdom_id, status) VALUES (" + getID(kingdom) + ", " + getID(relatingKingdom) + ", 22);");
                        if(s1.executeUpdate() == 1) {
                            return 1;
                        } else {
                            return 0;
                        }
                    }
                }  catch (SQLException e) {
                    e.printStackTrace();
                    return 0;

    /**
     * Sets the relationships between this kingdom and the specified kingdom to neutral.
     *
     * @param relatingKingdom
     */
    public void setAtNeutral(Kingdom relatingKingdom) {
        try {
            java.sql.PreparedStatement s = QuartzKingdoms.MySQLking.openConnection().prepareStatement("SELECT FROM relationships WHERE kingdom_id=? AND sec_kingdom_id=?;");
            s.setInt(1, this.id);
            s.setInt(2, relatingKingdom.getID());
            ResultSet res = s.executeQuery();
            if(res.next()) {
                java.sql.PreparedStatement s1 = QuartzKingdoms.MySQLking.openConnection().prepareStatement("DELETE FROM relationships WHERE kingdom_id=? AND sec_kingdom_id=?;");
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
    public void setAtWar(Kingdom relatingKingdom) {
        try {
            java.sql.PreparedStatement s = QuartzKingdoms.MySQLking.openConnection().prepareStatement("SELECT FROM relationships WHERE kingdom_id=? AND sec_kingdom_id=?;");
            s.setInt(1, this.id);
            s.setInt(2, relatingKingdom.getID());
            ResultSet res = s.executeQuery();
            if(isEnemy(relatingKingdom)) {
                return;
            } else if(res.next()) {
                if(res.getString("status") == "33") {
                    java.sql.PreparedStatement s1 = QuartzKingdoms.MySQLking.openConnection().prepareStatement("UPDATE relationships SET status=3 WHERE kingdom_id=? AND sec_kingdom_id=?;");
                    s1.setInt(1, this.id);
                    s1.setInt(2, relatingKingdom.getID());
                    s1.executeUpdate();
                } else {
                    java.sql.PreparedStatement s2 = QuartzKingdoms.MySQLking.openConnection().prepareStatement("UPDATE relationships SET status=33 WHERE kingdom_id=? AND sec_kingdom_id=?;");
                    s2.setInt(1, this.id);
                    s2.setInt(2, relatingKingdom.getID());
                    s2.executeUpdate();
                }
			case 3:
				//War
                try {
                    java.sql.PreparedStatement s = QuartzKingdoms.MySQLking.openConnection().prepareStatement("SELECT FROM relationships WHERE kingdom_id=" + getID(kingdom) + " AND sec_kingdom_id=" + getID(relatingKingdom) + ";");
                    ResultSet res = s.executeQuery();
                    if(isEnemy(kingdom, relatingKingdom)) {
                        return 0;
                    } else if(res.next()) {
                        if(res.getString("status") == "11") {
                            java.sql.PreparedStatement s1 = QuartzKingdoms.MySQLking.openConnection().prepareStatement("UPDATE relationships SET status=3 WHERE kingdom_id=" + getID(kingdom) + " AND sec_kingdom_id=" + getID(relatingKingdom) + ";");
                            if(s1.executeUpdate() == 1) {
                                return 2;
                            } else {
                                return 0;
                            }
                        } else {
                            java.sql.PreparedStatement s1 = QuartzKingdoms.MySQLking.openConnection().prepareStatement("UPDATE relationships SET status=33 WHERE kingdom_id=" + getID(kingdom) + " AND sec_kingdom_id=" + getID(relatingKingdom) + ";");
                            if(s1.executeUpdate() == 1) {
                                return 1;
                            } else {
                                return 0;
                            }
                        }
                    } else {
                        java.sql.PreparedStatement s1 = QuartzKingdoms.MySQLking.openConnection().prepareStatement("INSERT INTO relationships (kingdom_id, sec_kingdom_id, status) VALUES (" + getID(kingdom) + ", " + getID(relatingKingdom) + ", 33);");
                        if(s1.executeUpdate() == 1) {
                            return 1;
                        } else {
                            return 0;
                        }
                    }
                }  catch (SQLException e) {
                    e.printStackTrace();
                    return 0;
    /**
     * Sets the relationships between this kingdom and the specified kingdom to ally.
     *
     * @param relatingKingdom
     */
    public void setAtAlly(Kingdom relatingKingdom) {
        try {
            java.sql.PreparedStatement s = QuartzKingdoms.MySQLking.openConnection().prepareStatement("SELECT FROM relationships WHERE kingdom_id=? AND sec_kingdom_id=?;");
            s.setInt(1, this.id);
            s.setInt(2, relatingKingdom.getID());
            ResultSet res = s.executeQuery();
            if(isEnemy(relatingKingdom)) {
                return;
            } else if(res.next()) {
                if(res.getString("status") == "33") {
                    java.sql.PreparedStatement s1 = QuartzKingdoms.MySQLking.openConnection().prepareStatement("UPDATE relationships SET status=2 WHERE kingdom_id=? AND sec_kingdom_id=?;");
                    s1.setInt(1, this.id);
                    s1.setInt(2, relatingKingdom.getID());
                    s1.executeUpdate();
                } else {
                    java.sql.PreparedStatement s2 = QuartzKingdoms.MySQLking.openConnection().prepareStatement("UPDATE relationships SET status=22 WHERE kingdom_id=? AND sec_kingdom_id=?;");
                    s2.setInt(1, this.id);
                    s2.setInt(2, relatingKingdom.getID());
                    s2.executeUpdate();
                }
			default:
				//Do nothing
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
            java.sql.PreparedStatement s = QuartzKingdoms.MySQLking.openConnection().prepareStatement("SELECT * FROM relationships WHERE kingdom_id=? AND sec_kingdom_id=? AND status=3;");
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
            java.sql.PreparedStatement s = QuartzKingdoms.MySQLking.openConnection().prepareStatement("SELECT * FROM relationships WHERE kingdom_id=? AND sec_kingdom_id=? AND status=2;");
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
}
