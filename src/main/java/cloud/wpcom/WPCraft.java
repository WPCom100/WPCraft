package cloud.wpcom;

import org.bukkit.plugin.java.JavaPlugin;

public class WPCraft extends JavaPlugin {
    
    @Override
    public void onEnable() {
        
        getServer().getPluginManager().registerEvents(new BetterItemFrames(), this);
        getLogger().info("BetterItemFrames loaded!");
    }

    @Override
    public void onDisable() {

        getLogger().info("WPCraft signing off...");

    }
}
