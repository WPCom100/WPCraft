package cloud.wpcom.bedrockjukebox;

import java.util.ArrayList;
import org.bukkit.Location;
import org.bukkit.block.BlockState;
import org.bukkit.block.Jukebox;

public class BedrockJukebox {

    private ArrayList<JukeboxWrapper> jukeboxes = new ArrayList<JukeboxWrapper>(0);

    public void addJukebox(Jukebox jb) {

        jukeboxes.add(new JukeboxWrapper(jb));

    }

    public void removeJukebox(Jukebox jb) {

        jukeboxes.remove(getJukeboxAt(jb.getLocation()));

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

            if (j.getblock().getLocation().equals(b.getBlock().getLocation())) {

                return true;

            }

        }

        return false;

    }

}
