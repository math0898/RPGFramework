package sugaku.rpg.mobs.teir1.feyrith;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;
import sugaku.rpg.framework.items.ItemsManager;
import sugaku.rpg.framework.items.Rarity;
import sugaku.rpg.main;
import sugaku.rpg.mobs.CustomMob;

import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.function.Predicate;

public class FeyrithBoss extends CustomMob implements Listener {

    public FeyrithBoss() { super(name, EntityType.WITHER_SKELETON, Rarity.RARE, 350); }

    /**
     * Describes the metadata behind Feyrith projectiles.
     */
    static class FeyrithMeta implements MetadataValue {

        private final int value;

        public FeyrithMeta(int i) { value = i; }

        @Override
        public Object value() { return value; }

        @Override
        public int asInt() { return value; }

        @Override
        public float asFloat() { return value; }

        @Override
        public double asDouble() { return value; }

        @Override
        public long asLong() { return value; }

        @Override
        public short asShort() { return -1; }

        @Override
        public byte asByte() { return 0; }

        @Override
        public boolean asBoolean() { return false; }

        @Override
        public String asString() { return value + ""; }

        @Override
        public Plugin getOwningPlugin() { return main.plugin; }

        @Override
        public void invalidate() {}
    }

    /**
     * The name of the boss. This is then modified to create the display name including the plugin prefix and color
     * rarity of the boss.
     */
    private static final String name = "Feyrith, Apprentice Mage";

    /**
     * The max health of the boss.
     */
    private static final int maxHealth = 350;

    /**
     * The base damage of the boss.
     */
    private static final double baseDamage = 5.0;

    /**
     * An integer which represents the phase of the boss fight the boss is on. Determined based on health, phase 1,
     * 100->70, phase 2, 70->40, phase 3, 40->0.
     */
    private int phase = 1;

