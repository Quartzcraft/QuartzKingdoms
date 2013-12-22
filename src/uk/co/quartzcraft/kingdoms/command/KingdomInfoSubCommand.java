package uk.co.quartzcraft.kingdoms.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import uk.co.quartzcraft.core.command.QSubCommand;

public class KingdomInfoSubCommand extends QSubCommand {

	 public String getPermission() {
             return "QCK.Kingdom";
     }
     
     public void onCommand(CommandSender sender, Command cmd, String label, String[] args) {        
             sender.sendMessage("Info on Kingdom: ");
     }
}
