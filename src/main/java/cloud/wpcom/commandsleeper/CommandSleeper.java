package cloud.wpcom.commandsleeper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import org.bukkit.entity.Player;

import cloud.wpcom.tasks.CSVirtualBedExpiryTask;

public class CommandSleeper {
    
    private Map<Player, CSVirtualBedExpiryTask> commandSleepers;

    public CommandSleeper() {
        this.commandSleepers = new HashMap<>();
    }

    /**
     * Adds a player to the list of sleeping players.
     *
     * @param player The player for which to add to the list of sleeping players.
     */
    public void addCommandSleeper(@Nonnull Player player, @Nonnull CSVirtualBedExpiryTask expiryTask) {
        commandSleepers.put(player, expiryTask);
    }

    /**
     * Removes a player from the list of sleeping players. 
     *
     * @param player The player for which to remove to the list of sleeping players.
     */
    public void removeCommandSleeper(@Nonnull Player player) {
        commandSleepers.remove(player);
    }

    /**
     * Remove all players from list of sleeping players silently
     */
    public void clearCommandSleepers() {
        commandSleepers.values().forEach(expiryTask -> expiryTask.cancel());
        commandSleepers.clear();
    }

    /**
     * Remove all players from list of sleeping players forcefully (displays a message)
     * 
     * @param player The player causing players to be forcefully removed
     */
    public void forcefullyClearCommandSleepers(Player player) {
        commandSleepers.values().forEach(expiryTask -> expiryTask.cancelByPlayer(player));
        commandSleepers.clear();

    }

    /**
     * Returns a list of players sleeping using the sleep command
     * 
     * @return List of Players
     */
    public List<Player> getCommandSleepers() {
        List<Player> players = new ArrayList<>();
        commandSleepers.keySet().forEach(player -> players.add(player));
        return players;
    }
    
}
