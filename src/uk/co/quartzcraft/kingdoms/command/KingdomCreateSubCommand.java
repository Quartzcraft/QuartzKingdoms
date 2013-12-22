package uk.co.quartzcraft.kingdoms.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import uk.co.quartzcraft.core.command.QSubCommand;

public class KingdomCreateSubCommand extends QSubCommand {

	@Override
	public String getPermission() {
		return "QCK.Kingdom";
	}

	@Override
	public void onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		// TODO Auto-generated method stub
		
	}

}
