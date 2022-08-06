package cloud.wpcom.commands;

import java.util.List;

import javax.annotation.Nonnull;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import cloud.wpcom.WPCraft;
import cloud.wpcom.commandsleeper.CSUtil;
import cloud.wpcom.commandsleeper.CommandSleeper;
import cloud.wpcom.tasks.CSVirtualBedExpiryTask;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class CSSleepCommad implements TabExecutor {

    private final WPCraft wpcraft;
    private final CommandSleeper commandSleeper;
    
    public CSSleepCommad(@Nonnull WPCraft wpcraft, @Nonnull CommandSleeper commandSleeper) {
        this.wpcraft = wpcraft;
        this.commandSleeper = commandSleeper;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Get the player that sent the command
        Player playerSender = sender.getServer().getPlayer(sender.getName());
        World world = playerSender.getWorld();

        // Checks if a player is trying to sleep at night
        if (!CSUtil.isNight(world)) {
            playerSender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&cYou can't sleep right now! It's way too bright outside!"));

            return true;
        }
        
        // Check if player is sleeping in a bed already, trollll them
        if (playerSender.isSleeping()) {
            TextComponent trollMessage = new TextComponent("For you Taylor: https://media2.giphy.com/media/5ftsmLIqktHQA/giphy.gif");
            trollMessage.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL,
                    "https://media2.giphy.com/media/5ftsmLIqktHQA/giphy.gif"));
            playerSender.spigot().sendMessage(trollMessage);
            return true;
        }
        
        // Checks to make sure someone is sleeping in a bed, send a request if not
        if (!CSUtil.hasPhysicalSleeper(playerSender.getWorld())) {
            wpcraft.getServer()
                    .broadcastMessage(ChatColor.translateAlternateColorCodes('&',
                            "&f[&bWPCraft&f] &e[player] &fwants to sleep, but needs someone to get in bed!"
                                    .replace("[player]", playerSender.getName())));

            return true;
        }
        
        // Add to the list of sleeping players
        commandSleeper.addCommandSleeper(playerSender, new CSVirtualBedExpiryTask(wpcraft, commandSleeper, playerSender));

        // Send chat message
        wpcraft.getServer()
                .broadcastMessage(ChatColor.translateAlternateColorCodes('&',
                        "&f[&bWPCraft&f] &e[player] &fgot in virtual bed for 30 sec. ([sleeping]/[needed])"
                                .replace("[player]", playerSender.getName())
                                .replace("[sleeping]", CSUtil.getNumberSleeping(world, commandSleeper).toString())
                                .replace("[needed]", CSUtil.getNeededToSleep(world).toString())));

        return true;
    }
    
    @Override  // Not needed for the mod
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
       
        return null;
    }

}
