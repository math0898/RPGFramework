package io.github.math0898.rpgframework.classes.implementations;

import io.github.math0898.rpgframework.Cooldown;
import io.github.math0898.rpgframework.RpgPlayer;
import io.github.math0898.rpgframework.classes.AbstractClass;
import io.github.math0898.rpgframework.damage.DamageResistance;
import io.github.math0898.rpgframework.damage.DamageType;
import io.github.math0898.rpgframework.damage.events.AdvancedDamageEvent;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.SmallFireball;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.List;

import static io.github.math0898.rpgframework.classes.implementations.PyromancerClass.Abilities.*;

/**
 * The Pyromancer overwhelms nearby enemies with fire magic and enters short empowered burn phases.
 *
 * @author Cybellereaper, Sugaku
 */ // todo: Still a lot of magic numbers that could get cleaned up and improved. We should probably also start setting up a lang system.
public class PyromancerClass extends AbstractClass {

    /**
     * Base flame damage added to Pyromancer attacks.
     */
    private static final double MELEE_FIRE_BONUS_DAMAGE = 2.5;

    /**
     * Bonus damage to already ignited enemies.
     */
    private static final double IGNITED_BONUS_DAMAGE = 2.5;

    /**
     * Enum names for the Pyromancer's cooldowns. Provides constants for common ability values.
     */
    protected enum Abilities {

        /**
         * A point-blank burst of flame that ignites nearby enemies.
         */
        SCORCH(12, 2.0, 0),

        /**
         * A short empowerment window that improves the pyromancer's attacks.
         */
        KINDLE(20, 5.0, 8),

        /**
         * A spread of quick moving firebolts.
         */
        FLARE_VOLLEY(18, 0, 0),

        /**
         * A burst of restorative fire.
         */
        CAUTERIZE(35, 4.0, 0),

        /**
         * Phoenix themed cheat death and empowerment.
         */
        PHOENIX_RENEWAL(180, 8.0, 12); // Strength is the heal component

        /**
         * The amount of time before this ability recovers and is usable again.
         */
        private final int cooldown;

        /**
         * The power value of this ability. Effect varies.
         */
        private final double strength;

        /**
         * The amount of time that this ability lasts for.
         */
        private final int duration;

        /**
         * Creates a new ability with the given constant values.
         */
         Abilities (int cooldown, double strength, int duration) {
            this.cooldown = cooldown;
            this.strength = strength;
            this.duration = duration;
        }
    }

    /**
     * Creates a new AbstractClass object which is specific to the given player.
     *
     * @param p The player this class is specific to.
     */
    public PyromancerClass (RpgPlayer p) {
        super(p);
        Cooldown[] cds = new Cooldown[5];
        cds[SCORCH.ordinal()] = new Cooldown(SCORCH.cooldown);
        cds[KINDLE.ordinal()] = new Cooldown(KINDLE.cooldown);
        cds[FLARE_VOLLEY.ordinal()] = new Cooldown(FLARE_VOLLEY.cooldown);
        cds[CAUTERIZE.ordinal()] = new Cooldown(CAUTERIZE.cooldown);
        cds[PHOENIX_RENEWAL.ordinal()] = new Cooldown(PHOENIX_RENEWAL.cooldown);
        setCooldowns(cds);
        setClassItems(Material.BLAZE_POWDER, Material.BLAZE_ROD);
    }

    /**
     * Called whenever a player left-clicks while holding a class item.
     *
     * @param event The PlayerInteractEvent that lead to this method being called.
     * @param type  The type of material that was used in this cast.
     */
    @Override
    public void onLeftClickCast (PlayerInteractEvent event, Material type) {
        switch (type) {
            case BLAZE_POWDER -> castScorch();
            case BLAZE_ROD -> castFlareVolley();
        }
    }

    /**
     * Called whenever a player right-clicks while holding a class item.
     *
     * @param event The PlayerInteractEvent that lead to this method being called.
     * @param type  The type of material that was used in this cast.
     */
    @Override
    public void onRightClickCast (PlayerInteractEvent event, Material type) {
        switch (type) {
            case BLAZE_POWDER -> castKindle();
            case BLAZE_ROD -> castCauterize();
        }
    }

