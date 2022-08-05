package cloud.wpcom.tasks;

import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import cloud.wpcom.WPCraft;
import cloud.wpcom.bedrockjukebox.BJUtil;
import cloud.wpcom.bedrockjukebox.JukeboxWrapper;

public class BJDiscDurationTask extends BukkitRunnable {

    WPCraft plugin;
    JukeboxWrapper j;

    public BJDiscDurationTask(JukeboxWrapper j, WPCraft plugin) {
        WPCraft.server.broadcastMessage("Duration task created");
        this.plugin = plugin;
        this.j = j;
    }
    
    // Runs at the supposed end of the playing disc
    @Override
    public void run() {
        j.setPlaying(false);
        j.durationTask = null;

        // Check for output hopper
        if (!j.hasOutputHopper())
            return;

        // Check if output hopper is full
        if (j.getOutputHopperInventory().addItem(new ItemStack(j.getBlock().getPlaying())).size() == 1) {
            WPCraft.server.broadcastMessage("Output Hopper full");
            return;
        }
        WPCraft.server.broadcastMessage("Output Disc to Output Hopper " + j.getBlock().getPlaying());
        j.clearPlaying();
        j.getBlock().update();

        // Check for input hopper and play the next disc
        if (j.hasInputHopper())
            BJUtil.playNext(j, plugin);
    }
    
    // Runs when a player cancles the playing of a disc manuely
    @Override
    public void cancel() {
        WPCraft.server.broadcastMessage("Duration task cancled");
        j.setPlaying(false);
        j.clearPlaying();
        // Schedule next disc to play after disc is ejected in next tick
        new BukkitRunnable() {
            @Override
            public void run() {
                if (j.hasInputHopper())
                    BJUtil.playNext(j, plugin);
            }
        }.runTask(plugin);

        super.cancel();
    }
}
