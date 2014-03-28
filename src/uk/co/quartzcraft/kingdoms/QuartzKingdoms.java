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
import uk.co.quartzcraft.core.chat.ChatPhrase;
import uk.co.quartzcraft.core.database.MySQL;
import uk.co.quartzcraft.core.command.QCommand;
import uk.co.quartzcraft.kingdoms.command.*;
import uk.co.quartzcraft.kingdoms.listeners.*; 

public class QuartzKingdoms extends JavaPlugin {
	
	public Plugin plugin = this.plugin;
    public Plugin core = Bukkit.getPluginManager().getPlugin("QuartzCore");
	
	public static final Logger log = Logger.getLogger("Minecraft");
	
	public static String releaseVersion = QuartzCore.displayReleaseVersion();

	public static Connection DBKing = null;
	public static MySQL MySQLking = null;

    public QCommand commandFramework;
	
	@Override
	public void onDisable() {
				
    	//Shutdown notice
		log.info("[QK]The QuartzKingdoms Plugin has been disabled!");
	}
	
	@Override
	public void onEnable() {
		
		log.info("[QC]Running plugin configuration");
		this.saveDefaultConfig();
		
		String SQLKingHost = this.getConfig().getString("database.kingdoms.host");
		String SQLKingDatabase = this.getConfig().getString("database.kingdoms.database");
		String SQLKingUser = this.getConfig().getString("database.kingdoms.username");
		String SQLKingPassword = this.getConfig().getString("database.kingdoms.password");
		MySQLking = new MySQL(plugin, SQLKingHost, "3306", SQLKingDatabase, SQLKingUser, SQLKingPassword);
		
		//Phrases
		log.info("[QK][STARTUP]Creating Phrases");
		ChatPhrase.addPhrase("kingdom_name_single_word", "&cA kingdoms name may only be a single word!");
		ChatPhrase.addPhrase("created_kingdom_yes", "&aSuccessfully created kingdom: ");
		ChatPhrase.addPhrase("created_kingdom_no", "&cFailed to create kingdom: ");
		ChatPhrase.addPhrase("deleted_kingdom_yes", "&aSuccessfully deleted kingdom: ");
		ChatPhrase.addPhrase("deleted_kingdom_no", "&cFailed to delete kingdom: ");
		ChatPhrase.addPhrase("specify_kingdom_name", "&cPlease specify a name!");
		ChatPhrase.addPhrase("kingdomname_already_used", "&cAnother kingdom is using that name! &aPlease pick another name");
        ChatPhrase.addPhrase("kingdom_does_not_exist", "&cThe specified kingdom does not exist!");
		ChatPhrase.addPhrase("info_kingdom", "&bInfo on Kingdom: ");

        ChatPhrase.addPhrase("you_can_not_claim_land_in_this_world", "&cYou can not claim land in this world!");
		ChatPhrase.addPhrase("chunk_claimed_for_kingdom_yes", "&aChunk successfully claimed for Kingdom: ");
		ChatPhrase.addPhrase("chunk_claimed_for_kingdom_no", "&cChunk was not successfully claimed for Kingdom: ");
		ChatPhrase.addPhrase("chunk_unclaimed_for_kingdom_yes", "&aChunk successfully unclaimed for Kingdom: ");
		ChatPhrase.addPhrase("chunk_unclaimed_for_kingdom_no", "&cChunk was not successfully unclaimed for Kingdom: ");
        ChatPhrase.addPhrase("this_chunk_is_already_claimed", "&cthis chunk has already been claimed!");
        ChatPhrase.addPhrase("this_chunk_is_not_claimed", "&cThis chunk chunk is not claimed by your kingdom!");
		ChatPhrase.addPhrase("now_entering_the_land_of", "&aNow entering the land of ");
		ChatPhrase.addPhrase("now_leaving_the_land_of", "&aNow entering the land of ");
		
		ChatPhrase.addPhrase("got_promoted_kingdom_yes", "&aYou were moved group by your king!");

        ChatPhrase.addPhrase("you_must_be_member_kingdom", "&cYou must be a member of a kingdom!");
        ChatPhrase.addPhrase("you_must_be_member_kingdom_leave", "&cYou must be a member of a kingdom to leave one!");
        ChatPhrase.addPhrase("you_are_already_in_a_Kingdom", "&cYou are already a member of a kingdom!");
		ChatPhrase.addPhrase("successfully_joined_kingdom_X", "&aSuccessfully joined the kingdom ");
		ChatPhrase.addPhrase("failed_join_kingdom", "&cFailed to join the specified kingdom. Please check that it is not invite only.");
		ChatPhrase.addPhrase("successfully_left_kingdom_X", "&aSuccessfully left the kingdom ");
		ChatPhrase.addPhrase("failed_leave_kingdom", "&cFailed to leave the specified kingdom.");
        ChatPhrase.addPhrase("kingdom_not_open", "&cThis kingdom is not open for new members!");
        ChatPhrase.addPhrase("kingdom_not_found", "&cNo kingdom could be found using the specified name!");
        ChatPhrase.addPhrase("you_are_king_someone_else_must_be_to_leave", "&cYou are the king! You must make another player king before leaving your kingdom!");

        ChatPhrase.addPhrase("kingdom_already_open", " &cThe kingdom is already open!");
        ChatPhrase.addPhrase("kingdom_now_open", " &aYour kingdom is now open!");
        ChatPhrase.addPhrase("failed_open_kingdom", " &cFailed to open the kingdom!");
        ChatPhrase.addPhrase("kingdom_already_closed", " &cThe kingdom is already closed!");
        ChatPhrase.addPhrase("kingdom_now_closed", " &aYour kingdom is now closed!");
        ChatPhrase.addPhrase("failed_close_kingdom", " &cFailed to close the kingdom!");

		ChatPhrase.addPhrase("kingdom_is_now_at_war_with_kingdom", " &cis now at war with ");
		ChatPhrase.addPhrase("kingdom_is_now_allied_with_kingdom", " &ais now allied with ");
		ChatPhrase.addPhrase("kingdom_is_now_neutral_relationship_with_kingdom", " &6is now in a neutral relationship with ");
        ChatPhrase.addPhrase("kingdom_is_now_pending_war_with_kingdom", " &cis now pending war with ");
        ChatPhrase.addPhrase("kingdom_is_pending_allied_with_kingdom", " &ais now pending an allied relationship with ");
        ChatPhrase.addPhrase("kingdom_is_pending_neutral_relationship_with_kingdom", " &6is now pending a neutral relationship with ");
		ChatPhrase.addPhrase("failed_to_ally_with_kingdom", "&cFailed to become an ally with ");
		ChatPhrase.addPhrase("failed_to_neutral_with_kingdom", "&cFailed to become neutral with ");
		ChatPhrase.addPhrase("failed_to_war_with_kingdom", "&cFailed to go to war with ");
		
		ChatPhrase.addPhrase("could_not_create_kingdoms_player", "&cYour player data could not be added to the QuartzKingdoms database!");
		
		//Database
		log.info("[QK][STARTUP]Connecting to Database");
		DBKing = MySQLking.openConnection();
		
		//Listeners
		log.info("[QK][STARTUP]Registering Listeners");
		new BlockListener(this);
		new PlayerListener(this);
        new PlayerDeathListener(this);
		//new PlayerMoveListener(this);
		
		//Commands
		log.info("[QK][STARTUP]Registering Commands");
        commandFramework = new QCommand(this);
        commandFramework.registerCommands(new CommandKingdom(this, core));
	   	
        //Startup notice
		log.info("[QK]The QuartzKingdoms Plugin has been enabled!");
		log.info("[QK]Compiled using QuartzCore version " + releaseVersion);
	}

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        return commandFramework.handleCommand(sender, label, command, args);
    }

}
