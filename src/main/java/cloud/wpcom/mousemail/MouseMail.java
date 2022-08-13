package cloud.wpcom.mousemail;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.scheduler.BukkitRunnable;

import cloud.wpcom.WPCraft;
import cloud.wpcom.offlineplayermanager.OfflinePlayerManager;

public class MouseMail {

    private final WPCraft wpcraft;
    private final OfflinePlayerManager offlinePlayerManager;
    private final File file;
    private Mailboxes mailboxes;

    public MouseMail(WPCraft wpcraft, OfflinePlayerManager offlinePlayerManager) throws IOException {
        this.wpcraft = wpcraft;
        this.offlinePlayerManager = offlinePlayerManager;
        mailboxes = new Mailboxes(this.offlinePlayerManager);
        file = new File(wpcraft.getDataFolder().getAbsolutePath() + "/mailboxes.dat");

        // Check if file exists, if not creates the directory and blank file
        if (!file.exists()) {
            wpcraft.getLogger().log(Level.INFO, "mailboxes.dat not found. Creating it.");
            file.getParentFile().mkdirs();
            file.createNewFile();
            return;
        }

        // If it does, load it
        wpcraft.getLogger().log(Level.INFO, "Trying to load mailboxes.dat...");
        load();
    }

    public void addMailbox(UUID uuid, MMLocation location) {
        mailboxes.add(uuid, location);
        save();
    }

    public void removeMailbox(UUID uuid) {
        mailboxes.remove(uuid);
        save();
    }

    public Mailboxes getMailboxes() {
        return mailboxes;
    }

    public void save() {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    FileOutputStream fos = new FileOutputStream(file);
                    BufferedOutputStream bos = new BufferedOutputStream(fos);
                    ObjectOutputStream oos = new ObjectOutputStream(bos);
                    oos.writeObject(mailboxes);
                    oos.close();

                } catch (IOException e) {
                    wpcraft.getLogger().log(Level.SEVERE, "Error saving mailboxes:");
                    e.printStackTrace();
                }
            }
        }.runTaskAsynchronously(wpcraft);
    }

    private void load() {
        try {
            FileInputStream fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis);
            ObjectInputStream ois = new ObjectInputStream(bis);
            mailboxes = (Mailboxes) ois.readObject();
            ois.close();
        } catch (EOFException e) {
            wpcraft.getLogger().log(Level.WARNING, "mailboxes.dat empty. Skipping...");
        } catch (IOException e) {
            wpcraft.getLogger().log(Level.SEVERE, "Error loading mailboxes:");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            wpcraft.getLogger().log(Level.SEVERE, "Error loading mailboxes:");
            e.printStackTrace();
        }
    }
}
