package io.github.math0898.rpgframework.enemies.bosses;

import io.github.math0898.rpgframework.Rarity;
import io.github.math0898.rpgframework.enemies.AI;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;

import java.util.Map;
import java.util.Objects;

/**
 * The Boss class which describes generic bosses and their abilities.
 *
 * @author Sugaku
 */
public class Boss {

    /**
     * The name of this boss.
     */
    private final String name;

    /**
     * The type of entity this boss is.
     */
    private final EntityType type;

    /**
     * Nullable age of this entity. Used for zombies and other age able mobs.
     */
    private final Boolean adult;

    /**
     * The rarity of this boss. Effects the color of the boss name.
     */
    private final Rarity rarity;

    /**
     * The health that this boss has.
     */
    private final double health;

    /**
     * The armor this boss has.
     */
    private final double armor;

    /**
     * The damage that this boss does on hit.
     */
    private final double damage;

    /**
     * The name of the RPG item that spawns this boss.
     */
    private final String spawnItem;

    /**
     * A map of messages which are sent by this boss at various times.
     */
    private final Map<String, String> messages;

    /**
     * Any kind of custom AI to assign to this mob.
     */
    private final AI ai;

    /*
     * TODO: equipment, AI, drops, abilities
     */

    /**
     * Creates a new Boss prototype using the given configuration file to define mob values.
     */
    public Boss (ConfigurationSection config) {
        name = config.getString("name");
        type = EntityType.valueOf(config.getString("type"));
        if (config.contains("adult")) adult = config.getBoolean("adult");
        else adult = null;
        rarity = Rarity.valueOf(config.getString("rarity"));
        health = config.getDouble("health");
        armor = config.getDouble("armor");
        damage = config.getDouble("damage");
        spawnItem = config.getString("spawn");
        //noinspection unchecked
        messages = (Map<String, String>) config.getMapList("messages");
        if (config.contains("AI")) ai = null; // todo
        else ai = null;
    }

    /**
     * Spawns this boss at the given location.
     *
     * @param locale The location to spawn this boss at.
     */
    public void spawnBoss (Location locale) {
        Mob e = (Mob) Objects.requireNonNull(locale.getWorld()).spawnEntity(locale, type);

        e.setCustomNameVisible(true);
        e.setCustomName(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_GREEN + "RPG" + ChatColor.DARK_GRAY + "] " + Rarity.toColor(rarity) + name);
    }
}
