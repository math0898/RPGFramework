package io.github.math0898.rpgframework;

import io.github.math0898.rpgframework.classes.Archetype;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * The class manager for all the classes registered to the RPG Framework.
 *
 * @author Sugaku
 */
public class ClassManager {

    /**
     * The ArrayList of classes registered to the plugin.
     */
    private final ArrayList<Archetype> registeredClasses = new ArrayList<>();

    /**
     * This is a map of players and their currently registered class.
     */
    private final Map<Player, Archetype> playerClasses = new HashMap<>();

    /**
     * Returns the list of currently registered classes.
     *
     * @return The ArrayList<Archetype> holding all classes.
     */
    public ArrayList<Archetype> getRegisteredClasses () {
        return registeredClasses;
    }

    /**
     * Returns the Map of players and their registered classes.
     *
     * @return The Map<Player, Archetype> declaring player classes.
     */
    public Map<Player, Archetype> getPlayerClasses () {
        return playerClasses;
    }

    /**
     * Registers a class to the class manager. Also registers event listeners so plugins don't need to worry about doing
     * that themselves.
     *
     * @param archetype The class to register.
     */
    public void registerClass (Archetype archetype) {
        registeredClasses.add(archetype);
        Bukkit.getPluginManager().registerEvents(archetype, main.plugin);
    }
}
