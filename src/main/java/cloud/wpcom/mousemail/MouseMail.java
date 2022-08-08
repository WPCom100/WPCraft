package cloud.wpcom.mousemail;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Location;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.gson.Gson;

import cloud.wpcom.WPCraft;

public class MouseMail {

    private final WPCraft wpcraft;
    public ArrayList<Mailbox> mailboxes = new ArrayList<>();

    public MouseMail(WPCraft wpcraft) throws IOException {
        this.wpcraft = wpcraft;

        load();
    }

    public void addMailbox(UUID uuid, Location l) {
        mailboxes.add(new Mailbox(l, uuid, false)); // hasMail: checkMail(wpcraft.getServer().getPlayer(uuid))
        save();
    }

    public void removeMailbox(UUID uuid) {
        for (Mailbox mailbox : mailboxes) {
            if (mailbox.getUUID().equals(uuid)) {
                mailboxes.remove(mailbox);
                return;
            }
        }
    }

    public Chest getMailbox(UUID uuid) {
        for (Mailbox mailbox : mailboxes) {
            if (mailbox.getUUID().equals(uuid)) {
                return (Chest) mailbox.getLocation().getBlock();
            }
        }
        return null; // TODO CHECK FOR NULL WHEN USED
    }

    public ArrayList<Mailbox> getMailboxes() {
        return this.mailboxes;
    }

    public boolean checkMail(Player player) {
        return !getMailbox(player.getUniqueId()).getInventory().isEmpty();
    }

    public void save() {

        // new BukkitRunnable() {
        // @Override
        // public void run() {
        try {
            Gson gson = new Gson();
            File file = new File(wpcraft.getDataFolder().getAbsolutePath() + "/mailboxes.json");
            Writer writer = new FileWriter(file, false);
            file.createNewFile();
            gson.toJson(getMailboxes(), writer);
            writer.flush();
            writer.close();
            wpcraft.getLogger().log(Level.INFO, "MouseMail mailboxes.json saved!");
        } catch (IOException e) {
            wpcraft.getLogger().log(Level.SEVERE,
                    "MouseMail could not save mailboxes!");
            e.printStackTrace();
        }
        // }
        // }.runTaskAsynchronously(wpcraft);

        // STOPPED HERE TRY TO MAKE LOCATION X,Y,Z INSTED?

    }

    private void load() {
        try {
            Gson gson = new Gson();
            File file = new File(wpcraft.getDataFolder().getAbsolutePath() + "/mailboxes.json");

            if (file.exists()) {
                Reader reader = new FileReader(file);
                Mailbox[] m = gson.fromJson(reader, Mailbox[].class);
                if (m != null) {
                    mailboxes = new ArrayList<>(Arrays.asList(m));
                    wpcraft.getLogger().log(Level.INFO, "mailboxes.json loaded!");
                    return;
                } else {
                    wpcraft.getLogger().log(Level.INFO, "mailboxes.json empty. Ignoring.");
                    return;
                }
            }

            wpcraft.getLogger().log(Level.WARNING,
                    "MouseMail could not find mailboxes.json! Creating the file for you...");
            file.getParentFile().mkdirs();
            return;

        } catch (IOException e) {
            wpcraft.getLogger().log(Level.SEVERE, "MouseMail could not load mailboxes!");
            e.printStackTrace();
        }
    }
}
