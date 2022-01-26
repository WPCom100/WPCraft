package cloud.wpcom;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.ItemFrame;
import org.bukkit.Material;

public class EventListener implements Listener {


    // Blocks rotation of item frames unless holding an item frame in the right hand
    @EventHandler
    public void onEntityInteraction(PlayerInteractEntityEvent event, JavaPlugin plugin) {

        // Check to ensure the entity that was right clicked is an Item Frame
        if (event.getRightClicked() instanceof ItemFrame) {
            // Check to ensure the Item Frame is not empty
            if (((ItemFrame)event.getRightClicked()).getItem().getType() != Material.AIR) {
                // If the player is not holding an item frame in their main hand, cancel rotation event
                if (event.getPlayer().getEquipment().getItemInMainHand().getType() == Material.ITEM_FRAME) {

                    event.setCancelled(true);

                }
            }
        }
    }
}