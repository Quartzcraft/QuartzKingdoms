package uk.co.quartzcraft.kingdoms.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import uk.co.quartzcraft.core.command.framework.QCommandFramework;
import uk.co.quartzcraft.core.systems.chat.QCChat;
import uk.co.quartzcraft.core.command.framework.QCommand;
import uk.co.quartzcraft.core.command.framework.CommandArgs;
import uk.co.quartzcraft.kingdoms.QuartzKingdoms;
import uk.co.quartzcraft.kingdoms.data.QKPlayer;
import uk.co.quartzcraft.kingdoms.kingdom.Kingdom;
import uk.co.quartzcraft.kingdoms.managers.ChunkManager;

public class CommandKingdom {

    private static QuartzKingdoms plugin;
    private static Plugin core;
    private static QCommandFramework framework;

    public CommandKingdom(QuartzKingdoms plugin, Plugin core) {
        this.plugin = plugin;
        this.core = core;
        framework = new QCommandFramework(this.plugin);
        framework.registerCommands(this);
    }

    @QCommand(name = "kingdom", aliases = { "k" }, permission = "QCK.kingdom", description = "The main kingdoms command", usage = "Use /kingdom [subcommand]")
    public void kingdom(CommandArgs args) {
        args.getSender().sendMessage(QCChat.getPhrase("Specify_Subcommand"));
    }

    @QCommand(name = "kingdom.test", aliases = { "k.test" }, permission = "QCK.kingdom", description = "The test kingdoms command", usage = "Use /kingdom test")
    public void kingdomTest(CommandArgs args) {
        String[] args0 = args.getArgs();
        args.getSender().sendMessage("This is the test kingdoms command!" + "Args0[1] was equal to " + args0[1]);
        args.getSender().sendMessage("config preference settings.world equals:" + this.plugin.getConfig().getString("settings.world"));
    }

    @QCommand(name = "kingdom.info", aliases = { "k.info" }, permission = "QCK.kingdom.info", description = "Get information about a specified kingdom", usage = "Use /kingdom info [kingdom name]")
    public void kingdomInfo(CommandArgs args) {
        args.getSender().sendMessage("Info command testing");
    }

    @QCommand(name = "kingdom.create", aliases = { "k.create" }, permission = "QCK.kingdom.create", description = "Creates a kingdoms with the specified name", usage = "Use /kingdom create [kingdom name]")
    public void kingdomCreate(CommandArgs args) {
        CommandSender sender = args.getSender();
        Player player = (Player) sender;
        String[] args0 = args.getArgs();
        if(args0.length >= 1) {
            if(args0.length >= 2) {
                sender.sendMessage(QCChat.getPhrase("kingdom_name_single_word"));
            } else {
                if(QKPlayer.kingdom(player)) {
                    sender.sendMessage(QCChat.getPhrase("you_are_already_in_a_Kingdom"));
                } else {
                    String kingdomName = args0[0];
                    boolean created = Kingdom.createKingdom(args0[0], sender);
                    if(created) {
                        sender.sendMessage(QCChat.getPhrase("created_kingdom_yes") + ChatColor.WHITE + kingdomName);
                    } else {
                        sender.sendMessage(QCChat.getPhrase("created_kingdom_no") + ChatColor.WHITE + kingdomName);
                    }
                }
            }
        } else {
            sender.sendMessage(QCChat.getPhrase("specify_kingdom_name"));
        }
    }

    @QCommand(name = "kingdom.delete", aliases = { "k.delete" }, permission = "QCK.kingdom.delete", description = "Deletes the kingdom you specify. You must be the king.", usage = "Use /kingdom delete [kingdom name]")
    public void kingdomDelete(CommandArgs args0) {
        CommandSender sender = args0.getSender();
        String[] args = args0.getArgs();
        if(args.length >= 1) {
            if(Kingdom.deleteKingdom(args[0], sender)) {
                sender.sendMessage(QCChat.getPhrase("deleted_kingdom_yes") + ChatColor.WHITE + args[0]);
            } else {
                sender.sendMessage(QCChat.getPhrase("deleted_kingdom_no") + ChatColor.WHITE + args[0]);
            }
        } else {
            sender.sendMessage(QCChat.getPhrase("specify_kingdom_name"));
        }
    }
    
