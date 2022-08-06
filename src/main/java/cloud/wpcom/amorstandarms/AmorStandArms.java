package cloud.wpcom.amorstandarms;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import cloud.wpcom.WPCraft;

public class AmorStandArms implements Listener {

    private final WPCraft wpcraft;
    private final ItemStack armorStandItem;
    private final ShapedRecipe armorStandRecipe;

    public AmorStandArms(WPCraft wpcraft) {
        this.wpcraft = wpcraft;

        // Create the special armor stand
        ItemStack item = new ItemStack(Material.ARMOR_STAND);
        item.getItemMeta().setDisplayName("Armor Stand w/ Arms");
        item.getItemMeta().addEnchant(Enchantment.LUCK, 1, true);
        item.getItemMeta().addItemFlags(ItemFlag.HIDE_ENCHANTS);
        armorStandItem = item;

        // Create the recipe for the special armor stand
        ShapedRecipe recipe = new ShapedRecipe(NamespacedKey.minecraft("armorstandarms"), armorStandItem);
        recipe.shape("SSS",
                "SSS",
                "STS");
        recipe.setIngredient('S', Material.STICK);
        recipe.setIngredient('T', Material.STONE_SLAB);
        armorStandRecipe = recipe;

        // Register the recipe
        this.wpcraft.getServer().addRecipe(armorStandRecipe);
    }
    
    public void onArmorStandPlace(PlayerInteractEvent event) {
        // Check if the interact event was placing a item/block
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        // TODO might need to check for interactible?
        // Check if the interaction was the top face of a block
        if (event.getBlockFace() != BlockFace.UP) {
            return;
        }

        final Player player = event.getPlayer();
        final PlayerInventory playerInventory = player.getInventory();

        // Check if the block placed was a special armor stand in main hand
        if (!playerInventory.getItemInMainHand().equals(armorStandItem)) {
        } else if (!player.getInventory().getItemInOffHand().equals(armorStandItem)) { // Check if the block placed was a special armor stand in off hand
            return;
        }
        event.setCancelled(true);

        // TODO Check if player is sneaking? if so maybe adjust placement?
        // Get position data for the placed armor stand
        final Location placedLocation = event.getClickedBlock().getLocation();
        placedLocation.add(0, 1, 0);
        placedLocation.setYaw(player.getLocation().getYaw() + 180F);

        // Place down the item stand with arms
        final Entity entity = player.getWorld().spawnEntity(placedLocation, EntityType.ARMOR_STAND);
        final ArmorStand armorStand = (ArmorStand) entity;
        armorStand.setArms(true);

        // Remove one of the special items from player
        final ItemStack adjustedItemStack;
        if (playerInventory.getItemInMainHand().equals(armorStandItem)) { // Main Hand
            adjustedItemStack = playerInventory.getItemInMainHand();
            adjustedItemStack.setAmount(adjustedItemStack.getAmount() - 1);
            playerInventory.setItemInMainHand(adjustedItemStack);

        } else { // Off Hand
            adjustedItemStack = playerInventory.getItemInOffHand();
            adjustedItemStack.setAmount(adjustedItemStack.getAmount() - 1);
            playerInventory.setItemInOffHand(adjustedItemStack);

        }
    }
}
