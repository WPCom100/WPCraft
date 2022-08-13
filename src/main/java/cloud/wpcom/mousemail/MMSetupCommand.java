package cloud.wpcom.mousemail;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import cloud.wpcom.offlineplayermanager.OfflinePlayerManager;
import cloud.wpcom.offlineplayermanager.OfflinePlayerManager.OfflinePlayer;

public class MMSetupCommand implements CommandExecutor {

    private final MouseMail mouseMail;
    private final OfflinePlayerManager offlinePlayerManager;

    public MMSetupCommand(MouseMail mouseMail, OfflinePlayerManager offlinePlayerManager) {
        this.mouseMail = mouseMail;
        this.offlinePlayerManager = offlinePlayerManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Check for at least one argument
        if (args.length == 0) {
            return false;
        }

        // Check if they are requesting a list of all mailboxes
        if (args[0].equals("list")) {
            sender.sendMessage(mouseMail.getMailboxes().getMailboxOwners(offlinePlayerManager).toString());
            return true;
        }

        // Check if they are removing a player
        if (args[0].equals("remove")) {
            final OfflinePlayer player = offlinePlayerManager.getPlayer(args[1]);
            if (player != null) {
                mouseMail.removeMailbox(player.getUUID());
                sender.sendMessage(ChatColor.GREEN + "Player '" + args[1] + "' mailbox removed!");
                return true;
            }
            sender.sendMessage(ChatColor.RED + "Player '" + args[1] + "' not found!");
            return true;
        }

        // Check if they included x,y,z
        if (args.length != 4) {
            return false;
        }

        // Check if the player already has a mailbox
        for (String player : mouseMail.getMailboxes().getMailboxOwners(offlinePlayerManager)) {
            if (player.equals(args[0])) {
                sender.sendMessage(ChatColor.RED + "Player '" + args[0] + "' mailbox already exists!");
                return true;
            }
        }

        // Try to find the player and add a mailbox for them
        final OfflinePlayer player = offlinePlayerManager.getPlayer(args[0]);
        if (player != null) {
            mouseMail.addMailbox(player.getUUID(),
                    new MMLocation(((Player) sender).getWorld().getName(), Integer.parseInt(args[1]),
                            Integer.parseInt(args[2]), Integer.parseInt(args[3])));
            sender.sendMessage(ChatColor.GREEN + "Player '" + args[0] + "' mailbox added!");
            return true;
        }
        sender.sendMessage(ChatColor.RED + "Player '" + args[0] + "' not found!");
        return true;

    }
}
