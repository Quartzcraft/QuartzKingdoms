package uk.co.quartzcraft.kingdoms.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import uk.co.quartzcraft.core.command.QSubCommand;

public class InfoSubCommand extends QSubCommand {

	 public String getPermission() {
             return "launchme.info";
     }
     
     public void onCommand(CommandSender sender, Command cmd, String label, String[] args) {        
             
     }
}
