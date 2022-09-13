package cloud.wpcom.bedrockjukebox;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Jukebox;
import org.bukkit.block.Hopper;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class JukeboxWrapper {

    private final BedrockJukebox bedrockJukebox;
    private final Jukebox jukebox;
    private final Location location;
    private boolean hasInputHopper = false;
    private Map<BlockFace, Hopper> inputHoppers = new HashMap<>();
    private boolean hasOutputHopper = false;
    private Hopper outputHopper;
    private boolean isPlaying = false;
    private BJDiscDurationTask durationTask;
    
    public JukeboxWrapper(BedrockJukebox bedrockJukebox, Jukebox j) {
        this.bedrockJukebox = bedrockJukebox;
        this.jukebox = j;
        this.location = this.jukebox.getLocation();
    }
    
    public Jukebox getJukebox() {
        return jukebox;
    }

    public Location getLocation() {
        return location;
    }
    
    public boolean hasInputHopper() {
        return hasInputHopper;
    }

    public void addInput(Hopper hopper, BlockFace bf) {
        inputHoppers.put(bf, hopper);
        hasInputHopper = true;
    }

    @Nullable
    public Hopper getInput(BlockFace bf) {
        return inputHoppers.get(bf);
    }
    
    @Nullable
    public Collection<Hopper> getInputs() {
        return inputHoppers.values();
    }

    public void removeInput(BlockFace bf) {
        inputHoppers.remove(bf);
        if (inputHoppers.size() == 0) {
            hasInputHopper = false;
        }
    }

    public boolean hasOutputHopper() {
        return hasOutputHopper;
    }

    public void setOutput(Hopper hopper) {
        outputHopper = hopper;
        hasOutputHopper = true;
    }
    
    @Nullable
    public Hopper getOutput() {
        return outputHopper;
    }

    public Block getOutputHopperBlock() {
        return (Block) getOutput();
    }

    public Inventory getOutputInventory() {
        return getOutput().getInventory();
    }

    public void removeOutputHopper() {
        outputHopper = null;
        hasOutputHopper = false;
    }

    public void setPlaying(boolean isPlaying) {
            this.isPlaying = isPlaying;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void clearPlaying() {
        jukebox.setRecord(new ItemStack(Material.AIR));
    }

    // Play the given record, creating a task to continue when the record is finished
    public void playRecord(ItemStack record) {
        jukebox.setRecord(record);
        if (jukebox.update())
            isPlaying = true;

        durationTask = new BJDiscDurationTask(bedrockJukebox, this);
        durationTask.runTaskLater(bedrockJukebox.getPlugin(), BJUtil.getDiskDuration(record));
    }

    public BJDiscDurationTask getDurationTask() {
        return durationTask;
    }

    public void clearDurationTask() {
        durationTask.cancel();
        durationTask = null;
    }
}