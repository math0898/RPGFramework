package io.github.math0898.rpgframework.items.editor;

import io.github.math0898.rpgframework.Rarity;
import io.github.math0898.rpgframework.items.ArmorTypes;
import io.github.math0898.rpgframework.items.EquipmentSlots;
import io.github.math0898.rpgframework.items.RpgItem;
import io.github.math0898.rpgframework.items.WeaponType;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Objects;

/**
 * An ItemConstruct is a constructed item in the EditorGUI.
 *
 * @author Sugaku
 */
@Setter
public final class ItemConstruct {
    private Material material;
    private String name;
    private EquipmentSlots slot;
    private List<String> description;
    private Rarity rarity;
    private int health;
    private int damage;
    private double armor;
    private double toughness;
    private double attackSpeed;
    private ArmorTypes armorType;
    private WeaponType weaponType;
    private int[] color;

    /**
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
     */
    public ItemConstruct(Material material,
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
        this.material = material;
        this.name = name;
        this.slot = slot;
        this.description = description;
        this.rarity = rarity;
        this.health = health;
        this.damage = damage;
        this.armor = armor;
        this.toughness = toughness;
        this.attackSpeed = attackSpeed;
        this.armorType = armorType;
        this.weaponType = weaponType;
        this.color = color;
    }

    /**
     * Converts this ItemConstruct into an actual item in game that can be previewed.
     *
     * @return The ItemStack representation of this ItemConstruct.
     */
    public ItemStack toItemStack() {
        RpgItem rpg = new RpgItem(material, name, slot, description, rarity, health, damage, armor, toughness, attackSpeed, armorType, weaponType, color);
        return rpg.getItemStack();
    }

    public Material material() {
        return material;
    }

    public String name() {
        return name;
    }

    public EquipmentSlots slot() {
        return slot;
    }

    public List<String> description() {
        return description;
    }

    public Rarity rarity() {
        return rarity;
    }

    public int health() {
        return health;
    }

    public int damage() {
        return damage;
    }

    public double armor() {
        return armor;
    }

    public double toughness() {
        return toughness;
    }

    public double attackSpeed() {
        return attackSpeed;
    }

    public ArmorTypes armorType() {
        return armorType;
    }

    public WeaponType weaponType() {
        return weaponType;
    }

    public int[] color() {
        return color;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (ItemConstruct) obj;
        return Objects.equals(this.material, that.material) &&
                Objects.equals(this.name, that.name) &&
                Objects.equals(this.slot, that.slot) &&
                Objects.equals(this.description, that.description) &&
                Objects.equals(this.rarity, that.rarity) &&
                this.health == that.health &&
                this.damage == that.damage &&
                Double.doubleToLongBits(this.armor) == Double.doubleToLongBits(that.armor) &&
                Double.doubleToLongBits(this.toughness) == Double.doubleToLongBits(that.toughness) &&
                Double.doubleToLongBits(this.attackSpeed) == Double.doubleToLongBits(that.attackSpeed) &&
                Objects.equals(this.armorType, that.armorType) &&
                Objects.equals(this.weaponType, that.weaponType) &&
                Objects.equals(this.color, that.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(material, name, slot, description, rarity, health, damage, armor, toughness, attackSpeed, armorType, weaponType, color);
    }

    @Override
    public String toString() {
        return "ItemConstruct[" +
                "material=" + material + ", " +
                "name=" + name + ", " +
                "slot=" + slot + ", " +
                "description=" + description + ", " +
                "rarity=" + rarity + ", " +
                "health=" + health + ", " +
                "damage=" + damage + ", " +
                "armor=" + armor + ", " +
                "toughness=" + toughness + ", " +
                "attackSpeed=" + attackSpeed + ", " +
                "armorType=" + armorType + ", " +
                "weaponType=" + weaponType + ", " +
                "color=" + color + ']';
    }

}
