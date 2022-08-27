package cloud.wpcom.bedrockjukebox;

import java.util.ArrayList;

import javax.annotation.Nonnull;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Jukebox;
import org.bukkit.block.data.Directional;
import org.bukkit.inventory.ItemStack;

import cloud.wpcom.WPCraft;

public class BJUtil {

    // Returns the duration of a music disk in ticks
    /**
     * Gets the duration of any music disc in the game
     * 
     * @param record Record to get the duration of
     * @return Length of music disc in ticks
     */
    public static long getDiskDuration(ItemStack record) {
        switch (record.getType()) {
            case MUSIC_DISC_13:
                return 3560L;
            case MUSIC_DISC_CAT:
                return 3700L;
            case MUSIC_DISC_BLOCKS:
                return 6900L;
            case MUSIC_DISC_CHIRP:
                return 3700L;
            case MUSIC_DISC_FAR:
                return 3480L;
            case MUSIC_DISC_MALL:
                return 3940L;
            case MUSIC_DISC_MELLOHI:
                return 1920L;
            case MUSIC_DISC_STAL:
                return 3000L;
            case MUSIC_DISC_STRAD:
                return 3760L;
            case MUSIC_DISC_WARD:
                return 5020L;
            case MUSIC_DISC_11:
                return 1420L;
            case MUSIC_DISC_WAIT:
                return 4640L;
            case MUSIC_DISC_PIGSTEP:
                return 2960L;
            case MUSIC_DISC_OTHERSIDE:
                return 3900L;
            default:
                return 5000L;
        }
    }

    // Check if the given block (hopper) is facing (outputing) the given Jukebox
    /**
     * Checks if a hopper is facing (outputing) to a jukebox
     * 
     * @param jb Jukebox to check
     * @param hopper Hopper to check
     * @return If the hopper is faceing the jukebox
     */
    public static boolean isHopperFacing(Jukebox jb, Block hopper) {
        if (hopper.getRelative(((Directional) hopper.getBlockData()).getFacing())
                .getLocation().equals(jb.getLocation())) {
            return true;
        }
        return false;
    }

    /**
     * Calculates the faces of a jukebox that have a hopper
     * 
     * @param jb Jukebox to check
     * @return Faces of the jukebox that have a hopper
     */
    public static ArrayList<BlockFace> calcHopperFaces(Jukebox jb) {
        ArrayList<BlockFace> hopperFaces = new ArrayList<>();
        Block blockToCheck;

        // NORTH
        blockToCheck = jb.getLocation().getBlock().getRelative(BlockFace.NORTH);
        if (blockToCheck.getType() == Material.HOPPER) {
            if (isHopperFacing(jb, blockToCheck)) {
                hopperFaces.add(BlockFace.NORTH);
            }
        }

        // EAST
        blockToCheck = jb.getLocation().getBlock().getRelative(BlockFace.EAST);
        if (blockToCheck.getType() == Material.HOPPER) {
            if (isHopperFacing(jb, blockToCheck)) {
                hopperFaces.add(BlockFace.EAST);
            }
        }

        // SOUTH
        blockToCheck = jb.getLocation().getBlock().getRelative(BlockFace.SOUTH);
        if (blockToCheck.getType() == Material.HOPPER) {
            if (isHopperFacing(jb, blockToCheck)) {
                hopperFaces.add(BlockFace.SOUTH);
            }
        }

        // WEST
        blockToCheck = jb.getLocation().getBlock().getRelative(BlockFace.WEST);
        if (blockToCheck.getType() == Material.HOPPER) {
            if (isHopperFacing(jb, blockToCheck)) {
                hopperFaces.add(BlockFace.WEST);
            }
        }

        // ABOVE
        blockToCheck = jb.getLocation().getBlock().getRelative(BlockFace.UP);
        if (blockToCheck.getType() == Material.HOPPER) {
            if (isHopperFacing(jb, blockToCheck)) {
                hopperFaces.add(BlockFace.UP);
            }
        }

        // BELOW
        blockToCheck = jb.getLocation().getBlock().getRelative(BlockFace.DOWN);
        if (blockToCheck.getType() == Material.HOPPER) {
            hopperFaces.add(BlockFace.DOWN);
        }
        
        return hopperFaces;
    }

    // Returns item at a location in a hopper, removing it. AKA Popping
    // Returns AIR if no disc is found
    @Deprecated // REMOVE
    public static ItemStack popHopperAtIndex(int index, JukeboxWrapper j) {
        ItemStack waitingDisc = new ItemStack(Material.AIR);
        if (index == -1)
            return waitingDisc;
        else {
            waitingDisc = j.getInputHopperInventory().getItem(index).clone();
            j.getInputHopperInventory().clear(index);
            return waitingDisc;
        }
    }

    // If a disc is waiting in the input hopper, play it next
    @Deprecated // REMOVE
    public static void playNext(JukeboxWrapper j, WPCraft plugin) {
        int discIndex = j.getWaitingDisc();
        if (discIndex != -1) {
            j.playRecord(BJUtil.popHopperAtIndex(discIndex, j), plugin);
        }
    }

    /**
     * Calculates the jukebox {@link BlockFace} that a hopper is
     * facing
     * 
     * @param jbw Wrapper of the jukebox to check
     * @param block Block used to calculate the blockface
     * @return The blockface of the jukebox to which the hopper is located
     */
    public static BlockFace calcBlockFace(@Nonnull Jukebox jbw, @Nonnull Block block) {
        final Location diffrence = jbw.getLocation().subtract(block.getLocation());
        if (diffrence.getX() == -1) {
            return BlockFace.EAST;
        } else if (diffrence.getX() == 1) {
            return BlockFace.WEST;
        } else if (diffrence.getZ() == 1) {
            return BlockFace.NORTH;
        } else if (diffrence.getZ() == -1) {
            return BlockFace.SOUTH;
        } else if (diffrence.getY() == -1) {
            return BlockFace.UP;
        }
        return null;
    }
}
