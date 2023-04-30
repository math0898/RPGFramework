package sugaku.rpg.mobs.teir1.eiryeras;

import io.github.math0898.rpgframework.items.ItemBuilder;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValueAdapter;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import sugaku.rpg.framework.items.BossDrop;
import sugaku.rpg.framework.items.ItemsManager;
import sugaku.rpg.framework.items.Rarity;
import sugaku.rpg.main;
import sugaku.rpg.mobs.CustomMob;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.Random;

import static org.bukkit.Material.*;

/**
 * This class in its entirety declares the Eiryeras boss and handles all of its special effects against players.
 */
public class EiryerasBoss extends CustomMob implements Listener {


    /**
     * A simple enum to distinguish between the different types of arrows Eiryeras fires
     */

    enum Arrows { SLOWNESS, KNOCKBACK, POISON }

    /**
     * The arrowType class holds a state of the Arrows enum which can be stored as metadata on a game object.
     */
    static class arrowType extends MetadataValueAdapter {

        /**
         * The actual object which holds the metadata information.
         */
        private final Arrows arrow;

        /**
         * The basic constructor for the arrowType class. It just needs the state it needs to remember.
         *
         * @param owningPlugin The plugin which owns the meta data. Inherited from MetadataValueAdapter.
         * @param data The data the object should hold.
         */
        protected arrowType(Plugin owningPlugin, Arrows data) { super(owningPlugin); arrow = data;}

        /**
         * Serves as the accessor for the actual information held in the object.
         *
         * @return The value of arrow which was given at construction.
         */
        @Override
        public Object value() { return arrow; }

        /**
         * Invalidates the information of the object. There isn't anything to do. Ideally we'd delete the item however
         * we can leave that to java clean up.
         */
        @Override
        public void invalidate() { }
    }

    /**
     * The name of the boss. This is then modified to create the display name including the plugin prefix and color
     * rarity of the boss.
     */
    private static final String name = "Eiryeras, Honored Hunter";

    /**
     * The base damage for any attack done by Eiryeras.
     */
    private static final double baseDamage = 8.0;

    /**
     * The damage a slowness arrow from Eiryeras does.
     */
    private static final double slownessDamage = baseDamage + 0.0; //5.0

    /**
     * The damage a poison arrow from Eiryeras does.
     */
    private static final double poisonDamage = baseDamage - 2.5; //2.5

    /**
     * The damage a stronger, 'knockback', arrow from Eiryeras does.
     */
    private static final double knockbackDamage = baseDamage + 3.0; //8.0

    /**
     * The damage Eiryeras does when striking with their sword.
     */
    private static final double meleeDamage = baseDamage + 5.0; //10.0

    /**
     * Eiryeras' movement speed bonus in melee form.
     */
    private static final double bonusMoveSpeed = 0.25;

    /**
     * An array which stores all of the boss item drops.
     */
    private static final BossDrop[] bossDrops = new BossDrop[]{
            //Rare Eiryeras' Bow
            new BossDrop(new ItemBuilder(Material.BOW, 1, ChatColor.BLUE + "Hand Crafted Bow").setLore(new String[]{
                    ChatColor.GRAY + "A bow crafted by a very",
                    ChatColor.GRAY + "experienced artisan from",
                    ChatColor.BLUE + "Strathenberg" + ChatColor.GRAY + ". The unique",
                    ChatColor.GRAY + "Shape of the limbs help",
                    ChatColor.GRAY + "transfer more energy into",
                    ChatColor.GRAY + "each shot.",
                    ChatColor.BLUE + "Arrow Damage: + 4"
            }).setUnbreakable(true).build(), Rarity.RARE),

            //Uncommon Eiryeras' Boots
            new BossDrop(new ItemBuilder(Material.LEATHER_BOOTS, ChatColor.GREEN + "Worn Galoshes").setLore(new String[]{
                    ChatColor.GRAY + "While traveling it is very",
                    ChatColor.GRAY + "important to keep your feet",
                    ChatColor.GRAY + "dry. Failing to do so can",
                    ChatColor.GRAY + "result in fungal infections",
                    ChatColor.GRAY + "blisters, and warts. Do",
                    ChatColor.GRAY + "yourself a favor and get a",
                    ChatColor.GRAY + "good pair of footwear."
            }).setRGB(60, 115, 31).setModifiers(new AttributeModifier[]{
                    ItemsManager.attributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, 1.0, EquipmentSlot.FEET),
                    ItemsManager.attributeModifier(Attribute.GENERIC_MAX_HEALTH, 2.0, EquipmentSlot.FEET), // + 1
                    ItemsManager.attributeModifier(Attribute.GENERIC_ARMOR, 0.5, EquipmentSlot.FEET) // + 0.25
            }).setUnbreakable(true).build(), Rarity.UNCOMMON),