    /**
     * Makes the player attached to this class object cast the scorch ability.
     */
    private void castScorch () {
        if (!offCooldown(SCORCH.ordinal())) return;
        RpgPlayer rpg = getPlayer();
        Player player = rpg.getBukkitPlayer();
        List<LivingEntity> targets = rpg.nearbyEnemyCasterTargets(4.5, 3.0, 4.5);
        send(ChatColor.GOLD + "Scorch" + ChatColor.GREEN + " erupts around you!");
        player.getWorld().spawnParticle(Particle.FLAME, player.getLocation().add(0, 1.0, 0), 45, 1.8, 0.6, 1.8, 0.02);
        player.getWorld().playSound(player.getLocation(), Sound.ITEM_FIRECHARGE_USE, 1.0f, 0.9f);
        int hitCount = 0;
        for (LivingEntity entity : targets) {
            entity.setFireTicks(Math.max(entity.getFireTicks(), 5 * 20));
            entity.damage(SCORCH.strength, player);
            entity.getWorld().spawnParticle(Particle.LAVA, entity.getLocation().add(0, 1.0, 0), 6, 0.3, 0.4, 0.3, 0.01);
            hitCount++;
        }
        if (hitCount > 0)
            rpg.heal(Math.min(hitCount, 2));
        getCooldowns()[SCORCH.ordinal()].restart();
    }

