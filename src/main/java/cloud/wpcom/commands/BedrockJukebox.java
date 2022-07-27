package cloud.wpcom.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import cloud.wpcom.WPCraft;
import cloud.wpcom.bedrockjukebox.JukeboxWrapper;

public class BedrockJukebox implements CommandExecutor {
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] mssg) {

        for (JukeboxWrapper j : WPCraft.jb.getJukeboxes()) {

            //sender.getServer().broadcastMessage(j.getBlock().toString() + j.getLocation().toString());
            sender.getServer().broadcastMessage(j.getInputHopperBlock().toString());
            sender.getServer().broadcastMessage(j.getInputHopperInventory().toString());
            sender.getServer().broadcastMessage(j.getInputHopperInventory().getContents().toString());
        }

        return true;
    }
} 
