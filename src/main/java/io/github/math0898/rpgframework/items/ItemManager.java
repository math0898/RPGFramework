package io.github.math0898.rpgframework.items;

import org.bukkit.ChatColor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.github.math0898.rpgframework.main.console;

/**
 * The ItemManager is used to crate RPG related items on load.
 *
 * @author Sugaku
 */
public class ItemManager {

    /**
     * A list of RpgItems which have been registered.
     */
    private final Map<String, RpgItem> rpgItems = new HashMap<>();

    /**
     * Creates a new ItemManager.
     */
    public ItemManager () {
        File itemsDir = new File("./plugins/RPGFramework/items/");
        if (!itemsDir.exists()) if (!itemsDir.mkdirs()) {
            console("Failed to create item directories.", ChatColor.YELLOW);
            return;
        }
        File[] files = itemsDir.listFiles();
        if (files == null) {
            console("Cannot find any item files.", ChatColor.YELLOW);
            return;
        }
        for (File f : files) {
            try {
                RpgItem r = new RpgItem(f);
                rpgItems.put(r.getInternalName(), r);
                console("Registered item by name: " + r.getInternalName(), ChatColor.GRAY);
            } catch (IOException e) {
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
    public RpgItem getItem (String name) {
        return rpgItems.get(name);
    }
}
