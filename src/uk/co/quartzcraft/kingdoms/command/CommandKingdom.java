package uk.co.quartzcraft.kingdoms.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import uk.co.quartzcraft.core.command.framework.QCommandFramework;
import uk.co.quartzcraft.core.data.QPlayer;
import uk.co.quartzcraft.core.systems.chat.QCChat;
import uk.co.quartzcraft.core.command.framework.QCommand;
import uk.co.quartzcraft.core.command.framework.CommandArgs;
import uk.co.quartzcraft.core.systems.notifications.AlertArgs;
import uk.co.quartzcraft.core.systems.notifications.AlertBuilder;
import uk.co.quartzcraft.kingdoms.QuartzKingdoms;
import uk.co.quartzcraft.kingdoms.data.QKPlayer;
import uk.co.quartzcraft.kingdoms.features.kingdom.Kingdom;
import uk.co.quartzcraft.kingdoms.systems.landclaim.ChunkManager;
import uk.co.quartzcraft.kingdoms.util.KUtil;

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
        args.getSender().sendMessage("config preference settings.world equals:" + plugin.getConfig().getString("settings.world"));
    }

    @QCommand(name = "kingdom.info", aliases = { "k.info" }, permission = "QCK.kingdom.info", description = "Get information about a specified kingdom", usage = "Use /kingdom info [kingdom name]")
    public void kingdomInfo(CommandArgs args) {
        Kingdom kingdom;
        Player player = args.getPlayer();
        QKPlayer qkPlayer = new QKPlayer(player);

        if(args.getArgs().length != 1) {
            if(!qkPlayer.isKingdomMember()) {
                KUtil.sendMsg(args.getPlayer(), QCChat.getPhrase("specify_kingdom_name"));
                return;
            }
            kingdom = qkPlayer.getKingdom();
        } else {
            kingdom = new Kingdom(args.getArgs()[0]);
        }

        if(kingdom.getID() == 0) {
            KUtil.sendMsg(args.getPlayer(), QCChat.getPhrase("kingdom_does_not_exist"));
            return;
        }

        KUtil.sendMsg(args.getPlayer(), QCChat.getPhrase("info_kingdom") + kingdom.getName());

        if(kingdom.getID() != qkPlayer.getKingdom().getID() && qkPlayer.isKingdomMember()) {
            if(kingdom.isPendingAlly(qkPlayer.getKingdom())) {
                KUtil.sendMsg(args.getPlayer(), QCChat.getPhrase("kingdom_is_pending_ally_with_your_kingdom"));
            } else if(kingdom.isPendingEnemy(qkPlayer.getKingdom())) {
                KUtil.sendMsg(args.getPlayer(), QCChat.getPhrase("kingdom_is_pending_war_with_your_kingdom"));
            } else if(kingdom.isEnemy(qkPlayer.getKingdom())) {
                KUtil.sendMsg(args.getPlayer(), QCChat.getPhrase("kingdom_is_at_war_with_your_kingdom"));
            } else if(kingdom.isAlly(qkPlayer.getKingdom())) {
                KUtil.sendMsg(args.getPlayer(), QCChat.getPhrase("kingdom_is_allied_with_your_kingdom"));
            }
        }

        KUtil.sendMsg(args.getPlayer(), QCChat.getPhrase("kingdom_king_is_X") + kingdom.getKing().getQPlayer().getFancyName());
        KUtil.sendMsg(args.getPlayer(), QCChat.getPhrase("kingdom_level_is_X") + kingdom.getLevel());
        KUtil.sendMsg(args.getPlayer(), QCChat.getPhrase("kingdom_power_is_X") + kingdom.getPower());

        if(kingdom.isOpen()) {
            KUtil.sendMsg(args.getPlayer(), QCChat.getPhrase("kingdom_is_open"));
        } else {
            KUtil.sendMsg(args.getPlayer(), QCChat.getPhrase("kingdom_is_closed"));
        }

    }

    @QCommand(name = "kingdom.create", aliases = { "k.create", "create.kingdom" }, permission = "QCK.kingdom.create", description = "Creates a kingdoms with the specified name. This requires 80 power.", usage = "Use /kingdom create [kingdom name]")
    public void kingdomCreate(CommandArgs args) {
        //TODO make it so that you can use other players power. Player who contributes most power becomes king
        CommandSender sender = args.getSender();
        QKPlayer player = new QKPlayer(args.getPlayer());
        String[] args0 = args.getArgs();
        if(args0.length >= 1) {
            if(args0.length >= 2) {
                KUtil.sendMsg(args.getPlayer(), QCChat.getPhrase("kingdom_name_single_word"));
            } else {
                if(player.isKingdomMember()) {
                    KUtil.sendMsg(args.getPlayer(), QCChat.getPhrase("you_are_already_in_a_Kingdom"));
                } else if(player.getPower() >= 80) {
                    String kingdomName = args0[0];
                    Kingdom kingdom = Kingdom.createKingdom(args0[0], player);
                    if(kingdom != null) {
                        KUtil.sendMsg(args.getPlayer(), QCChat.getPhrase("created_kingdom_yes") + ChatColor.WHITE + kingdomName);
                        player.takePower(80);
                    } else {
                        KUtil.sendMsg(args.getPlayer(), QCChat.getPhrase("created_kingdom_no") + ChatColor.WHITE + kingdomName + ChatColor.RED + " A kingdom may already exist with the specified name.");
                    }
                } else {
                    KUtil.sendMsg(args.getPlayer(), QCChat.getPhrase("not_enough_power") + ChatColor.RED + " You require 80 power to create a kingdom!");
                }
            }
        } else {
            KUtil.sendMsg(args.getPlayer(), QCChat.getPhrase("specify_kingdom_name"));
        }
    }

    @QCommand(name = "kingdom.level", aliases = { "k.level" }, permission = "QCK.kingdom.info.level", description = "Find out the level the kingdom is on", usage = "Use /kingdom level")
    public void kingdomLevel(CommandArgs args) {
        Player player = (Player) args.getSender();
        QKPlayer qkPlayer = new QKPlayer(player);
        KUtil.sendMsg(args.getPlayer(), QCChat.getPhrase("your_kingdoms_level_is_X") + qkPlayer.getKingdom().getLevel());
    }

    @QCommand(name = "kingdom.power", aliases = { "k.power" }, permission = "QCK.kingdom.info.power", description = "Find out the power the kingdom has", usage = "Use /kingdom power")
    public void kingdomPower(CommandArgs args) {
        Player player = (Player) args.getSender();
        QKPlayer qkPlayer = new QKPlayer(player);
        KUtil.sendMsg(args.getPlayer(), QCChat.getPhrase("your_kingdoms_power_is_X") + qkPlayer.getKingdom().getPower());
    }

    @QCommand(name = "kingdom.disband", aliases = { "k.disband", "kingdom.delete", "k.delete" }, permission = "QCK.kingdom.disband", description = "Disbands the kingdom you specify. You must be the king.", usage = "Use /kingdom disband [kingdom name]")
    public void kingdomDisband(CommandArgs args0) {
        //TODO confirmation chest UI
        CommandSender sender = args0.getSender();
        QKPlayer player = new QKPlayer(args0.getPlayer());
        Kingdom kingdom = player.getKingdom();
        String[] args = args0.getArgs();
        if(args.length < 1) {
            KUtil.sendMsg(args0.getPlayer(), QCChat.getPhrase("specify_kingdom_name"));
            return;
        }

        if(player.isKing(kingdom) && player.getPlayer().hasPermission("QCK.kingdom.disband")) {
            if(kingdom.delete(player)) {
                KUtil.sendMsg(args0.getPlayer(), QCChat.getPhrase("deleted_kingdom_yes") + ChatColor.WHITE + args[0]);
            } else {
                KUtil.sendMsg(args0.getPlayer(), QCChat.getPhrase("deleted_kingdom_no") + ChatColor.WHITE + player.getKingdom().getName());
            }
        } else {
            KUtil.sendMsg(args0.getPlayer(), QCChat.getPhrase("you_must_be_king"));
        }
    }

    @QCommand(name = "kingdom.knight", aliases = { "k.knight", "knight" }, permission = "QCK.kingdom.promote.knight", description = "Promotes the specified player to a knight of the kingdom", usage = "Use /kingdom knight [playername]")
    public void kingdomKnight(CommandArgs args0) {
        CommandSender sender = args0.getSender();
        String[] args = args0.getArgs();
        QKPlayer psender = new QKPlayer((Player) sender);
        QKPlayer target;
        target = new QKPlayer(new QPlayer(args[0]));

        if(args.length == 1 && target.getID() != 0) {
            Kingdom kingdom = psender.getKingdom();
            if(kingdom.equals(target.getKingdom())) {
                target.setKingdomGroup(4);
                KUtil.sendMsg(args0.getPlayer(), QCChat.getPhrase("successfully_knighted_player"));
                KUtil.sendMsg(target.getPlayer(), QCChat.getPhrase("you_are_now_a_knight"));
            } else {
                KUtil.sendMsg(args0.getPlayer(), QCChat.getPhrase("player_must_be_member_of_your_kingdom"));
            }
        } else {
            KUtil.sendMsg(args0.getPlayer(), QCChat.getPhrase("specify_username"));
        }
    }

    @QCommand(name = "kingdom.noble", aliases = { "k.noble", "noble" }, permission = "QCK.kingdom.promote.noble", description = "Promotes the specified player to a noble of the kingdom", usage = "Use /kingdom noble [playername")
    public void kingdomNoble(CommandArgs args0) {
        CommandSender sender = args0.getSender();
        String[] args = args0.getArgs();
        QKPlayer psender = new QKPlayer((Player) sender);
        QKPlayer target;
        target = new QKPlayer(new QPlayer(args[0]));

        if(args.length == 1 && target.getID() != 0) {
            Kingdom kingdom = psender.getKingdom();
            if(kingdom.equals(target.getKingdom())) {
                target.setKingdomGroup(5);
                KUtil.sendMsg(args0.getPlayer(), QCChat.getPhrase("successfully_noble_player"));
                KUtil.sendMsg(target.getPlayer(), QCChat.getPhrase("you_are_now_a_noble"));
            } else {
                KUtil.sendMsg(args0.getPlayer(), QCChat.getPhrase("player_must_be_member_of_your_kingdom"));
            }
        } else {
            KUtil.sendMsg(args0.getPlayer(), QCChat.getPhrase("specify_username"));
        }
    }

    @QCommand(name = "kingdom.king", aliases = { "k.monarch", "monarch" }, permission = "QCK.kingdom.promote.king", description = "Promotes the specified player to the monarch of the kingdom. Makes the current king a noble", usage = "Use /kingdom king [playername")
    public void kingdomKing(CommandArgs args0) {
        CommandSender sender = args0.getSender();
        String[] args = args0.getArgs();
        QKPlayer psender = new QKPlayer((Player) sender);
        QKPlayer target;
        target = new QKPlayer(new QPlayer(args[0]));

        if(args.length == 1 && target.getID() != 0) {
            Kingdom kingdom = psender.getKingdom();
            if(kingdom.equals(target.getKingdom())) {
                kingdom.setKing(target);
                psender.setKingdomGroup(5);
                KUtil.sendMsg(args0.getPlayer(), QCChat.getPhrase("successfully_king_player"));
                KUtil.sendMsg(target.getPlayer(), QCChat.getPhrase("you_are_now_a_king"));
            } else {
                KUtil.sendMsg(args0.getPlayer(), QCChat.getPhrase("player_must_be_member_of_your_kingdom"));
            }
        } else {
            KUtil.sendMsg(args0.getPlayer(), QCChat.getPhrase("specify_username"));
        }
    }

    @QCommand(name = "kingdom.claim", aliases = { "k.claim" }, permission = "QCK.kingdom.claim", description = "Claims the chunk of land you are standing on for your kingdom. Uses 5 power.", usage = "Use /kingdom claim")
    public void kingdomClaim(CommandArgs args0) {
        CommandSender sender = args0.getSender();
        Player player = (Player) sender;
        QKPlayer qkPlayer = new QKPlayer(player);
        Chunk chunk = player.getLocation().getChunk();
        World world = player.getWorld();
        String WorldName = world.getName();
        String AWorldName = QuartzKingdoms.plugin.getConfig().getString("settings.world");

        if(!qkPlayer.isKing()) {
            KUtil.sendMsg(args0.getPlayer(), QCChat.getPhrase("you_must_be_king"));
            return;
        }

        if(WorldName.equalsIgnoreCase(AWorldName)) {
            if(ChunkManager.isClaimed(chunk)) {
                KUtil.sendMsg(args0.getPlayer(), QCChat.getPhrase("this_chunk_is_already_claimed"));
            } else {
                ChunkManager.claimChunk(qkPlayer.getKingdom(), player);
                KUtil.sendMsg(args0.getPlayer(), QCChat.getPhrase("chunk_claimed_for_kingdom_yes"));
                qkPlayer.getKingdom().takePower(5);
            }
        } else {
            KUtil.sendMsg(args0.getPlayer(), QCChat.getPhrase("you_can_not_claim_land_in_this_world"));
        }

    }

    @QCommand(name = "kingdom.unclaim", aliases = { "k.unclaim" }, permission = "QCK.kingdom.claim", description = "Unclaims the chunk of land you are standing on.", usage = "Use /kingdom unclaim")
    public void kingdomUnClaim(CommandArgs args0) {
        CommandSender sender = args0.getSender();
        Player player = (Player) sender;
        QKPlayer qkPlayer = new QKPlayer(player);
        Chunk chunk = player.getLocation().getChunk();

        if(ChunkManager.isClaimed(chunk) && !qkPlayer.isKing(ChunkManager.getKingdomOwner(chunk))) {
            KUtil.sendMsg(args0.getPlayer(), QCChat.getPhrase("you_must_be_king"));
            return;
        }

        if(ChunkManager.isClaimed(chunk) && ChunkManager.getKingdomOwner(chunk).getID() == qkPlayer.getKingdom().getID()) {
            ChunkManager.unClaimChunk(qkPlayer.getKingdom(), player);
            KUtil.sendMsg(args0.getPlayer(), QCChat.getPhrase("chunk_unclaimed_for_kingdom_yes"));
        } else {
            KUtil.sendMsg(args0.getPlayer(), QCChat.getPhrase("this_chunk_is_not_claimed"));
        }
    }

    @QCommand(name = "kingdom.war", aliases = { "k.war" }, permission = "QCK.kingdom.war", description = "Declares war against the specified kingdom. Uses 5 power.", usage = "Use /kingdom war [enemy kingdom name]")
    public void kingdomWar(CommandArgs args0) {
        CommandSender sender = args0.getSender();
        String[] args = args0.getArgs();
        Player sendplayer = (Player) sender;
        QKPlayer player = new QKPlayer(sendplayer);
        Kingdom kingdom1 = player.getKingdom();
        Kingdom kingdom2 = new Kingdom(args[0]);
        QKPlayer player1 = kingdom2.getKing();

        if(!player.isKing(kingdom1)) {
            KUtil.sendMsg(args0.getPlayer(), QCChat.getPhrase("no_permission"));
            return;
        }
        if(!Kingdom.exists(args[0])) {
            KUtil.sendMsg(args0.getPlayer(), QCChat.getPhrase("kingdom_does_not_exist"));
            return;
        }

        int suc = kingdom1.setAtWar(kingdom2);
        if(suc == 33) {
            KUtil.broadcastMsg(kingdom1.getName() + QCChat.getPhrase("kingdom_is_now_pending_war_with_kingdom") + ChatColor.GOLD + kingdom2.getName());
            player1.getQPlayer().alert(new AlertBuilder().setType("kingdom_war")
                    .setMessage("The kingdom " + kingdom1.getName() + " has declared war against you. Type the command /kingdom war " + kingdom1.getName() + " to confirm that you are at war.")
                    .setArgs(new AlertArgs().setInt("kingdom_id", kingdom1.getID())));
        } else if(suc == 3) {
            KUtil.broadcastMsg(kingdom1.getName() + QCChat.getPhrase("kingdom_is_now_at_war_with_kingdom") + ChatColor.GOLD + kingdom2.getName());
            kingdom1.takePower(5);
            kingdom2.takePower(5);
        } else {
            KUtil.sendMsg(args0.getPlayer(), QCChat.getPhrase("kingdom_is_now_at_war_with_kingdom"));
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
            KUtil.sendMsg(args0.getPlayer(), QCChat.getPhrase("no_permission"));
            return;
        }
        if(!Kingdom.exists(args[0])) {
            KUtil.sendMsg(args0.getPlayer(), QCChat.getPhrase("kingdom_does_not_exist"));
            return;
        }

        kingdom1.setAtNeutral(kingdom2);
        KUtil.broadcastMsg(QCChat.getPhrase(kingdom1.getName() + "kingdom_is_now_neutral_relationship_with_kingdom") + ChatColor.GOLD + kingdom2.getName());
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
            KUtil.sendMsg(args0.getPlayer(), QCChat.getPhrase("no_permission"));
            return;
        }
        if(!Kingdom.exists(args[0])) {
            KUtil.sendMsg(args0.getPlayer(), QCChat.getPhrase("kingdom_does_not_exist"));
            return;
        }

        int suc = kingdom1.setAtAlly(kingdom2);
        if(suc == 22) {
            KUtil.broadcastMsg(kingdom1.getName() + QCChat.getPhrase("kingdom_is_pending_allied_with_kingdom") + ChatColor.GOLD + kingdom2.getName());
            player1.getQPlayer().alert(new AlertBuilder().setType("kingdom_ally")
                    .setMessage("The kingdom " + kingdom1.getName() + " has proposed becoming an ally. Type the command /kingdom ally " + kingdom1.getName() + " to confirm that you are allies.")
                    .setArgs(new AlertArgs().setInt("kingdom_id", kingdom1.getID())));
        } else if(suc == 2) {
            KUtil.broadcastMsg(kingdom1.getName() + QCChat.getPhrase("kingdom_is_now_allied_with_kingdom") + ChatColor.GOLD + kingdom2.getName());
        } else {
            KUtil.sendMsg(args0.getPlayer(), QCChat.getPhrase("failed_to_ally_with_kingdom"));
        }
    }

    @QCommand(name = "kingdom.join", aliases = { "k.join", "join" }, permission = "QCK.kingdom.join", description = "Joins the specified kingdom, as long as you are allowed to. Gives the kingdom 2 power.", usage = "Use /kingdom join [kingdom name]")
    public void kingdomJoin(CommandArgs args0) {
        CommandSender sender = args0.getSender();
        String[] args = args0.getArgs();
        Player player = (Player) sender;
        QKPlayer qkPlayer = new QKPlayer(player);
        Kingdom kingdom = new Kingdom(args[0]);
        if(qkPlayer.isKingdomMember()) {
            KUtil.sendMsg(args0.getPlayer(), QCChat.getPhrase("you_are_already_in_a_Kingdom"));
        } else if(kingdom.getID() == 0) {
            KUtil.sendMsg(args0.getPlayer(), QCChat.getPhrase("kingdom_not_found"));
        } else if(!kingdom.isOpen()) {
            KUtil.sendMsg(args0.getPlayer(), QCChat.getPhrase("kingdom_not_open"));
        } else {
            qkPlayer.setKingdom(kingdom);
            qkPlayer.setKingdomGroup(2);
            kingdom.addPower(2);
            KUtil.sendMsg(args0.getPlayer(), QCChat.getPhrase("successfully_joined_kingdom_X") + kingdom.getName());
        }
    }

    @QCommand(name = "kingdom.leave", aliases = { "k.leave", "leave" }, permission = "QCK.kingdom.leave", description = "Leaves the kingdom you are in. Takes away 2 power from the kingdom.", usage = "Use /kingdom leave")
    public void kingdomLeave(CommandArgs args0) {
        CommandSender sender = args0.getSender();
        String[] args = args0.getArgs();
        Player player = (Player) sender;
        QKPlayer qkPlayer = new QKPlayer(player);

        if(args.length != 1) {
            KUtil.sendMsg(args0.getPlayer(), QCChat.getPhrase("specify_kingdom_name"));
            return;
        }

        Kingdom kingdom = new Kingdom(args[0]);

        if(qkPlayer.isKingdomMember()) {
            qkPlayer.setKingdom(null);
            qkPlayer.setKingdomGroup(1);
            kingdom.takePower(2);
            KUtil.sendMsg(args0.getPlayer(), QCChat.getPhrase("successfully_left_kingdom_X") + kingdom.getName());
        } else {
            KUtil.sendMsg(args0.getPlayer(), QCChat.getPhrase("you_must_be_member_kingdom"));
        }
    }

    @QCommand(name = "kingdom.open", aliases = { "k.open" }, permission = "QCK.kingdom.open", description = "Opens the kingdom so that players can join. Doing this gives your kingdom 3 power.", usage = "Use /kingdom open")
    public void kingdomOpen(CommandArgs args0) {
        CommandSender sender = args0.getSender();
        String[] args = args0.getArgs();
        QKPlayer player = new QKPlayer(args0.getPlayer());
        Kingdom kingdom = player.getKingdom();
        if(kingdom.isOpen()) {
            KUtil.sendMsg(args0.getPlayer(), QCChat.getPhrase("kingdom_already_open"));
        } else {
            kingdom.setOpen(true);
            KUtil.sendMsg(args0.getPlayer(), QCChat.getPhrase("kingdom_now_open"));
            kingdom.addPower(3);
        }
    }

    @QCommand(name = "kingdom.close", aliases = { "k.close" }, permission = "QCK.kingdom.open", description = "Prevents other players from joining your kingdom unless invited. Doing this takes 4 power.", usage = "Use /kingdom close")
    public void kingdomClose(CommandArgs args0) {
        CommandSender sender = args0.getSender();
        String[] args = args0.getArgs();
        QKPlayer player = new QKPlayer(args0.getPlayer());
        Kingdom kingdom = player.getKingdom();
        if(kingdom.isOpen()) {
            kingdom.setOpen(false);
            KUtil.sendMsg(args0.getPlayer(), QCChat.getPhrase("kingdom_now_closed"));
            kingdom.takePower(4);
        } else {
            KUtil.sendMsg(args0.getPlayer(), QCChat.getPhrase("kingdom_already_closed"));
        }
    }

    @QCommand(name = "kingdom.invite", aliases = { "k.invite"}, permission = "QCK.kingdom.invite", description = "Invites a player to your kingdom.", usage = "Use /kingdom invite [playername]")
    public void kingdomInvite(CommandArgs args0) {
        CommandSender sender = args0.getSender();
        Player player = (Player) sender;
        QKPlayer qkPlayer = new QKPlayer(player);
        String[] args = args0.getArgs();
        if(qkPlayer.isKingdomMember()) {
            if(qkPlayer.getKingdom().invitePlayer(new QKPlayer(new QPlayer(args[0])))) {
                KUtil.sendMsg(args0.getPlayer(), QCChat.getPhrase("invited_player_to_kingdom"));
            } else {
                KUtil.sendMsg(args0.getPlayer(), QCChat.getPhrase("failed_to_invited_player_to_kingdom"));
            }

        } else {
            KUtil.sendMsg(args0.getPlayer(), QCChat.getPhrase("you_must_be_member_kingdom"));
        }
    }

    @QCommand(name = "kingdom.accept", aliases = { "k.accept"}, permission = "QCK.kingdom.accept", description = "Accepts an invitation for membership of a kingdom", usage = "Use /kingdom accept [kingdomname]")
     public void kingdomAccept(CommandArgs args0) {
        CommandSender sender = args0.getSender();
        Player player = (Player) sender;
        QKPlayer qkPlayer = new QKPlayer(player);
        String[] args = args0.getArgs();
        Kingdom kingdom = new Kingdom(args[0]);
        if(kingdom.getID() == 0) {
            KUtil.sendMsg(args0.getPlayer(), QCChat.getPhrase("kingdom_not_found"));
        }

        if(qkPlayer.hasInvitation(kingdom.getID())) {
            if(qkPlayer.isKingdomMember()) {
                KUtil.sendMsg(args0.getPlayer(), QCChat.getPhrase("you_are_already_in_a_Kingdom"));
            } else {
                qkPlayer.setKingdom(kingdom);
                qkPlayer.setKingdomGroup(2);
                kingdom.addPower(2);
                KUtil.sendMsg(args0.getPlayer(), QCChat.getPhrase("successfully_joined_kingdom_X") + kingdom.getName());
                qkPlayer.getKingdom().removeInvite(qkPlayer.getID());
            }
        } else {
            KUtil.sendMsg(args0.getPlayer(), QCChat.getPhrase("you_have_not_been_invited_to_this_kingdom"));
        }
    }

    @QCommand(name = "kingdom.sethome", aliases = { "k.sethome"}, permission = "QCK.kingdom.sethome", description = "Sets the kingdom home point", usage = "Use /kingdom sethome")
    public void kingdomSetHome(CommandArgs args0) {
        QKPlayer qkPlayer = new QKPlayer(args0.getPlayer());

        if(qkPlayer.isKingdomMember() && qkPlayer.isKing()) {
            Kingdom kingdom = qkPlayer.getKingdom();
            if(kingdom.setHome(args0.getPlayer().getLocation()))
            KUtil.sendMsg(args0.getPlayer(), QCChat.getPhrase("successfully_set_kingdom_home"));
        } else {
            KUtil.sendMsg(args0.getPlayer(), QCChat.getPhrase("you_must_be_king"));
        }
    }

    @QCommand(name = "kingdom.home", aliases = { "k.home"}, permission = "QCK.kingdom.home", description = "Teleports to the kingdoms home", usage = "Use /kingdom home")
    public void kingdomHome(CommandArgs args0) {
        QKPlayer qkPlayer = new QKPlayer(args0.getPlayer());

        //TODO check if the player should be able to teleport from their current location
        if(qkPlayer.isKingdomMember()) {
            Kingdom kingdom = qkPlayer.getKingdom();
            if(kingdom.getHome() != null) {
                qkPlayer.getPlayer().teleport(kingdom.getHome());
                KUtil.sendMsg(args0.getPlayer(), QCChat.getPhrase("teleported_to_kingdom_home"));
            } else {
                KUtil.sendMsg(args0.getPlayer(), QCChat.getPhrase("there_is_no_kingdom_home"));
            }
        } else {
            KUtil.sendMsg(args0.getPlayer(), QCChat.getPhrase("you_must_be_member_kingdom"));
        }
    }
}
