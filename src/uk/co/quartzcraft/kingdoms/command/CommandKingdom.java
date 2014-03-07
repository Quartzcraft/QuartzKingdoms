package uk.co.quartzcraft.kingdoms.command;

import java.util.HashMap;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import uk.co.quartzcraft.core.chat.ChatPhrase;
import uk.co.quartzcraft.core.command.QSubCommand;

public class CommandKingdom implements CommandExecutor {
	
	private static HashMap<List<String>, QSubCommand> commands = new HashMap<List<String>, QSubCommand>();

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
            if(args.length >= 1) {
                    boolean match = false; 
                    
                    for(List<String> s : commands.keySet())
                    {
                            if(s.contains(args[0]))
                            {
                                    commands.get(s).runCommand(sender, cmd, label, args);
                                    match = true;
                            }
                    }
                    
                    if(!match)
                    {
                            sender.sendMessage(ChatPhrase.getPhrase("Unknown_SubCommand"));
                    }
            }
            else {
                    sender.sendMessage(ChatPhrase.getPhrase("Specify_SubCommand"));
            }
            
            return true;
    }
    
    public static void addCommand(List<String> cmds, QSubCommand s) {
            commands.put(cmds, s);
    }
}
