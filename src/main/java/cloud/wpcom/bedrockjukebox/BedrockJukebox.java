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

    public BedrockJukebox(WPCraft wpcraft) {
        this.wpcraft = wpcraft;
    }

    public void addJukebox(Jukebox jb, WPCraft plugin) {
        JukeboxWrapper jbw = new JukeboxWrapper(wpcraft, jb);
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

    public void removeJukebox(Jukebox jb) {
        // Get the jukebox wrapper for the appropriate jukebox
        JukeboxWrapper jbw = getJukeboxAt(jb.getLocation());

        // Stop any running tasks before removing it
        if (jbw.durationTask != null)
            jbw.durationTask.cancel();
        jukeboxes.remove(jbw);
        wpcraft.getServer().broadcastMessage("Registered jukebox removed!");
    }

    public ArrayList<JukeboxWrapper> getJukeboxes() {
        return jukeboxes;
    }

    public JukeboxWrapper getJukeboxAt(Location l) {
        for (JukeboxWrapper jb : jukeboxes) {
            if (jb.getLocation().equals(l)) {
                return jb;
            }
        }
        return null;
    }

    public boolean jukeboxExist(BlockState b) {
        for (JukeboxWrapper j : jukeboxes) {
            if (j.getBlock().getLocation().equals(b.getBlock().getLocation())) {
                return true;
            }
        }
        return false;
    }
}
