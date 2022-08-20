package cloud.wpcom.bedrockjukebox;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.block.BlockState;
import org.bukkit.block.Jukebox;
import org.bukkit.scheduler.BukkitRunnable;

import cloud.wpcom.WPCraft;

public class BedrockJukebox {

    private final WPCraft wpcraft;
    private ArrayList<JukeboxWrapper> jukeboxes = new ArrayList<JukeboxWrapper>(0);
    private final JukeboxManager jukeboxManager;

    public BedrockJukebox(WPCraft wpcraft) {
        this.wpcraft = wpcraft;
        jukeboxManager = new JukeboxManager(this.wpcraft, this);
    }

    public void registerJukebox(Jukebox jb) {
        jukeboxManager.add(jb); // Check for input hoppers etc here?
    }

    public void deregisterJukebox(Jukebox jb) {
        jukeboxManager.remove(jb);
    }  

    @Deprecated
    public void addJukebox(Jukebox jb, WPCraft plugin) {
        JukeboxWrapper jbw = new JukeboxWrapper(jb);
        jukeboxes.add(jbw);
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

    @Deprecated
    public void removeJukebox(Jukebox jb) {
        // Get the jukebox wrapper for the appropriate jukebox
        JukeboxWrapper jbw = getJukeboxAt(jb.getLocation());

        // Stop any running tasks before removing it
        if (jbw.durationTask != null)
            jbw.durationTask.cancel();
        jukeboxes.remove(jbw);
    }

    @Deprecated
    public ArrayList<JukeboxWrapper> getJukeboxes() {
        return jukeboxes;
    }

    @Deprecated
    public JukeboxWrapper getJukeboxAt(Location l) {
        for (JukeboxWrapper jb : jukeboxes) {
            if (jb.getLocation().equals(l)) {
                return jb;
            }
        }
        return null;
    }

    @Deprecated
    public boolean jukeboxExist(BlockState b) {
        for (JukeboxWrapper j : jukeboxes) {
            if (j.getBlock().getLocation().equals(b.getBlock().getLocation())) {
                return true;
            }
        }
        return false;
    }

    public JukeboxManager getManager() {
        return jukeboxManager;
    }
}
