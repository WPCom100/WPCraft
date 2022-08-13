package cloud.wpcom.mousemail;

import java.util.logging.Level;

import javax.annotation.Nonnull;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import cloud.wpcom.WPCraft;

public class MMJoinEvent implements Listener {

    private final WPCraft wpcraft;
    private final MouseMail mouseMail;

    public MMJoinEvent(@Nonnull WPCraft wpcraft, @Nonnull MouseMail mouseMail) {
        this.wpcraft = wpcraft;
        this.mouseMail = mouseMail;
    }

    @EventHandler
    public void checkMail(PlayerJoinEvent event) {
        // Check if the player has a mailbox
        if (mouseMail.getMailboxes().getMailbox(event.getPlayer()) == null) {
            wpcraft.getLogger().log(Level.INFO, "No mailbox found for " + event.getPlayer().getName());
            return;
        }

        // Check if they have mail next tick
        new BukkitRunnable() {
            @Override
            public void run() {
                if (mouseMail.getMailboxes().getMailbox(event.getPlayer()).checkMail(wpcraft)) {
                    wpcraft.getLogger().log(Level.INFO, "Mailbox found with mail for " + event.getPlayer().getName());
                    event.getPlayer().sendMessage(ChatColor.GREEN + "You have mail!");
                    return;
                }
                wpcraft.getLogger().log(Level.INFO, "Mailbox found without mail for " + event.getPlayer().getName());
                event.getPlayer().sendMessage(ChatColor.RED + "No new mail today.");
            }
        }.runTask(wpcraft);

    }
}
