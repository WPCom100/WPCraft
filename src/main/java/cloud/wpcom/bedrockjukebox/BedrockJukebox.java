package cloud.wpcom.bedrockjukebox;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.block.BlockState;
import org.bukkit.block.Jukebox;

import cloud.wpcom.WPCraft;

public class BedrockJukebox {

    private final WPCraft wpcraft;
    private ArrayList<JukeboxWrapper> jukeboxes = new ArrayList<JukeboxWrapper>(0);
    private final JukeboxManager jukeboxManager;
    private final JukeboxScheduler jukeboxScheduler;

    public BedrockJukebox(WPCraft wpcraft) {
        this.wpcraft = wpcraft;
        jukeboxManager = new JukeboxManager(this.wpcraft, this);
        jukeboxScheduler = new JukeboxScheduler(this.wpcraft, this);
    }

    public void registerJukebox(Jukebox jb) {
        jukeboxManager.add(jb);
        BJUtil.registerInputHoppers(jukeboxManager, jb);
        BJUtil.registerOutputHopper(jukeboxManager, jb);
        
        // TODO schedule input  hopper check
    }

    public void deregisterJukebox(Jukebox jb) {
        // Stop any running tasks before removing it
        if (jukeboxManager.get(jb).durationTask != null) // TODO Duration task private instead of public
            jukeboxManager.get(jb).durationTask.cancel();

        jukeboxManager.remove(jb);
    }  

    @Deprecated // REPLACED registerJukebox
    public void addJukebox(Jukebox jb, WPCraft plugin) {
        // JukeboxWrapper jbw = new JukeboxWrapper(jb);
        // jukeboxes.add(jbw);
        // // BJUtil.registerInputHoppers(jbw);
        // // BJUtil.registerOutputHopper(jbw);
        // // Plays disc from input hopper on next tick
        // if (jbw.hasInputHopper()) {
        //     new BukkitRunnable() {
        //         @Override
        //         public void run() {
        //             jbw.playRecord(jbw.popWaitingDisc(), plugin);
        //         }
        //     }.runTask(plugin);
        // }
    }

    @Deprecated // REPLACED deregisterJukebox
    public void removeJukebox(Jukebox jb) {
        // // Get the jukebox wrapper for the appropriate jukebox
        // JukeboxWrapper jbw = getJukeboxAt(jb.getLocation());

        // // Stop any running tasks before removing it
        // if (jbw.durationTask != null)
        //     jbw.durationTask.cancel();
        // jukeboxes.remove(jbw);
    }

    @Deprecated // REMOVE
    public ArrayList<JukeboxWrapper> getJukeboxes() {
        return jukeboxes;
    }

    @Deprecated // REMOVE, or move to jukebox manager if needed
    public JukeboxWrapper getJukeboxAt(Location l) {
        for (JukeboxWrapper jb : jukeboxes) {
            if (jb.getLocation().equals(l)) {
                return jb;
            }
        }
        return null;
    }

    @Deprecated // REMOVE
    public boolean jukeboxExist(BlockState b) {
        for (JukeboxWrapper j : jukeboxes) {
            if (j.getJukebox().getLocation().equals(b.getBlock().getLocation())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get the {@link JukeboxManager} for the plugin
     * 
     * @return The manager 
     */
    public JukeboxManager getManager() {
        return jukeboxManager;
    }

    /**
     * Get the {@link JukeboxScheduler} for the plugin
     * 
     * @return The scheduler
     */
    public JukeboxScheduler getScheduler() {
        return jukeboxScheduler;
    }
}
