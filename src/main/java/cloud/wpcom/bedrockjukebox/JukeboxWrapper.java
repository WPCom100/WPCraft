package cloud.wpcom.bedrockjukebox;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Jukebox;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Hopper;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import cloud.wpcom.WPCraft;
import cloud.wpcom.tasks.DiscDuration;

public class JukeboxWrapper {

    private Jukebox jukebox;
    private Location location;
    private boolean hasInputHopper = false; // TODO Multi hopper support
    private boolean hasOutputHopper = false;
    private Block inputHopper;
    private Block outputHopper;
    private boolean isPlaying = false;
    public DiscDuration durationTask;
    
    public JukeboxWrapper(Jukebox j) {
        this.jukebox = j;
        this.location = this.jukebox.getLocation();
        WPCraft.server.broadcastMessage("Jukebox Registered!");
    }
    
    public Jukebox getBlock() {
        return jukebox;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
    
    public boolean hasInputHopper() {
        return hasInputHopper;
    }

    public void setInputHopperBlock(Block inputHopper) {
        this.inputHopper = inputHopper;
        hasInputHopper = true;
        WPCraft.server.broadcastMessage("Input hopper added " + getLocation());
    }

    public Block getInputHopperBlock() {
        return inputHopper;
    }

    public Hopper getInputHopper() {
        return (Hopper) inputHopper.getBlockData();
    }

    public Inventory getInputHopperInventory() {
        return ((org.bukkit.block.Hopper) getInputHopperBlock().getState()).getInventory();
    }

    // Returns AIR if no disc is found
    public ItemStack popInputHopperAtIndex(int index) {
        ItemStack waitingDisc = new ItemStack(Material.AIR);
        if (index == -1)
            return waitingDisc;
        else {
            waitingDisc = getInputHopperInventory().getItem(index).clone();
            getInputHopperInventory().clear(index);
            return waitingDisc;
        }
    }

    public void removeInputHopper() {
        hasInputHopper = false;
        WPCraft.server.broadcastMessage("Input hopper removed " + getLocation());
    }

    public boolean hasOutputHopper() {
        return hasOutputHopper;
    }
    
    public void setOutputHopperBlock(Block outputHopper) {
        this.outputHopper = outputHopper;
        hasOutputHopper = true;
        WPCraft.server.broadcastMessage("Output hopper added " + getLocation());
    }

    public Block getOutputHopperBlock() {
        return outputHopper;
    }

    public Hopper getOutputHopper() {
        return (Hopper) outputHopper.getBlockData();
    }

    public Inventory getOutputHopperInventory() {
        return ((org.bukkit.block.Hopper) this.getOutputHopperBlock().getState()).getInventory();
    }

    public void removeOutputHopper() {
        hasOutputHopper = false;
        WPCraft.server.broadcastMessage("Output hopper removed " + getLocation());
    }

    public void setPlaying(boolean isPlaying) {
        this.isPlaying = isPlaying;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    // Play the given record, creating a task to continue when the record is finished
    public void playRecord(ItemStack record, WPCraft wpcraft) {
        jukebox.setRecord(record);
        WPCraft.server.broadcastMessage("Playing Disk:" + record.toString());
        if (jukebox.update())
            isPlaying = true;

        durationTask = new DiscDuration(this, wpcraft);
        durationTask.runTaskLater(wpcraft, JBUtil.getDiskDuration(record));
    }

    public int getWaitingDisc() {
        // Check if the jukebox has an input hopper
        if (hasInputHopper != true)
            return -1;

        WPCraft.server.broadcastMessage("Has input hopper, looking for disc in hopper");
        // Check if the input hopper has a disc in it
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
    public ItemStack popWaitingDisc() { // TODO Have two similar functions that do the same thing, popInputHopperAtIndex()
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
}
