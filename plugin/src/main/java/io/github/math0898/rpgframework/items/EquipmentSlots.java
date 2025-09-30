package io.github.math0898.rpgframework.items;

import org.bukkit.inventory.EquipmentSlot;

/**
 * There are a number of equipment slots which players can equip items in. Most, but not all, of these relate to a
 * corresponding vanilla slot.
 *
 * @author Sugaku
 */
public enum EquipmentSlots {

    /**
     * The Artifact slot is a custom item slot.
     */
    ARTIFACT,

    /**
     * The player's main hand.
     */
    HAND,

    /**
     * The player's off-hand slot.
     */
    OFF_HAND,

    /**
     * Helmets and other headgear.
     */
    HEAD,

    /**
     * Chest-plates and core protection.
     */
    CHEST,

    /**
     * Pants and shorts.
     */
    LEGS,

    /**
     * Boots and shoes.
     */
    FEET,

    /**
     * An item that is used like a spell instead of any kind of equipping.
     */
    ON_USE,

    /**
     * A custom item that is not equipped anywhere, instead just utilized for crafting.
     */
    MATERIAL;

    /**
     * Returns the Bukkit EquipmentSlot associated with this EquipmentSlot.
     *
     * @return The EquipmentSlot associated with this slot, if present in vanilla, otherwise null.
     */
    public EquipmentSlot getSlot () {
        return switch (this) {
            default -> null;
            case HAND -> EquipmentSlot.HAND;
            case OFF_HAND -> EquipmentSlot.OFF_HAND;
            case HEAD -> EquipmentSlot.HEAD;
            case CHEST -> EquipmentSlot.CHEST;
            case LEGS -> EquipmentSlot.LEGS;
            case FEET -> EquipmentSlot.FEET;
        };
    }
}
