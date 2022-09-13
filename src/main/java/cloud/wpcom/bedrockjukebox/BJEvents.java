package cloud.wpcom.bedrockjukebox;

import java.util.ArrayList;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Hopper;
import org.bukkit.block.Jukebox;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import cloud.wpcom.bedrockjukebox.JukeboxScheduler.UpdateType;

public class BJEvents implements Listener {

    private final BedrockJukebox bedrockJukebox;

    public BJEvents(BedrockJukebox bedrockJukebox) { // TODO SHIFT HOLD MOVES BREAK THE MOD?
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

    /**
     * Handles the movment of items moved by players
     * 
     * @param event Event that occrued
     */
    @EventHandler
    public void playerInventoryClickListener(InventoryClickEvent event) { // DONE
        // Checks if click was an inventory slot
        if (event.getSlotType() != InventoryType.SlotType.CONTAINER)
            return;

        // Check if event was a left click
        if (event.getClick() == ClickType.LEFT) {
            // Checks if the item was a record
            if ((!event.getCursor().getType().isRecord()))
                return;

            // Checks if the clicked inventory was a hopper
            final Inventory i = event.getClickedInventory();
            if (i.getType() != InventoryType.HOPPER)
                return;

            // Checks if the hopper is registered 
            final JukeboxWrapper jbw = bedrockJukebox.getManager().get((Hopper) i.getLocation().getBlock().getState());
            if (jbw == null)
                return;

            bedrockJukebox.getLogger().debug("Left click with registred hopper detected");
            bedrockJukebox.getScheduler().scheduleUpdate(jbw, UpdateType.BOTH); // TODO Handle detecting inpuit/output and updateing accordingly
        }

        if (event.getClick() == ClickType.SHIFT_LEFT) {
            // Checks if the item was a record
            if ((!event.getCurrentItem().getType().isRecord()))
                return;

            // Check if the top inventory is a hopper
            final Inventory topInv = event.getWhoClicked().getOpenInventory().getTopInventory();
            if (event.getWhoClicked().getOpenInventory().getTopInventory().getType() != InventoryType.HOPPER)
                return;

            // Check if the hopper is registered
            final JukeboxWrapper jbw = bedrockJukebox.getManager()
                    .get((Hopper) topInv.getLocation().getBlock().getState());
            if (jbw == null)
                return;

            bedrockJukebox.getLogger().debug("Shift left click with registred hopper detected");
            bedrockJukebox.getScheduler().scheduleUpdate(jbw, UpdateType.BOTH);
        }
    }

    /**
     * Handles items thrown manuely into a hopper
     * 
     * @param event Event that occrued
     */
    @EventHandler
    public void hopperPickupItemListener(InventoryPickupItemEvent event) {
        // Check if the item was picked up by a hopper
        if (event.getInventory().getType() != InventoryType.HOPPER)
            return;
        
        // Check if the item was a record
        if (!event.getItem().getItemStack().getType().isRecord())
            return;
        
        // Check if the hopper is registred
        final Hopper hopper = (Hopper) event.getInventory().getLocation().getBlock().getState();
        final JukeboxWrapper jbw = bedrockJukebox.getManager().get(hopper);
        if (jbw == null)
            return;

        bedrockJukebox.getScheduler().scheduleUpdate(jbw, UpdateType.INPUT);
        
        
    }
    /**
     * Handles the manuel ejection of dics from jukeboxes by players
     * 
     * @param event Event that occrued
     */
    @EventHandler
    public void playerJukeboxInteractionListener(PlayerInteractEvent event) { // DONE
        // If the event was a right click on a block
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;
        // And the block is Jukebox
        if (event.getClickedBlock().getType() != Material.JUKEBOX)
            return;

        final JukeboxWrapper jbw = bedrockJukebox.getManager().get((Jukebox) event.getClickedBlock().getState());
        // Handles jukebox ejection
        if (jbw.isPlaying()) {
            jbw.clearDurationTask();
            bedrockJukebox.getScheduler().scheduleUpdate(jbw, UpdateType.INPUT);
        }

        // Handles playing disc manuely 
        if (!jbw.isPlaying() && event.getItem().getType().isRecord()) {
            jbw.playRecord(event.getItem());
            event.getItem().setAmount(0);
            event.setCancelled(true);
        }
    }

    /**
     * Handles scheduling of hopper updates for unlocking hoppers via redstone
     * 
     * @param event Event that occrued
     */
    @EventHandler
    public void redstoneHopperInteractionListner(BlockRedstoneEvent event) { // DONE
        // Checks if it was a hopper "unlock" event, or redstone signal going to 0
        if (event.getNewCurrent() != 0)
            return;

        // Block faces that have adjacent hoppers
        final ArrayList<BlockFace> blockFaces = BJUtil.calcAdjacentBlock(event.getBlock(), Material.HOPPER);

        // Checks if no hoppers were nearby
        if (blockFaces.isEmpty())
            return;

        // Checks each face if it is a registered hopper
        for (BlockFace bf : blockFaces) {
            final JukeboxWrapper jbw = bedrockJukebox.getManager()
                    .get((Hopper) event.getBlock().getRelative(bf).getState());

            if (jbw == null)
                return;

            // Schedules an update if it is registered
            bedrockJukebox.getScheduler().scheduleUpdate(jbw, UpdateType.BOTH); // TODO Handle detecting inpuit/output and updateing accordingly
        }
    }

    /**
     * Handles the registration of jukeboxes and hoppers
     * into the {@link JukeboxManager}
     * 
     * @param event Event that occured
     */
    @EventHandler
    public void registerJukeboxComponet(BlockPlaceEvent event) { // DONE
        // Check if the block placed is a Jukebox
        if (event.getBlockPlaced().getType() == Material.JUKEBOX) {
            // Register the jukebox
            bedrockJukebox.registerJukebox((Jukebox) event.getBlock().getState());
            return;

            // Check if the block placed is a Hopper
        } else if (event.getBlockPlaced().getType() == Material.HOPPER) {

            // Check if it was placed against a jukebox
            if (event.getBlockAgainst().getType() == Material.JUKEBOX) {
                // Register input hopper
                final JukeboxWrapper jbw = bedrockJukebox.getManager()
                        .get((Jukebox) event.getBlockAgainst().getState());
                final BlockFace bf = BJUtil.calcHopperFacing(jbw.getJukebox(), event.getBlock());
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

    /**
     * Handles the deregistration of jukeboxes and hoppers
     * 
     * @param event Event that occured
     */
    @EventHandler
    public void deregisterJukeboxComponet(BlockBreakEvent event) { // DONE
        // If the block was a jukebox, deregister it
        if (event.getBlock().getType() == Material.JUKEBOX) {
            bedrockJukebox.deregisterJukebox((Jukebox) event.getBlock().getState());
            bedrockJukebox.getLogger().debug("Registred jukebox removed");

            // If the block was a hopper
        } else if (event.getBlock().getType() == Material.HOPPER) {

            // Check if this is a registered hopper
            final JukeboxWrapper jbw = bedrockJukebox.getManager().get((Hopper) event.getBlock().getState());
            if (jbw == null) {
                bedrockJukebox.getLogger().debug("Hopper destoryed was not registered. Ignoring...");
                return;
            }

            final BlockFace bf = BJUtil.calcHopperFacing(jbw.getJukebox(), event.getBlock());
            if (bf.equals(BlockFace.DOWN)) { // Handles output hoppers
                jbw.removeOutputHopper();
                bedrockJukebox.getLogger().debug("Removed output hopper");
            } else { // Handles input hoppers
                jbw.removeInput(bf);
                bedrockJukebox.getLogger().debug("Removed input hopper to the " + bf);
            }
        }
    }

    /**
     * Handles the registration of jukeboxes in chunks load
     * 
     * @param event Event that occured
     */
    @EventHandler
    public void chunkListener(ChunkLoadEvent event) { // DONE
        bedrockJukebox.getLogger().debug("Chunk loaded. Checking for jukeboxes");
        registerJBInChunk(event.getChunk());
    }

    /**
     * Handles the registration of jukeboxes in chunks when a world loads
     * 
     * @param event Event that occured
     */
    @EventHandler
    public void worldListener(WorldLoadEvent event) { // DONE
        bedrockJukebox.getLogger().debug("World '" + event.getWorld().getName() + "' loaded. Checking for jukeboxes");
        for (Chunk c : event.getWorld().getLoadedChunks()) {
            registerJBInChunk(c);
        }
    }

    /**
     * Registers unregistered jukeboxes in chunk
     * 
     * @param c Chunk to check for jukeboxes in
     */
    public void registerJBInChunk(Chunk c) { // DONE
        for (BlockState bs : c.getTileEntities()) {
            if (bs instanceof Jukebox) {
                // If Jukebox is already registred, ignore
                if (bedrockJukebox.getManager().get((Jukebox) bs) != null) {
                    bedrockJukebox.getLogger().debug("Jukebox found in chunk registered. Ignoring...");
                }
                // If Jukebox was not registred, register it    
                else
                    bedrockJukebox.registerJukebox((Jukebox) bs);
            }
        }
    }
}