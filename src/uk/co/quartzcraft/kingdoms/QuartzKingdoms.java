package uk.co.quartzcraft.kingdoms;

import java.sql.Connection;
import java.util.Arrays;
import java.util.logging.Logger;

import com.sun.tools.javac.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import uk.co.quartzcraft.core.QuartzCore;
import uk.co.quartzcraft.core.systems.chat.QCChat;
import uk.co.quartzcraft.core.command.framework.QCommandFramework;
import uk.co.quartzcraft.core.database.MySQL;
import uk.co.quartzcraft.core.command.framework.QCommand;
import uk.co.quartzcraft.core.systems.config.QCConfig;
import uk.co.quartzcraft.kingdoms.command.*;
import uk.co.quartzcraft.kingdoms.listeners.*; 

public class QuartzKingdoms extends JavaPlugin {
	
	public static Plugin plugin;
    public static Plugin core;

    public static QCConfig config;
	
	public final Logger logg = Logger.getLogger("Minecraft");
    public static final Logger log = Logger.getLogger("Minecraft");
	
	public static String releaseVersion = QuartzCore.version;
    public static String version;

	public static Connection DBKing = null;
	public static MySQL MySQLking = null;

    public QCommandFramework commandFramework;
	
	@Override
	public void onDisable() {
				
    	//Shutdown notice
		log.info("[QK] The QuartzKingdoms Plugin has been disabled!");
	}
	
