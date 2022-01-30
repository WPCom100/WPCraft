package cloud.wpcom.bedrockjukebox;

import org.bukkit.Location;
import org.bukkit.block.Jukebox;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Hopper;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import cloud.wpcom.WPCraft;

public class JukeboxWrapper {

    private Jukebox jukebox;
    private JukeboxLooper looper;
    private Location location;
    private boolean hasInputHopper = false;
    private boolean hasOutputHopper = false;
    private Block inputHopper;
    private Block outputHopper;
    private boolean isPlaying = false;
    
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

    public void playRecord(ItemStack record, WPCraft wpcraft) {
        
        jukebox.setRecord(record);
        if (jukebox.update())
            isPlaying = true;

        looper = new JukeboxLooper(this, wpcraft);
    }
}
