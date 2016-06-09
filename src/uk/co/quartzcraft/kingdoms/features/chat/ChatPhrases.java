package uk.co.quartzcraft.kingdoms.features.chat;

import uk.co.quartzcraft.core.systems.chat.QCChat;

public class ChatPhrases {

    public static void phrases() {
        QCChat.addPhrase("kingdoms_prefix", "&8[&aKingdoms&8]");

        QCChat.addPhrase("you_do_not_have_permission_to_build_here", "&cYou do not have permission to build here!");
        QCChat.addPhrase("power_updated_your_power_is_now", "&aYour power has been updated! You now have &r");

        QCChat.addPhrase("kingdom_name_single_word", "&cA kingdoms name may only be a single word!");
        QCChat.addPhrase("created_kingdom_yes", "&aSuccessfully created kingdom: ");
        QCChat.addPhrase("not_enough_power", "&cYou don't have enough power!");
        QCChat.addPhrase("created_kingdom_no", "&cFailed to create kingdom: ");
        QCChat.addPhrase("deleted_kingdom_yes", "&aSuccessfully disbanded kingdom: ");
        QCChat.addPhrase("deleted_kingdom_no", "&cFailed to disbanded kingdom: ");
        QCChat.addPhrase("specify_kingdom_name", "&cPlease specify a name!");
        QCChat.addPhrase("kingdomname_already_used", "&cAnother kingdom is using that name! &aPlease pick another name");
        QCChat.addPhrase("kingdom_does_not_exist", "&cThe specified kingdom does not exist!");
        QCChat.addPhrase("your_kingdom_has_been_disbanded", "&cYour kingdom has been disbanded! You are no longer a member of a kingdom.");

        QCChat.addPhrase("info_kingdom", "&bInformation on kingdom &r");
        QCChat.addPhrase("your_kingdoms_level_is_X", "&aYour kingdoms level is &b");
        QCChat.addPhrase("your_kingdoms_power_is_X", "&aYour kingdoms power is &b");
        QCChat.addPhrase("kingdom_level_is_X", "&aLevel: &r");
        QCChat.addPhrase("kingdom_power_is_X", "&aPower: &r");
        QCChat.addPhrase("kingdom_king_is_X", "&aKing: &r");
        QCChat.addPhrase("kingdom_is_closed", "&aThe kingdom is &cclosed &ato new members!");
        QCChat.addPhrase("kingdom_is_open", "&aThe kingdom is &2open &ato new members!");

        QCChat.addPhrase("player_level_is_X", "&aLevel: &r");
        QCChat.addPhrase("player_power_is_X", "&aPower: &r");
        QCChat.addPhrase("player_kingdom_is_X", "&aKingdom: &r");

        QCChat.addPhrase("you_can_not_claim_land_in_this_world", "&cYou can not claim land in this world!");
        QCChat.addPhrase("chunk_claimed_for_kingdom_yes", "&aChunk successfully claimed for your kingdom!");
        QCChat.addPhrase("chunk_claimed_for_kingdom_no", "&cChunk was not claimed for Kingdom: ");
        QCChat.addPhrase("chunk_unclaimed_for_kingdom_yes", "&aChunk successfully unclaimed for your kingdom!");
        QCChat.addPhrase("chunk_unclaimed_for_kingdom_no", "&cChunk was not unclaimed for Kingdom: ");
        QCChat.addPhrase("this_chunk_is_already_claimed", "&cthis chunk has already been claimed!");
        QCChat.addPhrase("this_chunk_is_not_claimed", "&cThis chunk chunk is not claimed by your kingdom!");
        QCChat.addPhrase("now_entering_the_land_of", "&aNow entering the land of ");
        QCChat.addPhrase("now_leaving_the_land_of", "&aNow entering the land of ");

        QCChat.addPhrase("successfully_set_kingdom_home", "&aSuccessfully set the home of the kingdom!");
        QCChat.addPhrase("there_is_no_kingdom_home", "&cyour kingdom does not have a home set!");
        QCChat.addPhrase("teleported_to_kingdom_home", "&aTeleported to kingdom home!");

        QCChat.addPhrase("successfully_knighted_player", "&aThe player has successfully been knighted!");
        QCChat.addPhrase("successfully_noble_player", "&aThe player has successfully been made a noble!");
        QCChat.addPhrase("successfully_king_player", "&aThe player has successfully been made the king!");
        QCChat.addPhrase("you_are_now_a_knight", "&aYou have been knighted by your king!");
        QCChat.addPhrase("you_are_now_a_noble", "&aYou have been made a noble of your kingdom by your king!");
        QCChat.addPhrase("you_are_now_a_king", "&aYou have been made the king of your kingdom!");
        QCChat.addPhrase("player_must_be_member_of_your_kingdom", "&cThe specified player is not a member of your kingdom!");

        QCChat.addPhrase("you_must_be_king", "&cYou must be the king to perform this action!");
        QCChat.addPhrase("you_must_be_member_kingdom", "&cYou must be a member of a kingdom!");
        QCChat.addPhrase("you_must_be_member_kingdom_leave", "&cYou must be a member of a kingdom to leave one!");
        QCChat.addPhrase("you_are_already_in_a_Kingdom", "&cYou are already a member of a kingdom!");
        QCChat.addPhrase("successfully_joined_kingdom_X", "&aSuccessfully joined the kingdom ");
        QCChat.addPhrase("failed_join_kingdom_check_invite", "&cFailed to join the specified kingdom. Please check that it is not invite only.");
        QCChat.addPhrase("failed_join_kingdom", "&cFailed to join the specified kingdom.");
        QCChat.addPhrase("successfully_left_kingdom_X", "&aSuccessfully left the kingdom ");
        QCChat.addPhrase("failed_leave_kingdom", "&cFailed to leave the specified kingdom.");
        QCChat.addPhrase("kingdom_not_open", "&cThis kingdom is not open for new members!");
        QCChat.addPhrase("kingdom_not_found", "&cNo kingdom could be found using the specified name!");
        QCChat.addPhrase("you_are_king_someone_else_must_be_to_leave", "&cYou are the king! You must make another player king before leaving your kingdom!");

        QCChat.addPhrase("invited_player_to_kingdom", "&aYou have successfully invited that player to join your kingdom!");
        QCChat.addPhrase("failed_to_invited_player_to_kingdom", "&cPlayer could not be invited to your kingdom! Please try again.");
        QCChat.addPhrase("you_have_not_been_invited_to_this_kingdom", "&cYou have not been invited to join this kingdom!");

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

        QCChat.addPhrase("kingdom_is_pending_ally_with_your_kingdom", " &aThis kingdom is pending an allied relationship with your kingdom");
        QCChat.addPhrase("kingdom_is_pending_war_with_your_kingdom", "&cThis kingdom is pending war with your kingdom!");
        QCChat.addPhrase("kingdom_is_at_war_with_your_kingdom", "&cThis kingdom is at war with your kingdom!");
        QCChat.addPhrase("kingdom_is_allied_with_your_kingdom", "&aThis kingdom is allied with your kingdom!");

        QCChat.addPhrase("could_not_create_kingdoms_player", "&cYour player data could not be added to the QuartzKingdoms database!");
    }
}
