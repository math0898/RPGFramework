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
        int mod = attributeId(a);
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
            default -> 0;
        };
        return new AttributeModifier(new UUID(slotN, mod), a.toString(), value, AttributeModifier.Operation.ADD_NUMBER, slot);
    }

    private static int attributeId(Attribute attribute) {
        if (attribute == Attribute.MAX_HEALTH) return 1;
        if (attribute == Attribute.ARMOR) return 2;
        if (attribute == Attribute.ARMOR_TOUGHNESS) return 3;
        if (attribute == Attribute.ATTACK_DAMAGE) return 4;
        if (attribute == Attribute.KNOCKBACK_RESISTANCE) return 5;
        if (attribute == Attribute.MOVEMENT_SPEED) return 6;
        if (attribute == Attribute.LUCK) return 7;
        if (attribute == Attribute.JUMP_STRENGTH) return 8;
        if (attribute == Attribute.ATTACK_SPEED) return 9;
        if (attribute == Attribute.ATTACK_KNOCKBACK) return 10;
        if (attribute == Attribute.FLYING_SPEED) return 11;
        if (attribute == Attribute.FOLLOW_RANGE) return 12;
        if (attribute == Attribute.SPAWN_REINFORCEMENTS) return 13;
        return 0;
    }
}
