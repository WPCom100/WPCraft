package cloud.wpcom.mousemail;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import cloud.wpcom.WPCraft;

public class MMSetupCommand implements CommandExecutor {

    private final WPCraft wpcraft;
    private final MouseMail mouseMail;

    public MMSetupCommand(WPCraft wpcraft, MouseMail mouseMail) {
        this.wpcraft = wpcraft;
        this.mouseMail = mouseMail;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        mouseMail.addMailbox(wpcraft.getServer().getPlayer(sender.getName()).getUniqueId(),
                wpcraft.getServer().getPlayer(sender.getName()).getLocation());
        return true;
    }

}
