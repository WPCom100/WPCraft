package cloud.wpcom.bedrockjukebox;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class BJCommand implements CommandExecutor {

    private final BedrockJukebox bedrockJukebox;

    public BJCommand(BedrockJukebox bedrockJukebox) {
        this.bedrockJukebox = bedrockJukebox;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] mssg) {
        return true;
    }
} 
