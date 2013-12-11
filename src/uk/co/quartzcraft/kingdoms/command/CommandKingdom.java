package uk.co.quartzcraft.kingdoms.command;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.VanillaCommand;
import org.bukkit.entity.Player;

import uk.co.quartzcraft.command.QSubCommand;
import uk.co.quartzcraft.kingdoms.QuartzKingdoms;
import uk.co.quartzcraft.kingdoms.command.subcommand.*;

public class CommandKingdom implements CommandExecutor {

	//protected static final Set<VanillaCommand> SubCommands = new HashSet<VanillaCommand>();
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] arg0) {
		
		if(command.getName().equalsIgnoreCase("kingdom")){ 
			
			Player player = (Player) sender;
			
			String[] SubCommandList = SubCommandsKingdom.list();
			
			if(arg0.length == 0) {
				player.sendMessage(ChatColor.RED + "Kingdom Help - Please see the QuartzCraft Wiki for more detail");
				//player.sendMessage(ChatColor.GREEN + "/kingdom info [kingdom name] - Gets information about a kingdom");
				//player.sendMessage(ChatColor.GREEN + "/kingdom create [kingdom name] - Creates a kingdom with the specified name");
				//player.sendMessage(ChatColor.GREEN + "/kingdom set [permission] [true/false] - Sets a kingdom permission(setting)");
				//player.sendMessage(ChatColor.GREEN + "/kingdom invite [username] - Invites a player to your kingdom");
				//player.sendMessage(ChatColor.GREEN + "/kingdom join [kingdom name] - Joins the specified kingdom if invite only is set to false");
				//player.sendMessage(ChatColor.GREEN + "/kingdom goempire - Turns your kingdom into an Empire!");
				//player.sendMessage(ChatColor.RED + "/kingdom delete [kingdom name] - Deletes the specified kingdom.");
				
				for(String SubCommand : SubCommandList){
					player.sendMessage(ChatColor.GREEN + "/kingdom " + SubCommand);
				}
				return false;
				
			} else {
				if(SubCommandsKingdom.getSubCommand(arg0[0]) != null) {
					String SubCommand = SubCommandsKingdom.getSubCommand(arg0[0]);		
					
					//run subcommands
					boolean[] SubCommandSuccess = new boolean[20];
					SubCommandSuccess[0] = new SubCommandKingdomCreate().onSubCommand(SubCommand, sender, arg0);
					SubCommandSuccess[1] = new SubCommandKingdomInfo().onSubCommand(SubCommand, sender, arg0);
					
					//run through successes
					for(boolean Result : SubCommandSuccess) {
						if(Result == true){
							return true;
						} else {
							return false;
						}
					}
				
				} else {
					player.sendMessage(ChatColor.RED + "Kingdom Help - Please see the QuartzCraft Wiki for more detail");
					for(String SubCommand : SubCommandList){
						player.sendMessage(ChatColor.GREEN + "/kingdom " + SubCommand);
					}
					return false;
				}
				
			}
			
		}
		return false;
	}
}