    @QCommand(name = "kingdom.disband", aliases = { "k.disband" }, permission = "QCK.kingdom.disband", description = "Disbands the kingdom you specify. You must be the king.", usage = "Use /kingdom disband [kingdom name]")
    public void kingdomDisband(CommandArgs args0) {
        CommandSender sender = args0.getSender();
        String[] args = args0.getArgs();
        if(args.length >= 1) {
            if(Kingdom.deleteKingdom(args[0], sender)) {
                sender.sendMessage(QCChat.getPhrase("disbanded_kingdom_yes") + ChatColor.WHITE + args[0]);
            } else {
                sender.sendMessage(QCChat.getPhrase("disbanded_kingdom_no") + ChatColor.WHITE + args[0]);
            }
        } else {
            sender.sendMessage(QCChat.getPhrase("specify_kingdom_name"));
        }
    }

    @QCommand(name = "kingdom.promote", aliases = { "k.promote" }, permission = "QCK.kingdom.promote", description = "Promotes the specified player to the specified rank in the kingdom", usage = "Use /kingdom promote [playername]")
    public void kingdomPromote(CommandArgs args0) {
        CommandSender sender = args0.getSender();
        String[] args = args0.getArgs();
        Player psender = (Player) sender;
        Player target = null;

        if(args.length >= 2) {
            if(Bukkit.getOfflinePlayer(args[0]).isOnline()) {
                target = Bukkit.getServer().getPlayer(args[0]);
            } else {
                psender.sendMessage(QCChat.getPhrase("specify_online_username"));
                return;
            }
            String kingdomName = QKPlayer.getKingdom(target);
            if(Kingdom.compareKingdom(target, psender)) {
                if(Kingdom.promotePlayer(kingdomName, sender, args[1], args[0], this.core)) {
                    sender.sendMessage(QCChat.getPhrase("promoted_player_yes") + ChatColor.WHITE + kingdomName);
                    target.sendMessage(QCChat.getPhrase("got_promoted_kingdom_yes"));
                } else {
                    sender.sendMessage(QCChat.getPhrase("promoted_player_no") + ChatColor.WHITE + kingdomName);
                }
            } else {
                sender.sendMessage(QCChat.getPhrase("no_permission"));
            }
        } else {
            sender.sendMessage(QCChat.getPhrase("specify_username"));
        }
    }

    @QCommand(name = "kingdom.claim", aliases = { "k.claim" }, permission = "QCK.kingdom.claim", description = "Claims the chunk of land you are standing on for your kingdom. Uses 4 power.", usage = "Use /kingdom claim")
    public void kingdomClaim(CommandArgs args0) {
        CommandSender sender = args0.getSender();
        Player player = (Player) sender;
        Chunk chunk = player.getLocation().getChunk();
        String kingdomName = QKPlayer.getKingdom(player);
        World world = player.getWorld();
        String WorldName = world.getName();
        String AWorldName = this.plugin.getConfig().getString("settings.world");

        if(WorldName.equals(AWorldName)) {
            if(ChunkManager.isClaimed(chunk)) {
                sender.sendMessage(QCChat.getPhrase("this_chunk_is_already_claimed"));
            } else {
                if(ChunkManager.claimChunkKingdom(player)) {
                    sender.sendMessage(QCChat.getPhrase("chunk_claimed_for_kingdom_yes") + ChatColor.WHITE + kingdomName);
                    Kingdom.setPower(QKPlayer.getKingdom(player), false, 4);
                } else {
                    sender.sendMessage(QCChat.getPhrase("chunk_claimed_for_kingdom_no") + ChatColor.WHITE + kingdomName);
                }
            }
        } else {
            sender.sendMessage(QCChat.getPhrase("you_can_not_claim_land_in_this_world"));
        }
    }

