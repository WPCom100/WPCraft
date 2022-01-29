package cloud.wpcom.bedrockjukebox;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Directional;
import cloud.wpcom.WPCraft;

public class JBUtil {

    //public static boolean hasInputHopper(Block b) {

    //}

    // Check if the given Block is under the given Jukebox Wrapper
    public static boolean isHopperUnder(JukeboxWrapper jb, Block hopper) {
        if (hopper.getRelative(BlockFace.UP).getType() == Material.JUKEBOX) {
            WPCraft.server.broadcastMessage("Hopper has jukebox above it!");
            return true;
        }

        WPCraft.server.broadcastMessage("Hopper does NOT have jukebox above it!");
        return false;

    }

    // Check if the given block (hopper) is facing (outputing) the given Jukebox Wrapper
    public static boolean isHopperFacing(JukeboxWrapper jb, Block hopper) {
        if (hopper.getRelative(((Directional) hopper.getBlockData()).getFacing())
                .getLocation().equals(jb.getLocation())) {
            WPCraft.server.broadcastMessage("Hopper is facing a registered jukebox!");
            return true;
        }

        WPCraft.server.broadcastMessage("Hopper is NOT facing a registered jukebox!");
        return false;

    }

}
