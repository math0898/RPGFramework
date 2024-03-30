package sugaku.rpg.mobs;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import sugaku.rpg.framework.items.BossDrop;
import sugaku.rpg.framework.items.Rarity;
import sugaku.rpg.framework.mobs.MobManager;

import java.util.Objects;
import java.util.Random;

public abstract class CustomMob {

    /**
     * The helmet of the custom mob.
     */
    private ItemStack helm;

    /**
     * The chestplate of the custom mob.
     */
    private ItemStack chestplate;

    /**
     * The leggings of the custom mob.
     */
    private ItemStack leggings;

    /**
     * The boots of the custom mob.
     */
    private ItemStack boots;

    /**
     * The held item of the custom mob.
     */
    private ItemStack hand;

    /**
     * The item held in the off hand of the custom mob.
     */
    private ItemStack offHand;

    /**
     * The type of mob the custom mob is.
     */
    private EntityType mobType;

    /**
     * The name string of the mob.
     */
    private String name;

    /**
     * The rarity of the boss. Somewhat related to difficulty.
     */
    private Rarity rarity;

    /**
     * The maximum health of the mob.
     */
    private int maxHealth;

    /**
     * A pointer to the living entity object.
     */
    private LivingEntity entity;

    /**
     * The number of mechanics this mob has, 1-20%,2-40%,3-60%,4-80%,5-onSpawn
     */
    private int activeMechanics = 0;

    /**
     * The prefix sent with every message.
     */
    private final String prefix = ChatColor.DARK_GRAY + "[" + ChatColor.DARK_GREEN + "RPG" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY;

    /**
     * Sends a message to the given recipient.
     */
    public void send(CommandSender user, String message) {
        user.sendMessage(prefix + message);
    }

    /**
     * A basic constructor to aid in the construction of a CustomMob. Contains everything essential to run spawn().
     * @param name The name of the mob.
     * @param mobType The type of mob that should be spawned.
     * @param rarity The rarity of the mob being spawned.
     * @param maxHealth The maximum health of the mob spawned
     */
    public CustomMob(String name, EntityType mobType, Rarity rarity, int maxHealth) {
        setName(name);
        setMobType(mobType);
        setRarity(rarity);
        setMaxHealth(maxHealth);
    }

    /**
     * Spawn the mob described by the CustomMob object at the given location l.
     * @param l The location the mob should be spawned at.
     */
    @SuppressWarnings("deprecation")
    public void spawn(Location l) {
        Mob e = (Mob) Objects.requireNonNull(l.getWorld()).spawnEntity(l, mobType);

        e.setCustomNameVisible(true);
        e.setCustomName(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_GREEN + "RPG" + ChatColor.DARK_GRAY + "] " + Rarity.toColor(rarity) + name);
        Objects.requireNonNull(e.getEquipment()).setHelmet(this.helm);
        Objects.requireNonNull(e.getEquipment()).setChestplate(this.chestplate);
        Objects.requireNonNull(e.getEquipment()).setLeggings(this.leggings);
        Objects.requireNonNull(e.getEquipment()).setBoots(this.boots);
        Objects.requireNonNull(e.getEquipment()).setItemInMainHand(this.hand);
        Objects.requireNonNull(e.getEquipment()).setItemInOffHand(this.offHand);
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

        this.entity = e;
    }

    /**
     * Sets the name of the mob
     * @param name The desired name.
     */
    public void setName(String name) { this.name = name; }

    /**
     * Applies the given armor to the mob.
     * @param helm The helmet of the new mob.
     * @param chestplate The chestplate of the new mob.
     * @param leggings The leggings of the new mob.
     * @param boots The boots of the new mob.
     */
    public void setArmor(ItemStack helm, ItemStack chestplate, ItemStack leggings, ItemStack boots) {
        setHelm(helm);
        setChestplate(chestplate);
        setLeggings(leggings);
        setBoots(boots);
    }

    /**
     * Sets the helmet of the new mob.
     * @param helm The desired helmet.
     */
    public void setHelm(ItemStack helm) { this.helm = helm; }

