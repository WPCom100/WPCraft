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

import cloud.wpcom.WPCraft;

public class JukeboxWrapper {

    private final BedrockJukebox bedrockJukebox;
    private final Jukebox jukebox;
    private final Location location;
    private boolean hasInputHopper = false;
    private boolean hasOutputHopper = false;
    @Deprecated
    private Block inputHopper;
    private Map<BlockFace, Hopper> inputHoppers = new HashMap<>();
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

    @Deprecated //Remove
    public void setInputHopperBlock(Block inputHopper) {
        this.inputHopper = inputHopper;
        hasInputHopper = true;
    }

    @Nullable
    public Hopper getInput(BlockFace bf) { // TODO Check for null in uses
        return inputHoppers.get(bf);
    }
    
    @Nullable
    public Collection<Hopper> getInputs() { // TODO Check for null in uses
        return inputHoppers.values();
    }

    public Block getInputHopperBlock(BlockFace bf) { //TODO Look at uses of this and see if it can be removed?
        return (Block) getInput(bf);
    }

    public Inventory getInputInventory(BlockFace bf) {
        return getInput(bf).getInventory();
    }

    public void removeInput(BlockFace bf) {
        inputHoppers.remove(bf);
        if (inputHoppers.size() == 0) {
            hasInputHopper = false;
        }
    }

    @Deprecated //Recreated
    public Block getInputHopperBlock() {
        return inputHopper;
    }

    @Deprecated //Recreated
    public Hopper getInputHopper() {
        return (Hopper) inputHopper.getBlockData();
    }

    @Deprecated //Recreated
    public Inventory getInputHopperInventory() {
        return ((org.bukkit.block.Hopper) getInputHopperBlock().getState()).getInventory();
    }

    @Deprecated //Recreated
    public void removeInputHopper() {
        hasInputHopper = false;
    }

    public boolean hasOutputHopper() {
        return hasOutputHopper;
    }

    public void setOutput(Hopper hopper) {
        outputHopper = hopper;
        hasOutputHopper = true;
    }
    
    @Deprecated //RECREATE
    public void setOutputHopperBlock(Block outputHopper) {
        this.outputHopper = (Hopper) outputHopper.getBlockData();
        hasOutputHopper = true;
    }
    
    @Nullable
    public Hopper getOutput() { // TODO Check for null in uses
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
    public void playRecord(ItemStack record, WPCraft wpcraft) {
        jukebox.setRecord(record);
        if (jukebox.update())
            isPlaying = true;

        durationTask = new BJDiscDurationTask(bedrockJukebox, this);
        durationTask.runTaskLater(wpcraft, BJUtil.getDiskDuration(record));
    }

    @Deprecated // REMOVE
    public int getWaitingDisc() {
        int discIndex = -1;
        for (ItemStack i : getInputHopperInventory().getStorageContents()) {
            ++discIndex;
            if (i instanceof ItemStack && i.getType().isRecord())
                return discIndex;
        }

        // Otherwise
        return -1;
    }

    // Gets the location of the first disc, and returns it as an ItemStack
    // Returns AIR if no disc is found
    @Deprecated // REMOVE
    public ItemStack popWaitingDisc() { // TODO Have two similar functions that do the same thing, popInputHopperAtIndex(), combo using args
        int discIndex = getWaitingDisc();
        ItemStack waitingDisc = new ItemStack(Material.AIR);
        if (discIndex == -1)
            return waitingDisc;
        else {
            waitingDisc = getInputHopperInventory().getItem(discIndex).clone();
            getInputHopperInventory().clear(discIndex);
            return waitingDisc;
        }
    }

    public BJDiscDurationTask getDurationTask() {
        return durationTask;
    }

    public void clearDurationTask() {
        durationTask.cancel();
        durationTask = null;
    }
}