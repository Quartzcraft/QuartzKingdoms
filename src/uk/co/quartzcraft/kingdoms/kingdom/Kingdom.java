package uk.co.quartzcraft.kingdoms.kingdom;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.Bukkit;

import uk.co.quartzcraft.core.QuartzCore;
import uk.co.quartzcraft.kingdoms.QuartzKingdoms;

public class Kingdom {
	
	private static QuartzKingdoms plugin;
	
	public void QuartzKingdomsConfig(QuartzKingdoms plugin) {
		this.plugin = plugin;
	}
	
	public static String createKingdom(String kingdomName, CommandSender sender) {
		return null;
		
	}
	
	public static String deleteKingdom(String kingdomName, CommandSender sender) {
		return null;
		
	}
	
	public static boolean promotePlayer(String kingdomName, CommandSender sender, String playerToPromote, String group) {
		String promoteCommand = plugin.getConfig().getString("settings.promote-command");
		promoteCommand.replaceAll("<group>", group);
		promoteCommand.replaceAll("<user>", playerToPromote);
		
		if(Bukkit.getServer().dispatchCommand(sender, promoteCommand)) {
			return true;
		} else {
			return false;
		}
		
	}
	
	public static String[] getInfo(String kingdomName) {
		return null;
		
	}
	
	public static String getName() {
		return null;
		
	}
	
	public static boolean setRelationshipStatus(String kingdom, String relatingKingdom, int status) {
		switch(status) {
			case 1:
				//Neutral
				//Update
			case 2:
				//Ally
				//Update
			case 3:
				//War
				//Update
			default:
				//Do nothing
				return false;
		}
		
	}
}
