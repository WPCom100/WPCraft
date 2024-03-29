package cloud.wpcom.bedrockjukebox;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Directional;
import org.bukkit.inventory.ItemStack;

import cloud.wpcom.WPCraft;

public class BJUtil {

    // Returns the duration of a music disk in ticks
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
                return 0L;
        }
    }

    // Check if the given Block is under the given Jukebox Wrapper
    public static boolean isHopperUnder(JukeboxWrapper jb, Block hopper) {
        if (hopper.getRelative(BlockFace.UP).getType() == Material.JUKEBOX) {
            return true;
        }
        return false;
    }

    // Check if the given block (hopper) is facing (outputing) the given Jukebox Wrapper
    public static boolean isHopperFacing(JukeboxWrapper jb, Block hopper) {
        if (hopper.getRelative(((Directional) hopper.getBlockData()).getFacing())
                .getLocation().equals(jb.getLocation())) {
            return true;
        }
        return false;
    }

    // Checks if a input hopper is attached to (facing) a jukebox and registers them
    public static void registerInputHoppers(JukeboxWrapper j) { // TODO Multi hopper support, future should return block array
        Block blockToCheck = j.getLocation().getBlock().getRelative(BlockFace.NORTH);
        if (blockToCheck.getType() == Material.HOPPER) { // NORTH
            if (isHopperFacing(j, blockToCheck)) {
                j.setInputHopperBlock(blockToCheck);
            }
        }
        blockToCheck = j.getLocation().getBlock().getRelative(BlockFace.EAST);
        if (blockToCheck.getType() == Material.HOPPER) { // EAST
            if (isHopperFacing(j, blockToCheck)) {
                j.setInputHopperBlock(blockToCheck);
            }
        }
        blockToCheck = j.getLocation().getBlock().getRelative(BlockFace.SOUTH);
        if (blockToCheck.getType() == Material.HOPPER) { // SOUTH
            if (isHopperFacing(j, blockToCheck)) {
                j.setInputHopperBlock(blockToCheck);
            }
        }
        blockToCheck = j.getLocation().getBlock().getRelative(BlockFace.WEST);
        if (blockToCheck.getType() == Material.HOPPER) { // WEST
            if (isHopperFacing(j, blockToCheck)) {
                j.setInputHopperBlock(blockToCheck);
            }
        }
        blockToCheck = j.getLocation().getBlock().getRelative(BlockFace.UP);
        if (blockToCheck.getType() == Material.HOPPER) { // ABOVE
            if (isHopperFacing(j, blockToCheck)) {
                j.setInputHopperBlock(blockToCheck);
            }
        }
    }

    // Checks if a output hopper is below a jukebox and registers it
    public static void registerOutputHopper(JukeboxWrapper j) {
        Block blockToCheck = j.getLocation().getBlock().getRelative(BlockFace.DOWN);
        if (blockToCheck.getType() == Material.HOPPER) {
            if (isHopperUnder(j, blockToCheck)) {
                j.setOutputHopperBlock(blockToCheck);
            }
        }
    }

    // Returns item at a location in a hopper, removing it. AKA Popping
    // Returns AIR if no disc is found
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
    public static void playNext(JukeboxWrapper j, WPCraft plugin) {
        int discIndex = j.getWaitingDisc();
        if (discIndex != -1) {
            j.playRecord(BJUtil.popHopperAtIndex(discIndex, j), plugin);
        }
    }
}   // TODO Add logging to console, insted of in game chat. TEMP pass server to util here
