package cloud.wpcom.events;

import org.bukkit.Material;
import org.bukkit.block.Jukebox;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.block.BlockState;

import cloud.wpcom.WPCraft;

public class BedrockJukebox implements Listener {

    @EventHandler
    public void onJukeboxPlaced(BlockPlaceEvent event) {

        // Check if the block placed is a jukebox
        if (event.getBlock().getType() == Material.JUKEBOX) {

            // Add to jukebox db
            WPCraft.jb.addJukebox((Jukebox) event.getBlock().getState());

        }

    }
    
    @EventHandler
    public void onJukeboxBreak(BlockBreakEvent event) {

        // Check if the block placed is a jukebox
        if (event.getBlock().getType() == Material.JUKEBOX) {

            // Remove from jukebox db
            WPCraft.jb.removeJukebox((Jukebox) event.getBlock().getState());

        } 
        
    }

    @EventHandler
    public void onJukeboxLoad(ChunkLoadEvent event) {

        for (BlockState b : event.getChunk().getTileEntities()) {

            if (b instanceof Jukebox) {

                if (WPCraft.jb.jukeboxExist(b)) {

                } else {

                    WPCraft.jb.addJukebox((Jukebox) b);

                }

            }

        }

    }

}