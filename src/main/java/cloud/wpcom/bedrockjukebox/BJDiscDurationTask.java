package cloud.wpcom.bedrockjukebox;

import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import cloud.wpcom.WPCraft;

public class BJDiscDurationTask extends BukkitRunnable {

    private final WPCraft wpcraft;
    private final JukeboxWrapper j;

    public BJDiscDurationTask(WPCraft wpcraft, JukeboxWrapper j) {
        wpcraft.getServer().broadcastMessage("Duration task created");
        this.wpcraft = wpcraft;
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
        if (j.getOutputInventory().addItem(new ItemStack(j.getBlock().getPlaying())).size() == 1) {
            wpcraft.getServer().broadcastMessage("Output Hopper full");
            return;
        }
        wpcraft.getServer().broadcastMessage("Output Disc to Output Hopper " + j.getBlock().getPlaying());
        j.clearPlaying();
        j.getBlock().update();

        // Check for input hopper and play the next disc
        if (j.hasInputHopper())
            BJUtil.playNext(j, wpcraft);
    }
    
    // Runs when a player cancles the playing of a disc manuely
    @Override
    public void cancel() {
        wpcraft.getServer().broadcastMessage("Duration task cancled");
        j.setPlaying(false);
        j.clearPlaying();
        // Schedule next disc to play after disc is ejected in next tick
        new BukkitRunnable() {
            @Override
            public void run() {
                if (j.hasInputHopper())
                    BJUtil.playNext(j, wpcraft);
            }
        }.runTask(wpcraft);

        super.cancel();
    }
}
