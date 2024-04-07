package io.github.math0898.rpgframework.items;

import io.github.math0898.rpgframework.RPGFramework;
import io.github.math0898.rpgframework.Rarity;
import io.github.math0898.utils.StringUtils;
import io.github.math0898.utils.items.ItemBuilder;
import io.github.math0898.utils.items.ItemParser;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * An RpgItem is a wrapper for an ItemStack. It contains data on the item's rarity and supports placeholders.
 *
 * @author Sugaku
 */
public class RpgItem {

    /**
     * A generated ItemStack representation of this RpgItem.
     */
    private ItemStack item;

    /**
     * The rarity of this RpgItem.
     */
    private final Rarity rarity;

    /**
     * The material of this RpgItem.
     */
    private final Material material;

    /**
     * The name of this RpgItem.
     */
    private final String name;

    /**
     * The slot that this ItemStack should be equipped in.
     */
    private final EquipmentSlots slot;

    /**
     * The description of the ItemStack.
     */
    private final List<String> description;

    /**
     * The amount of health this ItemStack gives.
     */
    private final int health;

    /**
     * The amount of damage this ItemStack gives.
     */
    private final int damage;

    /**
     * The amount of armor that this ItemStack gives.
     */
    private final double armor;

    /**
     * The amount of armor toughness that this ItemStack gives.
     */
    private final double toughness;

    /**
     * The amount of attack speed that this ItemStack has.
     */
    private final double attackSpeed;

    /**
     * The color of this leather armor. Stored in ARGB.
     */
    private final int[] color;

    /**
     * The armor type of this RpgItem.
     */
    private final ArmorTypes armorType;

    /**
     * The weapon type of this RpgItem.
     */
    private final WeaponType weaponType;

    /**
     * Creates a new RpgItem by loading the given configuration section.
     *
     * @param section The configuration section to load.
     */
    public RpgItem (ConfigurationSection section) {
        if (section == null)throw new NullPointerException("Configuration section is null!");
        if (section.contains("meta")) {
            Logger logger = RPGFramework.getInstance().getLogger();
            logger.log(Level.WARNING, "Using legacy item support.");
            item = new ItemParser(section).build();
        }
        material = Material.valueOf(section.getString("material", "COBBLESTONE"));
        name = section.getString("name", "No name");
        slot = EquipmentSlots.valueOf(section.getString("slot", "HEAD"));
        description = section.getStringList("description");
        rarity = Rarity.valueOf(section.getString("rarity", "COMMON"));
        health = section.getInt("stats.health", 0);
        damage = section.getInt("stats.damage", 0);
        armor = section.getDouble("stats.armor", 0);
        toughness = section.getDouble("stats.toughness", 0);
        attackSpeed = section.getDouble("stats.attack-speed", 0);
        if (section.contains("armor-type")) armorType = ArmorTypes.valueOf(section.getString("armor-type", "HEAVY"));
        else armorType = null;
        if (section.contains("weapon-type")) weaponType = WeaponType.valueOf(section.getString("weapon-type", "SWORD"));
        else weaponType = null;
        color = new int[]{ section.getInt("color.alpha", 255),
                section.getInt("color.red", 0),
                section.getInt("color.green", 0),
                section.getInt("color.blue", 0) };
        if (item == null) item = generateItemStack();
    }

    /**
     * Generates an ItemStack with the assigned values in this item.
     */
    private ItemStack generateItemStack () {
        return generateItemStack(false);
    }

    /**
     * Generates an ItemStack with the assigned values in this item.
     *
     * @param update Whether placeholders should be incremented and added.
     */
    private ItemStack generateItemStack (boolean update) {
        ItemBuilder builder = new ItemBuilder(material);
        builder.setDisplayName(StringUtils.convertHexCodes(rarity.getHexColor() + name));
        List<String> clone = new ArrayList<>(description);
        for (int i = 0; i < clone.size(); i++) // todo: Placeholder support.
            clone.set(i, StringUtils.convertHexCodes("#CCCCCC" + description.get(i)));
        clone.add(StringUtils.convertHexCodes("#606060") + " ----- ----- ----- ");
        if (damage != 0) clone.add(StringUtils.convertHexCodes("#D93747") + "Damage: " + damage);
        if (attackSpeed != 0) clone.add(StringUtils.convertHexCodes("#3366FF") + "Attack Speed:" + attackSpeed);
        if (health != 0) clone.add(StringUtils.convertHexCodes("#F454DA") + "Health: " + health);
        if (armor != 0) clone.add(StringUtils.convertHexCodes("#3FB74A") + "Armor: " + armor);
        if (toughness != 0) clone.add(StringUtils.convertHexCodes("#CCFF33") + "Toughness: " + toughness);
        clone.add(StringUtils.convertHexCodes("#606060") + " ----- ----- ----- ");
        String details = "#CCCCCC" + StringUtils.capitalize(slot.toString());
        if (armorType != null) details = details + "#606060 | " + armorType.getFormattedName();
        if (weaponType != null) details = details + "#606060 | " + weaponType.getFormattedName();
        details = details + "#606060 | #F2D951" + getGearScore();
        clone.add(StringUtils.convertHexCodes(details));
        builder.setLore(clone.toArray(new String[0]));
        builder.addAttributeModifier(Attribute.GENERIC_MAX_HEALTH, health / 5.0, slot);
        builder.addAttributeModifier(Attribute.GENERIC_ARMOR, armor, slot);
        builder.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, toughness, slot);
        builder.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, attackSpeed, slot);
        builder.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, damage / 5.0, slot);
        builder.setColor(color);
        builder.setUnbreakable(true);
        builder.addItemFlag(ItemFlag.HIDE_ATTRIBUTES);
        builder.addItemFlag(ItemFlag.HIDE_UNBREAKABLE);
        return builder.build();
    }

    /**
     * A helper method that calculates the gear score of ths RpgItem.
     *
     * @return The gear score of this item.
     */
    public int getGearScore () {
        return (int) ((health * 1.0) + (damage * 5.0) + (armor * 2.0)) + (rarity.ordinal() * 10);
    }

    /**
     * An accessor method to the ItemStack represented in this RpgItem. This will not activate and increment
     * placeholders.
     *
     * @return Returns an ItemStack representation of this item.
     */
    public ItemStack getItemStack () {
        return item;
    }
}
