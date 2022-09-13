package cloud.wpcom;

import java.util.logging.Level;

public class WPLogger {
    
    private final WPCraft wpcraft;
    private final String pluginName;
    private boolean isDebug;

    /**
     * Logger used to easily log and debug plugins
     * 
     * @param wpcraft The plugin (Instance of {@link JavaPlugin})
     * @param pluginName The name of the plugin module
     * @param isDebug If the logger is in debug mode
     */
    public WPLogger(WPCraft wpcraft, String pluginName, boolean isDebug) {
        this.wpcraft = wpcraft;
        this.pluginName = '[' + pluginName + "] ";
        this.isDebug = isDebug;
    }

    /**
     * Log a debug message with info log level
     * 
     * @param msg Message to log
     */
    public void debug(String msg) {
        if (isDebug)
            info("[DEBUG]" + msg);
    }

    /**
     * Log a message with info log level
     * 
     * @param msg Message to log
     */
    public void info(String msg) {
        wpcraft.getLogger().log(Level.INFO, pluginName + msg);
    }

    /**
     * Log a message with warning log level
     * 
     * @param msg Message to log
     */
    public void warning(String msg) {
        wpcraft.getLogger().log(Level.WARNING, pluginName + msg);
    }

    /**
     * Log a message with severe log level
     * 
     * @param msg Message to log
     */
    public void severe(String msg) {
        wpcraft.getLogger().log(Level.SEVERE, pluginName + msg);
    }
    
}
