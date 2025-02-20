package io.github.math0898.utils.items;

import io.github.math0898.rpgframework.items.EquipmentSlots;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlot;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AttributeBuilder { // todo: Design and implement

    /**
     * Creates an AttributeModifier with a unique UUID.
     *
     * @param a The attribute being modified.
     * @param value The desired value.
     * @param slot The slot this should apply to.
     * @return The AttributeModifier with a unique UUID.
     */
    public static AttributeModifier attributeModifier (Attribute a, double value, EquipmentSlot slot) {
        int mod = switch (a) { // Todo: Support additional, new attribute types.
            case GENERIC_MAX_HEALTH -> 1;
            case GENERIC_ARMOR -> 2;
            case GENERIC_ARMOR_TOUGHNESS -> 3;
            case GENERIC_ATTACK_DAMAGE -> 4;
            case GENERIC_KNOCKBACK_RESISTANCE -> 5;
            case GENERIC_MOVEMENT_SPEED -> 6;
            case GENERIC_LUCK -> 7;
            case GENERIC_JUMP_STRENGTH -> 8;
            case GENERIC_ATTACK_SPEED -> 9;
            case GENERIC_ATTACK_KNOCKBACK -> 10;
            case GENERIC_FLYING_SPEED -> 11;
            case GENERIC_FOLLOW_RANGE -> 12;
            case ZOMBIE_SPAWN_REINFORCEMENTS -> 13;
            default -> 0;
        };
        int slotN;
        if (slot == null) slotN = 0;
        else slotN = switch (slot) {
            case FEET -> 1;
            case LEGS -> 2;
            case CHEST -> 3;
            case HEAD -> 4;
            case HAND -> 5;
            case OFF_HAND -> 6;
            case BODY -> 7;
        };
        return new AttributeModifier(new UUID(slotN, mod), a.toString(), value, AttributeModifier.Operation.ADD_NUMBER, slot);
    }
}
