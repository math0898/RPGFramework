package io.github.math0898.utils.items;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ColorableArmorMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.*;

/**
 * The ItemBuilder is made to easily manufacture ItemStacks.
 *
 * @author Sugaku
 */
public class ItemBuilder {

    /**
     * The material that the resulting item will have.
     */
    private Material material;

    /**
     * The display name that the resulting item will have.
     */
    private String displayName = null;

    /**
     * The Lore that the resulting item will have.
     */
    private List<String> lore = null;

    /**
     * The uuid of the owning player. Ignored if not a player head.
     */
    private UUID owningPlayer = null;

    /**
     * A list of AttributeModifiers to this ItemStack.
     */
    private Map<Attribute, List<AttributeModifier>> modifiers = new HashMap<>();

    /**
     * The color of this Item. Ignored if not an instance of Colorable. Stored in ARGB.
     */
    private int[] color = null;

    /**
     * Any ItemFlags tha tare present on this ItemBuilder.
     */
    private List<ItemFlag> flags = new ArrayList<>();

    /**
     * Creates a new ItemBuilder using the given material.
     *
     * @param material The material to use for this ItemBuilder.
     */
    public ItemBuilder(Material material) {
        this.material = material;
    }

    /**
     * Called to set the material used for this ItemBuilder. Returns the ItemBuilder to allow for chain calling.
     *
     * @param material The material to set this ItemBuilder to.
     * @return The mutated ItemBuilder.
     */
    public ItemBuilder setMaterial (Material material) {
        this.material = material;
        return this;
    }

    /**
     * Called to set the display name used for this ItemBuilder. Returns the ItemBuilder to allow for chain calling.
     *
     * @param name The display name for the resulting item.
     * @return The mutated ItemBuilder.
     */
    public ItemBuilder setDisplayName (String name) {
        this.displayName = name;
        return this;
    }

    /**
     * Called to set the lore value used for this ItemBuilder. Returns the ItemBuilder to allow for chain calling.
     *
     * @param lore The lore for the resulting item.
     * @return The mutated ItemBuilder.
     */
    public ItemBuilder setLore (String[] lore) {
        this.lore = new ArrayList<>(List.of(lore));
        return this;
    }

    /**
     * Used to set the owning player for skulls. Otherwise, ignored.
     *
     * @param uuid The uuid of the owning player.
     * @return The mutated ItemBuilder.
     */
    public ItemBuilder setOwningPlayer (UUID uuid) {
        this.owningPlayer = uuid;
        return this;
    }

    /**
     * Adds an Attribute modifier with the given values to this item.
     *
     * @param attribute The attribute being modified.
     * @param value     The desired value.
     * @param slot      The slot this item will be equipped in.
     * @return The mutated ItemBuilder.
     */
    public ItemBuilder addAttributeModifier (Attribute attribute, double value, EquipmentSlot slot) {
        modifiers.putIfAbsent(attribute, new ArrayList<>());
        List<AttributeModifier> list = modifiers.get(attribute);
        list.add(attributeModifier(attribute, value, slot));
        return this;
    }

    /**
     * Sets the color of the resulting Item. Passed color should have a length of 4 and be in ARGB order.
     *
     * @param color The color to assign to this Item.
     * @return The mutated ItemBuilder.
     */
    public ItemBuilder setColor (int[] color) {
        if (color.length != 4) return this;
        this.color = color;
        return this;
    }

    /**
     * Adds flags to apply to this Item.
     *
     * @param flag The item flag to add to this item.
     * @return The mutated ItemBuilder.
     */
    public ItemBuilder addItemFlag (ItemFlag flag) {
        flags.add(flag);
        return this;
    }

    /**
     * Called to get the resulting ItemStack object.
     *
     * @return The builder's resulting item.
     */
    public ItemStack build () {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta == null) Bukkit.getItemFactory().getItemMeta(material);
        assert meta != null;
        if (displayName != null) meta.setDisplayName(displayName);
        if (lore != null) meta.setLore(lore);
        if (owningPlayer != null && meta instanceof SkullMeta skull)
            skull.setOwningPlayer(Bukkit.getOfflinePlayer(owningPlayer));
        modifiers.forEach((a, list) -> {
            if (list != null)
                if (!list.isEmpty())
                    list.forEach((m) -> meta.addAttributeModifier(a, m));
        });
        if (color != null && meta instanceof ColorableArmorMeta colorable)
            colorable.setColor(Color.fromARGB(color[0], color[1], color[2], color[3]));
        if (!flags.isEmpty())
            meta.addItemFlags(flags.toArray(new ItemFlag[0]));
        item.setItemMeta(meta);
        return item;
    }

    /**
     * Creates an AttributeModifier with a unique UUID.
     *
     * @param a The attribute being modified.
     * @param value The desired value.
     * @param slot The slot this should apply to.
     * @return The AttributeModifier with a unique UUID.
     */
    private AttributeModifier attributeModifier (Attribute a, double value, EquipmentSlot slot) {
        int mod = switch (a) {
            case GENERIC_MAX_HEALTH -> 1;
            case GENERIC_ARMOR -> 2;
            case GENERIC_ARMOR_TOUGHNESS -> 3;
            case GENERIC_ATTACK_DAMAGE -> 4;
            case GENERIC_KNOCKBACK_RESISTANCE -> 5;
            case GENERIC_MOVEMENT_SPEED -> 6;
            case GENERIC_LUCK -> 7;
            case HORSE_JUMP_STRENGTH -> 8;
            case GENERIC_ATTACK_SPEED -> 9;
            case GENERIC_ATTACK_KNOCKBACK -> 10;
            case GENERIC_FLYING_SPEED -> 11;
            case GENERIC_FOLLOW_RANGE -> 12;
            case ZOMBIE_SPAWN_REINFORCEMENTS -> 13;
            default -> 0;
        };
        int slotN = switch (slot) {
            case FEET -> 1;
            case LEGS -> 2;
            case CHEST -> 3;
            case HEAD -> 4;
            case HAND -> 5;
            case OFF_HAND -> 6;
        };
        return new AttributeModifier(new UUID(slotN, mod), a.toString(), value, AttributeModifier.Operation.ADD_NUMBER, slot);
    }
}