    @QCommand(name = "kingdom.unclaim", aliases = { "k.unclaim" }, permission = "QCK.kingdom.claim", description = "Unclaims the chunk of land you are standing on. Returns 2 power.", usage = "Use /kingdom unclaim")
    public void kingdomUnClaim(CommandArgs args0) {
        CommandSender sender = args0.getSender();
        Player player = (Player) sender;
        Chunk chunk = player.getLocation().getChunk();
        String kingdomName = QKPlayer.getKingdom(player);

        if(ChunkManager.isClaimed(chunk) && ChunkManager.getKingdomOwner(chunk).equals(kingdomName)) {
            if(ChunkManager.unClaimChunkKingdom(player)) {
                sender.sendMessage(QCChat.getPhrase("chunk_unclaimed_for_kingdom_yes") + ChatColor.WHITE + kingdomName);
                Kingdom.setPower(QKPlayer.getKingdom(player), true, 2);
            } else {
                sender.sendMessage(QCChat.getPhrase("chunk_unclaimed_for_kingdom_no") + ChatColor.WHITE + kingdomName);
            }
        } else {
            sender.sendMessage(QCChat.getPhrase("this_chunk_is_not_claimed"));
        }
    }

    @QCommand(name = "kingdom.war", aliases = { "k.war" }, permission = "QCK.kingdom.war", description = "Declares war against the specified kingdom. Uses 4 power.", usage = "Use /kingdom war [enemy kingdom name]")
    public void kingdomWar(CommandArgs args0) {
        CommandSender sender = args0.getSender();
        String[] args = args0.getArgs();
        Player player = (Player) sender;
        Player player1 = Bukkit.getServer().getPlayer(Kingdom.getKing(args[0]));
        String kingdom = QKPlayer.getKingdom(player);

        if(Kingdom.exists(QKPlayer.getKingdom(player1))) {
            if(QKPlayer.isKing(kingdom, player)) {
                if(Kingdom.setRelationshipStatus(kingdom, args[0], 3) == 1) {
                    Bukkit.broadcastMessage(QCChat.getPhrase(kingdom + "kingdom_is_now_pending_war_with_kingdom") + ChatColor.WHITE + QKPlayer.getKingdom(player1));
                    //Kingdom.setPower(QKPlayer.getKingdom(player), false, 4);
                    player1.sendMessage(ChatColor.GREEN + "The kingdom " + ChatColor.WHITE + kingdom + ChatColor.GREEN + " has declared war against your kingdom. Type " + ChatColor.WHITE + "/kingdom war " + kingdom + ChatColor.GREEN + " to also declare war.");
                } else if(Kingdom.setRelationshipStatus(kingdom, args[0], 3) == 2) {
                    Bukkit.broadcastMessage(QCChat.getPhrase(kingdom + "kingdom_is_now_at_war_with_kingdom") + ChatColor.WHITE + QKPlayer.getKingdom(player1));
                    Kingdom.setPower(QKPlayer.getKingdom(player), false, 4);
                } else {
                    sender.sendMessage(QCChat.getPhrase("failed_to_war_with_kingdom"));
                }
            } else {
                sender.sendMessage(QCChat.getPhrase("no_permission"));
            }
        } else {
            sender.sendMessage(QCChat.getPhrase("specifed_kingdom_does_not_exist"));
        }
    }

