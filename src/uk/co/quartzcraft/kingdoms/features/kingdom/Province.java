package uk.co.quartzcraft.kingdoms.features.kingdom;

import org.bukkit.Location;
import org.bukkit.plugin.Plugin;
import uk.co.quartzcraft.kingdoms.QuartzKingdoms;
import uk.co.quartzcraft.kingdoms.data.QKPlayer;

public class Province {

    private Plugin plugin = QuartzKingdoms.plugin;

    private int id;
    private String name;
    private int noble;
    private Location home;

    public Province(int id) {

    }

    public Province(String name) {

    }

    public static Province createProvince(String name, QKPlayer player) {
        return null;
    }

    public static boolean exists(String name) {
        return false;
    }

    public boolean delete(QKPlayer player) {
        return false;
    }
}
