package cloud.wpcom.events;

import cloud.wpcom.WPCraft;
import cloud.wpcom.bedrockjukebox.JBUtil;
import cloud.wpcom.bedrockjukebox.JukeboxWrapper;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Jukebox;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.block.BlockState;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class BedrockJukebox implements Listener {

    private final WPCraft wpcraft;

    public BedrockJukebox(WPCraft wpcraft) {
        this.wpcraft = wpcraft;
    }

    @EventHandler
    public void onBlockDispense(InventoryMoveItemEvent event) {

        // Checks if the item moved to an input hopper on a registred Jukebox
        for (JukeboxWrapper j : WPCraft.jb.getJukeboxes()) {
            if (!event.getDestination().equals(j.getInputHopperInventory()))
                continue;

            // If the item is a record and the Jukebox is not playing a disc
            if ((event.getItem().getType().isRecord()) && (!j.isPlaying())) {
                // Pop the record and play it
                j.playRecord(event.getItem(), wpcraft);
                event.setItem(new ItemStack(Material.AIR));
                return;
            }
        } // TODO CHECK FOR LOCKED HOPPER

    }

    // Handles players dropping discs into input hoppers manually
    @EventHandler
    public void onInventoryTransfer(InventoryClickEvent event) {
        for (JukeboxWrapper j : WPCraft.jb.getJukeboxes()) {
            if (j.isPlaying())
                continue;
            if (event.getClickedInventory().equals(j.getInputHopperInventory())) {
                if (!event.getCursor().getType().isRecord())
                    continue;
                j.playRecord(j.popWaitingDisc(), wpcraft);
                return;
            } else {
                // Handles shift click inventory moves
                if (!event.isShiftClick())
                    return;
                    // NOTE: Could use this method of checking in previous check
                if (!event.getWhoClicked().getOpenInventory().getTopInventory().equals(j.getInputHopperInventory()))
                    continue;

                if (event.getCurrentItem().getType().isRecord()) {
                    // Schedule disc to play after it is transfered
                    BukkitRunnable playTask = new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (j.hasInputHopper())
                                j.playRecord(j.popWaitingDisc(), wpcraft);
                        }
                    };
                    playTask.runTask(wpcraft);
                    return;
                }
            }
        }
    }

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
                    return;

                    // Check if the hopper is under the Jukebox, meaning output hopper
                } else if (JBUtil.isHopperUnder(j, event.getBlock())) {
                    // Set output Hopper
                    j.setOutputHopperBlock(event.getBlock());
                    return;
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
                        return;
                    }
                }
                if (j.hasOutputHopper()) {
                    if (event.getBlock().getLocation().equals(j.getOutputHopperBlock().getLocation())) {
                        j.removeOutputHopper();
                        return;
                    }
                }
            }
        }
    }

    // Checks chunks as they load for jukeboxes to register
    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {
        checkChunkForJukebox(event.getChunk());
    }

    // Get all loaded chunks and checks for jukeboxes to register
    @EventHandler
    public void onWorldLoad(WorldLoadEvent event) {
        for (Chunk c : event.getWorld().getLoadedChunks()) {
            checkChunkForJukebox(c);
        }
    }

    // Registers Jukeboxes in given chunk
    public void checkChunkForJukebox(Chunk c) { // TODO MISLEADING FUNCTION NAME, 
        for (BlockState bs : c.getTileEntities()) {
            if (bs instanceof Jukebox) {
                // If Jukebox is already registred, ignore
                if (WPCraft.jb.jukeboxExist(bs))
                    break;
                // If Jukebox was not registred, register it    
                else
                    WPCraft.jb.addJukebox((Jukebox) bs);
            }
        }
    }
    
    // Cancle playing of a disc on jukebox eject, as well as associated tasks
    // This triggers an 'update' of the input hopper
    @EventHandler
    public void onBlockRightClick(PlayerInteractEvent event) {
        // If the event was a right click on a block
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;
        // And the block is Jukebox
        if (event.getClickedBlock().getType() != Material.JUKEBOX)
            return;
        // Find registered Jukebox and check if it is
        for (JukeboxWrapper j : WPCraft.jb.getJukeboxes()) {
            if (j.getLocation().equals(event.getClickedBlock().getLocation())) {
                if (j.isPlaying()) {
                    j.durationTask.cancel();
                    return;
                }
            }
        }

    }
}

// TODO CHECK FOR UNLOCKING HOPPERS FOR DISCS 