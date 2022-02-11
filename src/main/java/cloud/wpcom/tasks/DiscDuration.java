package cloud.wpcom.tasks;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import cloud.wpcom.WPCraft;
import cloud.wpcom.bedrockjukebox.JukeboxWrapper;

public class DiscDuration extends BukkitRunnable {

    WPCraft plugin;
    JukeboxWrapper jukeboxw;

    public DiscDuration(JukeboxWrapper jukeboxw, WPCraft plugin) {
        this.plugin = plugin;
        this.jukeboxw = jukeboxw;
    }
    
    // Runs at the supposed end of the playing disc
    @Override
    public void run() {
        jukeboxw.setPlaying(false);

        // If the Jukebox has an output hopper, place the current playing disc into it
        if (jukeboxw.hasOutputHopper()) {
            jukeboxw.getInputHopperInventory().addItem(new ItemStack(jukeboxw.getBlock().getPlaying()));
            //TODO HANDLE IF HOPPER IS FULL
            jukeboxw.getBlock().setRecord(new ItemStack(Material.AIR));
        }

        // If a disc is waiting in the input hopper, play it next
        if (jukeboxw.hasWaitingDisc() != -1)
            jukeboxw.playRecord(jukeboxw.popWaitingDisc(), plugin);
    }

    @Override
    public void cancel() {
        super.cancel(); // TODO MIGHT NOT BE NEEDED
    }

}
