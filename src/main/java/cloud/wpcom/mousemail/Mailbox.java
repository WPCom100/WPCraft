package cloud.wpcom.mousemail;

import java.io.Serializable;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.block.Chest;

import cloud.wpcom.WPCraft;

public class Mailbox implements Serializable {

    private static final long serialVersionUID = 1L;
    private final MMLocation location;
    private final UUID uuid;

    /**
     * 
     * Data class for storeing informaiton about the MouseMail mailboxes
     * 
     * @param location Location of the mailbox
     * @param uuid     UUID of the owner of the mailbox
     * 
     */
    public Mailbox(MMLocation location, UUID uuid) {
        this.location = location;
        this.uuid = uuid;
    }

    public Location getLocation(WPCraft wpcraft) {
        return this.location.getLocation(wpcraft);
    }

    public UUID getUUID() {
        return this.uuid;
    }

    public boolean checkMail(WPCraft wpcraft) {
        if (((Chest) getLocation(wpcraft).getBlock().getState()).getInventory().isEmpty()) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "UUID: " + getUUID() + "\n" + "Location: " + location;
    }

}