    /**
     * Sets the chestplate of the new mob.
     * @param chestplate The desired chestplate.
     */
    public void setChestplate(ItemStack chestplate) { this.chestplate = chestplate; }

    /**
     * Sets the leggings of the new mob.
     * @param leggings The desired leggings.
     */
    public void setLeggings(ItemStack leggings) { this.leggings = leggings; }

    /**
     * Sets the boots of the new mob.
     * @param boots The desired boots.
     */
    public void setBoots(ItemStack boots) { this.boots = boots; }

    /**
     * Sets the main hand of the new mob.
     * @param hand The desired weapon of the mob.
     */
    public void setHand(ItemStack hand) { this.hand = hand; }

    /**
     * Sets the offhand of the new mob.
     * @param offHand The desired offhand of the mob.
     */
    public void setOffHand(ItemStack offHand) { this.offHand = offHand; }

    /**
     * Sets the type of mob that will be spawned.
     * @param mobType The desired type of mob.
     */
    public void setMobType(EntityType mobType) { this.mobType = mobType; }

    /**
     * Sets the rarity of the mob that will be spawned.
     * @param rarity The desired rarity of the mob.
     */
    public void setRarity(Rarity rarity) { this.rarity = rarity; }

    /**
     * Sets the max health of the mob.
     * @param maxHealth The desired max health of the mob.
     */
    public void setMaxHealth(int maxHealth) { this.maxHealth = maxHealth; }

    /**
     * Returns the custom name of the mob being created.
     */
    public String getCustomName() {
        return ChatColor.DARK_GRAY + "[" + ChatColor.DARK_GREEN + "RPG" + ChatColor.DARK_GRAY + "] " + rarity + name; }

    /**
     * Handles drops of the custom mob. By default no items are dropped.
     */
    public static void handleDrops(EntityDeathEvent event) {
        event.getDrops().clear();
        event.setDroppedExp(0);
    }

    /**
     * Handles the drops of the custom mob. Uses the table of BossDrops combined with the boss's rarity to determine
     * what loot should drop at what rate.
     *
     * @param event The event where the entity died.
     * @param bossDrops The table of drops.
     */
    public static void handleDrops(EntityDeathEvent event, BossDrop[] bossDrops, Rarity rarity) {
        handleDrops(event);
        event.setDroppedExp(25 * rarity.toInt(rarity));
        Random rand = new Random();
        double roll = rand.nextDouble();
        double check = 0.0;
        while (true) {
            for (BossDrop i : bossDrops) {
                if (roll < check + getRate(i.getRarity(), rarity)) {
                    MobManager.drop(i.getItem(), event.getEntity().getLocation());
                    return;
                } else check += getRate(i.getRarity(), rarity);
            }
            roll = rand.nextDouble();
        }
    }

    private static double getRate(Rarity item, Rarity boss) {
        switch (boss) {
            case UNCOMMON:
                return switch (item) {
                    case COMMON -> 0.16;
                    case UNCOMMON -> 0.08;
                    case RARE -> 0.04;
                    case LEGENDARY -> 0.02;
                    case HEROIC -> 0.01;
                    case MYTHIC -> 0.005;
                };
            case RARE:
                return switch (item) {
                    case COMMON -> 0.16;
                    case UNCOMMON -> 0.08;
                    case RARE -> 0.04;
                    case LEGENDARY -> 0.02;
                    case HEROIC -> 0.01;
                    case MYTHIC -> 0.005;
                };
        }
        return 0.0;
    }

    public LivingEntity getEntity() { return entity; }

    public void damaged(Player attacker) {
        if (getEntity().isInWater()) {
            Objects.requireNonNull(attacker.getLocation().getWorld()).strikeLightning(attacker.getLocation());
            send(attacker, "The gods smite you for your attempted cheese.");
        }
    }

    public int getMaxHealth() {return maxHealth; }

    public int getActiveMechanics() { return activeMechanics; }

    public void setActiveMechanics(int m) { activeMechanics = m; }

    public void firstMechanic(Player attacker) { activeMechanics = 0; }

    public void secondMechanic(Player attacker) { activeMechanics = 1; }
}
