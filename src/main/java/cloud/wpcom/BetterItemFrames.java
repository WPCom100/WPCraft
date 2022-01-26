package cloud.wpcom;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.entity.ItemFrame;
import org.bukkit.Material;
import org.bukkit.Location;
import org.bukkit.block.Container;

public class BetterItemFrames implements Listener {


    // Blocks rotation of item frames unless holding an item frame in the right hand
    @EventHandler
    public void onEntityInteraction(PlayerInteractEntityEvent event) {

        // If the entity that was right clicked is an Item Frame
        if (event.getRightClicked() instanceof ItemFrame) {
            // If the Item Frame is not empty
            if (((ItemFrame) event.getRightClicked()).getItem().getType() != Material.AIR) {
                // If the player is not holding an item frame in their main hand
                if (event.getPlayer().getEquipment().getItemInMainHand().getType() != Material.ITEM_FRAME) {

                    // Cancles the Item Frame rotation
                    event.setCancelled(true);

                    // Location of the Item Frame, which transforms to block behind face of Item Frame
                    Location ifLocation = event.getRightClicked().getLocation(); 

                    // Gets the location of the block behind the Item Frame depending on the way it is facing
                    // TODO Possibly replace with Block - getRelative(int modX, int modY, int modZ)
                    switch (event.getRightClicked().getFacing()) {
                        default:
                            break;
                        case NORTH:
                            ifLocation.setZ(ifLocation.getZ() + 1);
                            break;
                        case EAST:
                            ifLocation.setX(ifLocation.getX() - 1);
                            break;
                        case SOUTH:
                            ifLocation.setZ(ifLocation.getZ() - 1);
                            break;
                        case WEST:
                            ifLocation.setX(ifLocation.getX() + 1);
                            break;
                        case UP:
                            ifLocation.setY(ifLocation.getY() - 1);
                            break;
                        case DOWN:
                            ifLocation.setY(ifLocation.getY() + 1);
                            break;
                    }

                    // Check to see if block behind the Item Frame is a Barrel or Chest
                    if ((ifLocation.getBlock().getType() == Material.BARREL)
                            || (ifLocation.getBlock().getType() == Material.CHEST)) {
                        
                        // Cast the BlockState to a Container and pass the Inventory to the player to open
                        Container c = (Container) ifLocation.getBlock().getState();
                        event.getPlayer().openInventory(c.getInventory());

                    }
                }
            }
        }
    }
}