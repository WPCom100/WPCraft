package cloud.wpcom.commandsleeper;

import javax.annotation.Nonnull;

import org.bukkit.World;
import org.bukkit.entity.Player;

public class CSUtil {
    
    // Checks if a world is night
    public static boolean isNight(@Nonnull World world) {
        return world.getTime() >= 12500 && world.getTime() <= 23950;
    }

    // Checks if a single player is in bed on the same world as a player
    public static boolean hasPhysicalSleeper(@Nonnull Player player) {
        return getNumberPhysicalSleepers(player.getWorld()) != 0;
    }

    // Returns number of players physically sleeping in a world
    public static Integer getNumberPhysicalSleepers(@Nonnull World world) {
        return ((Long) world.getPlayers().stream().filter(Player::isSleeping).count()).intValue();
    }

    // Returns number of players needed to sleep to skip the night
    public static Integer getNeededToSleep(@Nonnull World world) {
        return world.getPlayers().size() / 2;
    }

    // Returns number of players sleeping using a physical bed and the sleep command
    public static Integer getNumberSleeping(@Nonnull World world, CommandSleeper commandSleeper) {
        return getNumberPhysicalSleepers(world) + commandSleeper.getCommandSleepers().size();
    }
}
