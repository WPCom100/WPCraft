package cloud.wpcom.bedrockjukebox;

import org.bukkit.Location;
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
    private boolean hasInputHopper = false;
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
    }

    public Block getInputHopperBlock() {
        return inputHopper;
    }

    public Inventory getInputHopperInventory() {
        return ((org.bukkit.block.Hopper) this.getInputHopperBlock().getState()).getInventory();
    }
    
    public Hopper getInputHopper() {
        return (Hopper) inputHopper.getBlockData();
    }

    public void removeInputHopper() {
        hasInputHopper = false;
    }

    public boolean hasOutputHopper() {
        return hasOutputHopper;
    }
    
    public void setOutputHopperBlock(Block outputHopper) {
        this.outputHopper = outputHopper;
        hasOutputHopper = true;
    }

    public Block getOutputHopperBlock() {
        return outputHopper;
    }

    public Inventory getOutputHopperInventory() {
        return ((org.bukkit.block.Hopper) this.getOutputHopperBlock().getState()).getInventory();
    }

    public Hopper getOutputHopper() {
        return (Hopper) outputHopper.getBlockData();
    }

    public void removeOutputHopper() {
        hasOutputHopper = false;
    }

    public void setPlaying(boolean isPlaying) {
        this.isPlaying = isPlaying;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void playRecord(ItemStack record, WPCraft wpcraft) {
        
        jukebox.setRecord(record);
        if (jukebox.update())
            isPlaying = true;

        durationTask = new DiscDuration(this, wpcraft);
        durationTask.runTaskLater(wpcraft, JBUtil.getDiskDuration(record));
    }
}
