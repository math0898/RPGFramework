package io.github.math0898.rpgframework.items.editor;

import io.github.math0898.rpgframework.Rarity;
import io.github.math0898.rpgframework.items.ArmorTypes;
import io.github.math0898.rpgframework.items.EquipmentSlots;
import io.github.math0898.rpgframework.items.RpgItem;
import io.github.math0898.rpgframework.items.WeaponType;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * An ItemConstruct is a constructed item in the EditorGUI.
 *
 * @param material    The material of this construct.
 * @param name        The display name of this construct.
 * @param slot        The equipment slot this construct belongs in.
 * @param description Any lore that should be present on this construct.
 * @param rarity      The rarity of this construct.
 * @param health      Any health that this construct may give.
 * @param damage      Any damage stat included on this construct.
 * @param armor       The armor value of this construct.
 * @param toughness   Toughness on this construct.
 * @param attackSpeed The attackSpeed stat of this construct.
 * @param armorType   Any, if, armor type is present.
 * @param weaponType  Any, if, weapon type is present.
 * @param color       The ARGB of this construct.
 * @author Sugaku
 */
public record ItemConstruct (Material material,
                             String name,
                             EquipmentSlots slot,
                             List<String> description,
                             Rarity rarity,
                             int health,
                             int damage,
                             double armor,
                             double toughness,
                             double attackSpeed,
                             ArmorTypes armorType,
                             WeaponType weaponType,
                             int[] color) {

    /**
     * Converts this ItemConstruct into an actual item in game that can be previewed.
     *
     * @return The ItemStack representation of this ItemConstruct.
     */
    public ItemStack toItemStack () {
        RpgItem rpg = new RpgItem(material, name, slot, description, rarity, health, damage, armor, toughness, attackSpeed, armorType, weaponType, color);
        return rpg.getItemStack();
    }
}
