package cloud.wpcom.bedrockjukebox;

import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import cloud.wpcom.bedrockjukebox.JukeboxScheduler.UpdateType;

public class BJDiscDurationTask extends BukkitRunnable {

    private final JukeboxWrapper jbw;
    private final BedrockJukebox bedrockJukebox;

    public BJDiscDurationTask(BedrockJukebox bedrockJukebox, JukeboxWrapper jbw) {
        bedrockJukebox.getLogger().debug("Duration task created");
        this.bedrockJukebox = bedrockJukebox;
        this.jbw = jbw;
    }
    
    // Runs at the supposed end of the playing disc
    @Override
    public void run() {
        jbw.setPlaying(false);
        jbw.clearDurationTask();

        // Check for output hopper
        if (!jbw.hasOutputHopper())
            return;

        // Check if output hopper is full
        if (jbw.getOutputInventory().addItem(new ItemStack(jbw.getJukebox().getPlaying())).size() == 1) {
            bedrockJukebox.getLogger().debug("Output hopper full");
            return;
        }

        // Clear playing disc
        bedrockJukebox.getLogger().debug("Output Disc to Output Hopper " + jbw.getJukebox().getPlaying());
        jbw.clearPlaying();
        jbw.getJukebox().update();

        // Schedule input hopper check
        bedrockJukebox.getScheduler().scheduleUpdate(jbw, UpdateType.INPUT);
    }
    
    // Runs when a player cancles the playing of a disc manuely
    @Override
    public void cancel() {
        bedrockJukebox.getLogger().debug("Duration task canceled");
        
        // Clear playing disc
        jbw.setPlaying(false);
        jbw.clearPlaying();

        // Schedule input hopper check
        bedrockJukebox.getScheduler().scheduleUpdate(jbw, UpdateType.INPUT);

        super.cancel();
    }
}
