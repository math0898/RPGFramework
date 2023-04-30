package io.github.math0898.rpgframework.items;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.Arrays;

/**
 * Utility class used to make item creation easier. Follows the builder paradigm.
 *
 * @author Sugaku
 */
public class ItemBuilder {

    /**
     * The material to use when creating a new ItemStack.
     */
    private Material material;

    /**
     * The amount of items in the stack of this ItemStack.
     */
    private int amount = 1;

    /**
     * The name to give this ItemStack.
     */
    private String name = null;

    /**
     * Whether this item is unbreakable or not.
     */
    private boolean unbreakable = false;

    /**
     * The lore to assign to this item.
     */
    private String[] lore = null;

    /**
     * RGB values to assign to leather armor when constructing leather.
     */
    private int[] rgb = null;

    /**
     * The attribute modifiers to add to this ItemStack.
     */
    private AttributeModifier[] modifiers = null;

    /**
     * Creates a new ItemBuilder with the given material as the base.
     *
     * @param material The material to use in item creation.
     */
    public ItemBuilder (Material material) {
        this.material = material;
    }

    /**
     * Creates a new ItemBuilder with the given material and name as the base.
     *
     * @param material The material to use in item creation.
     * @param name The name to give this ItemStack.
     */
    public ItemBuilder (Material material, String name) {
        this.material = material;
        this.name = name;
    }

    /**
     * Creates a new ItemBuilder with the given material and amount as the base.
     *
     * @param material The material to use in item creation.
     * @param amount The amount of this item.
     */
    public ItemBuilder (Material material, int amount) {
        this.material = material;
        this.amount = amount;
    }

    /**
     * Creates a new ItemBuilder with the given material, amount, and name as the base.
     *
     * @param material The material to use in item creation.
     * @param amount The amount of this item.
     * @param name The name of this item.
     */
    public ItemBuilder (Material material, int amount, String name) {
        this.material = material;
        this.amount = amount;
        this.name = name;
    }

    /**
     * Assigns a different material to this ItemStack.
     *
     * @param material The material to change this ItemStack to.
     * @return This ItemBuilder object to allow chaining.
     */
    public ItemBuilder setMaterial (Material material) {
        this.material = material;
        return this;
    }

    /**
     * Assigns the given name to this ItemStack.
     *
     * @param name The name to give this ItemStack.
     * @return This ItemBuilder object to allow chaining.
     */
    public ItemBuilder setName (String name) {
        this.name = name;
        return this;
    }

    /**
     * Sets the unbreakable flag to the given value for this ItemStack.
     *
     * @param unbreakable The value to assign to the unbreakable flag.
     * @return This ItemBuilder object to allow chaining.
     */
    public ItemBuilder setUnbreakable (boolean unbreakable) {
        this.unbreakable = unbreakable;
        return this;
    }

    /**
     * Sets the lore of this ItemStack.
     *
     * @param lore The lore to assign to this ItemStack.
     * @return This ItemBuilder object to allow chaining.
     */
    public ItemBuilder setLore (String[] lore) {
        this.lore = lore;
        return this;
    }

    /**
     * Sets the rgb value of this ItemStack using three ints.
     *
     * @param r The red value.
     * @param g The green value.
     * @param b The blue value.
     * @return This ItemBuilder object to allow chaining.
     */
    public ItemBuilder setRGB (int r, int g, int b) {
        this.rgb = new int[]{ r, g, b};
        return this;
    }

    /**
     * Sets the rgb value of this ItemStack using an int array.
     *
     * @param rgb The rgb values to assign to this item.
     * @return This ItemBuilder object to allow chaining.
     */
    public ItemBuilder setRGB (int[] rgb) {
        this.rgb = new int[]{ rgb[0], rgb[1], rgb[2] };
        return this;
    }

    /**
     * Sets the attribute modifiers to live on this ItemStack.
     *
     * @param modifiers The array of modifiers to apply to this item.
     * @return This ItemBuilder object to allow chaining.
     */
    public ItemBuilder setModifiers (AttributeModifier[] modifiers) {
        this.modifiers = modifiers;
        return this;
    }

    /**
     * Creates the ItemStack that was constructed using this builder.
     *
     * @return The ItemStack that results from this builder configuration.
     */
    public ItemStack build () {
        ItemStack i = new ItemStack(material, amount);
        ItemMeta meta = i.getItemMeta();
        if (meta == null) return i;
        if (name != null) meta.setDisplayName(name);
        if (lore != null) meta.setLore(Arrays.asList(lore));
        if (rgb != null)
            if (meta instanceof LeatherArmorMeta leather)
                leather.setColor(Color.fromRGB(rgb[0], rgb[1], rgb[2]));
        if (modifiers != null)
            for (AttributeModifier m : modifiers)
                meta.addAttributeModifier(Attribute.valueOf(m.getName()), m);
        meta.setUnbreakable(unbreakable);
        i.setItemMeta(meta);
        return i;
    }
}