    @QCommand(name = "kingdom.neutral", aliases = { "k.neutral" }, permission = "QCK.kingdom.neutral", description = "Removes all relationships between kingdoms, not at war or allied with the specified kingdom.", usage = "Use /kingdom neutral [other kingdom name]")
    public void kingdomNeutral(CommandArgs args0) {
        CommandSender sender = args0.getSender();
        String[] args = args0.getArgs();
        Player player = (Player) sender;
        Player player1 = Bukkit.getServer().getPlayer(Kingdom.getKing(args[0]));
        String kingdom = QKPlayer.getKingdom(player);

        if(Kingdom.exists(QKPlayer.getKingdom(player1))) {
            if(QKPlayer.isKing(kingdom, player)) {
                if(Kingdom.setRelationshipStatus(kingdom, args[0], 1) == 1) {
                    Bukkit.broadcastMessage(QCChat.getPhrase(kingdom + "kingdom_is_pending_neutral_relationship_with_kingdom") + ChatColor.WHITE + QKPlayer.getKingdom(player1));
                    player1.sendMessage(ChatColor.GREEN + "The kingdom " + ChatColor.WHITE + kingdom + ChatColor.GREEN + " is offering neutral relations with your kingdom. Type " + ChatColor.WHITE + "/kingdom neutral " + kingdom + ChatColor.GREEN + " to also make neutral relations.");
                } else if(Kingdom.setRelationshipStatus(kingdom, args[0], 1) == 2) {
                    Bukkit.broadcastMessage(QCChat.getPhrase(kingdom + "kingdom_is_now_neutral_relationship_with_kingdom") + ChatColor.WHITE + QKPlayer.getKingdom(player1));
                } else {
                    sender.sendMessage(QCChat.getPhrase("failed_to_neutral_with_kingdom"));
                }
            } else {
                sender.sendMessage(QCChat.getPhrase("no_permission"));
            }
        } else {
            sender.sendMessage(QCChat.getPhrase("specifed_kingdom_does_not_exist"));
        }
    }

    @QCommand(name = "kingdom.ally", aliases = { "k.ally" }, permission = "QCK.kingdom.ally", description = "Allies with a kingdom, members of allied kingdoms can not hurt each other.", usage = "Use /kingdom ally [ally kingdom name]")
    public void kingdomAlly(CommandArgs args0) {
        CommandSender sender = args0.getSender();
        String[] args = args0.getArgs();
        Player player = (Player) sender;
        Player player1 = Bukkit.getServer().getPlayer(Kingdom.getKing(args[0]));
        String kingdom = QKPlayer.getKingdom(player);

        if(Kingdom.exists(QKPlayer.getKingdom(player1))) {
            if(QKPlayer.isKing(kingdom, player)) {
                if(Kingdom.setRelationshipStatus(kingdom, args[0], 2) == 1) {
                    Bukkit.broadcastMessage(QCChat.getPhrase(kingdom + "kingdom_is_pending_allied_with_kingdom") + ChatColor.WHITE + QKPlayer.getKingdom(player1));
                    player1.sendMessage(ChatColor.GREEN + "The kingdom " + ChatColor.WHITE + kingdom + ChatColor.GREEN + " is offering to become an ally with your kingdom. Type " + ChatColor.WHITE + "/kingdom ally " + kingdom + ChatColor.GREEN + " to accept the offer.");
                } else if(Kingdom.setRelationshipStatus(kingdom, args[0], 2) == 2) {
                    Bukkit.broadcastMessage(QCChat.getPhrase(kingdom + "kingdom_is_now_allied_with_kingdom") + ChatColor.WHITE + QKPlayer.getKingdom(player1));
                } else {
                    sender.sendMessage(QCChat.getPhrase("failed_to_ally_with_kingdom"));
                }
            } else {
                sender.sendMessage(QCChat.getPhrase("no_permission"));
            }
        } else {
            sender.sendMessage(QCChat.getPhrase("specifed_kingdom_does_not_exist"));
        }
    }

    @QCommand(name = "kingdom.join", aliases = { "k.join" }, permission = "QCK.kingdom.join", description = "Joins the specified kingdom, as long as you are allowed to. Gives the kingdom 2 power.", usage = "Use /kingdom join [kingdom name]")
    public void kingdomJoin(CommandArgs args0) {
        CommandSender sender = args0.getSender();
        Player player = (Player) sender;
        String[] args = args0.getArgs();
        if(QKPlayer.kingdom(player)) {
            sender.sendMessage(QCChat.getPhrase("you_are_already_in_a_Kingdom"));
        } else if(Kingdom.isOpen(args[0])) {
            if(QKPlayer.joinKingdom(player, args[0])) {
                sender.sendMessage(QCChat.getPhrase("successfully_joined_kingdom_X") + args[0]);
                Kingdom.setPower(QKPlayer.getKingdom(player), true, 2);
            } else {
                sender.sendMessage(QCChat.getPhrase("failed_join_kingdom"));
            }
        } else if(!Kingdom.isOpen(args[0])) {
            sender.sendMessage(QCChat.getPhrase("kingdom_not_open"));
        } else {
            sender.sendMessage(QCChat.getPhrase("kingdom_not_found"));
        }
    }

