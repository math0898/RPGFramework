package io.github.math0898.utils.items;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

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
        if (section.contains("meta")) meta = parseMeta(section.getConfigurationSection("meta"));
    }

    /**
     * Parses the given configuration section into ItemMeta.
     *
     * @param section The configuration section to pull the ItemMeta from.
     * @return The parsed ItemMeta.
     */
    public ItemMeta parseMeta (ConfigurationSection section) { // todo: ItemFlags
        String displayName = section.getString("display-name", ChatColor.WHITE + "Failed to Parse Name");
        List<String> lore = section.getStringList("lore");
        boolean unbreakable = section.getBoolean("Unbreakable", false);
        int customModelData = section.getInt("custom-model-data", 0);
        ItemMeta meta = Bukkit.getItemFactory().getItemMeta(type);
        assert meta != null;
        meta.setLore(lore);
        if (section.contains("attribute-modifiers"))
            parseAttributes(section.getConfigurationSection("attribute-modifiers"), meta);
        meta.setDisplayName(displayName);
        meta.setUnbreakable(unbreakable);
        meta.setCustomModelData(customModelData);
        return meta;
    }

    /**
     * Parses the attributes in the given section and mutates the given ItemMeta.
     *
     * @param section The configuration section to pull the data from.
     * @param meta The meta to mutate.
     */
    public void parseAttributes (ConfigurationSection section, ItemMeta meta) {
        // todo: Implement.
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
