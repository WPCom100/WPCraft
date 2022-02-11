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
    public void run() { //TODO HANDLE IF HOPPER IS FULL
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


        // If the Jukebox has an output hopper, place the current playing disc into it
        if (jukeboxw.hasOutputHopper()) {
            WPCraft.server
                    .broadcastMessage("Output Disc to Output Hopper " + jukeboxw.getBlock().getPlaying().toString());
            jukeboxw.getOutputHopperInventory().addItem(new ItemStack(jukeboxw.getBlock().getPlaying()));
            jukeboxw.getBlock().setRecord(new ItemStack(Material.AIR));
            jukeboxw.getBlock().update();
        }
        playNext();
    }
    
    // Runs when a player cancles the playing of a disc manuely
    @Override
    public void cancel() {
        WPCraft.server.broadcastMessage("Duration task cancled");
        jukeboxw.setPlaying(false);
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
        int waitingDiscIndex = jukeboxw.getWaitingDisc();
        if (waitingDiscIndex != -1) {
            WPCraft.server.broadcastMessage("Jukebox has a waiting disk at: " + waitingDiscIndex);
            jukeboxw.playRecord(jukeboxw.popInputHopperAtIndex(waitingDiscIndex), plugin);
        }
    }
}
