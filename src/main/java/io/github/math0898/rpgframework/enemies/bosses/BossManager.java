package io.github.math0898.rpgframework.enemies.bosses;

import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static io.github.math0898.rpgframework.main.console;
import static io.github.math0898.rpgframework.main.plugin;

/**
 * The BossManager handles bosses. That is reading them from files, special events, boss drops, and summoning.
 *
 * @author Sugaku
 */
public class BossManager implements Listener {

    /**
     * The BossManager instance to be used by everything.
     */
    private static final BossManager bossManager = new BossManager();

    /**
     * All the bosses that have been registered with the BossManager.
     */
    private static final Map<String, Boss> bosses = new HashMap<>();

    /**
     * Default constructor that is private to prevent the creation of other BossManager instances.
     */
    private BossManager () {
        File itemsDir = new File("./plugins/RPGFramework/mobs/bosses");
        if (!itemsDir.exists()) {
            if (!itemsDir.mkdirs()) {
                console("Failed to create mob directories.", ChatColor.YELLOW);
                return;
            }
            plugin.saveResource("mobs/bosses/krusk.yml", false); // todo: refactor to reduce scope when adding multiple bosses and sets.
        }
        File[] files = itemsDir.listFiles();
        if (files == null) {
            console("Cannot find any mob files.", ChatColor.YELLOW);
            return;
        }
        for (File f : files) {
            try {
                YamlConfiguration yaml = new YamlConfiguration();
                yaml.load(f);
                for (String k : yaml.getKeys(false)) {
                    bosses.put(k, new Boss(Objects.requireNonNull(yaml.getConfigurationSection(k))));
                }
            } catch (InvalidConfigurationException | IOException e) {
                console(e.getMessage(), ChatColor.RED);
                console("Failed to parse mob located at: " + f.getPath(), ChatColor.RED);
            }
        }
    }

    /**
     * Gets the single BossManager to be used.
     *
     * @return The single BossManager instance.
     */
    public static BossManager getInstance () {
        return bossManager;
    }
}
