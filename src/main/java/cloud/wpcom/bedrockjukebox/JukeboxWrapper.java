package cloud.wpcom.bedrockjukebox;

import org.bukkit.Location;
import org.bukkit.block.Jukebox;

public class JukeboxWrapper {

    private Jukebox jukebox;
    private Location location;
    
    public JukeboxWrapper(Jukebox j) {
        
        this.jukebox = j;
        this.location = this.jukebox.getLocation();
        
    }

    public Location getLocation() {

        return location;

    }

    public void setLocation(Location l) {

        location = l;

    }

    public Jukebox getblock() {

        return jukebox;

    }

    
}
