package uk.co.quartzcraft.kingdoms.command.subcommand;

import org.bukkit.command.CommandSender;

import uk.co.quartzcraft.command.QSubCommand;

public class SubCommandKingdomInfo implements QSubCommand {

	@Override
	public boolean onSubCommand(String SubCommand, CommandSender sender, String[] arg0) {
		if(SubCommand == "Info"){
			return true;
		}
		return false;
	}

}