    @QCommand(name = "kingdom.leave", aliases = { "k.leave" }, permission = "QCK.kingdom.join", description = "Leaves the kingdom you are in. Takes away 1 power from the kingdom.", usage = "Use /kingdom leave")
    public void kingdomLeave(CommandArgs args0) {
        CommandSender sender = args0.getSender();
        Player player = (Player) sender;
        String[] args = args0.getArgs();
        if(QKPlayer.kingdom(player)) {
            if(QKPlayer.isKing(QKPlayer.getKingdom(player), player)) {
                sender.sendMessage(QCChat.getPhrase("you_are_king_someone_else_must_be_to_leave"));
            } else {
                if(QKPlayer.leaveKingdom(player, QKPlayer.getKingdom(player))) {
                    sender.sendMessage(QCChat.getPhrase("successfully_left_kingdom_X") + QKPlayer.getKingdom(player));
                    Kingdom.setPower(QKPlayer.getKingdom(player), false, 1);
                } else {
                    sender.sendMessage(QCChat.getPhrase("failed_leave_kingdom"));
                }
            }
        } else {
            sender.sendMessage(QCChat.getPhrase("you_must_be_member_kingdom"));
        }
    }

    @QCommand(name = "kingdom.open", aliases = { "k.open" }, permission = "QCK.kingdom.open", description = "Opens the kingdom so that players can join. Doing this gives your kingdom 4 power.", usage = "Use /kingdom open")
    public void kingdomOpen(CommandArgs args0) {
        CommandSender sender = args0.getSender();
        Player player = (Player) sender;
        String[] args = args0.getArgs();
        if(Kingdom.isOpen(QKPlayer.getKingdom(player))) {
            sender.sendMessage(QCChat.getPhrase("kingdom_already_open"));
        } else {
            if(Kingdom.setOpen(QKPlayer.getKingdom(player), true)) {
                sender.sendMessage(QCChat.getPhrase("kingdom_now_open"));
                Kingdom.setPower(QKPlayer.getKingdom(player), true, 4);
            } else {
                sender.sendMessage(QCChat.getPhrase("failed_open_kingdom"));
            }
        }
    }

    @QCommand(name = "kingdom.close", aliases = { "k.close" }, permission = "QCK.kingdom.open", description = "Prevents other players from joining your kingdom unless invited. Doing this removes 3 power.", usage = "Use /kingdom close")
    public void kingdomClose(CommandArgs args0) {
        CommandSender sender = args0.getSender();
        Player player = (Player) sender;
        String[] args = args0.getArgs();
        if(Kingdom.isOpen(QKPlayer.getKingdom(player))) {
            if(Kingdom.setOpen(QKPlayer.getKingdom(player), false)) {
                sender.sendMessage(QCChat.getPhrase("kingdom_now_closed"));
                Kingdom.setPower(QKPlayer.getKingdom(player), false, 3);
            } else {
                sender.sendMessage(QCChat.getPhrase("failed_close_kingdom"));
            }

        } else {
            sender.sendMessage(QCChat.getPhrase("kingdom_already_closed"));
        }
    }

    @QCommand(name = "kingdom.invite", aliases = { "k.invite" }, permission = "QCK.kingdom.invite", description = "Invites a player to your kingdom.", usage = "Use /kingdom invite [playername]")
    public void kingdomInvite(CommandArgs args0) {
        CommandSender sender = args0.getSender();
        Player player = (Player) sender;
        String[] args = args0.getArgs();
        sender.sendMessage(QCChat.getPhrase("feature_unavalible"));
    }
}
