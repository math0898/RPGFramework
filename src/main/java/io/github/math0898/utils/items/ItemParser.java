package io.github.math0898.utils.items;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ColorableArmorMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * The ItemParser class parses configuration sections into a usable ItemStack.
 *
 * @author Sugaku
 */
public class ItemParser {

    /**
     * The material of the ItemStack in this ItemParser.
     */
    private Material type;

    /**
     * The amount of the ItemStack in this ItemParser.
     */
    private int amount;

    /**
     * The meta of this ItemStack.
     */
    private ItemMeta meta;

    /**
     * Creates a new ItemParser with the given configuration section.
     *
     * @param section The configuration section to parse into an ItemStack.
     */
    public ItemParser (ConfigurationSection section) {
        type = Material.valueOf(section.getString("type", "IRON_NUGGET"));
        amount = section.getInt("amount", 1);
        if (section.contains("meta")) {
            ConfigurationSection tmp = section.getConfigurationSection("meta");
            assert tmp != null; // 46
            meta = parseMeta(tmp);
        }
    }

    /**
     * Parses the given configuration section into ItemMeta.
     *
     * @param section The configuration section to pull the ItemMeta from.
     * @return The parsed ItemMeta.
     */
    public ItemMeta parseMeta (ConfigurationSection section) {
        String displayName = section.getString("display-name", ChatColor.WHITE + "Failed to Parse Name");
        List<String> lore = section.getStringList("lore");
        boolean unbreakable = section.getBoolean("Unbreakable", false);
        int customModelData = section.getInt("custom-model-data", 0);
        List<String> itemFlags = section.getStringList("ItemFlags");
        List<ItemFlag> flags = new ArrayList<>();
        for (String s : itemFlags)
            flags.add(ItemFlag.valueOf(s));
        ItemMeta meta = Bukkit.getItemFactory().getItemMeta(type);
        assert meta != null;
        if (meta instanceof ColorableArmorMeta colorable) parseColor(colorable, section.getConfigurationSection("color"));
        meta.setLore(lore);
        if (section.contains("attribute-modifiers")) {
            ConfigurationSection tmp = section.getConfigurationSection("attribute-modifiers");
            assert tmp != null; // 63
            parseAttributes(tmp, meta);
        }
        meta.setDisplayName(displayName);
        meta.setUnbreakable(unbreakable);
        meta.setCustomModelData(customModelData);
        meta.addItemFlags(flags.toArray(new ItemFlag[0]));
        return meta;
    }

    /**
     * Parses the attributes in the given section and mutates the given ItemMeta.
     *
     * @param section The configuration section to pull the data from.
     * @param meta The meta to mutate.
     */
    public void parseAttributes (ConfigurationSection section, ItemMeta meta) {
        for (String s : section.getKeys(false)) {
            Attribute attribute = Attribute.valueOf(s);
            double amnt = section.getDouble(s + ".amount", 0.0);
            String name = section.getString(s + ".name", "DefaultName");
            EquipmentSlot slot = EquipmentSlot.valueOf(section.getString(s + ".slot", "CHEST"));
            UUID uuid = UUID.fromString(section.getString(s + ".uuid", "00000000-0000-0000-0000-000000000000"));
            AttributeModifier.Operation op = AttributeModifier.Operation.ADD_NUMBER;
            AttributeModifier modifier = new AttributeModifier(uuid,  name, amnt, op, slot);
            meta.addAttributeModifier(attribute, modifier);
        }
    }

    /**
     * Parses the given color configuration section and mutates the given ColorableArmorMeta to use the given color.
     *
     * @param meta    The meta to mutate.
     * @param section The section to pull the data from.
     */
    public void parseColor (ColorableArmorMeta meta, ConfigurationSection section) {
        meta.setColor(Color.fromARGB(section.getInt("ALPHA", 255),
                section.getInt("RED", 0),
                section.getInt("GREEN", 0),
                section.getInt("BLUE", 0)));
    }

    /**
     * Builds this ItemParser into an actual ItemStack.
     *
     * @return The final ItemStack.
     */
    public ItemStack build () {
        ItemStack item = new ItemStack(type, amount);
        if (meta != null) item.setItemMeta(meta);
        return item;
    }
}
