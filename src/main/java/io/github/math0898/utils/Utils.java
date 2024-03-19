package io.github.math0898.utils;

import io.github.math0898.rpgframework.main;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Substitutes for the main JavaPlugin class when concerned with utils classes and objects.
 *
 * @author Sugaku
 */
public class Utils {

    /**
     * Accessor method to get the plugin in use.
     *
     * @return The active plugin.
     */
    public static JavaPlugin getPlugin () {
        return main.getInstance();
    }
}
