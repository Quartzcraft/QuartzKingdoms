package uk.co.quartzcraft.kingdoms.command.subcommand;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import uk.co.quartzcraft.command.QSubCommand;
import uk.co.quartzcraft.kingdoms.QuartzKingdoms;

public class SubCommandKingdomCreate implements QSubCommand {

	@Override
	public boolean onSubCommand(String SubCommand, CommandSender sender, String[] arg0) {
		
		if(SubCommand == "Create") {
			if(SubCommand == "Create") {
				sender.sendMessage(ChatColor.GREEN + "The kingdom was created with the name: " + arg0[0]);
				return true;
			} else {
				sender.sendMessage(ChatColor.GREEN + "The kingdom could not be created! Try again.");
				return false;
			}
		}
		return false;
	}

}
