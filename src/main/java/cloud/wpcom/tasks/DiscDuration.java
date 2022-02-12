package cloud.wpcom.tasks;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import cloud.wpcom.WPCraft;
import cloud.wpcom.bedrockjukebox.JukeboxWrapper;

public class DiscDuration extends BukkitRunnable {

    WPCraft plugin;
    JukeboxWrapper j;

    public DiscDuration(JukeboxWrapper j, WPCraft plugin) {
        this.plugin = plugin;
        this.j = j;
    }
    
    // Runs at the supposed end of the playing disc
    @Override
    public void run() { //TODO HANDLE IF HOPPER IS FULL
        // If the Jukebox has an output hopper, place the current playing disc into it
        if (j.hasOutputHopper()) {
            WPCraft.server
                    .broadcastMessage("Output Disc to Output Hopper " + j.getBlock().getPlaying().toString());
            j.getOutputHopperInventory().addItem(new ItemStack(j.getBlock().getPlaying()));
            j.getBlock().setRecord(new ItemStack(Material.AIR));
            j.getBlock().update();
        }
        
            playNext();
    }
    
    // Runs when a player cancles the playing of a disc manuely
    @Override
    public void cancel() {
        WPCraft.server.broadcastMessage("Duration task cancled");
        j.setPlaying(false);
        super.cancel();
        
        // Schedule next disc to play after disc is ejected in next tick
        BukkitRunnable playTask = new BukkitRunnable() {
            @Override
            public void run() {
                playNext();
            }
        };
        playTask.runTask(plugin);

    }

    // If a disc is waiting in the input hopper, play it next
    public void playNext() {
        int waitingDiscIndex = j.getWaitingDisc();
        if (waitingDiscIndex != -1) {
            WPCraft.server.broadcastMessage("Jukebox has a waiting disk at: " + waitingDiscIndex);
            j.playRecord(j.popInputHopperAtIndex(waitingDiscIndex), plugin);
        }
    }
}
