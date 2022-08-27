package cloud.wpcom.bedrockjukebox;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Hopper;
import org.bukkit.block.Jukebox;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import cloud.wpcom.WPCraft;
import cloud.wpcom.bedrockjukebox.JukeboxScheduler.UpdateType;

public class BJEvents implements Listener {

    private final WPCraft wpcraft;
    private final BedrockJukebox bedrockJukebox;

    public BJEvents(WPCraft wpcraft, BedrockJukebox bedrockJukebox) {
        this.wpcraft = wpcraft;
        this.bedrockJukebox = bedrockJukebox;
    }

    /**
     * Handles the movment of items moveded automaticly by the game
     * such as by a hopper or other block that can pull/push items
     * 
     * @param event Event that occrued
     */
    @EventHandler
    public void itemMovmentListener(InventoryMoveItemEvent event) { //DONE
        // Checks if moving item is a disc
        if ((!event.getItem().getType().isRecord()))
            return;

        // Checks if the item was moved to a hopper inventory (for input hopper)
        if (event.getDestination().getType() == InventoryType.HOPPER) {
            // Checks if item was moved to an input hopper
            final Hopper hopper = (Hopper) event.getDestination().getLocation().getBlock().getState();
            final JukeboxWrapper jbw = bedrockJukebox.getManager().get(hopper);
            if (jbw == null)
                return;

            // Checks if the input hopper is locked
            if (hopper.isLocked())
                return;
            
            /// Schedules an update of the input hoppers (Could check only updated hopper?)
            bedrockJukebox.getScheduler().scheduleUpdate(jbw, UpdateType.INPUT);

        // Checks if the item was moved from a hopper inventory (for output hopper)
        } else if (event.getSource().getType() == InventoryType.HOPPER) {
            // Checks if item was moved from a output hopper

            final Hopper hopper = (Hopper) event.getSource().getLocation().getBlock().getState(); // LEFT OFF DIAGNOSING Why THE OUTPUT HOPPER DIDNT SCHEDULE A CHECK WITH UPDATE. THINK IT HAS SOMTHING TO DO WITH HOPPER BEING NULL?
            final JukeboxWrapper jbw = bedrockJukebox.getManager().get(hopper);
            if (jbw == null) {
                return;
            }

            // Schedules an update of the output hopper
            bedrockJukebox.getScheduler().scheduleUpdate(jbw, UpdateType.OUTPUT);

        } else { // If an input/output hopper was not involved
            return;
        }
    }

    // Handles players transfering discs into input hoppers manually
    /**
     * Handles the movment of items moved by players.
     * Checks if a disc should be played, and plays it.
     * 
     * @param event Event that occrued
     */
    @EventHandler
    public void inputHopperCheck(InventoryClickEvent event) { // TODO SHIFT HOLD MOVES BREAK THE MOD?
        // // Checks if click was an inventory slot
        // if (event.getSlotType() != InventoryType.SlotType.CONTAINER)
        //     return;

        // // Check if event was a left click
        // if (event.getClick() == ClickType.LEFT) {
        //     // Checks if the item was a record
        //     //if ((!event.getCurrentItem().getType().isRecord()) && (!event.getCursor().getType().isRecord()))
        //     event.getWhoClicked()
        //             .sendMessage("Left-Current Item: " + event.getCurrentItem() + " Cursor: " + event.getCursor());
        //     return;
        //     // Check if the clicked inventory was an input hopper
        // }

        // if (event.getClick() == ClickType.SHIFT_LEFT) {

        //     event.getWhoClicked()
        //             .sendMessage("ShiftL-Current Item: " + event.getCurrentItem() + " Cursor: " + event.getCursor());
        //     return;
        // }
            

        
        

        // for (JukeboxWrapper j : bedrockJukebox.getJukeboxes()) {
        //     // Discs should not be played by jukeboxes with active music
        //     if (j.isPlaying())
        //         continue;

        //     // Checks if the interaction was with an output hopper to handle full ouput
        //     // hoppers at the end of Disc Duration cycle.
        //     if (outputHopperCheck(event, j))
        //         return;
            
        //     // Check for overloading a jukebox
        //     if (j.getJukebox().getPlaying() != Material.AIR)
        //         return;

        //     // Schedule to play next tick
        //     if (event.getClickedInventory().equals(j.getInputHopperInventory())) { 
        //         new BukkitRunnable() {
        //                 @Override
        //                 public void run() {
        //                     j.playRecord(j.popWaitingDisc(), wpcraft);
        //                 }
        //             }.runTask(wpcraft); 
        //         return;

        //     } else { // Handles shift click inventory moves

        //         if (!event.isShiftClick())
        //             return;

        //         if (!event.getWhoClicked().getOpenInventory().getTopInventory().equals(j.getInputHopperInventory())) // NOTE: Could use this method of checking in previous check?
        //             continue;

        //         if (event.getCurrentItem().getType().isRecord()) {
        //             // Schedule disc to play after it is transfered
        //             new BukkitRunnable() {
        //                 @Override
        //                 public void run() {
        //                     if (j.hasInputHopper())
        //                         j.playRecord(j.popWaitingDisc(), wpcraft);
        //                 }
        //             }.runTask(wpcraft); 
        //             return;
        //         }
        //     }
        // }
    }

