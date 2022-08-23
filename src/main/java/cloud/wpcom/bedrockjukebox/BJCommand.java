package cloud.wpcom.bedrockjukebox;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class BJCommand implements CommandExecutor {

    public BJCommand(BedrockJukebox bedrockJukebox) {
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] mssg) {
        return true;
    }
} 
