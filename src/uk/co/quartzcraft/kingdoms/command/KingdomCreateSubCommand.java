package uk.co.quartzcraft.kingdoms.command;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import uk.co.quartzcraft.core.command.QSubCommand;
import uk.co.quartzcraft.kingdoms.QuartzKingdoms;

public class KingdomCreateSubCommand extends QSubCommand {

	@Override
	public String getPermission() {
		return "QCK.Kingdom.create";
	}

	@Override
	public void onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(args[1] != null) {
			try {
				Statement s = QuartzKingdoms.MySQLking.openConnection().createStatement();
				ResultSet res = s.executeQuery("INSERT INTO Kingdoms ();");
		        res.next();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			
		}
		
	}

}
