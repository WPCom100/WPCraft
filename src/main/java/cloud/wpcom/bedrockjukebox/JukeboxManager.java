package cloud.wpcom.bedrockjukebox;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.block.Jukebox;

import cloud.wpcom.WPCraft;

public class JukeboxManager {

    private final WPCraft wpcraft;
    private final BedrockJukebox bedrockJukebox;
    private Map<Jukebox, JukeboxWrapper> jukeboxes;

    /**
     * Manages and stores information on jukeboxes in the world
     * 
     * @param wpcraft Instance of the plugin
     * @param bedrockJukebox Instance of the {@link BedrockJukebox} class
     */
    public JukeboxManager(WPCraft wpcraft, BedrockJukebox bedrockJukebox) {
        this.wpcraft = wpcraft;
        this.bedrockJukebox = bedrockJukebox;
        jukeboxes = new HashMap<>();
    }

    /**
     * Adds a jukebox to the manager 
     * 
     * @param jb A bukkit Jukebox
     */
    public void add(Jukebox jb) {
        jukeboxes.put(jb, new JukeboxWrapper(jb));

        // JukeboxWrapper jbw = new JukeboxWrapper(jb);
        // jukeboxes.add(jbw);
        BJUtil.registerInputHoppers(jbw);
        BJUtil.registerOutputHopper(jbw);
        // Plays disc from input hopper on next tick
        if (jbw.hasInputHopper()) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    jbw.playRecord(jbw.popWaitingDisc(), plugin);
                }
            }.runTask(plugin);
        }
    }

    /**
     * Removes a jukebox from the manager
     * 
     * @param jb The jukebox to remove
     */
    public void remove(Jukebox jb) {
        // Get the jukebox wrapper for the appropriate jukebox
        final JukeboxWrapper jbw = jukeboxes.get(jb);

        // Stop any running tasks before removing it
        if (jbw.durationTask != null) {
            jbw.durationTask.cancel();
        }
        jukeboxes.remove(jb);
    }
    
}
