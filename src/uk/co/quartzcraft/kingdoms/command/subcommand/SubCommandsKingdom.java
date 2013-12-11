package uk.co.quartzcraft.kingdoms.command.subcommand;

import java.lang.reflect.Array;

import uk.co.quartzcraft.kingdoms.QuartzKingdoms;

public class SubCommandsKingdom {
	
	public static String[] list() {
		
		String[] SubCommands;
		
		SubCommands = new String[12];
		
		SubCommands[0] = "create";
		SubCommands[1] = "info";
		SubCommands[2] = "invite";
		SubCommands[3] = "set";
		 
		return SubCommands;
	}
	
	public static String getSubCommand(String arg) {
		
		String[] SubCommandList = list();
		 
		 for(String SubCommand : SubCommandList){
			if(SubCommand == arg) {
				
				return SubCommand;
			} else {
				return null;
			}
		}
		return null;	
	}

	
}
