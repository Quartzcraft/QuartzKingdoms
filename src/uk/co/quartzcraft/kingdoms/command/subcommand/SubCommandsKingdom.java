package uk.co.quartzcraft.kingdoms.command.subcommand;

import java.lang.reflect.Array;

public class SubCommandsKingdom {
	
	public static String[] list() {
		
		String[] SubCommandList;
		
		SubCommandList = new String[12];
		
		 SubCommandList[0] = "create";
		 SubCommandList[1] = "info";
		 SubCommandList[2] = "invite";
		 SubCommandList[3] = "set";
		 
		return SubCommandList;
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
