package uk.co.quartzcraft.kingdoms.command;

import org.bukkit.plugin.Plugin;
import uk.co.quartzcraft.core.command.framework.CommandArgs;
import uk.co.quartzcraft.core.command.framework.QCommand;
import uk.co.quartzcraft.core.command.framework.QCommandFramework;
import uk.co.quartzcraft.core.systems.chat.QCChat;
import uk.co.quartzcraft.kingdoms.QuartzKingdoms;

public class CommandClan {

    private static QuartzKingdoms plugin;
    private static Plugin core;
    private static QCommandFramework framework;

    public CommandClan(QuartzKingdoms plugin, Plugin core) {
        this.plugin = plugin;
        this.core = core;
        framework = new QCommandFramework(this.plugin);
        framework.registerCommands(this);
    }

    @QCommand(name = "clan", aliases = { "cl" }, permission = "QCK.clan", description = "The main clan command", usage = "Use /clan [subcommand]")
    public void clan(CommandArgs args) {
        args.getSender().sendMessage(QCChat.getPhrase("Specify_Subcommand"));
    }
}
