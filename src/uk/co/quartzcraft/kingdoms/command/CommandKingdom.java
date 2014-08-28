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
import uk.co.quartzcraft.kingdoms.features.kingdom.Kingdom;
import uk.co.quartzcraft.kingdoms.systems.landclaim.ChunkManager;

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
        QKPlayer player = new QKPlayer(args.getPlayer());
        String[] args0 = args.getArgs();
        if(args0.length >= 1) {
            if(args0.length >= 2) {
                player.getQPlayer().sendMessage(QCChat.getPhrase("kingdom_name_single_word"));
            } else {
                if(player.kingdomMember()) {
                    player.getQPlayer().sendMessage(QCChat.getPhrase("you_are_already_in_a_Kingdom"));
                } else {
                    String kingdomName = args0[0];
                    Kingdom kingdom = Kingdom.createKingdom(args0[0], player);
                    if(kingdom != null) {
                        player.getQPlayer().sendMessage(QCChat.getPhrase("created_kingdom_yes") + ChatColor.WHITE + kingdomName);
                    } else {
                        player.getQPlayer().sendMessage(QCChat.getPhrase("created_kingdom_no") + ChatColor.WHITE + kingdomName);
                    }
                }
            }
        } else {
            player.getQPlayer().sendMessage(QCChat.getPhrase("specify_kingdom_name"));
        }
    }

    @QCommand(name = "kingdom.level", aliases = { "k.level" }, permission = "QCK.kingdom.info.level", description = "Find out the level the kingdom is on", usage = "Use /kingdom level")
    public void kingdomLevel(CommandArgs args) {
        Player player = (Player) args.getSender();
        QKPlayer qkPlayer = new QKPlayer(player);
        args.getSender().sendMessage(QCChat.getPhrase("your_kingdoms_level_is_X") + qkPlayer.getKingdom().getLevel());
    }

    @QCommand(name = "kingdom.power", aliases = { "k.power" }, permission = "QCK.kingdom.info.power", description = "Find out the power the kingdom has", usage = "Use /kingdom power")
    public void kingdomPower(CommandArgs args) {
        Player player = (Player) args.getSender();
        QKPlayer qkPlayer = new QKPlayer(player);
        args.getSender().sendMessage(QCChat.getPhrase("your_kingdoms_power_is_X") + qkPlayer.getKingdom().getPower());
    }

    @QCommand(name = "kingdom.disband", aliases = { "k.disband", "disband" }, permission = "QCK.kingdom.disband", description = "Disbands the kingdom you specify. You must be the king.", usage = "Use /kingdom disband [kingdom name]")
    public void kingdomDisband(CommandArgs args0) {
        //TODO confirmation chest UI
        CommandSender sender = args0.getSender();
        QKPlayer player = new QKPlayer(args0.getPlayer());
        Kingdom kingdom = player.getKingdom();
        String[] args = args0.getArgs();
        if(kingdom.getKing().equals(player.getKingdom()) && player.isKing(kingdom)) {
            if(kingdom.delete(player)) {
                player.getQPlayer().sendMessage(QCChat.getPhrase("disbanded_kingdom_yes") + ChatColor.WHITE + player.getKingdom().getName());
            } else {
                player.getQPlayer().sendMessage(QCChat.getPhrase("disbanded_kingdom_no") + ChatColor.WHITE + player.getKingdom().getName());
            }
        } else {
            player.getQPlayer().sendMessage(QCChat.getPhrase("you_must_be_king_to_delete_kingdom"));
        }
    }

    @QCommand(name = "kingdom.knight", aliases = { "k.knight", "knight" }, permission = "QCK.kingdom.promote.knight", description = "Promotes the specified player to a knight of the kingdom", usage = "Use /kingdom knight [playername]")
    public void kingdomKnight(CommandArgs args0) {
        CommandSender sender = args0.getSender();
        String[] args = args0.getArgs();
        QKPlayer psender = new QKPlayer((Player) sender);
        QKPlayer target = new QKPlayer(Bukkit.getPlayer(args[0]));

        if(args.length >= 2) {
            Kingdom kingdom = psender.getKingdom();
            if(kingdom.equals(target.getKingdom())) {
                target.setKingdomGroup(4);
                sender.sendMessage(QCChat.getPhrase("successfully_knighted_player"));
                target.getQPlayer().sendMessage(QCChat.getPhrase("you_are_now_a_knight"));
            } else {
                sender.sendMessage(QCChat.getPhrase("no_permission"));
            }
        } else {
            sender.sendMessage(QCChat.getPhrase("specify_username"));
        }
    }

    @QCommand(name = "kingdom.noble", aliases = { "k.noble", "noble" }, permission = "QCK.kingdom.promote.noble", description = "Promotes the specified player to a noble of the kingdom", usage = "Use /kingdom noble [playername")
    public void kingdomNoble(CommandArgs args0) {
        CommandSender sender = args0.getSender();
        String[] args = args0.getArgs();
        QKPlayer psender = new QKPlayer((Player) sender);
        QKPlayer target = new QKPlayer(Bukkit.getPlayer(args[0]));

        if(args.length >= 2) {
            Kingdom kingdom = psender.getKingdom();
            if(kingdom.equals(target.getKingdom())) {
                target.setKingdomGroup(5);
                sender.sendMessage(QCChat.getPhrase("successfully_noble_player"));
                target.getQPlayer().sendMessage(QCChat.getPhrase("you_are_now_a_noble"));
            } else {
                sender.sendMessage(QCChat.getPhrase("no_permission"));
            }
        } else {
            sender.sendMessage(QCChat.getPhrase("specify_username"));
        }
    }

    @QCommand(name = "kingdom.king", aliases = { "k.king", "king" }, permission = "QCK.kingdom.promote.king", description = "Promotes the specified player to the king of the kingdom", usage = "Use /kingdom king [playername")
    public void kingdomKing(CommandArgs args0) {
        CommandSender sender = args0.getSender();
        String[] args = args0.getArgs();
        QKPlayer psender = new QKPlayer((Player) sender);
        QKPlayer target = new QKPlayer(Bukkit.getPlayer(args[0]));

        if(args.length >= 2) {
            Kingdom kingdom = psender.getKingdom();
            if(kingdom.equals(target.getKingdom())) {
                target.setKingdomGroup(6);
                sender.sendMessage(QCChat.getPhrase("successfully_king_player"));
                target.getQPlayer().sendMessage(QCChat.getPhrase("you_are_now_a_king"));
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
        Player sendplayer = (Player) sender;
        QKPlayer player = new QKPlayer(sendplayer);
        Kingdom kingdom1 = player.getKingdom();
        Kingdom kingdom2 = new Kingdom(args[0]);
        QKPlayer player1 = kingdom2.getKing();

        if(!player.isKing(kingdom1)) {
            sender.sendMessage(QCChat.getPhrase("no_permission"));
            return;
        }
        if(!Kingdom.exists(args[0])) {
            sender.sendMessage(QCChat.getPhrase("kingdom_does_not_exist"));
            return;
        }

        int suc = kingdom1.setAtAlly(kingdom2);
        if(suc == 33) {
            Bukkit.broadcastMessage(QCChat.getPhrase(kingdom1.getName() + "kingdom_is_now_pending_war_with_kingdom") + ChatColor.WHITE + kingdom2.getName());
            if(player1.getQPlayer().getPlayer().isOnline()) {
                player1.getQPlayer().sendMessage(ChatColor.GREEN + "The kingdom " + ChatColor.WHITE + kingdom1.getName() + ChatColor.GREEN + " has declared war against your kingdom. Type " + ChatColor.WHITE + "/kingdom war " + kingdom1.getName() + ChatColor.GREEN + " to also declare war.");
            }
        } else if(suc == 3) {
            Bukkit.broadcastMessage(QCChat.getPhrase(kingdom1.getName() + "kingdom_is_now_at_war_with_kingdom") + ChatColor.WHITE + kingdom2.getName());
        } else {
            sender.sendMessage(QCChat.getPhrase("kingdom_is_now_at_war_with_kingdom"));
        }
    }

    @QCommand(name = "kingdom.neutral", aliases = { "k.neutral" }, permission = "QCK.kingdom.neutral", description = "Removes all relationships between kingdoms, not at war or allied with the specified kingdom.", usage = "Use /kingdom neutral [other kingdom name]")
    public void kingdomNeutral(CommandArgs args0) {
        CommandSender sender = args0.getSender();
        String[] args = args0.getArgs();
        Player sendplayer = (Player) sender;
        QKPlayer player = new QKPlayer(sendplayer);
        Kingdom kingdom1 = player.getKingdom();
        Kingdom kingdom2 = new Kingdom(args[0]);
        QKPlayer player1 = kingdom2.getKing();

        if(!player.isKing(kingdom1)) {
            sender.sendMessage(QCChat.getPhrase("no_permission"));
            return;
        }
        if(!Kingdom.exists(args[0])) {
            sender.sendMessage(QCChat.getPhrase("kingdom_does_not_exist"));
            return;
        }

        kingdom1.setAtNeutral(kingdom2);
        Bukkit.broadcastMessage(QCChat.getPhrase(kingdom1.getName() + "kingdom_is_now_neutral_relationship_with_kingdom") + ChatColor.WHITE + kingdom2.getName());
    }

    @QCommand(name = "kingdom.ally", aliases = { "k.ally" }, permission = "QCK.kingdom.ally", description = "Allies with a kingdom, members of allied kingdoms can not hurt each other.", usage = "Use /kingdom ally [ally kingdom name]")
    public void kingdomAlly(CommandArgs args0) {
        CommandSender sender = args0.getSender();
        String[] args = args0.getArgs();
        Player sendplayer = (Player) sender;
        QKPlayer player = new QKPlayer(sendplayer);
        Kingdom kingdom1 = player.getKingdom();
        Kingdom kingdom2 = new Kingdom(args[0]);
        QKPlayer player1 = kingdom2.getKing();

        if(!player.isKing(kingdom1)) {
            sender.sendMessage(QCChat.getPhrase("no_permission"));
            return;
        }
        if(!Kingdom.exists(args[0])) {
            sender.sendMessage(QCChat.getPhrase("kingdom_does_not_exist"));
            return;
        }

        int suc = kingdom1.setAtAlly(kingdom2);
        if(suc == 22) {
            Bukkit.broadcastMessage(QCChat.getPhrase(kingdom1.getName() + "kingdom_is_pending_allied_with_kingdom") + ChatColor.WHITE + kingdom2.getName());
            if(player1.getQPlayer().getPlayer().isOnline()) {
                player1.getQPlayer().sendMessage(ChatColor.GREEN + "The kingdom " + ChatColor.WHITE + kingdom1.getName() + ChatColor.GREEN + " is offering to become an ally with your kingdom. Type " + ChatColor.WHITE + "/kingdom ally " + kingdom1.getName() + ChatColor.GREEN + " to accept the offer.");
            }
        } else if(suc == 2) {
            Bukkit.broadcastMessage(QCChat.getPhrase(kingdom1.getName() + "kingdom_is_now_allied_with_kingdom") + ChatColor.WHITE + kingdom2.getName());
        } else {
            sender.sendMessage(QCChat.getPhrase("failed_to_ally_with_kingdom"));
        }
    }

    @QCommand(name = "kingdom.join", aliases = { "k.join" }, permission = "QCK.kingdom.join", description = "Joins the specified kingdom, as long as you are allowed to. Gives the kingdom 2 power.", usage = "Use /kingdom join [kingdom name]")
    public void kingdomJoin(CommandArgs args0) {
        CommandSender sender = args0.getSender();
        String[] args = args0.getArgs();
        Player player = (Player) sender;
        QKPlayer qkPlayer = new QKPlayer(player);
        Kingdom kingdom = new Kingdom(args[0]);
        if(qkPlayer.kingdomMember()) {
            sender.sendMessage(QCChat.getPhrase("you_are_already_in_a_Kingdom"));
        } else if(kingdom.isOpen()) {
            qkPlayer.setKingdom(kingdom);
            kingdom.addPower(2);
            sender.sendMessage(QCChat.getPhrase("successfully_joined_kingdom_X") + kingdom.getName());
        } else if(kingdom.isOpen()) {
            sender.sendMessage(QCChat.getPhrase("kingdom_not_open"));
        } else {
            sender.sendMessage(QCChat.getPhrase("kingdom_not_found"));
        }
    }

    @QCommand(name = "kingdom.leave", aliases = { "k.leave" }, permission = "QCK.kingdom.join", description = "Leaves the kingdom you are in. Takes away 1 power from the kingdom.", usage = "Use /kingdom leave")
    public void kingdomLeave(CommandArgs args0) {
        CommandSender sender = args0.getSender();
        String[] args = args0.getArgs();
        Player player = (Player) sender;
        QKPlayer qkPlayer = new QKPlayer(player);
        Kingdom kingdom = new Kingdom(args[0]);
        if(qkPlayer.kingdomMember()) {
            qkPlayer.setKingdom(null);
            kingdom.takePower(1);
            sender.sendMessage(QCChat.getPhrase("successfully_left_kingdom_X") + kingdom.getName());
        } else {
            sender.sendMessage(QCChat.getPhrase("you_must_be_member_kingdom"));
        }
    }

    @QCommand(name = "kingdom.open", aliases = { "k.open" }, permission = "QCK.kingdom.open", description = "Opens the kingdom so that players can join. Doing this gives your kingdom 4 power.", usage = "Use /kingdom open")
    public void kingdomOpen(CommandArgs args0) {
        CommandSender sender = args0.getSender();
        String[] args = args0.getArgs();
        QKPlayer player = new QKPlayer(Bukkit.getPlayer(sender.getName()));
        Kingdom kingdom = player.getKingdom();
        if(kingdom.isOpen()) {
            sender.sendMessage(QCChat.getPhrase("kingdom_already_open"));
        } else {
            kingdom.setOpen(true);
            sender.sendMessage(QCChat.getPhrase("kingdom_now_open"));
            kingdom.addPower(4);
        }
    }

    @QCommand(name = "kingdom.close", aliases = { "k.close" }, permission = "QCK.kingdom.open", description = "Prevents other players from joining your kingdom unless invited. Doing this removes 3 power.", usage = "Use /kingdom close")
    public void kingdomClose(CommandArgs args0) {
        CommandSender sender = args0.getSender();
        String[] args = args0.getArgs();
        QKPlayer player = new QKPlayer(Bukkit.getPlayer(sender.getName()));
        Kingdom kingdom = player.getKingdom();
        if(kingdom.isOpen()) {
            kingdom.setOpen(true);
            sender.sendMessage(QCChat.getPhrase("kingdom_now_closed"));
            kingdom.takePower(3);
        } else {
            sender.sendMessage(QCChat.getPhrase("kingdom_already_closed"));
        }
    }

    @QCommand(name = "kingdom.invite", aliases = { "k.invite", "invite" }, permission = "QCK.kingdom.invite", description = "Invites a player to your kingdom.", usage = "Use /kingdom invite [playername]")
    public void kingdomInvite(CommandArgs args0) {
        CommandSender sender = args0.getSender();
        Player player = (Player) sender;
        QKPlayer qkPlayer = new QKPlayer(player);
        String[] args = args0.getArgs();
        if(qkPlayer.kingdomMember()) {
            qkPlayer.getKingdom().invitePlayer(new QKPlayer(Bukkit.getPlayer(args[0])));
            sender.sendMessage(QCChat.getPhrase("invited_player_to_kingdom"));
        } else {
            sender.sendMessage(QCChat.getPhrase("you_must_be_member_kingdom"));
        }
    }

    @QCommand(name = "kingdom.accept", aliases = { "k.accept", "accept" }, permission = "QCK.kingdom.accept", description = "Accepts an invitation for membership of a kingdom", usage = "Use /kingdom accept")
    public void kingdomAccept(CommandArgs args0) {
        CommandSender sender = args0.getSender();
        Player player = (Player) sender;
        QKPlayer qkPlayer = new QKPlayer(player);
        String[] args = args0.getArgs();
        if(qkPlayer.kingdomMember()) {
            sender.sendMessage(QCChat.getPhrase("you_are_already_in_a_Kingdom"));
        } else {
            qkPlayer.setKingdom(null); //TODO Fix this
            sender.sendMessage(QCChat.getPhrase("you_must_be_member_kingdom"));
        }
    }
}
