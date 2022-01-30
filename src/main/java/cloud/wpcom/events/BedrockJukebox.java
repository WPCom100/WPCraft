package cloud.wpcom.events;

import cloud.wpcom.WPCraft;
import cloud.wpcom.bedrockjukebox.JBUtil;
import cloud.wpcom.bedrockjukebox.JukeboxWrapper;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Jukebox;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.WorldLoadEvent;
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

                // If the hopper is not next to the registered Jukebox, continue
                if (event.getBlock().getLocation().distance(j.getLocation()) != 1.0) {
                    continue;
                }

                // If the hopper is facing the registered Jukebox
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

        // Check if the block broken is a jukebox
        if (event.getBlock().getType() == Material.JUKEBOX) {
            // Remove from jukebox db
            WPCraft.jb.removeJukebox((Jukebox) event.getBlock().getState());

            // Check if the block broken is a Hopper
        } else if (event.getBlock().getType() == Material.HOPPER) {

            // If the hopper was a registered hopper, remove it.
            for (JukeboxWrapper j : WPCraft.jb.getJukeboxes()) {

                // If Hopper is not next to a registered Jukebox, continue
                if (event.getBlock().getLocation().distance(j.getLocation()) != 1.0) {
                    continue;
                }

                // Check if it was input or output hopper and remove it
                if (j.hasInputHopper()) {
                    if (event.getBlock().getLocation().equals(j.getInputHopperBlock().getLocation())) {
                        j.removeInputHopper();
                        break;
                    }
                }
                if (j.hasOutputHopper()) {
                    if (event.getBlock().getLocation().equals(j.getOutputHopperBlock().getLocation())) {
                        j.removeOutputHopper();
                        break;
                    }
                }
            }
        }
    }

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {

        // Check loaded chunk for Jukeboxes
        for (BlockState bs : event.getChunk().getTileEntities()) {
            if (bs instanceof Jukebox) {

                // If Jukebox is already registred, ignore
                if (WPCraft.jb.jukeboxExist(bs)) {
                    WPCraft.server.broadcastMessage("Registered jukebox found in loaded chunk!");
                    return;

                // If Jukebox was not registred, register it
                } else {
                    WPCraft.jb.addJukebox((Jukebox) bs);
                    WPCraft.server.broadcastMessage("Unregistred Jukebox found in loaded chunk!");

                }
            }
        }
    }

    @EventHandler
    public void onWorldLoad(WorldLoadEvent event) {

        // Get all loaded chunks
        for (Chunk c : event.getWorld().getLoadedChunks()) {
            // Check each chunk for a jukebox
            for (BlockState bs : c.getTileEntities()) {
                if (bs instanceof Jukebox) {

                    // If Jukebox is already registred, ignore
                    if (WPCraft.jb.jukeboxExist(bs)) {
                        WPCraft.server.getLogger().info("Registered jukebox found in loaded chunk!");
                        return;

                    // If Jukebox was not registred, register it    
                    } else {
                    WPCraft.jb.addJukebox((Jukebox) bs);
                    WPCraft.server.getLogger().info("Unregistred Jukebox found in loaded chunk! Registered!");

                    }
                }
            }
        }
    }
}