    // Handles players pulling discs from full ouput hoppers that have a disc waiting in the jukebox
    public boolean outputHopperCheck(InventoryClickEvent event, JukeboxWrapper j) {
        if (!j.hasOutputHopper()) { // Checks for jukeboxs without output hoppers
            return false;
        }

        if (event.getClickedInventory().equals(j.getOutputInventory())) {
            if (j.getJukebox().getPlaying() != Material.AIR)
                checkHopperNextTick(j);

            return true;

        } else { // Handles shift click inventory moves

            if (!event.isShiftClick())
                return false;
            if (!event.getWhoClicked().getOpenInventory().getTopInventory().equals(j.getOutputInventory()))
                return false;

            if (j.getJukebox().getPlaying() != Material.AIR)
                checkHopperNextTick(j);

            return true;

        }
    }

    // Used by functions that handle full output hoppers
    // Schedules output hopper to be checked next tick
    @Deprecated
    public void checkHopperNextTick(JukeboxWrapper j) {
        new BukkitRunnable() {
            @Override
            public void run() {
                // If the output hopper is still full
                if (j.getOutputInventory().addItem(new ItemStack(j.getJukebox().getPlaying())).size() == 1)
                    return;

                j.clearPlaying();
                if (j.hasInputHopper())
                    BJUtil.playNext(j, wpcraft);
            }
        }.runTask(wpcraft);
    }

    /**
     * Handles the registration of jukeboxes and hoppers
     * into the {@link JukeboxManager}
     * 
     * @param event Event that occured
     */
    @EventHandler
    public void registerJukeboxComponet(BlockPlaceEvent event) {
        // Check if the block placed is a Jukebox
        if (event.getBlockPlaced().getType() == Material.JUKEBOX) {
            // Register the jukebox
            bedrockJukebox.registerJukebox((Jukebox) event.getBlock().getState());
            bedrockJukebox.getLogger().debug("Jukebox registered");
            return;

        // Check if the block placed is a Hopper
        } else if (event.getBlockPlaced().getType() == Material.HOPPER) {

            // Check if it was placed against a jukebox
            if (event.getBlockAgainst().getType() == Material.JUKEBOX) {
                // Register input hopper
                final JukeboxWrapper jbw = bedrockJukebox.getManager()
                        .get((Jukebox) event.getBlockAgainst().getState());
                final BlockFace bf = BJUtil.calcBlockFace(jbw.getJukebox(), event.getBlock());
                jbw.addInput((Hopper) event.getBlock().getState(), bf);
                jbw.getInput(bf).getInventory().addItem(new ItemStack(Material.BEEF, 1));
                bedrockJukebox.getLogger().debug("Input hopper registered to the " + bf);
            }

            // Check if it was placed below a jukebox
            final Block above = event.getBlock().getLocation().add(0, 1, 0).getBlock();
            if (above.getType() == Material.JUKEBOX) {
                final JukeboxWrapper jbw = bedrockJukebox.getManager().get((Jukebox) above.getState());
                jbw.setOutput((Hopper) event.getBlock().getState());
                jbw.getOutput().getInventory().addItem(new ItemStack(Material.LANTERN, 1));
                bedrockJukebox.getLogger().debug("Output hopper registered");
            }
        }
    }

    @EventHandler
    public void deleteJukeboxComponets(BlockBreakEvent event) {

        // Check if the block broken is a jukebox
        if (event.getBlock().getType() == Material.JUKEBOX) {
            // Remove from jukebox db
            bedrockJukebox.removeJukebox((Jukebox) event.getBlock().getState());

        // Check if the block broken is a Hopper
        } else if (event.getBlock().getType() == Material.HOPPER) {

            // If the hopper was a registered hopper, remove it.
            for (JukeboxWrapper j : bedrockJukebox.getJukeboxes()) {

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
    public void chunkListener(ChunkLoadEvent event) {
        registerJBInChunk(event.getChunk());
    }

    // Get all loaded chunks and checks for jukeboxes to register
    @EventHandler
    public void worldListener(WorldLoadEvent event) {
        for (Chunk c : event.getWorld().getLoadedChunks()) {
            registerJBInChunk(c);
        }
    }

    // Registers Jukeboxes in given chunk
    public void registerJBInChunk(Chunk c) { // TODO MISLEADING FUNCTION NAME, 
        for (BlockState bs : c.getTileEntities()) {
            if (bs instanceof Jukebox) {
                // If Jukebox is already registred, ignore
                if (bedrockJukebox.jukeboxExist(bs))
                    break;
                // If Jukebox was not registred, register it    
                else
                    bedrockJukebox.addJukebox((Jukebox) bs, wpcraft);
            }
        }
    }
    
    // Cancle playing of a disc on jukebox eject, as well as associated tasks
    // This triggers an 'update' of the input hopper
    @EventHandler
    public void discEjectionListener(PlayerInteractEvent event) {
        // // If the event was a right click on a block
        // if (event.getAction() != Action.RIGHT_CLICK_BLOCK)
        //     return;
        // // And the block is Jukebox
        // if (event.getClickedBlock().getType() != Material.JUKEBOX)
        //     return;
        // // Find registered Jukebox and check if it is
        // for (JukeboxWrapper j : bedrockJukebox.getJukeboxes()) {
        //     if (!j.getLocation().equals(event.getClickedBlock().getLocation()))
        //         continue;
        //     if (!j.isPlaying()) {
        //         // Full output hopper check
        //         if (j.getJukebox().getRecord().getType() != Material.AIR) {
        //             j.clearPlaying();
        //             return;
        //         } else
        //             return;
        //     }
        //     j.durationTask.cancel();
        //     return;
        // }
    }
}

// TODO ADD MANUEL DISC INTO JUKEBOX WITH CHECKING FOR HOPPERS 