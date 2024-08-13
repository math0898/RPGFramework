package io.github.math0898.rpgframework.enemies;

import io.github.math0898.rpgframework.items.ItemManager;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

/**
 * A MobDrop represents an ItemStack that mobs can drop on death.
 *
 * @author Sugaku
 */
public class MobDrop {

    /**
     * The chance to drop this item.
     */
    private final double chance;

    /**
     * The minimum amount that must be awarded.
     */
    private final int minAmount;

    /**
     * The maximum amount that can be awarded.
     */
    private final int maxAmount;

    /**
     * The NamespaceKey of the item in the ItemManager this MobDrop corresponds to. May be null.
     */
    private final String namespaceKey;

    /**
     * The material associated to this MobDrop.
     */
    private final Material material;

    /**
     * Creates a new MobDrop with data from the given ConfigurationSection.
     *
     * @param section The configuration section to pull data from.
     */
    public MobDrop (ConfigurationSection section) {
        if (section == null) { // Generate a degenerate object when given a null ConfigurationSection. Saves on error checking.
            chance = 0;
            minAmount = 0;
            maxAmount = 0;
            namespaceKey = null;
            material = null;
            return;
        }
        chance = section.getDouble("chance", 1.0);
        minAmount = section.getInt("min-amount", 1);
        maxAmount = section.getInt("max-amount", 1);
        if (section.contains("namespace-key"))
            namespaceKey = section.getString("namespace-key");
        else namespaceKey = null;
        if (section.contains("material"))
            material = Material.valueOf(section.getString("material", "AIR"));
        else material = null;
    }

    /**
     * An accessor method to get the chance associated with a particular drop.
     *
     * @return The chance (0,1] to drop this item.
     */
    public double getChance () {
        return chance;
    }

    /**
     * Gets the ItemStack associated with this MobDrop. This will generate from the ItemManager if a NamespaceKey is
     * provided. It will also roll for the amount.
     *
     * @return The ItemStack defined by this MobDrop.
     */
    public ItemStack getItemStack () {
        ItemStack item = new ItemStack(Material.AIR);
        if (namespaceKey != null) item = ItemManager.getInstance().getItem(namespaceKey);
        else if (material != null) item.setType(material);
        if (minAmount != maxAmount) item.setAmount(new Random().nextInt(maxAmount - minAmount + 1) + minAmount);
        else item.setAmount(minAmount);
        return item;
    }
}
