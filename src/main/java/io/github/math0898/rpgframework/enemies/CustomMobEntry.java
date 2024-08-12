package io.github.math0898.rpgframework.enemies;
import io.github.math0898.rpgframework.Rarity;
import io.github.math0898.utils.StringUtils;
import io.github.math0898.utils.items.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.*;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;

import java.util.Objects;

/**
 * A CustomMob is a mob specific to the RPG plugin and has custom equipment, names, drops, and abilities.
 *
 * @author Sugaku
 */
public class CustomMobEntry { // todo: Drops and xp entries. Items need to be able to be from ItemManager.

    /**
     * The helmet of the custom mob.
     */
    private final ItemBuilder helm;

    /**
     * The chestplate of the custom mob.
     */
    private final ItemBuilder chestplate;

    /**
     * The leggings of the custom mob.
     */
    private final ItemBuilder leggings;

    /**
     * The boots of the custom mob.
     */
    private final ItemBuilder boots;

    /**
     * The held item of the custom mob.
     */
    private final ItemBuilder hand;

    /**
     * The item held in the off-hand of the custom mob.
     */
    private final ItemBuilder offHand;

    /**
     * The type of mob the custom mob is.
     */
    private final EntityType mobType;

    /**
     * The name string of the mob.
     */
    private final String name;

    /**
     * The rarity of the boss. Somewhat related to difficulty.
     */
    private final Rarity rarity;

    /**
     * The maximum health of the mob.
     */
    private final int maxHealth;

    /**
     * This references the specific class that should be created on spawn for this CustomMobEntry.
     */
    private final String instanceClass;

    /**
     * This is the namespaceKey you can find this CustomMobEntry in MobManager.
     */
    private final String namespaceKey;

    /**
     * Creates a new CustomMob using the given configuration section.
     *
     * @param section      The configuration section that defines this CustomMob.
     * @param namespaceKey This is the key to the CustomMobEntry inside the MobManger.
     */
    public CustomMobEntry (ConfigurationSection section, String namespaceKey) {
        maxHealth = section.getInt("stats.health", 20) / 5;
        rarity = Rarity.valueOf(section.getString("rarity", "COMMON"));
        name = StringUtils.convertHexCodes(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_GREEN + "RPG" + ChatColor.DARK_GRAY + "] "
                + rarity.getHexColor() + section.getString("name", "Default Name"));
        mobType = EntityType.valueOf(section.getString("mob-type", "ZOMBIE"));
        offHand = new ItemBuilder(section.getConfigurationSection("equipment.off-hand")).setUnbreakable(true);
        hand = new ItemBuilder(section.getConfigurationSection("equipment.hand")).setUnbreakable(true);
        boots = new ItemBuilder(section.getConfigurationSection("equipment.boots")).setUnbreakable(true);
        leggings = new ItemBuilder(section.getConfigurationSection("equipment.leggings")).setUnbreakable(true);
        chestplate = new ItemBuilder(section.getConfigurationSection("equipment.chestplate")).setUnbreakable(true);
        helm = new ItemBuilder(section.getConfigurationSection("equipment.helmet")).setUnbreakable(true);
        instanceClass = section.getString("instance-class-name", "default");
        this.namespaceKey = namespaceKey;
    }

    /**
     * Spawn the mob described by the CustomMob object at the given location l.
     * @param l The location the mob should be spawned at.
     */
    @SuppressWarnings("deprecation")
    public void spawn (Location l) {
        Mob e = (Mob) Objects.requireNonNull(l.getWorld()).spawnEntity(l, mobType);

        e.setCustomNameVisible(true);
        e.setCustomName(name);

        EntityEquipment equipment = e.getEquipment();
        if (equipment != null) {
            ItemBuilder[] builders = new ItemBuilder[]{helm, chestplate, leggings, boots, hand, offHand};
            EquipmentSlot[] slots = new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET, EquipmentSlot.HAND, EquipmentSlot.OFF_HAND};
            for (int i = 0; i < builders.length; i++)
                equipment.setItem(slots[i], builders[i].build());

            equipment.setHelmetDropChance(-1);
            equipment.setChestplateDropChance(-1);
            equipment.setLeggingsDropChance(-1);
            equipment.setBootsDropChance(-1);
            equipment.setItemInMainHandDropChance(-1);
            equipment.setItemInOffHandDropChance(-1);
        }
        e.setMaxHealth(this.maxHealth); //Deprecated
        e.setHealth(this.maxHealth);
        e.setLootTable(null);
        e.setCanPickupItems(false); //No more stealing gear
        createInstanceClass(e);
    }

    /**
     * Calls the constructor for the specific instance of the mob described.
     *
     * @param entity The entity for the specific instance we're about to create.
     */
    private void createInstanceClass (LivingEntity entity) {
        switch (instanceClass) {
            case "SeignourBoss" -> new SeignourBoss(entity, namespaceKey);
            default -> new ActiveCustomMob(entity, namespaceKey);
        }
    }

    /**
     * Called whenever an instance of this CustomMobEntry dies.
     *
     * @param location The location that the mob died at.
     */
    public void handleDeath (Location location) {
        System.out.println("Handling death of " + namespaceKey + " at " + location + "!");
        // todo: Implement!
    }
//    /**
//     * Handles drops of the custom mob. By default no items are dropped.
//     */
//    public static void handleDrops(EntityDeathEvent event) {
//        event.getDrops().clear();
//        event.setDroppedExp(0);
//    }
//
//    /**
//     * Handles the drops of the custom mob. Uses the table of BossDrops combined with the boss's rarity to determine
//     * what loot should drop at what rate.
//     *
//     * @param event The event where the entity died.
//     * @param bossDrops The table of drops.
//     */
//    public static void handleDrops(EntityDeathEvent event, Object[] bossDrops, LegacyRarity rarity) {
////        handleDrops(event);
////        event.setDroppedExp(25 * rarity.toInt(rarity));
////        double roll = new Random().nextDouble();
////        double check = 0.0;
////        for (BossDrop i: bossDrops) {
////            if (roll < check + getRate(i.getRarity(), rarity)) { MobManager.drop(i.getItem(), event.getEntity().getLocation()); break; }
////            else check += getRate(i.getRarity(), rarity);
////        }
//    }
//
//    private static double getRate(LegacyRarity item, LegacyRarity boss) {
//        switch (boss) {
//            case UNCOMMON: switch (item) {
//                case COMMON: return 0.16;
//                case UNCOMMON: return 0.08;
//                case RARE: return 0.04;
//                case LEGENDARY: return 0.02;
//                case HEROIC: return 0.01;
//                case MYTHIC: return 0.005;
//            }
//        }
//        return 0.0;
//    }
}
