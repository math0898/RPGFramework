package io.github.math0898.rpgframework.items;

import io.github.math0898.rpgframework.RPGFramework;
import io.github.math0898.rpgframework.items.implementations.SylvathianThornWeaver;
import io.github.math0898.rpgframework.items.implementations.WrathOfFeyrith;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static io.github.math0898.rpgframework.RPGFramework.*;

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
     */ // todo: Refactor to singleton design pattern.
    public ItemManager () {
        File itemsDir = new File("./plugins/RPGFramework/items/");
        if (!itemsDir.exists()) {
            if (!itemsDir.mkdirs()) {
                console("Failed to create item directories.", ChatColor.YELLOW);
                return;
            }
        }
        for (String itemResources : new String[]{ "items/krusk.yml", "items/other.yml", "items/eiryeras.yml", "items/feyrith.yml"})
            plugin.saveResource(itemResources, true); // todo: refactor to reduce scope when adding multiple bosses and sets.
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
                    ItemStack i = null;
                    try {
                        i = yaml.getItemStack(k);
                    } catch (Exception ignored) {}
                    String goodName = toCamelSpaceNamespace(f.getName(), k);
                    if (i != null) {
                        rpgItems.put(goodName, i);
                        console("Registered item by name: " + goodName, ChatColor.GRAY);
                    } else console("Failed to parse: " + goodName + " in: " + f.getPath(), ChatColor.RED);
                }
            } catch (InvalidConfigurationException | IOException e) {
                console(e.getMessage(), ChatColor.RED);
                console("Failed to parse item located at: " + f.getPath(), ChatColor.RED);
            }
        }
        Bukkit.getScheduler().runTaskAsynchronously(getInstance(), this::passives);
        Bukkit.getPluginManager().registerEvents(new SylvathianThornWeaver(), getInstance());
        Bukkit.getPluginManager().registerEvents(new WrathOfFeyrith(), getInstance());
    }

    /**
     * A utility method to convert a given file name and item name into a camel space namespace string.
     *
     * @param key      The key in the result.
     * @param fileName The namespace in the beginning.
     * @return The resulting namespace key.
     */
    private String toCamelSpaceNamespace (String fileName, String key) {
        String toReturn = fileName.replace(".yml", "").replace(".yaml", "") + ":";
        char[] tmp = key.toCharArray();
        tmp[0] = Character.toUpperCase(tmp[0]);
        for (int i = 1; i < tmp.length; i++)
            if (tmp[i] == '-') {
                if (i < tmp.length - 1)
                    tmp[i + 1] = Character.toUpperCase(tmp[i + 1]);
                i++;
            }
        return toReturn + new String(tmp).replace("-", "");
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

    /**
     * Checks whether an item by the given name exists.
     *
     * @param name The name of the item to check for.
     * @return True if the item exists.
     */
    public boolean hasItem (String name) {
        return rpgItems.containsKey(name);
    }

    /**
     * Passive check. Periodically ran to check if players are using armor that gives them special effects.
     * todo: Refactor to be async and per player.
     */
    public void passives () {
        if (!RPGFramework.getInstance().isEnabled()) return;
        for (Player p : Bukkit.getOnlinePlayers()) {
            EntityEquipment equipment = p.getEquipment();
            if (equipment == null) continue;
            ItemStack helm = p.getEquipment().getHelmet();
            if (helm == null) continue;
            if (helm.getType().equals(Material.LEATHER_HELMET)) {
                if (helm.equals(getItem("other:HelmetOfDarkness"))) {
                    Bukkit.getScheduler().runTask(RPGFramework.getInstance(), () -> {
                        p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 21 * 20, 255, true, false));
                        p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 21 * 20, 255, true, false));
                    });
                }
            }
        }
        Bukkit.getScheduler().runTaskLaterAsynchronously(RPGFramework.getInstance(), this::passives, 20 * 20);
    }
}
