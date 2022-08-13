package cloud.wpcom.offlineplayermanager;

import java.io.File;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

import javax.annotation.Nonnull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import cloud.wpcom.WPCraft;

public class OfflinePlayerManager {

    private final WPCraft wpcraft;
    private final File playerList;
    private List<OfflinePlayer> offlinePlayers;

    /**
     * Manages any player that has ever connected to the server.
     * Mostly used to store and access UUID's
     * 
     * @param wpcraft The server plugin
     */
    public OfflinePlayerManager(@Nonnull WPCraft wpcraft) {
        this.wpcraft = wpcraft;
        playerList = new File(wpcraft.getDataFolder().getAbsolutePath() + "/../../usercache.json");

        loadPlayers();
    }

    private void loadPlayers() {
        wpcraft.getLogger().log(Level.INFO, "Attempting to load offline player list from usercache.json");
        try {
            Reader reader = Files.newBufferedReader(Paths.get(playerList.toString()));
            offlinePlayers = new Gson().fromJson(reader, new TypeToken<List<OfflinePlayer>>() {
            }.getType());
            reader.close();

        } catch (Exception e) {
            wpcraft.getLogger().log(Level.WARNING, "Could not load offline player list from usercache.json!");
            e.printStackTrace();
        }
    }

    /**
     * Gets a list of all players ever on the server
     * 
     * @return A list of offline players
     */
    @Nonnull
    public List<OfflinePlayer> getAllPlayers() {
        return offlinePlayers;
    }

    /**
     * Returns a single offline player
     * 
     * @param name Name of the player to retreive
     * @return The player if found, returns {@code null} if not
     */
    public OfflinePlayer getPlayer(String name) {
        for (OfflinePlayer player : offlinePlayers) {
            if (player.getName().equals(name)) {
                return player;
            }
        }
        return null;
    }

    /**
     * Returns a single offline player
     * 
     * @param uuid UUID of the player to retreive
     * @return The player if found, returns {@code null} if not
     */
    public OfflinePlayer getPlayer(UUID uuid) {
        for (OfflinePlayer player : offlinePlayers) {
            if (player.getUUID().equals(uuid)) {
                return player;
            }
        }
        return null;
    }

    public class OfflinePlayer {
        private final String name;
        private final UUID uuid;

        /**
         * Data class used to hold information about an offline player
         * 
         * @param name Name of the player
         * @param uuid UUID of the player
         */
        public OfflinePlayer(@Nonnull String name, @Nonnull UUID uuid) {
            this.name = name;
            this.uuid = uuid;
        }

        /**
         * Get the name of offline player
         * 
         * @return Name of the player
         */
        @Nonnull
        public String getName() {
            return name;
        }

        /**
         * Get the UUID of offline player
         * 
         * @return UUID of the player
         */
        @Nonnull
        public UUID getUUID() {
            return uuid;
        }

        @Override
        public String toString() {
            return "Name: " + name + " UUID: " + uuid;
        }
    }

}
