package cloud.wpcom.bedrockjukebox;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.Jukebox;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
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

public class BJEvents implements Listener {

    private final WPCraft wpcraft;
    private final BedrockJukebox bedrockJukebox;

    public BJEvents(WPCraft wpcraft, BedrockJukebox bedrockJukebox) {
        this.wpcraft = wpcraft;
        this.bedrockJukebox = bedrockJukebox;
    }

    @EventHandler
    public void hopperListener(InventoryMoveItemEvent event) {
        if ((!event.getItem().getType().isRecord()))
            return;
        
        // Check if the disc moved to an input hopper on a registred Jukebox
        for (JukeboxWrapper j : bedrockJukebox.getJukeboxes()) {
            if (!event.getDestination().equals(j.getInputHopperInventory()))
                continue;
            // Check for overloading a jukebox
            if (j.getBlock().getRecord().getType() != Material.AIR)
                return;
            // Schedule task to play disc next tick
            new BukkitRunnable() {
                        @Override
                        public void run() {
                            j.playRecord(j.popWaitingDisc(), wpcraft);
                        }
                    }.runTask(wpcraft); 

        } // TODO CHECK FOR LOCKED HOPPER

    }

    // Handles players transfering discs into input hoppers manually
    @EventHandler
    public void inputHopperCheck(InventoryClickEvent event) { // TODO SHIFT HOLD MOVES BREAK THE MOD
        // Ignores clicks outside of the gui
        if (event.getSlotType() == InventoryType.SlotType.OUTSIDE)
            return;

        // Check if the shift click/click was with a record
        if ((!event.getCurrentItem().getType().isRecord()) && (!event.getCursor().getType().isRecord())) {
            return;
        }

        for (JukeboxWrapper j : bedrockJukebox.getJukeboxes()) {
            // Discs should not be played by jukeboxes with active music
            if (j.isPlaying())
                continue;

            // Checks if the interaction was with an output hopper to handle full ouput
            // hoppers at the end of Disc Duration cycle.
            if (outputHopperCheck(event, j))
                return;
            
            // Check for overloading a jukebox
            if (j.getBlock().getPlaying() != Material.AIR)
                return;

            // Schedule to play next tick
            if (event.getClickedInventory().equals(j.getInputHopperInventory())) { 
                new BukkitRunnable() {
                        @Override
                        public void run() {
                            j.playRecord(j.popWaitingDisc(), wpcraft);
                        }
                    }.runTask(wpcraft); 
                return;

            } else { // Handles shift click inventory moves

                if (!event.isShiftClick())
                    return;

                if (!event.getWhoClicked().getOpenInventory().getTopInventory().equals(j.getInputHopperInventory())) // NOTE: Could use this method of checking in previous check?
                    continue;

                if (event.getCurrentItem().getType().isRecord()) {
                    // Schedule disc to play after it is transfered
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (j.hasInputHopper())
                                j.playRecord(j.popWaitingDisc(), wpcraft);
                        }
                    }.runTask(wpcraft); 
                    return;
                }
            }
        }
    }

    // Handles players pulling discs from full ouput hoppers that have a disc waiting in the jukebox
    public boolean outputHopperCheck(InventoryClickEvent event, JukeboxWrapper j) {
        if (!j.hasOutputHopper()) { // Checks for jukeboxs without output hoppers
            return false;
        }

        if (event.getClickedInventory().equals(j.getOutputHopperInventory())) {
            if (j.getBlock().getPlaying() != Material.AIR)
                checkHopperNextTick(j);

            return true;

        } else { // Handles shift click inventory moves

            if (!event.isShiftClick())
                return false;
            if (!event.getWhoClicked().getOpenInventory().getTopInventory().equals(j.getOutputHopperInventory()))
                return false;

            if (j.getBlock().getPlaying() != Material.AIR)
                checkHopperNextTick(j);

            return true;

        }
    }
    // Used by functions that handle full output hoppers
    // Schedules output hopper to be checked next tick
    public void checkHopperNextTick(JukeboxWrapper j) {
        new BukkitRunnable() {
                    @Override
                    public void run() {
                        // If the output hopper is still full
                        if (j.getOutputHopperInventory().addItem(new ItemStack(j.getBlock().getPlaying())).size() == 1)
                            return;
                        
                        j.clearPlaying();
                        if (j.hasInputHopper())
                            BJUtil.playNext(j, wpcraft);
                    }
                }.runTask(wpcraft);
    }

    @EventHandler
    public void registerJukeboxComponets(BlockPlaceEvent event) {

        // Check if the block placed is a Jukebox
        if (event.getBlock().getType() == Material.JUKEBOX) {
            // Add to jukebox db
            bedrockJukebox.addJukebox((Jukebox) event.getBlock().getState(), wpcraft);

            // Check if the block placed is a Hopper
        } else if (event.getBlock().getType() == Material.HOPPER) {
            for (JukeboxWrapper j : bedrockJukebox.getJukeboxes()) {

                // If the hopper is not next to the registered Jukebox, continue
                if (event.getBlock().getLocation().distance(j.getLocation()) != 1.0) {
                    continue;
                }

                // If the hopper is facing the registered Jukebox
                if (BJUtil.isHopperFacing(j, event.getBlock())) {
                    // Set input Hopper
                    j.setInputHopperBlock(event.getBlock());
                    return;

                    // Check if the hopper is under the Jukebox, meaning output hopper
                } else if (BJUtil.isHopperUnder(j, event.getBlock())) {
                    // Set output Hopper
                    j.setOutputHopperBlock(event.getBlock());
                    return;
                }
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
        // If the event was a right click on a block
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;
        // And the block is Jukebox
        if (event.getClickedBlock().getType() != Material.JUKEBOX)
            return;
        // Find registered Jukebox and check if it is
        for (JukeboxWrapper j : bedrockJukebox.getJukeboxes()) {
            if (!j.getLocation().equals(event.getClickedBlock().getLocation()))
                continue;
            if (!j.isPlaying()) {
                // Full output hopper check
                if (j.getBlock().getRecord().getType() != Material.AIR) {
                    j.clearPlaying();
                    return;
                } else
                    return;
            }
            j.durationTask.cancel();
            return;
        }
    }
}

// TODO CHECK FOR UNLOCKING HOPPERS FOR DISCS 
// TODO ADD MANUEL DISC INTO JUKEBOX WITH CHECKING FOR HOPPERS 