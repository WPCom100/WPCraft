package cloud.wpcom.tasks;

import org.bukkit.scheduler.BukkitRunnable;
import cloud.wpcom.WPCraft;
import cloud.wpcom.bedrockjukebox.JukeboxWrapper;

public class DiscDuration extends BukkitRunnable {

    WPCraft plugin;
    JukeboxWrapper jukeboxw;

    public DiscDuration(JukeboxWrapper jukeboxw, WPCraft plugin) {
        this.plugin = plugin;
        this.jukeboxw = jukeboxw;
    }
    
    @Override
    public void run() {
        plugin.getServer().broadcastMessage("Done Playing!");
        jukeboxw.setPlaying(false);
    }

    @Override
    public void cancel() {
        WPCraft.server.broadcastMessage("Task Stopped");
        super.cancel();
    }

}
