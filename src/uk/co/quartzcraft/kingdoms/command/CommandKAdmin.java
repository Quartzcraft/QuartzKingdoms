package uk.co.quartzcraft.kingdoms.command;

import org.bukkit.plugin.Plugin;
import uk.co.quartzcraft.core.command.framework.CommandArgs;
import uk.co.quartzcraft.core.command.framework.QCommand;
import uk.co.quartzcraft.core.command.framework.QCommandFramework;
import uk.co.quartzcraft.core.systems.chat.QCChat;
import uk.co.quartzcraft.kingdoms.QuartzKingdoms;

public class CommandKAdmin {

    private static QuartzKingdoms plugin;
    private static Plugin core;
    private static QCommandFramework framework;

    public CommandKAdmin(QuartzKingdoms plugin, Plugin core) {
        this.plugin = plugin;
        this.core = core;
        framework = new QCommandFramework(this.plugin);
        framework.registerCommands(this);
    }

    @QCommand(name = "kadmin", aliases = { "ka" }, permission = "QCK.kadmin", description = "The main kingdoms administration/moderation command", usage = "Use /kadmin [subcommand]")
    public void kadmin(CommandArgs args) {
        args.getSender().sendMessage(QCChat.getPhrase("Specify_Subcommand"));
    }
}
