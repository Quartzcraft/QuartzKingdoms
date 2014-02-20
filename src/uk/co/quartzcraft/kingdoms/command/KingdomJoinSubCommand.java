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
import uk.co.quartzcraft.kingdoms.kingdom.Kingdom;
import uk.co.quartzcraft.kingdoms.managers.ChunkManager;

public class KingdomJoinSubCommand extends QSubCommand {

	@Override
	public String getPermission() {
		return "QCK.Kingdom.join";
	}

	@Override
	public void onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		//TODO 
	}
}
