package cloud.wpcom.mousemail;

import java.util.UUID;

import org.bukkit.Location;

public class Mailbox {

    private final Location location;
    private final UUID uuid;
    private Boolean hasMail;

    /**
     * 
     * Data class for storeing informaiton about the MouseMail mailboxes
     * 
     * @param location Location of the mailbox
     * @param uuid     UUID of the owner of the mailbox
     * @param hasMail  If the mailbox currently has mail
     * 
     */
    public Mailbox(Location location, UUID uuid, Boolean hasMail) {
        this.location = location;
        this.uuid = uuid;
        this.hasMail = hasMail;
    }

    public Location getLocation() {
        return this.location;
    }

    public UUID getUUID() {
        return this.uuid;
    }

    public Boolean getHasMail() {
        return this.hasMail;
    }

    public void setHasMail(Boolean hasMail) {
        this.hasMail = hasMail;
    }
}
