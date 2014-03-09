package uk.co.quartzcraft.kingdoms.command;

import java.util.HashMap;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import uk.co.quartzcraft.core.QuartzCore;
import uk.co.quartzcraft.core.chat.ChatPhrase;
import uk.co.quartzcraft.core.command.QCommand;
import uk.co.quartzcraft.core.command.QSubCommand;
import uk.co.quartzcraft.kingdoms.QuartzKingdoms;
import uk.co.quartzcraft.kingdoms.kingdom.Kingdom;

public class CommandKingdom implements CommandExecutor {
	
	private static HashMap<List<String>, QSubCommand> commands = new HashMap<List<String>, QSubCommand>();
    private static QuartzKingdoms plugin;
    private static QCommand framework;

    public CommandKingdom(QuartzKingdoms plugin) {
        this.plugin = plugin;
        framework = new QCommand(this.plugin);
        framework.registerCommands(this);
    }

    @QCommand.Command(name = "kingdom", aliases = { "k" }, permission = "QCK.kingdom", description = "The main kingdoms command", usage = "Use /kingdom [subcommand]")
    public void kingdom(QCommand.CommandArgs args) {
        args.getSender().sendMessage(ChatPhrase.getPhrase("Specify_Subcommand"));
    }

    @QCommand.Command(name = "kingdom.info", aliases = { "k.info" }, permission = "QCK.kingdom.info", description = "Get information about a specified kingdom", usage = "Use /kingdom info [kingdom name]")
    public void kingdomInfo(QCommand.CommandArgs args) {
        args.getSender().sendMessage("Info command testing");
    }

    @QCommand.Command(name = "kingdom.create", aliases = { "k.create" }, permission = "QCK.kingdom.create", description = "Creates a kingdoms with the specified name", usage = "Use /kingdom create [kingdom name]")
    public void kingdomCreate(QCommand.CommandArgs args) {
        CommandSender sender = args.getSender();
        String[] args0 = args.getArgs();;
        if(args0[1] != null) {
            if(args0[2] != null) {
                sender.sendMessage(ChatPhrase.getPhrase("kingdom_name_single_word"));
            } else {
                String kingdomName = args0[1];
                boolean created = Kingdom.createKingdom(args0[1], sender);
                if(created) {
                    if(kingdomName == args0[1]) {
                        sender.sendMessage(ChatPhrase.getPhrase("created_kingdom_yes") + ChatColor.WHITE + kingdomName);
                    } else if(kingdomName == "name_error") {
                        sender.sendMessage(ChatPhrase.getPhrase("kingdomname_already_used") + ChatColor.WHITE + kingdomName);
                    }
                } else {
                    sender.sendMessage(ChatPhrase.getPhrase("created_kingdom_no") + ChatColor.WHITE + kingdomName);
                }
            }
        } else {
            sender.sendMessage(ChatPhrase.getPhrase("specify_kingdom_name") + ChatColor.WHITE + args0[1]);
        }
    }
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
