package cloud.wpcom.bedrockjukebox;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Hopper;
import org.bukkit.block.Jukebox;

import cloud.wpcom.WPCraft;
import cloud.wpcom.WPLogger;
import cloud.wpcom.bedrockjukebox.JukeboxScheduler.UpdateType;

public class BedrockJukebox {

    private final WPCraft wpcraft;
    private ArrayList<JukeboxWrapper> jukeboxes = new ArrayList<JukeboxWrapper>(0);
    private final JukeboxManager jukeboxManager;
    private final JukeboxScheduler jukeboxScheduler;
    private final WPLogger logger;

    public BedrockJukebox(WPCraft wpcraft) {
        this.wpcraft = wpcraft;
        this.logger = new WPLogger(wpcraft, "BedrockJukebox", true);
        jukeboxManager = new JukeboxManager(this.wpcraft, this);
        jukeboxScheduler = new JukeboxScheduler(this.wpcraft, this);
    }

    public void registerJukebox(Jukebox jb) {
        final JukeboxWrapper jbw = jukeboxManager.add(jb);
        final ArrayList<BlockFace> bf = BJUtil.calcHopperFaces(jb);
        //Register the jukebox
        jukeboxManager.add(jb);

        // Register output and input hoppers
        if (bf.contains(BlockFace.DOWN)) {
            jukeboxManager.registerOutputHopper(jb, (Hopper) jb.getBlock().getRelative(BlockFace.DOWN).getState());
            bf.remove(BlockFace.DOWN);
        }
        bf.forEach(face -> jukeboxManager.registerInputHopper(jb, (Hopper) jb.getBlock().getRelative(face).getState(), face));
        
        // Schedule input hopper update
        getScheduler().scheduleUpdate(jbw, UpdateType.INPUT);
    }

    public void deregisterJukebox(Jukebox jb) {
        // Stop any running tasks before removing it
        if (jukeboxManager.get(jb).getDurationTask() != null)
            jukeboxManager.get(jb).clearDurationTask();

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

    /**
     * Get the {@link WPLogger} for the plugin
     * 
     * @return The logger
     */
    public WPLogger getLogger() {
        return logger;
    }
}
