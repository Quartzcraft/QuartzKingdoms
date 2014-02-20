package uk.co.quartzcraft.kingdoms.listeners;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

import uk.co.quartzcraft.*;
import uk.co.quartzcraft.core.QuartzCore;
import uk.co.quartzcraft.core.chat.ChatPhrase;
import uk.co.quartzcraft.core.entity.QPlayer;
import uk.co.quartzcraft.kingdoms.QuartzKingdoms;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ConnectionListener implements Listener {
	
	private static QuartzKingdoms plugin;
	
	public ConnectionListener(QuartzKingdoms plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;
    }
	
	/**
	 * Manages the login stuff for QuartzCore.
	 * @param login
	 */
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerLogin(PlayerLoginEvent login) {
		Player player = login.getPlayer();
		String splayer = player.toString();
		
		UUID UUID = player.getUniqueId();
		String SUUID = UUID.toString();
		
		Statement s1;
		try {
			s1 = QuartzCore.MySQLcore.openConnection().createStatement();
			ResultSet res1 = s1.executeQuery("SELECT * FROM PlayerData WHERE UUID='" + SUUID + "'");
			if(res1.next()) {
				String NSUUID = res1.getString("UUID").replaceAll("-", "");
				if(NSUUID == SUUID) {
					QPlayer.setConnectionStatus(player, true);
					QPlayer.autoManageGroups(player);
					plugin.log.info("[QC] Player, " + player.getDisplayName() + " sucessfully joined!");
				} else {
					plugin.log.info("[QC] Something went wrong!");
				}
			} else {
				if(QPlayer.createPlayer(player)) {
					plugin.log.info("[QC] Player, " + player.getDisplayName() + " was created with UUID of " + SUUID);
				} else {
					player.kickPlayer(ChatPhrase.getPhrase("database_error_contact") + ChatPhrase.getPhrase("could_not_create_player"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerJoinHigh(PlayerJoinEvent join) {
		Player player = join.getPlayer();
		UUID UUID = player.getUniqueId();
		String SUUID = UUID.toString();
		
		String splayer = player.toString();
		String playername = player.getDisplayName();
		
		if(plugin.getConfig().getString("settings.connection-broadcast") == "true") {
			String lastSeen = QPlayer.getLastSeen(SUUID);
			String message = playername + ChatColor.YELLOW + " joined, last seen " + lastSeen;
			
			join.setJoinMessage(message);
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerJoinLow(PlayerJoinEvent join) {
		Player player = join.getPlayer();
		UUID UUID = player.getUniqueId();
		String SUUID = UUID.toString();
		
		String splayer = player.toString();
		String playername = player.getDisplayName();
		
		player.sendMessage("Welcome back, " + playername + "!");
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerQuit(PlayerQuitEvent quit) {
		Player player = quit.getPlayer();
		
		if(plugin.getConfig().getString("settings.connection-broadcast") == "true") {
			String message = player.getDisplayName() + ChatColor.YELLOW + " disconnected.";
			
			quit.setQuitMessage(message);
		}
	}
	
}
