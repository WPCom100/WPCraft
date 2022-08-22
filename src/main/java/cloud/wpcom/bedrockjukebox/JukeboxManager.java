package cloud.wpcom.bedrockjukebox;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import org.bukkit.block.BlockFace;
import org.bukkit.block.Hopper;
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
    }

    /**
     * Removes a jukebox from the manager
     * 
     * @param jb The jukebox to remove
     */
    public void remove(Jukebox jb) {
        jukeboxes.remove(jb);
    }

    /**
     * Gets the {@link JukeboxWrapper} for the given jukebox. Returns
     * {@code null} if not found.
     * 
     * @param jb Jukebox to look for
     * @return Wrapper of the associated jukebox or null
     */
    @Nullable
    public JukeboxWrapper get(Jukebox jb) {
        return jukeboxes.get(jb);
    }

    /**
     * Gets the {@link JukeboxWrapper} for the given hopper. Returns
     * {@code null} if not found.
     * 
     * @param hopper Hopper to look for
     * @return Wrapper of the associated jukebox or null
     */
    @Nullable
    public JukeboxWrapper get(Hopper hopper) {
        for (JukeboxWrapper jbw : jukeboxes.values()) {
            // Check if jukebox has input hoppers
            if (!jbw.hasInputHopper()) {
                continue;
            }

            // If so, go through them and compare to the given hopper
            for (Hopper h : jbw.getInputs()) {
                if (h.equals(hopper)) {
                    return jbw;
                }
            }
        }

        return null;
    }

    /**
     * Register an input hopper to a jukebox. A jukebox
     * can have several input hoppers.
     * 
     * @param jb Jukebox to register hopper with
     * @param hopper Hopper to be registered
     */
    public void registerInputHopper(Jukebox jb, Hopper hopper, BlockFace bf) {
        get(jb).addInput(hopper, bf);
    }

    /**
     * Register an output hopper to a jukebox. A jukebox can only
     * have one output hopper. Running this a second time will replace
     * the currently registered output hopper.
     * 
     * @param jb Jukebox to register hopper with
     * @param hopper Hopper to be registered
     */
    public void registerOutputHopper(Jukebox jb, Hopper hopper) {
        get(jb).setOutput(hopper);
    }
    
}
