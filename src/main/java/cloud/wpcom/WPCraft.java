package cloud.wpcom;

import cloud.wpcom.amorstandarms.AmorStandArms;
import cloud.wpcom.bedrockjukebox.BJCommand;
import cloud.wpcom.bedrockjukebox.BJEvents;
import cloud.wpcom.bedrockjukebox.BedrockJukebox;
import cloud.wpcom.betteritemframes.BetterItemFrameEvent;
import cloud.wpcom.commandsleeper.CSBedEvents;
import cloud.wpcom.commandsleeper.CSSleepCommad;
import cloud.wpcom.commandsleeper.CommandSleeper;
import cloud.wpcom.joinmessages.JoinMessageEvents;
import cloud.wpcom.serverbroadcast.ServerBroadcastCommand;

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
        getServer().getPluginManager().registerEvents(new BetterItemFrameEvent(), this);
        getLogger().info("BetterItemFrames loaded!");

        // Load BedrockJukebox
        BedrockJukebox bedrockJukebox = new BedrockJukebox(this);
        getServer().getPluginManager().registerEvents(new BJEvents(this, bedrockJukebox), this);
        getCommand("jb").setExecutor(new BJCommand(bedrockJukebox));
        getLogger().info("BedrockJukebox loaded!");

        // Load ServerBroadcast
        getCommand("bc").setExecutor(new ServerBroadcastCommand());
        getLogger().info("ServerBroadcast loaded!");

        // Load CommandSleeper
        CommandSleeper commandSleeper = new CommandSleeper();
        getServer().getPluginManager().registerEvents(new CSBedEvents(this, commandSleeper), this);
        getCommand("sleep").setExecutor(new CSSleepCommad(this, commandSleeper));
        getLogger().info("CommandSleeper loaded!");

        // Load Armor Stand Arms
        getServer().getPluginManager().registerEvents(new AmorStandArms(this), this);
        getLogger().info("Armor Stand Arms loaded!");
        

    }

    @Override
    public void onDisable() {

        getLogger().info("WPCraft signing off...");

    }
}
