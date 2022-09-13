package cloud.wpcom.bedrockjukebox;

import java.util.ArrayList;

import org.bukkit.block.BlockFace;
import org.bukkit.block.Hopper;
import org.bukkit.block.Jukebox;

import cloud.wpcom.WPCraft;
import cloud.wpcom.WPLogger;
import cloud.wpcom.bedrockjukebox.JukeboxScheduler.UpdateType;

public class BedrockJukebox {

    private final WPCraft wpcraft;
    private final JukeboxManager jukeboxManager;
    private final JukeboxScheduler jukeboxScheduler;
    private final WPLogger logger;

    public BedrockJukebox(WPCraft wpcraft) {
        this.wpcraft = wpcraft;
        this.logger = new WPLogger(this.getPlugin(), "BedrockJukebox", true);
        jukeboxManager = new JukeboxManager(this);
        jukeboxScheduler = new JukeboxScheduler(this);
    }

    /**
     * Registeres a jukebox with the {@link JukeboxManager}
     * 
     * @param jb Jukebox to register
     */
    public void registerJukebox(Jukebox jb) {
        final JukeboxWrapper jbw = jukeboxManager.add(jb);
        final ArrayList<BlockFace> bf = BJUtil.calcHopperFaces(jb);
        getLogger().debug("Jukebox registered");

        // Register output and input hoppers
        if (bf.contains(BlockFace.DOWN)) {
            jukeboxManager.registerOutputHopper(jb, (Hopper) jb.getBlock().getRelative(BlockFace.DOWN).getState());
            bf.remove(BlockFace.DOWN);
        }
        bf.forEach(face -> jukeboxManager.registerInputHopper(jb, (Hopper) jb.getBlock().getRelative(face).getState(),
                face)); // TODO Handle no blockfaces!!

        // Schedule input hopper update
        getScheduler().scheduleUpdate(jbw, UpdateType.INPUT);
    }

    /**
     * Deregisteres a jukebox with the {@link JukeboxManager}
     * 
     * @param jb Jukebox to deregister
     */
    public void deregisterJukebox(Jukebox jb) {
        // Stop any running tasks before removing it
        if (jukeboxManager.get(jb).getDurationTask() != null)
            jukeboxManager.get(jb).clearDurationTask();

        jukeboxManager.remove(jb);
        getLogger().debug("Jukebox deregistered");
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

    /**
     * Get the {@link WPCraft} plugin
     * 
     * @return WPCraft plugin
     */
    public WPCraft getPlugin() {
        return wpcraft;
    }

}
