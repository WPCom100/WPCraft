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

        for (JukeboxWrapper j : bedrockJukebox.getJukeboxes()) {

            //sender.getServer().broadcastMessage(j.getBlock().toString() + j.getLocation().toString());
            sender.getServer().broadcastMessage(j.getInputHopperBlock().toString());
            sender.getServer().broadcastMessage(j.getInputHopperInventory().toString());
            sender.getServer().broadcastMessage(j.getInputHopperInventory().getContents().toString());
        }

        return true;
    }
} 
