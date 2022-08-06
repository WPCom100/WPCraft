package cloud.wpcom.commandsleeper;

import javax.annotation.Nonnull;

import org.bukkit.World;
import org.bukkit.entity.Player;

public class CSUtil {
    
    /**
     * Checks if it is night on a world
     *
     * @param world The world to check if its night
     * 
     * @return If it is night
     */
    public static boolean isNight(@Nonnull World world) {
        return world.getTime() >= 12500 && world.getTime() <= 23950;
    }

    /**
     * Checks if at least one player is in a world
     *
     * @param world The world to check for a physically sleeping player 
     * 
     * @return If a player is physically sleeping on the same world as another
     */
    public static boolean hasPhysicalSleeper(@Nonnull World world) {
        return getNumberPhysicalSleepers(world) != 0;
    }

    /**
     * Returns number of players physically sleeping in a world
     *
     * @param world The world to check for players
     * 
     * @return An Integer of players physically sleeping
     */
    public static Integer getNumberPhysicalSleepers(@Nonnull World world) {
        return ((Long) world.getPlayers().stream().filter(Player::isSleeping).count()).intValue();
    }

    /**
     * Returns number of players needed to sleep to skip the night
     *
     * @param world The world to check for players
     * 
     * @return An Integer of players needed to sleep in the world
     */
    public static Integer getNeededToSleep(@Nonnull World world) {
        return world.getPlayers().size() / 2;
    }
    
    /**
     * Returns number of players sleeping using a physical bed and the sleep command
     *
     * @param world The world to check for physical players sleeping
     * @param commandSleeper The sleeper module to refrence for virtual sleeping players
     * 
     * @return An Integer of players sleeping using any method
     */
    public static Integer getNumberSleeping(@Nonnull World world, CommandSleeper commandSleeper) {
        return getNumberPhysicalSleepers(world) + commandSleeper.getCommandSleepers().size();
    }
}
