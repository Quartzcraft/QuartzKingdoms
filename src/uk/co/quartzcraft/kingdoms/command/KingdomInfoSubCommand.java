package uk.co.quartzcraft.kingdoms.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import uk.co.quartzcraft.core.chat.ChatPhrase;
import uk.co.quartzcraft.core.command.QSubCommand;
import uk.co.quartzcraft.kingdoms.kingdom.Kingdom;

public class KingdomInfoSubCommand extends QSubCommand {

	 public String getPermission() {
             return "QCK.Kingdom";
     }
     
     public void onCommand(CommandSender sender, Command cmd, String label, String[] args) {  
    	 String arg = args[1];
         String[] info = Kingdom.getInfo(arg);
         
         if(info != null) {
        	 sender.sendMessage(ChatPhrase.getPhrase("info_kingdom"));
         }
     }
}
