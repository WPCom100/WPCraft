package cloud.wpcom;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.ChatColor;
import org.bukkit.Server;

public class WPCraft extends JavaPlugin {

    public static final String PREFIX = "[" + ChatColor.AQUA + "WPCraft" + ChatColor.WHITE + "]"; // Prefix for all messages sent from server
    public static final String PM_PREFIX = "[" + ChatColor.RED + "PM" + ChatColor.WHITE + "]"; // Prefix for all private messages/confirmations sent from the server to a player
    public static cloud.wpcom.bedrockjukebox.BedrockJukebox jb = new cloud.wpcom.bedrockjukebox.BedrockJukebox();
    public static Server server;

    @Override
    public void onEnable() {

        server = getServer();

        // Load JoinMessages
        getServer().getPluginManager().registerEvents(new cloud.wpcom.events.JoinMessages(), this);
        getLogger().info("JoinMessages loaded!");

        // Load BetterItemFrames
        getServer().getPluginManager().registerEvents(new cloud.wpcom.events.BetterItemFrames(), this);
        getLogger().info("BetterItemFrames loaded!");

        // Load BedrockJukebox
        getServer().getPluginManager().registerEvents(new cloud.wpcom.events.BedrockJukebox(this), this);
        getCommand("jb").setExecutor(new cloud.wpcom.commands.BedrockJukebox());
        getLogger().info("BedrockJukebox loaded!");

        // Load ServerBroadcast
        getCommand("bc").setExecutor(new cloud.wpcom.commands.ServerBroadcast());
        getLogger().info("ServerBroadcast loaded!");

    }

    @Override
    public void onDisable() {

        getLogger().info("WPCraft signing off...");

    }
}
