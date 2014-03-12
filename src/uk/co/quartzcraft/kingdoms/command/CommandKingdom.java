package uk.co.quartzcraft.kingdoms.command;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import org.bukkit.entity.Player;
import uk.co.quartzcraft.core.QuartzCore;
import uk.co.quartzcraft.core.chat.ChatPhrase;
import uk.co.quartzcraft.core.command.QCommand;
import uk.co.quartzcraft.core.command.QSubCommand;
import uk.co.quartzcraft.kingdoms.QuartzKingdoms;
import uk.co.quartzcraft.kingdoms.entity.QKPlayer;
import uk.co.quartzcraft.kingdoms.kingdom.Kingdom;
import uk.co.quartzcraft.kingdoms.managers.ChunkManager;

public class CommandKingdom {
	
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
        String[] args0 = args.getArgs();
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

    @QCommand.Command(name = "kingdom.delete", aliases = { "k.delete" }, permission = "QCK.kingdom.delete", description = "Deletes the kingdom you specify. You must be the king.", usage = "Use /kingdom create [kingdom name]")
    public void kingdomDelete(QCommand.CommandArgs args0) {
        CommandSender sender = args0.getSender();
        String[] args = args0.getArgs();;
        if(args[1] != null) {
            String kingdomName = Kingdom.deleteKingdom(args[1], sender);
            if(kingdomName != null) {
                if(kingdomName == args[1]) {
                    sender.sendMessage(ChatPhrase.getPhrase("deleted_kingdom_yes") + ChatColor.WHITE + kingdomName);
                } else if(kingdomName == "error") {
                    sender.sendMessage(ChatPhrase.getPhrase("deleted_kingdom_no") + ChatColor.WHITE + kingdomName);
                }
            } else {
                sender.sendMessage(ChatPhrase.getPhrase("deleted_kingdom_no") + ChatColor.WHITE + kingdomName);
            }
        } else {
            sender.sendMessage(ChatPhrase.getPhrase("specify_kingdom_name") + ChatColor.WHITE + args[1]);
        }
    }

    @QCommand.Command(name = "kingdom.promote", aliases = { "k.promote" }, permission = "QCK.kingdom.promote", description = "Promotes the specified player to the specified rank in the kingdom", usage = "Use /kingdom promote [playername]")
    public void kingdomPromote(QCommand.CommandArgs args0) {
        CommandSender sender = args0.getSender();
        String[] args = args0.getArgs();;
        Player psender = (Player) sender;

        if(args[1] != null) {
            Player target = Bukkit.getServer().getPlayer(args[1]);
            String kingdomName = QKPlayer.getKingdom(target);
            if(Kingdom.compareKingdom(target, psender)) {
                if(Kingdom.promotePlayer(kingdomName, sender, args[1], args[2])) {
                    sender.sendMessage(ChatPhrase.getPhrase("promoted_player_yes") + ChatColor.WHITE + kingdomName);
                    target.sendMessage(ChatPhrase.getPhrase("got_promoted_kingdom_yes"));
                } else {
                    sender.sendMessage(ChatPhrase.getPhrase("promoted_player_no") + ChatColor.WHITE + kingdomName);
                }
            } else {
                sender.sendMessage(ChatPhrase.getPhrase("no_permission"));
            }
        } else {
            sender.sendMessage(ChatPhrase.getPhrase("specify_username") + ChatColor.WHITE + args[1]);
        }
    }

    @QCommand.Command(name = "kingdom.claim", aliases = { "k.claim" }, permission = "QCK.kingdom.claim", description = "Claims the chunk of land you are standing on for your kingdom. Uses 5 power.", usage = "Use /kingdom claim")
    public void kingdomClaim(QCommand.CommandArgs args0) {
        CommandSender sender = args0.getSender();
        Player player = (Player) sender;
        Chunk chunk = player.getLocation().getChunk();
        String kingdomName = QKPlayer.getKingdom(player);

        if(ChunkManager.isClaimed(chunk)) {
            sender.sendMessage(ChatPhrase.getPhrase("this_chunk_is_already_claimed"));
        } else {
            if(ChunkManager.claimChunkKingdom(player)) {
                sender.sendMessage(ChatPhrase.getPhrase("chunk_claimed_for_kingdom_yes") + ChatColor.WHITE + kingdomName);
            } else {
                sender.sendMessage(ChatPhrase.getPhrase("chunk_claimed_for_kingdom_no") + ChatColor.WHITE + kingdomName);
            }
        }
    }

    @QCommand.Command(name = "kingdom.unclaim", aliases = { "k.unclaim" }, permission = "QCK.kingdom.claim", description = "Unclaims the chunk of land you are standing on. Returns 3 power..", usage = "Use /kingdom unclaim")
    public void kingdomUnClaim(QCommand.CommandArgs args0) {
        CommandSender sender = args0.getSender();
        Player player = (Player) sender;
        Chunk chunk = player.getLocation().getChunk();
        String kingdomName = QKPlayer.getKingdom(player);

        if(ChunkManager.getKingdomOwner(chunk) == kingdomName) {
            if(ChunkManager.unClaimChunkKingdom(player)) {
                sender.sendMessage(ChatPhrase.getPhrase("chunk_unclaimed_for_kingdom_yes") + ChatColor.WHITE + kingdomName);
            } else {
                sender.sendMessage(ChatPhrase.getPhrase("chunk_unclaimed_for_kingdom_no") + ChatColor.WHITE + kingdomName);
            }
        } else {
            sender.sendMessage(ChatPhrase.getPhrase("this_chunk_is_not_claimed"));
        }
    }

