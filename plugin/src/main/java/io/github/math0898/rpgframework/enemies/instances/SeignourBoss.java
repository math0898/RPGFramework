package io.github.math0898.rpgframework.enemies.instances;

import io.github.math0898.rpgframework.Cooldown;
import io.github.math0898.rpgframework.damage.AdvancedDamageHandler;
import io.github.math0898.rpgframework.damage.events.AdvancedDamageEvent;
import io.github.math0898.rpgframework.enemies.ActiveCustomMob;
import io.github.math0898.utils.Utils;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static io.github.math0898.rpgframework.enemies.instances.SeignourBoss.Abilities.*;

/**
 * A specific case of CustomMob that applies to the SeignourBoss mob.
 *
 * @author Suagku
 */
public class SeignourBoss extends ActiveCustomMob { // todo: Charge Counter attack with an AOE smash, break blocks.
 // todo: Charge heal interrupted by attacks.
// todo: Pull players towards boss and give slowness 3 for a few seconds.
    // todo: When breaking blocks, simply drop while in town. Regenerate when in wilds.

    /**
     * The amount of velocity to give the victim during "GET_OVER_HERE."
     */
    private static final double GET_OVER_HERE_STRENGTH = 2.0;

    /**
     * The AI task when the boss is on the field.
     */
    private final BukkitTask aiTask;

    /**
     * The last player to attack Seignour.
     */
    private Player lastAttacker = null;

    /**
     * Array of cooldowns for managing Seignour's abilities.
     */
    private final Cooldown[] cds;

    /**
     * The amount of damage received during POWER_OF_LIGHT.
     */
    private double damagePowerOfLight = 0;

    /**
     * The amount of damage received during COUNTER_SLAM.
     */
    private double damageCounterSlam = 0;

    enum Abilities {

        COUNTER_SLAM,

        POWER_LIGHT,

        GET_OVER_HERE;
    }

    /**
     * Creates a new ActiveCustomMob with the given entity instance.
     *
     * @param entity       The entity to attach to this specific ActiveCustomMob instance.
     * @param namespaceKey The key for the CustomMobEntry inside the MobManager.
     */
    public SeignourBoss (LivingEntity entity, String namespaceKey) {
        super(entity, namespaceKey);
        aiTask = Bukkit.getScheduler().runTaskTimer(Utils.getPlugin(), this::runAi, 20L, 20L);
        cds = new Cooldown[3];
        cds[COUNTER_SLAM.ordinal()] = new Cooldown(17);
        cds[POWER_LIGHT.ordinal()] = new Cooldown(29);
        cds[GET_OVER_HERE.ordinal()] = new Cooldown(15);
    }

    /**
     * Runs the Ai for Seignour including decison making.
     */
    public void runAi () { // todo: Make this managed by ActiveCustomMob, then overriden here.
        if (entity.isDead()) {
            Bukkit.getPlayer("math0898").sendMessage("Mob Died, unregistering.");
            aiTask.cancel();
            return;
        }

        if (cds[POWER_LIGHT.ordinal()].isComplete()) {
            // do power_light things
            cds[POWER_LIGHT.ordinal()].restart();
            Bukkit.getPlayer("math0898").sendMessage("Power of light");
            abilityPowerOfLight();
        } else if (cds[COUNTER_SLAM.ordinal()].isComplete()) {
            // do COUNTER_SLAM things
            cds[COUNTER_SLAM.ordinal()].restart();
            abilityCounterSlam();
        } else if (cds[GET_OVER_HERE.ordinal()].isComplete()) {
            // do GET_OVER_HERE things
            cds[GET_OVER_HERE.ordinal()].restart();
            abilityGetOverHere();
        }
    }

    /**
     * Adds velocity to the given Entity's velocity relative to Seignour and scaled by the given scalar.
     *
     * @param target The victim.
     * @param scalar The magnitude of the effect.
     */
    private void launchRelativeToSeignour (Entity target, double scalar) {
        Vector current = target.getVelocity();
        Vector seigLocation = entity.getLocation().toVector();
        Vector targetLocation = target.getLocation().toVector();

        Vector seigToPlayer = seigLocation.subtract(targetLocation).normalize();

        target.setVelocity(current.add(seigToPlayer.multiply(scalar)));
    }

    /**
     * The ability "GET_OVER_HERE" runs on a given target and pulls them towards Seignour.
     */
    private void abilityGetOverHere () {
        entity.swingMainHand();
        entity.swingOffHand();

        Player target = lastAttacker;
        if (target == null)
            target = findPlayerInRange(10, 10, 10);
        if (target == null) return;

        launchRelativeToSeignour(target, GET_OVER_HERE_STRENGTH);
        target.playSound(target.getLocation(), Sound.ITEM_ARMOR_EQUIP_CHAIN, 2.0f, 0.5f);
        speak(target, "GET OVER HERE!");
    }

