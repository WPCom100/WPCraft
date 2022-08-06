package cloud.wpcom.amorstandarms;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import cloud.wpcom.WPCraft;

public class AmorStandArms implements Listener {

    private final WPCraft wpcraft;
    private final ItemStack armorStandItem;

    public AmorStandArms(WPCraft wpcraft) {
        this.wpcraft = wpcraft;

        // Create the special armor stand
        ItemStack item = new ItemStack(Material.ARMOR_STAND);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName("Armor Stand w/ Arms");
        itemMeta.addEnchant(Enchantment.LUCK, 1, true);
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(itemMeta);
        armorStandItem = item;

        // Create the recipe for the special armor stand
        ShapedRecipe recipe = new ShapedRecipe(NamespacedKey.minecraft("armorstandarms"), armorStandItem);
        recipe.shape("SSS", "SSS", "STS");
        recipe.setIngredient('S', Material.STICK);
        recipe.setIngredient('T', Material.SMOOTH_STONE_SLAB);

        // Create the upgrade recipe for the special armor stand
        ShapelessRecipe upgradeRecipe = new ShapelessRecipe(NamespacedKey.minecraft("armorstandarmsupgrade"), armorStandItem);
        upgradeRecipe.addIngredient(2, Material.STICK);
        upgradeRecipe.addIngredient(1, Material.ARMOR_STAND);

        // Register the recipes
        this.wpcraft.getServer().addRecipe(recipe);
        this.wpcraft.getServer().addRecipe(upgradeRecipe);
    }
    
    @EventHandler
    public void onArmorStandPlace(PlayerInteractEvent event) {
        // Ignores offhand events to avoid duplicates
        if (event.getHand().equals(EquipmentSlot.OFF_HAND)) {
            return;
        }

        // Check if the interact event was placing a item/block
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        final Player player = event.getPlayer();
        final PlayerInventory playerInventory = player.getInventory();

        // Check if the block placed was a special armor stand in main hand
        if (!playerInventory.getItemInMainHand().isSimilar(armorStandItem)) {   
            if (!player.getInventory().getItemInOffHand().isSimilar(armorStandItem)) { // Off hand
                return;
            }
        }

        // Check if block was interactable and player is not sneaking
        if (event.getClickedBlock().getType().isInteractable() && !player.isSneaking()) {
            return;
        }

        // Check if the interaction was the top face of a block
        if (event.getBlockFace() != BlockFace.UP) {
            event.setCancelled(true);
            return;
        }

        event.setCancelled(true);

        // Get position data for the placed armor stand
        final Location placedLocation = event.getClickedBlock().getLocation();
        placedLocation.add(0.5, 1, 0.5);
        placedLocation.setYaw(player.getLocation().getYaw() + 180F);

        // Place down the item stand with arms
        final Entity entity = player.getWorld().spawnEntity(placedLocation, EntityType.ARMOR_STAND);
        final ArmorStand armorStand = (ArmorStand) entity;
        armorStand.setArms(true);

        // Give the stand a persistant key to handle being picked back up
        armorStand.getPersistentDataContainer().set(NamespacedKey.minecraft("arms"), PersistentDataType.INTEGER, 1);

        // Remove one of the special items from player
        final ItemStack adjustedItemStack;
        if (playerInventory.getItemInMainHand().isSimilar(armorStandItem)) { // Main Hand
            adjustedItemStack = playerInventory.getItemInMainHand();
            adjustedItemStack.setAmount(adjustedItemStack.getAmount() - 1);
            playerInventory.setItemInMainHand(adjustedItemStack);

        } else { // Off Hand
            adjustedItemStack = playerInventory.getItemInOffHand();
            adjustedItemStack.setAmount(adjustedItemStack.getAmount() - 1);
            playerInventory.setItemInOffHand(adjustedItemStack);

        }
    }

    //TODO add entity kill event to check for armor stands with data that says it had arms
    // if it had arms, drop one of the items
    @EventHandler
    public void onArmorStandBreak(EntityDeathEvent event) {
        // Check if a the entity was an armor stand
        if (event.getEntityType() != EntityType.ARMOR_STAND) {
            return;
        }

        // Check if it was an armor stand with arms
        if (event.getEntity().getPersistentDataContainer().has(NamespacedKey.minecraft("arms"),
                PersistentDataType.INTEGER)) {
            event.getDrops().clear();
            event.getDrops().add(armorStandItem);
        }
    }
}