	@Override
	public void onEnable() {

        version = this.getDescription().getVersion();
        plugin = this;
        core = QuartzCore.plugin;
		
		log.info("[QC] Running plugin configuration");
		this.saveDefaultConfig();
		
		String SQLKingHost = this.getConfig().getString("database.kingdoms.host");
		String SQLKingDatabase = this.getConfig().getString("database.kingdoms.database");
		String SQLKingUser = this.getConfig().getString("database.kingdoms.username");
		String SQLKingPassword = this.getConfig().getString("database.kingdoms.password");
		MySQLking = new MySQL(plugin, SQLKingHost, "3306", SQLKingDatabase, SQLKingUser, SQLKingPassword);
		
		//Phrases
		log.info("[QK][STARTUP]Creating Phrases");

        QCChat.addPhrase("kingdom_name_single_word", "&cA kingdoms name may only be a single word!");
        QCChat.addPhrase("created_kingdom_yes", "&aSuccessfully created kingdom: ");
        QCChat.addPhrase("created_kingdom_no", "&cFailed to create kingdom: ");
        QCChat.addPhrase("deleted_kingdom_yes", "&aSuccessfully deleted kingdom: ");
        QCChat.addPhrase("deleted_kingdom_no", "&cFailed to delete kingdom: ");
        QCChat.addPhrase("specify_kingdom_name", "&cPlease specify a name!");
        QCChat.addPhrase("kingdomname_already_used", "&cAnother kingdom is using that name! &aPlease pick another name");
        QCChat.addPhrase("kingdom_does_not_exist", "&cThe specified kingdom does not exist!");
        QCChat.addPhrase("info_kingdom", "&bInfo on Kingdom: ");
        QCChat.addPhrase("your_kingdoms_level_is_X", "&ayour kingdoms level is &b");
        QCChat.addPhrase("your_kingdoms_power_is_X", "&aYour kingdoms power is &b");

        QCChat.addPhrase("you_can_not_claim_land_in_this_world", "&cYou can not claim land in this world!");
        QCChat.addPhrase("chunk_claimed_for_kingdom_yes", "&aChunk successfully claimed for your kingdom!");
        QCChat.addPhrase("chunk_claimed_for_kingdom_no", "&cChunk was not successfully claimed for Kingdom: ");
        QCChat.addPhrase("chunk_unclaimed_for_kingdom_yes", "&aChunk successfully unclaimed for your kingdom!");
        QCChat.addPhrase("chunk_unclaimed_for_kingdom_no", "&cChunk was not successfully unclaimed for Kingdom: ");
        QCChat.addPhrase("this_chunk_is_already_claimed", "&cthis chunk has already been claimed!");
        QCChat.addPhrase("this_chunk_is_not_claimed", "&cThis chunk chunk is not claimed by your kingdom!");
        QCChat.addPhrase("now_entering_the_land_of", "&aNow entering the land of ");
        QCChat.addPhrase("now_leaving_the_land_of", "&aNow entering the land of ");

        QCChat.addPhrase("successfully_knighted_player", "&aThe player has successfully been knighted!");
        QCChat.addPhrase("successfully_noble_player", "&aThe player has successfully been made a noble!");
        QCChat.addPhrase("successfully_king_player", "&aThe player has successfully been made the king!");
        QCChat.addPhrase("you_are_now_a_knight", "&aYou have been knighted by your king!");
        QCChat.addPhrase("you_are_now_a_noble", "&aYou have been made a noble of your kingdom by your king!");
        QCChat.addPhrase("you_are_now_a_king", "&aYou have been made the king of your kingdom!");

        QCChat.addPhrase("you_must_be_king", "&cYou must be the king to perform this action!");
        QCChat.addPhrase("you_must_be_member_kingdom", "&cYou must be a member of a kingdom!");
        QCChat.addPhrase("you_must_be_member_kingdom_leave", "&cYou must be a member of a kingdom to leave one!");
        QCChat.addPhrase("you_are_already_in_a_Kingdom", "&cYou are already a member of a kingdom!");
        QCChat.addPhrase("successfully_joined_kingdom_X", "&aSuccessfully joined the kingdom ");
        QCChat.addPhrase("failed_join_kingdom", "&cFailed to join the specified kingdom. Please check that it is not invite only.");
        QCChat.addPhrase("successfully_left_kingdom_X", "&aSuccessfully left the kingdom ");
        QCChat.addPhrase("failed_leave_kingdom", "&cFailed to leave the specified kingdom.");
        QCChat.addPhrase("kingdom_not_open", "&cThis kingdom is not open for new members!");
        QCChat.addPhrase("kingdom_not_found", "&cNo kingdom could be found using the specified name!");
        QCChat.addPhrase("you_are_king_someone_else_must_be_to_leave", "&cYou are the king! You must make another player king before leaving your kingdom!");

        QCChat.addPhrase("kingdom_already_open", " &cThe kingdom is already open!");
        QCChat.addPhrase("kingdom_now_open", " &aYour kingdom is now open!");
        QCChat.addPhrase("failed_open_kingdom", " &cFailed to open the kingdom!");
        QCChat.addPhrase("kingdom_already_closed", " &cThe kingdom is already closed!");
        QCChat.addPhrase("kingdom_now_closed", " &aYour kingdom is now closed!");
        QCChat.addPhrase("failed_close_kingdom", " &cFailed to close the kingdom!");

        QCChat.addPhrase("kingdom_is_now_at_war_with_kingdom", " &cis now at war with ");
        QCChat.addPhrase("kingdom_is_now_allied_with_kingdom", " &ais now allied with ");
        QCChat.addPhrase("kingdom_is_now_neutral_relationship_with_kingdom", " &6is now in a neutral relationship with ");
        QCChat.addPhrase("kingdom_is_now_pending_war_with_kingdom", " &cis now pending war with ");
        QCChat.addPhrase("kingdom_is_pending_allied_with_kingdom", " &ais now pending an allied relationship with ");
        QCChat.addPhrase("kingdom_is_pending_neutral_relationship_with_kingdom", " &6is now pending a neutral relationship with ");
        QCChat.addPhrase("failed_to_ally_with_kingdom", "&cFailed to become an ally with ");
        QCChat.addPhrase("failed_to_neutral_with_kingdom", "&cFailed to become neutral with ");
        QCChat.addPhrase("failed_to_war_with_kingdom", "&cFailed to go to war with ");

        QCChat.addPhrase("could_not_create_kingdoms_player", "&cYour player data could not be added to the QuartzKingdoms database!");
		
		//Database
		log.info("[QK][STARTUP] Connecting to Database");
		DBKing = MySQLking.openConnection();
		
		//Listeners
		log.info("[QK][STARTUP] Registering Listeners");
		new BlockListener(this);
		new PlayerListener(this);
		
		//Commands
		log.info("[QK][STARTUP] Registering Commands");
        commandFramework = new QCommandFramework(this);
        commandFramework.registerCommands(new CommandKingdom(this, core));
        commandFramework.registerCommands(new CommandKAdmin(this, core));
	   	
        //Startup notice
		log.info("[QK] The QuartzKingdoms Plugin has been enabled!");
		log.info("[QK] QuartzKingdoms version " + version + " compiled with QuartzCore version " + releaseVersion);
	}

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        return commandFramework.handleCommand(sender, label, command, args);
    }

}
