package io.github.math0898.rpgframework.items;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static io.github.math0898.rpgframework.main.console;
import static io.github.math0898.rpgframework.main.plugin;
import static org.bukkit.attribute.AttributeModifier.Operation.ADD_NUMBER;

/**
 * The ItemManager is used to crate RPG related items on load.
 *
 * @author Sugaku
 */
public class ItemManager {

    /**
     * A list of RpgItems which have been registered.
     */
    private final Map<String, ItemStack> rpgItems = new HashMap<>();

    /**
     * Creates a new ItemManager.
     */
    public ItemManager () {
        File itemsDir = new File("./plugins/RPGFramework/items/");
        if (!itemsDir.exists()) {
            if (!itemsDir.mkdirs()) {
                console("Failed to create item directories.", ChatColor.YELLOW);
                return;
            }
            plugin.saveResource("items/krusk.yml", false); // todo: refactor to reduce scope when adding multiple bosses and sets.
        }
        File[] files = itemsDir.listFiles();
        if (files == null) {
            console("Cannot find any item files.", ChatColor.YELLOW);
            return;
        }
        for (File f : files) {
            try {
                YamlConfiguration yaml = new YamlConfiguration();
                yaml.load(f);
                for (String k : yaml.getKeys(false)) {
                    ItemStack i = yaml.getItemStack(k);
                    if (i != null) {
                        rpgItems.put(k, i);
                        console("Registered item by name: " + k, ChatColor.GRAY);
                    } else console("Failed to parse: " + k + " in: " + f.getPath(), ChatColor.RED);
                }
            } catch (InvalidConfigurationException | IOException e) {
                console(e.getMessage(), ChatColor.RED);
                console("Failed to parse item located at: " + f.getPath(), ChatColor.RED);
            }
        }
    }

    /**
     * Accessor method for all the items in the ItemManager.
     *
     * @return The list of items registered with the ItemManager.
     */
    public List<String> getItemNames () {
        return new ArrayList<>(rpgItems.keySet());
    }

    /**
     * Accessor method for items by the given name.
     *
     * @param name The name of the item to get.
     * @return The RpgItem associated with the given name.
     */
    public ItemStack getItem (String name) {
        return rpgItems.get(name);
    }
}
