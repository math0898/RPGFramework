package io.github.math0898.rpgframework.enemies.bosses;

import io.github.math0898.rpgframework.Rarity;
import io.github.math0898.rpgframework.enemies.AI;
import io.github.math0898.rpgframework.enemies.abilities.Ability;
import io.github.math0898.rpgframework.enemies.abilities.UndeadAura;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.*;

import static io.github.math0898.rpgframework.RPGFramework.plugin;

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

    /**
     * A map of equipment for this boss.
     */
    private final Map<String, Material> items;

    /**
     * The potential drops for this boss along with drop rates.
     */
    private final Map<String, Double> bossDrops;

    /**
     * Abilities which this boss has.
     */
    private final ArrayList<Ability> abilities; // todo: Constructor taking configuration section in constructor.

    /*
     * TODO: abilities
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
        items = new HashMap<>();
        for (String s : new String[]{ "helmet", "chestplate", "leggings", "boots", "main-hand", "off-hand" }) {
            String m = config.getString(s);
            if (m == null) continue;
            items.put(s, Material.getMaterial(m));
        }
        ConfigurationSection drops = config.getConfigurationSection("drops");
        bossDrops = new HashMap<>();
        if (drops != null)
            for (String s : drops.getKeys(false))
                bossDrops.put(s, drops.getDouble(s));
        abilities = new ArrayList<>(); // todo actually implement.
        Bukkit.getPluginManager().registerEvents(new UndeadAura(), plugin);
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
        if (items.get("helmet") != null) Objects.requireNonNull(e.getEquipment()).setHelmet(new ItemStack(items.get("helmet"))); // TODO: Armor
        if (items.get("chestplate") != null) Objects.requireNonNull(e.getEquipment()).setChestplate(new ItemStack(items.get("chestplate")));
        if (items.get("leggings") != null) Objects.requireNonNull(e.getEquipment()).setLeggings(new ItemStack(items.get("leggings")));
        if (items.get("boots") != null) Objects.requireNonNull(e.getEquipment()).setBoots(new ItemStack(items.get("boots")));
        if (items.get("main-hand") != null) Objects.requireNonNull(e.getEquipment()).setItemInMainHand(sugaku.rpg.framework.items.ItemsManager.createItem(items.get("main-hand"), 1, "", new String[]{}, new AttributeModifier[]{ new AttributeModifier(new UUID(0l, 1l), "generic.dmg", damage, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND)})); // TODO: Refactor
        if (items.get("off-hand") != null) Objects.requireNonNull(e.getEquipment()).setItemInOffHand(new ItemStack(items.get("off-hand")));

        e.setHealth(health);

        e.setLootTable(null);
        if (e.getEquipment() != null) {
            e.getEquipment().setHelmetDropChance(-1);
            e.getEquipment().setChestplateDropChance(-1);
            e.getEquipment().setLeggingsDropChance(-1);
            e.getEquipment().setBootsDropChance(-1);
            e.getEquipment().setItemInMainHandDropChance(-1);
            e.getEquipment().setItemInOffHandDropChance(-1);
        }
        e.setCanPickupItems(false);
        if (adult != null) ((Ageable) e).setAdult();
    }
}
