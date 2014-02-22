package uk.co.quartzcraft.kingdoms.command;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import uk.co.quartzcraft.core.chat.ChatPhrase;
import uk.co.quartzcraft.core.command.QSubCommand;
import uk.co.quartzcraft.kingdoms.QuartzKingdoms;
import uk.co.quartzcraft.kingdoms.entity.QKPlayer;
import uk.co.quartzcraft.kingdoms.kingdom.Kingdom;;

public class KingdomPromoteSubCommand extends QSubCommand {

	@Override
	public String getPermission() {
		return "QCK.Kingdom.promote";
	}

	@Override
	public void onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player target = Bukkit.getServer().getPlayer(args[1]);
		Player psender = (Player) sender;
		String kingdomName = QKPlayer.getKingdom(target);
		
		if(Kingdom.compareKingdom(target, psender)) {
			if(args[1] != null) {
				if(Kingdom.promotePlayer(kingdomName, sender, args[1], args[2])) {
					sender.sendMessage(ChatPhrase.getPhrase("promoted_player_yes") + ChatColor.WHITE + kingdomName);
					target.sendMessage(ChatPhrase.getPhrase("got_promoted_kingdom_yes"));
				} else {
					sender.sendMessage(ChatPhrase.getPhrase("promoted_player_no") + ChatColor.WHITE + kingdomName);
				}
			} else {
				sender.sendMessage(ChatPhrase.getPhrase("specify_username") + ChatColor.WHITE + args[1]);
			}
		} else {
			sender.sendMessage(ChatPhrase.getPhrase("no_permission"));
		}
		
	}

}
