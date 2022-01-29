package cloud.wpcom.bedrockjukebox;

import org.bukkit.Location;
import org.bukkit.block.Jukebox;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Hopper;

import cloud.wpcom.WPCraft;

public class JukeboxWrapper {

    private Jukebox jukebox;
    private Location location;
    private boolean hasInputHopper = false;
    private boolean hasOutputHopper = false;
    private Block inputHopper;
    private Block outputHopper;
    
    public JukeboxWrapper(Jukebox j) {
        this.jukebox = j;
        this.location = this.jukebox.getLocation();
        WPCraft.server.broadcastMessage("Jukebox Registered!");
    }
    
    public Jukebox getblock() {
        return jukebox;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location l) {
        location = l;
    }
    
    public boolean hasInputHopper() {
        return hasInputHopper;
    }

    public void setInputHopperBlock(Block h) {
        inputHopper = h;
        hasInputHopper = true;
        WPCraft.server.broadcastMessage("Input hopper set for jukebox at: " + location.toString());
    }

    public Block getInputHopperBlock() {
        return inputHopper;
    }
    
    public Hopper getInputHopper() {
        return (Hopper) inputHopper.getBlockData();
    }

    public void removeInputHopper() {
        hasInputHopper = false;
        WPCraft.server.broadcastMessage("Input hopper removed!");
    }

    public boolean hasOutputHopper() {
        return hasOutputHopper;
    }
    
    public void setOutputHopperBlock(Block h) {
        outputHopper = h;
        hasOutputHopper = true;
        WPCraft.server.broadcastMessage("Output hopper set for jukebox at: " + location.toString());
    }

    public Block getOutputHopperBlock() {
        return outputHopper;
    }

    public Hopper getOutputHopper() {
        return (Hopper) outputHopper.getBlockData();
    }

    public void removeOutputHopper() {
        hasOutputHopper = false;
        WPCraft.server.broadcastMessage("Output hopper removed!");
    }
}
