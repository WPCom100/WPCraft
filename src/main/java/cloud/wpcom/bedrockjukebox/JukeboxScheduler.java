package cloud.wpcom.bedrockjukebox;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.block.Hopper;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import cloud.wpcom.WPCraft;

public class JukeboxScheduler extends BukkitRunnable {

    private final WPCraft wpcraft;
    private final BedrockJukebox bedrockJukebox;
    private Map<JukeboxWrapper, UpdateType> toCheck;

    /**
     * Handles the scheduling of tasks used to check for updates in jukeboxes
     * input and output hoppers.
     * 
     * @param wpcraft WPCraft plugin
     * @param bedrockJukebox Bedrock Jukebox plugin
     */
    public JukeboxScheduler(WPCraft wpcraft, BedrockJukebox bedrockJukebox) {
        this.wpcraft = wpcraft;
        this.bedrockJukebox = bedrockJukebox;
        this.toCheck = new HashMap<>();
        runTaskTimer(wpcraft, 20L, 20L);
    }

    @Override
    public void run() {
        for (JukeboxWrapper jbw : toCheck.keySet()) {
            switch (toCheck.get(jbw)) {
                case INPUT:
                    updateInputHoppers(jbw);
                    toCheck.remove(jbw);
                    continue;
                case OUTPUT:
                    updateOutputHoppers(jbw);
                    toCheck.remove(jbw);
                    continue;
                case BOTH:
                    updateInputHoppers(jbw);
                    updateOutputHoppers(jbw);
                    toCheck.remove(jbw);
                    continue;
            }
        }
    }

    /**
     * Schedules an update to occur next cycle on a jukebox
     * 
     * @param jbw Wrapper of the jukebox to perform the update on
     * @param type Type of update to perform on the jukebox
     */
    public void scheduleUpdate(JukeboxWrapper jbw, UpdateType type) {
        toCheck.put(jbw, type);
    }

    /**
     * Schedules update of input hoppers
     * 
     * @param jbw Jukebox to check
     */
    private void updateInputHoppers(JukeboxWrapper jbw) {
        new BukkitRunnable() {
            @Override
            public void run() {
                // Check if jukebox has an output hopper
                if (!jbw.hasInputHopper())
                    return;

                // Check if a disc is already playing
                if (jbw.isPlaying())
                    return;

                // Go through each hoppers inventory to check if a disc is waiting
                for (Hopper h : jbw.getInputs()) { // TODO Manage how hopper to pull from is selected
                    int index = 0;
                    for (ItemStack i : h.getInventory().getStorageContents()) {
                        if (i instanceof ItemStack && i.getType().isRecord()) {
                            jbw.playRecord(i, wpcraft);
                            h.getInventory().clear(index);
                            return;
                        }
                        index++;
                    }
                }
            }
        }.runTask(wpcraft);
    }

    /**
     * Schedules update of output hopper
     * 
     * @param jbw Jukebox to check
     */
    private void updateOutputHoppers(JukeboxWrapper jbw) {
        new BukkitRunnable() {
            @Override
            public void run() {
                // Check if jukebox has an output hopper
                if (!jbw.hasOutputHopper())
                    return;

                // Checks if jukebox has a waiting disc to eject
                if (jbw.getJukebox().getPlaying() == Material.AIR)
                    return;

                // Checks if output hopper has space for waiting disc
                if (jbw.getOutputInventory().addItem(new ItemStack(jbw.getJukebox().getPlaying())).size() == 1)
                    return;

                // Clears disc in jukebox and schedules input hopper check
                jbw.clearPlaying();
                if (jbw.hasInputHopper()) {
                    scheduleUpdate(jbw, UpdateType.INPUT);
                }
            }
        }.runTask(wpcraft);
    }

    /**
     * Used to describe what type of update task should be run on each jukebox wrapper
     * {@code INPUTHOPPER, OUTPUTHOPPER, BOTH}
     */
    public enum UpdateType {
        INPUT(),
        OUTPUT(),
        BOTH()
    }
    
}
