package cloud.wpcom.mousemail;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.entity.Player;

import cloud.wpcom.offlineplayermanager.OfflinePlayerManager;

public class Mailboxes implements Serializable {

    private ArrayList<Mailbox> mailboxes;

    public Mailboxes(OfflinePlayerManager offlinePlayerManager) {
        mailboxes = new ArrayList<>();
    }

    public Mailbox getMailbox(Player player) {
        for (Mailbox mailbox : mailboxes) {
            if (mailbox.getUUID().equals(player.getUniqueId())) {
                return mailbox;
            }
        }
        return null;
    }

    public void add(UUID uuid, MMLocation location) {
        mailboxes.add(new Mailbox(location, uuid));
    }

    public void remove(UUID uuid) {
        for (Mailbox mailbox : mailboxes) {
            if (mailbox.getUUID().equals(uuid)) {
                mailboxes.remove(mailbox);
                return;
            }
        }
    }

    public ArrayList<String> getMailboxOwners(OfflinePlayerManager offlinePlayerManager) {
        ArrayList<String> owners = new ArrayList<>();
        for (Mailbox mailbox : mailboxes) {
            owners.add(offlinePlayerManager.getPlayer(mailbox.getUUID()).getName());
        }
        return owners;
    }
}
