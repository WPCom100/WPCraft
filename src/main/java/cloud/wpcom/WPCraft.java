package cloud.wpcom;

import cloud.wpcom.bedrockjukebox.BedrockJukebox;
import cloud.wpcom.commands.*;
import cloud.wpcom.commandsleeper.CommandSleeper;
import cloud.wpcom.events.*;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.ChatColor;

public class WPCraft extends JavaPlugin {

    public static final String PREFIX = "[" + ChatColor.AQUA + "WPCraft" + ChatColor.WHITE + "]"; // Prefix for all messages sent from server
    public static final String PM_PREFIX = "[" + ChatColor.RED + "PM" + ChatColor.WHITE + "]"; // Prefix for all private messages/confirmations sent from the server to a player

    @Override
    public void onEnable() {

        // Load JoinMessages
        getServer().getPluginManager().registerEvents(new JoinMessageEvents(), this);
        getLogger().info("JoinMessages loaded!");

        // Load BetterItemFrames
        getServer().getPluginManager().registerEvents(new BetterItemFrameEvents(), this);
        getLogger().info("BetterItemFrames loaded!");

        // Load BedrockJukebox
        BedrockJukebox bedrockJukebox = new BedrockJukebox(this);
        getServer().getPluginManager().registerEvents(new BJEvents(this, bedrockJukebox), this);
        getCommand("jb").setExecutor(new BJCommands(bedrockJukebox));
        getLogger().info("BedrockJukebox loaded!");

        // Load ServerBroadcast
        getCommand("bc").setExecutor(new ServerBroadcastCommands());
        getLogger().info("ServerBroadcast loaded!");

        // Load CommandSleeper
        CommandSleeper commandSleeper = new CommandSleeper();
        getServer().getPluginManager().registerEvents(new CSBedEvents(this, commandSleeper), this);
        getCommand("sleep").setExecutor(new CSSleepCommad(this, commandSleeper));
        

    }

    @Override
    public void onDisable() {

        getLogger().info("WPCraft signing off...");

    }
}
