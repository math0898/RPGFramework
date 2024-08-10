package io.github.math0898.rpgframework.enemies;
import io.github.math0898.rpgframework.Rarity;
import io.github.math0898.rpgframework.damage.AdvancedDamageEvent;
import io.github.math0898.rpgframework.damage.DamageModifier;
import io.github.math0898.utils.StringUtils;
import io.github.math0898.utils.items.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;
import java.util.Random;

/**
 * A CustomMob is a mob specific to the RPG plugin and has custom equipment, names, drops, and abilities.
 *
 * @author Sugaku
 */
public class CustomMob implements DamageModifier {

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
     * Creates a new CustomMob using the given configuration section.
     *
     * @param section The configuration section that defines this CustomMob.
     */
    public CustomMob (ConfigurationSection section) {
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
    }

        /**
     * Spawn the mob described by the CustomMob object at the given location l.
     * @param l The location the mob should be spawned at.
     */
    @SuppressWarnings("deprecation") // todo: This needs to spawn a new instance of CustomMob.
    public void spawn (Location l) {
        Mob e = (Mob) Objects.requireNonNull(l.getWorld()).spawnEntity(l, mobType);

        e.setCustomNameVisible(true);
        e.setCustomName(name);
        Objects.requireNonNull(e.getEquipment()).setHelmet(helm.build());
        Objects.requireNonNull(e.getEquipment()).setChestplate(chestplate.build());
        Objects.requireNonNull(e.getEquipment()).setLeggings(leggings.build());
        Objects.requireNonNull(e.getEquipment()).setBoots(boots.build());
        Objects.requireNonNull(e.getEquipment()).setItemInMainHand(hand.build());
        Objects.requireNonNull(e.getEquipment()).setItemInOffHand(offHand.build());
        e.setMaxHealth(this.maxHealth); //Deprecated
        e.setHealth(this.maxHealth);

        e.setLootTable(null);
        e.getEquipment().setHelmetDropChance(-1);
        e.getEquipment().setChestplateDropChance(-1);
        e.getEquipment().setLeggingsDropChance(-1);
        e.getEquipment().setBootsDropChance(-1);
        e.getEquipment().setItemInMainHandDropChance(-1);
        e.getEquipment().setItemInOffHandDropChance(-1);
        e.setCanPickupItems(false); //No more stealing gear
    }

    /**
     * Called whenever this DamageModifier is relevant on a defensive front.
     *
     * @param event The AdvancedDamageEvent to consider.
     */
    @Override
    public void damaged (AdvancedDamageEvent event) {

    }

    /**
     * Called whenever this DamageModifier is relevant on an offensive front.
     *
     * @param event The AdvancedDamageEvent to consider.
     */
    @Override
    public void attack (AdvancedDamageEvent event) {

    }

    @Override
    public void damaged (EntityDamageEvent event) {

    }

    @Override
    public void attack (EntityDamageByEntityEvent event) {

    }
//
//    /**
//     * A pointer to the living entity object.
//     */
//    private LivingEntity entity;
//
//    /**
//     * The number of mechanics this mob has, 1-20%,2-40%,3-60%,4-80%,5-onSpawn
//     */
//    private int activeMechanics = 0;
//
//    /**
//     * The prefix sent with every message.
//     */
//    private final String prefix = ChatColor.DARK_GRAY + "[" + ChatColor.DARK_GREEN + "RPG" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY;
//
//    /**
//     * Sends a message to the given recipient.
//     */
//    public void send(CommandSender user, String message) {
//        user.sendMessage(prefix + message);
//    }
//
//    /**
//     * A basic constructor to aid in the construction of a CustomMob. Contains everything essential to run spawn().
//     * @param name The name of the mob.
//     * @param mobType The type of mob that should be spawned.
//     * @param rarity The rarity of the mob being spawned.
//     * @param maxHealth The maximum health of the mob spawned
//     */
//    public CustomMob (String name, EntityType mobType, LegacyRarity rarity, int maxHealth) {
//        setName(name);
//        setMobType(mobType);
//        setRarity(rarity);
//        setMaxHealth(maxHealth);
//    }
//
//
//    /**
//     * Sets the name of the mob
//     * @param name The desired name.
//     */
//    public void setName(String name) { this.name = name; }
//
//    /**
//     * Applies the given armor to the mob.
//     * @param helm The helmet of the new mob.
//     * @param chestplate The chestplate of the new mob.
//     * @param leggings The leggings of the new mob.
//     * @param boots The boots of the new mob.
//     */
//    public void setArmor(ItemStack helm, ItemStack chestplate, ItemStack leggings, ItemStack boots) {
//        setHelm(helm);
//        setChestplate(chestplate);
//        setLeggings(leggings);
//        setBoots(boots);
//    }
//
//    /**
//     * Sets the helmet of the new mob.
//     * @param helm The desired helmet.
//     */
//    public void setHelm(ItemStack helm) { this.helm = helm; }
//
//    /**
//     * Sets the chestplate of the new mob.
//     * @param chestplate The desired chestplate.
//     */
//    public void setChestplate(ItemStack chestplate) { this.chestplate = chestplate; }
//
//    /**
//     * Sets the leggings of the new mob.
//     * @param leggings The desired leggings.
//     */
//    public void setLeggings(ItemStack leggings) { this.leggings = leggings; }
//
//    /**
//     * Sets the boots of the new mob.
//     * @param boots The desired boots.
//     */
//    public void setBoots(ItemStack boots) { this.boots = boots; }
//
//    /**
//     * Sets the main hand of the new mob.
//     * @param hand The desired weapon of the mob.
//     */
//    public void setHand(ItemStack hand) { this.hand = hand; }
//
//    /**
//     * Sets the offhand of the new mob.
//     * @param offHand The desired offhand of the mob.
//     */
//    public void setOffHand(ItemStack offHand) { this.offHand = offHand; }
//
//    /**
//     * Sets the type of mob that will be spawned.
//     * @param mobType The desired type of mob.
//     */
//    public void setMobType(EntityType mobType) { this.mobType = mobType; }
//
//    /**
//     * Sets the rarity of the mob that will be spawned.
//     * @param rarity The desired rarity of the mob.
//     */
//    public void setRarity(LegacyRarity rarity) { this.rarity = rarity; }
//
//    /**
//     * Sets the max health of the mob.
//     * @param maxHealth The desired max health of the mob.
//     */
//    public void setMaxHealth(int maxHealth) { this.maxHealth = maxHealth; }
//
//    /**
//     * Returns the custom name of the mob being created.
//     */
//    public String getCustomName() { return ChatColor.DARK_GRAY + "[" + ChatColor.DARK_GREEN + "RPG" + ChatColor.DARK_GRAY + "] " + rarity + name; }
//
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
//
//    public LivingEntity getEntity() { return entity; }
//
//    public void damaged(Player attacker) {
//        if (getEntity().isInWater()) {
//            Objects.requireNonNull(attacker.getLocation().getWorld()).strikeLightning(attacker.getLocation());
//            send(attacker, "The gods smite you for your attempted cheese.");
//        }
//    }
//
//    public int getMaxHealth() {return maxHealth; }
//
//    public int getActiveMechanics() { return activeMechanics; }
//
//    public void setActiveMechanics(int m) { activeMechanics = m; }
//
//    public void firstMechanic(Player attacker) { activeMechanics = 0; }
//
//    public void secondMechanic(Player attacker) { activeMechanics = 1; }
}
