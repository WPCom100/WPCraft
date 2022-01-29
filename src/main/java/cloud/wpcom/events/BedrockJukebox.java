package cloud.wpcom.events;

import cloud.wpcom.bedrockjukebox.JBUtil;
import cloud.wpcom.WPCraft;
import cloud.wpcom.bedrockjukebox.JukeboxWrapper;
import org.bukkit.Material;
import org.bukkit.block.Jukebox;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.block.BlockState;

public class BedrockJukebox implements Listener {

    @EventHandler
    public void onBlockPlaced(BlockPlaceEvent event) {

        // Check if the block placed is a Jukebox
        if (event.getBlock().getType() == Material.JUKEBOX) {
            // Add to jukebox db
            WPCraft.jb.addJukebox((Jukebox) event.getBlock().getState());
            

        // Check if the block placed is a Hopper
        } else if (event.getBlock().getType() == Material.HOPPER) {
            for (JukeboxWrapper j : WPCraft.jb.getJukeboxes()) {
                
                // If the hopper is not next to a registered bdJukebox
                if (event.getBlock().getLocation().distance(j.getLocation()) != 1.0) {
                    return;
                }
                                    
                // If the hopper is facing the registered bdJukebox
                if (JBUtil.isHopperFacing(j, event.getBlock())) {
                    // Set input Hopper
                    j.setInputHopperBlock(event.getBlock());

                // Check if the hopper is under the Jukebox, meaning output hopper
                } else if (JBUtil.isHopperUnder(j, event.getBlock())) {
                        // Set output Hopper
                        j.setOutputHopperBlock(event.getBlock());
                }
            }
        }
    }
    
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {

        // Check if the block placed is a jukebox
        if (event.getBlock().getType() == Material.JUKEBOX) {
            // Remove from jukebox db
            WPCraft.jb.removeJukebox((Jukebox) event.getBlock().getState());

        // Check if the block broken is a Hopper
        } else if (event.getBlock().getType() == Material.HOPPER) {

            for (JukeboxWrapper j : WPCraft.jb.getJukeboxes()) {
                // If the hopper was a registered hopper, remove it.
                if (event.getBlock().getLocation().equals(j.getInputHopperBlock().getLocation())) {
                    j.removeInputHopper();
                } else if (event.getBlock().getLocation().equals(j.getOutputHopperBlock().getLocation())) {
                    j.removeOutputHopper();
                }
            }
        }
    }

    @EventHandler
    public void onJukeboxLoad(ChunkLoadEvent event) {

        // Check loaded chunk for Jukeboxes
        for (BlockState b : event.getChunk().getTileEntities()) {
            if (b instanceof Jukebox) {

                // If Jukebox is already registred, ignore
                if (WPCraft.jb.jukeboxExist(b)) {
                    WPCraft.server.broadcastMessage("Registered jukebox found in loaded chunk!");
                    return;

                // If Jukebox was not registred, register it
                } else {
                    WPCraft.jb.addJukebox((Jukebox) b);
                    WPCraft.server.broadcastMessage("Unregistred Jukebox found in loaded chunk!");

                }
            }
        }
    }
}