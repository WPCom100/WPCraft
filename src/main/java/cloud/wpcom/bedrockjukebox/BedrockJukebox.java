package cloud.wpcom.bedrockjukebox;

import java.util.ArrayList;
import org.bukkit.Location;
import org.bukkit.block.BlockState;
import org.bukkit.block.Jukebox;
import org.bukkit.scheduler.BukkitRunnable;

import cloud.wpcom.WPCraft;

public class BedrockJukebox {

    private ArrayList<JukeboxWrapper> jukeboxes = new ArrayList<JukeboxWrapper>(0);

    public void addJukebox(Jukebox jb, WPCraft plugin) {
        JukeboxWrapper j = new JukeboxWrapper(jb);
        jukeboxes.add(j);
        JBUtil.registerInputHoppers(j);
        JBUtil.registerOutputHopper(j);
        // Plays disc from input hopper on next tick
        if (j.hasInputHopper()) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    j.playRecord(j.popWaitingDisc(), plugin);
                }
            }.runTask(plugin);
        }
    }

    public void removeJukebox(Jukebox jb) {
        jukeboxes.remove(getJukeboxAt(jb.getLocation()));
        WPCraft.server.broadcastMessage("Registered jukebox removed!");
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