    /**
     * Creates and spawns a Feyrith boss at the given location.
     *
     * @param l The location to spawn the boss.
     */
    public FeyrithBoss(Location l) {
        super(name, EntityType.WITHER_SKELETON, Rarity.RARE, maxHealth);

        setArmor(new ItemStack(Material.AIR, 0),
                ItemsManager.createLeatherArmor(Material.LEATHER_CHESTPLATE, " ", new String[]{}, 25, 64, 255),
                ItemsManager.createLeatherArmor(Material.LEATHER_LEGGINGS, " ", new String[]{}, 148, 161, 227),
                ItemsManager.createLeatherArmor(Material.LEATHER_BOOTS, " ", new String[]{}, 22, 44, 156));
        spawn(l);
        getEntity().setGravity(false);
        getEntity().setVelocity(new Vector(0, 0, 0));
        Objects.requireNonNull(getEntity().getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE)).setBaseValue(1000);
        runAI();
    }

    /**
     * The main AI loop of the boss. Called every 4 seconds to determine its next action.
     */
    public void runAI() {
        LivingEntity entity = getEntity();
        Location location = entity.getLocation();
        if(entity.isDead()) return; //If we're dead there's nothing else to do.
        else Bukkit.getServer().getScheduler().runTaskLater(main.plugin, this::runAI, 4*20);
        List<Entity> nearby = (List<Entity>) entity.getWorld().getNearbyEntities(new BoundingBox(location.getX() - 10, location.getY() - 5, location.getZ() - 10, location.getX() + 10, location.getY() + 5, location.getZ() + 10), new Predicate<Entity>() {
            @Override
            public boolean test(Entity entity) {
                return entity instanceof Player;
            }
        });

        //Determine boss phase
        if (entity.getHealth() < (maxHealth * 0.40) && phase == 2) phase = 3;
        else if (entity.getHealth() < (maxHealth * 0.70) && phase == 1) phase = 2;

        //Run specific phase AI
        if (phase != 3) move(entity, nearby);
        switch(phase) {
            case 3:
                //TODO: Fireball rain
                break;
            case 2:
                double attack1 = Math.abs(new Random().nextDouble());
                if (attack1 < 0.33) lightingAttack(nearby);
                else if (attack1 < 0.66) waveAttack();
                else if (attack1 < 1) fireballAttack(entity, nearby);
            case 1:
                //What attack are we going to do next?
                double attack2 = Math.abs(new Random().nextDouble());
                if (attack2 < 0.33) lightingAttack(nearby);
                else if (attack2 < 0.66) waveAttack();
                else if (attack2 < 1) fireballAttack(entity, nearby);
                break;
        }
    }

    private void lightingAttack(List<Entity> nearby) {
        if (phase == 1) dyeArmor(25, 64, 255, 0.75, 0.5, 1);
        for (Entity e: nearby) {
            particlesVert(Particle.WATER_DROP, e.getLocation(), 30, 5);
            double x = e.getLocation().getX();
            double y = e.getLocation().getY();
            double z = e.getLocation().getZ();
            Bukkit.getScheduler().runTaskLater(main.plugin, () -> e.getWorld().strikeLightning(new Location(e.getWorld(), x, y, z)), 2*20);
        }
    }

    private void waveAttack() {
        if (phase == 1) dyeArmor(25, 255, 64, 0.75, 1, 0.5); //TODO: Implement
    }

    private void fireballAttack(Entity entity, List<Entity> nearby) {
        if (phase == 1) dyeArmor(255, 64, 25, 1, 0.75, 0.5);
        double flightSpeed = 40;
        for (Entity e: nearby) {
            double dx = e.getLocation().getX() - entity.getLocation().getX();
            double dy = e.getLocation().getY() - entity.getLocation().getY();
            double dz = e.getLocation().getZ() - entity.getLocation().getZ();
            Vector v = new Vector(dx/flightSpeed, dy/flightSpeed, dz/flightSpeed);
            Projectile p = ((ProjectileSource) entity).launchProjectile(Fireball.class, v);
            p.setGravity(false);
            p.setRotation(0.25f, 0.3f);
            p.setMetadata("feyrith", new FeyrithMeta(1));
        }
    }

    private void move(Entity entity, List<Entity> nearby) {
        Location old = entity.getLocation();
        Location target = findSpot(entity);
        if (nearby.size() != 0) target = findSpot(nearby.get(0));
        entity.teleport(target);
        particlesVert(Particle.PORTAL, old, 20, 4);
    }

    private Location findSpot(Entity closest) {
        Random rand = new Random();
        int x = (int) Math.floor(closest.getLocation().getX());
        int y = (int) Math.floor(closest.getLocation().getY());
        int z = (int) Math.floor(closest.getLocation().getZ());
        if (rand.nextBoolean()) x += -(rand.nextInt() % 10);
        else x += (rand.nextInt() % 10);
        if (rand.nextBoolean()) z += -(rand.nextInt() % 10);
        else z += (rand.nextInt() % 10);
        for (int i = -3; i < 10; i++) if (closest.getWorld().getBlockAt(x, y+i, z).getType() == Material.AIR &&
                                          closest.getWorld().getBlockAt(x, y+i+1, z).getType() == Material.AIR &&
                                          closest.getWorld().getBlockAt(x, y+i+2, z).getType() == Material.AIR) { y += i; break; }
        return new Location(closest.getWorld(), x, y, z);
    }

    private void particlesVert(Particle p, Location l, int n, int h) {
        for (int i = 0; i < n; i++) Objects.requireNonNull(l.getWorld()).spawnParticle(p, new Location(l.getWorld(), l.getX(), l.getY() + (i * (h * 1.0)/(n * 1.0)), l.getZ()), n);
    }

    private void dyeArmor(int r, int g, int b, double c, double l, double f) {
        LivingEntity entity = getEntity();
        EntityEquipment equipment = entity.getEquipment();
        assert equipment != null;
        equipment.setChestplate(ItemsManager.createLeatherArmor(Material.LEATHER_CHESTPLATE, " ", new String[]{}, (int) (r * c), (int) (g * c), (int) (b * c)));
        equipment.setLeggings(ItemsManager.createLeatherArmor(Material.LEATHER_LEGGINGS, " ", new String[]{}, (int) (r * l), (int) (g * l), (int) (b * l)));
        equipment.setBoots(ItemsManager.createLeatherArmor(Material.LEATHER_BOOTS, " ", new String[]{}, (int) (r * f), (int) (g * f), (int) (b * f)));
    }

    /**
     * Returns the name of the Feyrith the boss.
     *
     * @return The string name of Feyrith.
     */
    public static String getName() { return name; }

    /**
     * Handles the situation when a player gets damaged from one of Feyrith's attacks.
     *
     * @param event First filtered by cause then does things that need to happen.
     */
    @EventHandler
    public static void EntityDamaged(EntityDamageEvent event) {

        Entity entity = event.getEntity();
        Location location = entity.getLocation();

        if (entity instanceof Player && event.getCause() == EntityDamageEvent.DamageCause.LIGHTNING) {
            List<Entity> nearby = (List<Entity>) entity.getWorld().getNearbyEntities(new BoundingBox(location.getX() - 10, location.getY() - 5, location.getZ() - 10, location.getX() + 10, location.getY() + 5, location.getZ() + 10), new Predicate<Entity>() {
                @Override
                public boolean test(Entity entity) { return entity.getName().contains(name); }});
            if (nearby.size() > 0) event.setDamage(baseDamage * 2.0);
        }
    }

    /**
     * Handles the situation when a player gets hit by a projectile.
     *
     * @param event Filtered by entities that are players and then by projectiles with the Feyrith metadata.
     */
    @EventHandler
    public static void EntityHit(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Fireball) {
            if (event.getDamager().hasMetadata("feyrith")) {
                event.getEntity().setFireTicks(20);
                event.setDamage(baseDamage);
            }
        }
    }
}
