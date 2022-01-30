package cloud.wpcom.bedrockjukebox;

import org.bukkit.scheduler.BukkitTask;

import cloud.wpcom.WPCraft;

public class JukeboxLooper {

    private JukeboxWrapper jukebox;
    public boolean isLooping = false;
    private final WPCraft wpcraft;
    
    public JukeboxLooper(JukeboxWrapper jukebox, WPCraft wpcraft) {

        this.jukebox = jukebox;
        this.wpcraft = wpcraft;
        loop();

    }

    private void loop() {
        
        isLooping = true;
        WPCraft.server.broadcastMessage("Looper Started");

        BukkitTask task = new JukeboxTask(wpcraft).runTask(wpcraft);

        WPCraft.server.broadcastMessage("Looper Stopped");
        isLooping = false;

    }
}