            //Uncommon Eiryeras' Leggings
            new BossDrop(new ItemBuilder(Material.LEATHER_LEGGINGS, ChatColor.GREEN + "Hide Leggings").setLore(new String[]{
                    ChatColor.GRAY + "Woods which do not reside in",
                    ChatColor.GRAY + "floodplains often cultivate",
                    ChatColor.GRAY + "a large amount of undergrowth",
                    ChatColor.GRAY + "which can be thorny and",
                    ChatColor.GRAY + "difficult to move through.",
                    ChatColor.GRAY + "A good pair of protective",
                    ChatColor.GRAY + "pants is a good idea."
            }).setRGB(93, 161, 124).setModifiers(new AttributeModifier[]{
                    ItemsManager.attributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, 1.0, EquipmentSlot.LEGS),
                    ItemsManager.attributeModifier(Attribute.GENERIC_MAX_HEALTH, 3.0, EquipmentSlot.LEGS), // + 1
                    ItemsManager.attributeModifier(Attribute.GENERIC_ARMOR, 0.75, EquipmentSlot.LEGS) // + 0.25
            }).setUnbreakable(true).build(), Rarity.UNCOMMON),

            //Uncommon Eiryeras' Chestplate
            new BossDrop(new ItemBuilder(Material.LEATHER_CHESTPLATE, ChatColor.GREEN + "Hunter's Cloak").setLore(new String[]{
                    ChatColor.GRAY + "Similar to hoods, cloaks help",
                    ChatColor.GRAY + "greatly to reduce environmental",
                    ChatColor.GRAY + "hazards as well as recolor the",
                    ChatColor.GRAY + "wearer to match their terrain.",
                    ChatColor.GRAY + "This particular cloak is",
                    ChatColor.GRAY + "specifically colored for the",
                    ChatColor.GRAY + "forests of Agloytan."
            }).setRGB(113, 172, 11).setModifiers(new AttributeModifier[]{
                    ItemsManager.attributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, 1.0, EquipmentSlot.CHEST),
                    ItemsManager.attributeModifier(Attribute.GENERIC_MAX_HEALTH, 4.0, EquipmentSlot.CHEST), // + 1
                    ItemsManager.attributeModifier(Attribute.GENERIC_ARMOR, 1, EquipmentSlot.CHEST) // + 0.25
            }).setUnbreakable(true).build(), Rarity.UNCOMMON),

            //Uncommon Eiryeras' Helmet
            new BossDrop(new ItemBuilder(Material.LEATHER_HELMET, ChatColor.GREEN + "Hunter's Hood").setLore(new String[]{
                    ChatColor.GRAY + "Hoods serve to help to conceal",
                    ChatColor.GRAY + "humanoid features among the",
                    ChatColor.GRAY + "trees as well as protect the",
                    ChatColor.GRAY + "wearer against most",
                    ChatColor.GRAY + "environmental hazards."
            }).setRGB(60, 115, 31).setModifiers(new AttributeModifier[]{
                    ItemsManager.attributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, 1.0, EquipmentSlot.HEAD),
                    ItemsManager.attributeModifier(Attribute.GENERIC_MAX_HEALTH, 2.0, EquipmentSlot.HEAD), // + 1
                    ItemsManager.attributeModifier(Attribute.GENERIC_ARMOR, 0.5, EquipmentSlot.HEAD) // + 0.25
            }).setUnbreakable(true).build(), Rarity.UNCOMMON),

            //Rare Eiryeras' Knife
            new BossDrop(new ItemBuilder(Material.IRON_SWORD,1, ChatColor.BLUE + "Ceremonial Knife").setLore(new String[]{
                    ChatColor.GRAY + "This is a ceremonial knife used",
                    ChatColor.GRAY + "by followers of" + ChatColor.RED +" Inos" + ChatColor.GRAY + "," + ChatColor.RED + " God of",
                    ChatColor.RED + "Spring" + ChatColor.GRAY + ". The blade is used when",
                    ChatColor.GRAY + "gathering natural resources to",
                    ChatColor.GRAY + "show thanks and to humbly ask",
                    ChatColor.GRAY + "for its rejuvenation."
            }).setModifiers(new AttributeModifier[]{ItemsManager.attributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, 10.0, EquipmentSlot.HAND)/* + 2.0*/}).setUnbreakable(true).build(), Rarity.RARE),

            //Legendary Eiryeras' Lore
            new BossDrop(new ItemBuilder(Material.IRON_SWORD,1, ChatColor.GOLD + "Eiryeras Lore").setLore(new String[]{
                    ChatColor.GREEN + "Eiryeras" + ChatColor.GRAY +  " from a young age has",
                    ChatColor.GRAY + "always been very chaotic. Rather",
                    ChatColor.GRAY + "than study" + ChatColor.GREEN + " Eiryeras" + ChatColor.GRAY + " would much",
                    ChatColor.GRAY + "rather spend their time amongst",
                    ChatColor.GRAY + "the bugs and birds of the forest.",
                    ChatColor.GREEN + "Eiryeras" + ChatColor.GRAY +  " in old age would often",
                    ChatColor.GRAY + "proclaim that the company of",
                    ChatColor.GRAY + "animals is far superior to that",
                    ChatColor.GRAY + "of man."
            }).setUnbreakable(true).build(), Rarity.LEGENDARY)
    };

    /**
     * The hunter's knife Eiryeras uses to charge down enemies and deal the finishing blow.
     */
    private static final ItemStack huntersKnife = new ItemBuilder(IRON_SWORD).setModifiers(new AttributeModifier[]{
            ItemsManager.attributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, meleeDamage, EquipmentSlot.HAND),
            ItemsManager.attributeModifier(Attribute.GENERIC_MOVEMENT_SPEED, bonusMoveSpeed, EquipmentSlot.HAND)
    }).build();

    /**
     * Creates an Eiryeras boss for reference purposes.
     */
    public EiryerasBoss() {
        super(name, EntityType.SKELETON, Rarity.UNCOMMON, 150);
    }

    /**
     * Spawns Eiryeras as the given location l. Also makes sure that items and other things required for the boss fight
     * are setup.
     *
     * @param l The spawn location of Eiryeras.
     */
    public EiryerasBoss(Location l) {
        super(name, EntityType.SKELETON, Rarity.UNCOMMON, 150);
        setArmor(new ItemBuilder(Material.LEATHER_HELMET).setRGB(60, 115, 31).build(),
                new ItemBuilder(Material.LEATHER_CHESTPLATE).setRGB(113, 172, 11).build(),
                new ItemBuilder(Material.LEATHER_LEGGINGS).setRGB(93, 161, 124).build(),
                new ItemBuilder(Material.LEATHER_BOOTS).setRGB(60, 115, 31).build());
        setHand(new ItemStack(Material.BOW, 1));
        spawn(l);
    }

    /**
     * Accessor method for the boss.
     *
     * @return The name of the Eiryeras boss.
     */
    public static String getName() { return name; }

    /**
     * Returns the array of boss drop items.
     *
     * @return The boss drop items in the form of an array.
     */
    public static BossDrop[] getBossDrops() { return bossDrops; }

    /**
     * Returns the array of boss drop items stripped of their rarity property.
     *
     * @return The ItemStack array of the items in bossDrops.
     */
    public static Collection<? extends ItemStack> getBossItems() {
        ArrayList<ItemStack> items = new ArrayList<>();
        for (BossDrop b: getBossDrops()) items.add(b.getItem());
        return items;
    }

    /**
     * Handles when an Eiryeras instance fires and arrow. Since we're firing custom arrows we need to add some metadata
     * so we can find them after they hit. This method also handles the logic for when Eiryeras should go in for a kill
     * with the sword.
     *
     * @param event The entity firing a bow event. Filtered to entities with custom names, then to those which contain
     *              the constant 'name' declared above.
     */
    @EventHandler
    public void onShot(EntityShootBowEvent event) {
        if (event.getEntity().getCustomName() != null) if (event.getEntity().getCustomName().contains(name)) {
            switch (determineShot(event.getEntity())) {
                case SLOWNESS -> event.getProjectile().setMetadata("eiryeras", new arrowType(main.plugin, Arrows.SLOWNESS));
                case POISON -> event.getProjectile().setMetadata("eiryeras", new arrowType(main.plugin, Arrows.POISON));
                case KNOCKBACK -> event.getProjectile().setMetadata("eiryeras", new arrowType(main.plugin, Arrows.KNOCKBACK));
            }
            for (Entity e: event.getEntity().getNearbyEntities(25, 25, 25)) {
                if (e instanceof Player) if (((Player) e).getPotionEffect(PotionEffectType.POISON) != null
                        && ((Player) e).getPotionEffect(PotionEffectType.SLOW) != null ) {
                    switchToSword(event.getEntity());
                    Bukkit.getScheduler().runTaskLater(main.plugin, () -> switchToBow(event.getEntity()), 60);
                    break;
                }
            }
            if (event.getEntity().getNearbyEntities(4, 4, 4).size() > 0) {
                switchToSword(event.getEntity());
                Bukkit.getScheduler().runTaskLater(main.plugin, () -> switchToBow(event.getEntity()), 60);
            }
        }
    }

    /**
     * Handles the situation when a player gets hit by an Eiryeras arrow.
     *
     * @param e The entity damaged by another entity event which then gets filtered down to situations which are a
     *          player damaged by an arrow. After that its narrowed further by arrows with the "eiryeras" metadata.
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public static void onArrowHit(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player && e.getDamager() instanceof Arrow a) {
            if (a.hasMetadata("eiryeras")) {
                switch ((Arrows) Objects.requireNonNull(a.getMetadata("eiryeras").get(0).value())) {
                    case SLOWNESS -> {
                        e.setDamage(slownessDamage);
                        ((Player) e.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 5 * 20, 0, true));
                    }
                    case POISON -> {
                        e.setDamage(poisonDamage);
                        ((Player) e.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.POISON, 8 * 20, 0, true));
                        ((Player) e.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 2 * 20, 0, true));
                    }
                    case KNOCKBACK -> e.setDamage(knockbackDamage);
                }
            }
        }
    }

    /**
     * Handles the situation when an Eiryeras instance dies. Only kills caused by a player will result in drops.
     *
     * @param event The entity death event first filtered by custom name visible and then by mobs which contain
     *              Eiryeras' name in their name.
     */
    @EventHandler
    public static void onDeath(EntityDeathEvent event) {
        if (event.getEntity().getCustomName() != null) {
            if (event.getEntity().getCustomName().contains(name)) {
                CustomMob.handleDrops(event, bossDrops, Rarity.UNCOMMON);
                Random r = new Random();
                LivingEntity e = event.getEntity();
                Objects.requireNonNull(e.getLocation().getWorld()).dropItem(e.getLocation(), new ItemStack(BONE, (int) (r.nextDouble() * 5) + 1));
                Objects.requireNonNull(e.getLocation().getWorld()).dropItem(e.getLocation(), new ItemStack(ARROW, (int) (r.nextDouble() * 5) + 1));
            }
        }
    }

    /**
     * Switches the entity to wielding a sword and gives them a *minor* speed boost.
     *
     * @param e The entity, presumably an instance of Eiryeras, to switch the state of.
     */
    private void switchToSword(LivingEntity e) {
        EntityEquipment equip = e.getEquipment();
        if (equip == null) return;

        equip.setItemInMainHand(huntersKnife);
        equip.setItemInMainHandDropChance(0f);
    }

    /**
     * Switches the entity to wielding a bow and removes the *minor* speed boost.
     *
     * @param e The entity, presumably an instance of Eiryeras, to switch the state of.
     */
    private void switchToBow(LivingEntity e) {
        EntityEquipment equip = e.getEquipment();
        if (equip == null) return;

        equip.setItemInMainHand(new ItemStack(Material.BOW, 1));
        equip.setItemInMainHandDropChance(0f);
    }

    /**
     * Performs some logic to determine the type of arrow Eiryeras will use. If an enemy is too close Eiryeras will use
     * knockback and otherwise alternate between poison and slowness.
     *
     * @param e The entity, presumably an instance of Eiryeras, which the logic is being determined for.
     * @return The arrow to be fired.
     */
    private Arrows determineShot (LivingEntity e) {

        int roll = new Random().nextInt();

        if (roll % 13 == 0) return Arrows.POISON;
        else if (roll % 11 == 0) return Arrows.SLOWNESS;
        else if (roll % 10 == 0) return Arrows.KNOCKBACK;

        if (new Random().nextInt() % 2 == 0) return Arrows.POISON;
        else return Arrows.SLOWNESS;
    }
}