    /**
     * The ability "COUNTER_SLAM" causes Seignour to reflect a portion of the damage he takes in the form of a large slam.
     */
    private void abilityCounterSlam () {
        final int CHARGE_DURATION = 3 * 20;
        entity.setAI(false);
        Bukkit.getScheduler().runTaskLater(Utils.getPlugin(), () -> entity.setAI(true), CHARGE_DURATION);
        damageCounterSlam = 0;

        final World world = entity.getWorld();
        Random rand = new Random();
        for (int i = 0; i < CHARGE_DURATION; i++) {
            Bukkit.getScheduler().runTaskLater(Utils.getPlugin(),
                    () -> world.spawnParticle(Particle.ANGRY_VILLAGER, entity.getLocation().add(rand.nextDouble() * 0.5, 2.0 + rand.nextDouble() * 0.5, rand.nextDouble() * 0.5), 1), i);
            for (int j = 0; j < 20; j++) { // todo: Make an actual circle.
                double dx = rand.nextDouble() * ((0.1 * CHARGE_DURATION) / (i + 1));
                double dz = rand.nextDouble() * ((0.1 * CHARGE_DURATION) / (i + 1));
                Bukkit.getScheduler().runTaskLater(Utils.getPlugin(),
                        () -> world.spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, entity.getLocation().add(dx, rand.nextDouble() * 0.1, dz), 1), i);
            }
        }
        Bukkit.getScheduler().runTaskLater(Utils.getPlugin(), () -> {
            world.playSound(entity.getLocation(), Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 0.7f, 0.8f);
            world.playSound(entity.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 0.8f, 0.6f);
            world.playSound(entity.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 0.9f, 1.3f);
            }, CHARGE_DURATION);
        // todo: Block breaking and replacing once he's gone.
        Bukkit.getScheduler().runTaskLater(Utils.getPlugin(), this::abilityCounterSlamInstant, CHARGE_DURATION);
    }

    /**
     * Called when the counter slam has finished and is dealing damage.
     */
    private void abilityCounterSlamInstant () {
        List<Entity> nearby = entity.getNearbyEntities(12, 12, 12);
        Location location = entity.getLocation();
        List<Entity> nearestRange = new ArrayList<>();
        List<Entity> moderateRange = new ArrayList<>();
        List<Entity> farthestRange = new ArrayList<>();
        for (Entity e: nearby) {
            double distance = location.distance(e.getLocation());
            if (distance < 2.0) nearestRange.add(e);
            else if (distance < 5.0) moderateRange.add(e);
            else if (distance < 7.0) farthestRange.add(e);
        }
        for (Entity e : nearestRange) {
            if (e instanceof LivingEntity le) {
                launchRelativeToSeignour(e, -7.0);
                le.damage(30.0 + (damageCounterSlam * 5.0), entity);
            }
        }
        for (Entity e : moderateRange) {
            if (e instanceof LivingEntity le) {
                launchRelativeToSeignour(e, -3.0);
                le.damage(15.0 + (damageCounterSlam * 3.0), entity);
            }
        }
        for (Entity e : farthestRange) {
            if (e instanceof LivingEntity le) {
                launchRelativeToSeignour(e, -0.5);
                le.damage(2.0 + damageCounterSlam, entity);
            }
        }
    }

    /**
     * The ability "POWER_OF_LIGHT" causes Seignour to charge himself a heal, which is reduced depending on player damage.
     */
    private void abilityPowerOfLight () {
        final int CHARGE_DURATION = 3 * 20;
        entity.setAI(false);
        Bukkit.getScheduler().runTaskLater(Utils.getPlugin(), () -> entity.setAI(true), CHARGE_DURATION);
        damagePowerOfLight = 0;

        final World world = entity.getWorld();
        Random rand = new Random();
        for (int i = 0; i < CHARGE_DURATION; i++) {
            Bukkit.getScheduler().runTaskLater(Utils.getPlugin(), // todo: Perhaps different from flame.
                    () -> world.spawnParticle(Particle.FLAME, entity.getLocation().add(rand.nextDouble() * 0.5, 2.0 + rand.nextDouble() * 0.5, rand.nextDouble() * 0.5), 1), i);
            Particle[] particles = new Particle[]{ Particle.HAPPY_VILLAGER, Particle.ELECTRIC_SPARK };
            for (int j = 0; j < 20; j++) {
                int particle = rand.nextInt(2); // todo: Make an actual circle.
                double dx = rand.nextDouble() * ((0.1 * CHARGE_DURATION) / (i + 1));
                double dz = rand.nextDouble() * ((0.1 * CHARGE_DURATION) / (i + 1));
                Bukkit.getScheduler().runTaskLater(Utils.getPlugin(),
                        () -> world.spawnParticle(particles[particle], entity.getLocation().add(dx, rand.nextDouble() * 2, dz), 1), i);
            }
        }
        // Todo: Perhaps slightly different.
        Bukkit.getScheduler().runTaskLater(Utils.getPlugin(), () -> {
            world.playSound(entity.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.7f, 0.8f);
            world.playSound(entity.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.8f, 0.6f);
            world.playSound(entity.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.9f, 1.3f);
            entity.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 6 * 20, -1, false, true));
            entity.setHealth(entity.getHealth() + Math.abs(20.0 - damagePowerOfLight));
        }, CHARGE_DURATION);
    }

    /**
     * Attempts to locate a player in the given range. Used as a fallback for when a target cannot otherwise be found.
     *
     * @return Any player within range, or null if none found.
     */
    private Player findPlayerInRange (double dx, double dy, double dz) {
        List<Entity> nearby = entity.getNearbyEntities(dx, dy, dz);
        for (Entity e : nearby)
            if (e instanceof Player player)
                return player;
        return null;
    }

    /**
     * Called whenever this DamageModifier is relevant on a defensive front.
     *
     * @param event The AdvancedDamageEvent to consider.
     */
    @Override
    public void damaged (AdvancedDamageEvent event) {
        super.damaged(event);
        if (event.getBasicEvent() instanceof EntityDamageByEntityEvent evp) {
            if (evp.getDamager() instanceof Player player)
                lastAttacker = player;
        }
        if (cds[COUNTER_SLAM.ordinal()].getRemaining() >= 17 - 3) damageCounterSlam += AdvancedDamageHandler.damageCalculation(event);
        if (cds[POWER_LIGHT.ordinal()].getRemaining() >= 17 - 3) damagePowerOfLight += AdvancedDamageHandler.damageCalculation(event);
    }
}
