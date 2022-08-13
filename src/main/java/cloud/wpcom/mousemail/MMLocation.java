package cloud.wpcom.mousemail;

import java.io.Serializable;

import javax.annotation.Nonnull;

import org.bukkit.Location;

import cloud.wpcom.WPCraft;

public class MMLocation implements Serializable {

    private final String world;
    private final int x;
    private final int y;
    private final int z;

    /**
     * 
     * Data class for storing a location using a valid bukkit location
     * 
     * @param wpcraft  Server plugin
     * @param location Valid {@code org.bukkit.Location} location
     * 
     */
    public MMLocation(@Nonnull Location location) {
        this.x = (int) location.getX();
        this.y = (int) location.getY();
        this.z = (int) location.getZ();
        this.world = location.getWorld().toString();

    }

    /**
     * 
     * Data class for storing a location without a valid bukkit location
     * 
     * @param wpcraft Server plugin
     * @param world   The name of the world which the location exists, or null
     * @param x       X position
     * @param y       Y position
     * @param z       Z position
     */
    public MMLocation(@Nonnull String world, @Nonnull int x, @Nonnull int y, @Nonnull int z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.world = world;
    }

    /**
     * Returns a valid bukkit location
     * 
     * @return bukkit location
     */
    @Nonnull
    public Location getLocation(WPCraft wpcraft) {
        return new Location(wpcraft.getServer().getWorld(world), x, y, z);
    }

    /**
     * Returns the x value of the location
     * 
     * @return x value
     */
    @Nonnull
    public int getX() {
        return x;
    }

    /**
     * Returns the y value of the location
     * 
     * @return y value
     */
    @Nonnull
    public int getY() {
        return y;
    }

    /**
     * Returns the z value of the location
     * 
     * @return z value
     */
    @Nonnull
    public int getZ() {
        return z;
    }

    @Override
    public String toString() {
        return "X: " + getX() + " Y: " + getY() + " Z: " + getZ();
    }
}