    @QCommand.Command(name = "kingdom.war", aliases = { "k.war" }, permission = "QCK.kingdom.war", description = "Declares war against the specified kingdom. Uses 5 power.", usage = "Use /kingdom war [enemy kingdom name]")
    public void kingdomWar(QCommand.CommandArgs args0) {
        CommandSender sender = args0.getSender();
        String[] args = args0.getArgs();
        Player player = (Player) sender;
        String kingdom = QKPlayer.getKingdom(player);

        if(QKPlayer.isKing(kingdom, player)) {
            if(Kingdom.setRelationshipStatus(kingdom, args[1], 3)) {
                Bukkit.broadcastMessage(ChatPhrase.getPhrase(null + "kingdom_is_now_at_war_with_kingdom") + ChatColor.WHITE + null);
            } else {
                sender.sendMessage(ChatPhrase.getPhrase("failed_to_war_with_kingdom"));
            }
        } else {
            sender.sendMessage(ChatPhrase.getPhrase("no_permission"));
        }
    }

    @QCommand.Command(name = "kingdom.neutral", aliases = { "k.neutral" }, permission = "QCK.kingdom.neutral", description = "Removes all relationships between kingdoms, not at war or allied with the specified kingdom.", usage = "Use /kingdom neutral [other kingdom name]")
    public void kingdomNeutral(QCommand.CommandArgs args0) {
        CommandSender sender = args0.getSender();
        String[] args = args0.getArgs();
        Player player = (Player) sender;
        String kingdom = QKPlayer.getKingdom(player);

        if(QKPlayer.isKing(kingdom, player)) {
            if(Kingdom.setRelationshipStatus(kingdom, args[1], 1)) {
                Bukkit.broadcastMessage(ChatPhrase.getPhrase(null + "kingdom_is_now_neutral_relationship_with_kingdom") + ChatColor.WHITE + null);
            } else {
                sender.sendMessage(ChatPhrase.getPhrase("failed_to_neutral_with_kingdom"));
            }
        } else {
            sender.sendMessage(ChatPhrase.getPhrase("no_permission"));
        }
    }

    @QCommand.Command(name = "kingdom.ally", aliases = { "k.ally" }, permission = "QCK.kingdom.ally", description = "Allies with a kingdom, members of allied kingdoms can not hurt each other.", usage = "Use /kingdom ally [ally kingdom name]")
    public void kingdomAlly(QCommand.CommandArgs args0) {
        CommandSender sender = args0.getSender();
        String[] args = args0.getArgs();
        Player player = (Player) sender;
        String kingdom = QKPlayer.getKingdom(player);

        if(QKPlayer.isKing(kingdom, player)) {
            if(Kingdom.setRelationshipStatus(kingdom, args[1], 2)) {
                Bukkit.broadcastMessage(ChatPhrase.getPhrase(null + "kingdom_is_now_allied_with_kingdom") + ChatColor.WHITE + null);
            } else {
                sender.sendMessage(ChatPhrase.getPhrase("failed_to_ally_with_kingdom"));
            }
        } else {
            sender.sendMessage(ChatPhrase.getPhrase("no_permission"));
        }
    }

    @QCommand.Command(name = "kingdom.join", aliases = { "k.join" }, permission = "QCK.kingdom.join", description = "Joins the specified kingdom, as long as you are allowed to. Gives the kingdom 6 power.", usage = "Use /kingdom join [kingdom name]")
    public void kingdomJoin(QCommand.CommandArgs args0) {
        CommandSender sender = args0.getSender();
        Player player = (Player) sender;
        String[] args = args0.getArgs();
        if(QKPlayer.getKingdom(player) != null) {
            sender.sendMessage(ChatPhrase.getPhrase("you_are_already_in_a_Kingdom"));
        } else if(Kingdom.isOpen(args[1])) {
            if(QKPlayer.joinKingdom(player, args[1])) {
                sender.sendMessage(ChatPhrase.getPhrase("successfully_joined_kingdom_X") + args[1]);
            } else {
                sender.sendMessage(ChatPhrase.getPhrase("failed_join_kingdom"));
            }
        } else if(Kingdom.isOpen(args[1]) == false) {
            sender.sendMessage(ChatPhrase.getPhrase("kingdom_not_open"));
        } else {
            sender.sendMessage(ChatPhrase.getPhrase("kingdom_not_found"));
        }
    }


    /*
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
    */

    public static void addCommand(List<String> cmds, QSubCommand s) {
            commands.put(cmds, s);
    }
}