    /**
     * Makes the player attached to this class object cast the Kindle ability.
     */
    private void castKindle () {
        if (!offCooldown(Abilities.KINDLE.ordinal())) return;
        Player player = getPlayer().getBukkitPlayer();
        send(ChatColor.GOLD + "Kindle" + ChatColor.GREEN + " empowers your flames!");
        getPlayer().addPotionEffect(PotionEffectType.SPEED, KINDLE.duration * 20, 0);
        getPlayer().addPotionEffect(PotionEffectType.FIRE_RESISTANCE, KINDLE.duration * 20, 0);
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_BLAZE_AMBIENT, 0.8f, 1.3f);
        player.getWorld().spawnParticle(Particle.FLAME, player.getLocation().add(0, 1.0, 0), 30, 0.4, 0.6, 0.4, 0.01);
        getCooldowns()[Abilities.KINDLE.ordinal()].restart();
    }

    /**
     * Makes the player attached to this class object cast the flare volley ability.
     */
    private void castFlareVolley () {
        if (!offCooldown(Abilities.FLARE_VOLLEY.ordinal())) return;
        Player player = getPlayer().getBukkitPlayer();
        send(ChatColor.GOLD + "Flare Volley" + ChatColor.GREEN + " streaks forward!");
        Vector forward = player.getEyeLocation().getDirection().normalize();
        Vector sideways = forward.clone().crossProduct(new Vector(0, 1, 0));
        if (sideways.lengthSquared() == 0)
            sideways = new Vector(1, 0, 0);
        sideways.normalize();
        for (double offset : new double[]{-0.18, 0.0, 0.18}) {
            Vector velocity = forward.clone().add(sideways.clone().multiply(offset)).normalize().multiply(0.9);
            SmallFireball projectile = player.launchProjectile(SmallFireball.class, velocity); // todo: Add a metadatavalue to this so we can scale the damage it does.
            projectile.setIsIncendiary(true);
        }
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 1.0f, 1.1f);
        getCooldowns()[Abilities.FLARE_VOLLEY.ordinal()].restart();
    }

    /**
     * Makes the player attached to this class object cast the Cauterize ability.
     */
    private void castCauterize () {
        if (!offCooldown(Abilities.CAUTERIZE.ordinal())) return;
        RpgPlayer rpg = getPlayer();
        Player player = rpg.getBukkitPlayer();
        send(ChatColor.GOLD + "Cauterize" + ChatColor.GREEN + " seals your wounds.");
        rpg.heal(CAUTERIZE.strength);
        rpg.addPotionEffect(PotionEffectType.REGENERATION, 8 * 20, 1);
        player.getWorld().playSound(player.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 0.8f, 1.4f);
        player.getWorld().spawnParticle(Particle.ASH, player.getLocation().add(0, 1.0, 0), 25, 0.5, 0.8, 0.5, 0.02);
        getCooldowns()[Abilities.CAUTERIZE.ordinal()].restart();
    }

    /**
     * Called when the class user has 'died'.
     *
     * @return Whether a death should be respected or not.
     */
    @Override
    public boolean onDeath () {
        if (offCooldown(Abilities.PHOENIX_RENEWAL.ordinal())) {
            RpgPlayer rpg = getPlayer();
            Player player = rpg.getBukkitPlayer();
            send(ChatColor.GREEN + "You've used " + ChatColor.GOLD + "Phoenix Renewal" + ChatColor.GREEN + "!");
            player.getWorld().playSound(player.getLocation(), Sound.ITEM_TOTEM_USE, 0.8f, 1.0f);
            player.getWorld().spawnParticle(Particle.FLAME, player.getLocation().add(0, 1.0, 0), 80, 0.6, 0.9, 0.6, 0.03);
            rpg.heal(PHOENIX_RENEWAL.strength);
            rpg.addPotionEffect(PotionEffectType.REGENERATION, PHOENIX_RENEWAL.duration * 20, 2);
            rpg.addPotionEffect(PotionEffectType.STRENGTH, PHOENIX_RENEWAL.duration * 20, 0);
            rpg.addPotionEffect(PotionEffectType.SPEED, PHOENIX_RENEWAL.duration * 20, 0);
            getCooldowns()[Abilities.PHOENIX_RENEWAL.ordinal()].restart();
            return false;
        }
        return true;
    }

    /**
     * Called whenever this DamageModifier is relevant on a defensive front.
     *
     * @param event The AdvancedDamageEvent to consider.
     */
    @Override
    public void damaged (AdvancedDamageEvent event) {
        event.getEntity().setFireTicks(0);
        event.setResistance(DamageType.FIRE, DamageResistance.RESISTANCE);
        if (isPhoenixEmpowered())
            event.addDamage(-2.5, event.getPrimaryDamage());
            // This felt a bit much at -5.0. We need data analytics and testing.
    }

    /**
     * Called whenever this DamageModifier is relevant on an offensive front.
     *
     * @param event The AdvancedDamageEvent to consider.
     */
    @Override
    public void attack (AdvancedDamageEvent event) {
        double fireDamage = MELEE_FIRE_BONUS_DAMAGE;
        if (event.getEntity().getFireTicks() > 0)
            fireDamage += IGNITED_BONUS_DAMAGE;
        if (isKindled())
            fireDamage += KINDLE.strength;
        if (isPhoenixEmpowered()) {
            fireDamage += 5.0;
            getPlayer().heal(1.0);
        }
        event.addDamage(fireDamage, DamageType.FIRE);
        // Always igniting enemies could be kinda fun.
        event.getEntity().setFireTicks(Math.max(event.getEntity().getFireTicks(), isPhoenixEmpowered() ? 8 * 20 : 5 * 20));
    }

    /**
     * Checks whether this player is empowered by kindled or not.
     *
     * @return True if within kindled's duration after a cooldown, otherwise false.
     */ // todo: We should probably create an implementation depending on last used. This means the player "used" the button as soon as they login. Low priority.
    private boolean isKindled () {
        return getCooldowns()[Abilities.KINDLE.ordinal()].getRemaining() >= (KINDLE.cooldown - KINDLE.duration);
    }

    /**
     * Checks whether this player is empowered by phoenix empowerment or not.
     *
     * @return True if within phoenix empowerment's duration after a cooldown, otherwise false.
     */ // todo: We should probably create an implementation depending on last used. This means the player "used" the button as soon as they login. Low priority.
    private boolean isPhoenixEmpowered () {
        return getCooldowns()[Abilities.PHOENIX_RENEWAL.ordinal()].getRemaining() >= (PHOENIX_RENEWAL.cooldown - PHOENIX_RENEWAL.duration);
    }
}
