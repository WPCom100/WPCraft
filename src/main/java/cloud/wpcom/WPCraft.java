package cloud.wpcom;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.ChatColor;

public class WPCraft extends JavaPlugin {

    public static final String PREFIX = "[" + ChatColor.AQUA + "WPCraft" + ChatColor.WHITE + "]"; // Prefix for all messages sent from server
    public static final String PM_PREFIX = "[" + ChatColor.RED + "PM" + ChatColor.WHITE + "]"; // Prefix for all private messages/confirmations sent from the server to a player

    @Override
    public void onEnable() {

        // Load BetterItemFrames
        getServer().getPluginManager().registerEvents(new BetterItemFrames(), this);
        getLogger().info("BetterItemFrames loaded!");

        // Load ServerBroadcast
        getCommand("bc").setExecutor(new ServerBroadcast());
        getLogger().info("BetterItemFrames loaded!");

    }

    @Override
    public void onDisable() {

        getLogger().info("WPCraft signing off...");

    }
}
