package io.github.math0898.rpgframework.classes.implementations;

import io.github.math0898.rpgframework.Cooldown;
import io.github.math0898.rpgframework.PlayerManager;
import io.github.math0898.rpgframework.RPGFramework;
import io.github.math0898.rpgframework.RpgPlayer;
import io.github.math0898.rpgframework.classes.AbstractClass;
import io.github.math0898.rpgframework.damage.events.AdvancedDamageEvent;
import io.github.math0898.rpgframework.damage.DamageResistance;
import io.github.math0898.rpgframework.damage.DamageType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.LargeFireball;
import org.bukkit.entity.Player;
import org.bukkit.entity.SmallFireball;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * The Pyromancer is a master of fire and the phoenix.
 *
 * @author Sugaku
 */
public class PyromancerClass extends AbstractClass { // todo: Add metadata to fireballs that contain information about firer, Eiryeras is an example on how to do this.

    /**
     * A simple enum to help keep cooldown references understandable.
     *
     * @author Sugaku
     */
    enum Abilities {

        /**
         * Single small fireball.
         */
        SMALL_FIREBALL,

        /**
         * A single large fireball.
         */
        LARGE_FIREBALL,

        /**
         * A small fireball barrage.
         */
        SMALL_FIREBALL_BARRAGE,

        /**
         * A large fireball barrage.
         */
        LARGE_FIREBALL_BARRAGE,

        /**
         * The pyromancer's revive ability.
         */
        PHOENIX_RENEWAL
    }

    /**
     * Creates a new AbstractClass object which is specific to the given player.
     *
     * @param p The player this class is specific to.
     */
    public PyromancerClass (RpgPlayer p) {
        super(p);
        Cooldown[] cds = new Cooldown[5];
        cds[Abilities.SMALL_FIREBALL.ordinal()] = new Cooldown(3);
        cds[Abilities.LARGE_FIREBALL.ordinal()] = new Cooldown(5);
        cds[Abilities.SMALL_FIREBALL_BARRAGE.ordinal()] = new Cooldown(10);
        cds[Abilities.LARGE_FIREBALL_BARRAGE.ordinal()] = new Cooldown(15);
        cds[Abilities.PHOENIX_RENEWAL.ordinal()] = new Cooldown(180);
        setCooldowns(cds);
        setClassItems(Material.BLAZE_POWDER, Material.BLAZE_ROD);
    }

    /**
     * Creates a new AbstractClass object which is specific to the given player.
     *
     * @param p The player this class is specific to.
     */
    @Deprecated
    public PyromancerClass (sugaku.rpg.framework.players.RpgPlayer p) {
        this(PlayerManager.getPlayer(p.getUuid()));
    }

    /**
     * Called whenever a player left-clicks while holding a class item. To reach this method, the player must be holding
     * a class item. No promises are made if they're wearing armor or not.
     *
     * @param event The PlayerInteractEvent that lead to this method being called.
     * @param type  The type of material that was used in this cast.
     */
    @Override
    public void onLeftClickCast (PlayerInteractEvent event, Material type) {
        Player player = getPlayer().getBukkitPlayer();
        switch (type) {
            case BLAZE_POWDER -> {
                if (offCooldown(Abilities.LARGE_FIREBALL.ordinal())) {
                    send(ChatColor.GREEN + "You've fired a fireball!");
                    player.launchProjectile(LargeFireball.class);
                    getCooldowns()[Abilities.LARGE_FIREBALL.ordinal()].restart();
                }
            }
            case BLAZE_ROD -> {
                if (offCooldown(Abilities.LARGE_FIREBALL_BARRAGE.ordinal())) {
                    send(ChatColor.GREEN + "You've fired a fireball barrage!");
                    BukkitRunnable runnable = new BukkitRunnable() {
                        @Override
                        public void run() {
                            player.launchProjectile(LargeFireball.class);
                        }
                    };
                    runnable.runTaskTimer(RPGFramework.getInstance(), 0, 5);
                    Bukkit.getScheduler().runTaskLater(RPGFramework.getInstance(), runnable::cancel, 11);
                    getCooldowns()[Abilities.LARGE_FIREBALL_BARRAGE.ordinal()].restart();
                }
            }
        }
    }

    /**
     * Called whenever a player right-clicks while holding a class item. To reach this method, the player must be
     * holding a class item. No promises are made if they're wearing armor or not.
     *
     * @param event The PlayerInteractEvent that lead to this method being called.
     * @param type  The type of material that was used in this cast.
     */
    @Override
    public void onRightClickCast (PlayerInteractEvent event, Material type) {
        Player player = getPlayer().getBukkitPlayer();
        switch (type) {
            case BLAZE_POWDER -> {
                if (offCooldown(Abilities.SMALL_FIREBALL.ordinal())) {
                    send(ChatColor.GREEN + "You've fired a fireball!");
                    player.launchProjectile(SmallFireball.class);
                    getCooldowns()[Abilities.SMALL_FIREBALL.ordinal()].restart();
                }
            }
            case BLAZE_ROD -> {
                if (offCooldown(Abilities.SMALL_FIREBALL_BARRAGE.ordinal())) {
                    send(ChatColor.GREEN + "You've fired a fireball barrage!");
                    BukkitRunnable runnable = new BukkitRunnable() {
                        @Override
                        public void run() {
                            player.launchProjectile(SmallFireball.class);
                        }
                    };
                    runnable.runTaskTimer(RPGFramework.getInstance(), 0, 5);
                    Bukkit.getScheduler().runTaskLater(RPGFramework.getInstance(), runnable::cancel, 11);
                    getCooldowns()[Abilities.SMALL_FIREBALL_BARRAGE.ordinal()].restart();
                }
            }
        }
    }

    /**
     * Called when the class user has 'died'.
     *
     * @return Whether a death should be respected or not.
     */
    @Override
    public boolean onDeath () {
        if(offCooldown(Abilities.PHOENIX_RENEWAL.ordinal())) {
            send(ChatColor.GREEN + "You've used " + ChatColor.GOLD + "Phoenix Renewal" + ChatColor.GREEN + "!");
            RpgPlayer rpg = getPlayer();
            Player player = rpg.getBukkitPlayer();
            player.getWorld().playSound(player.getLocation(), Sound.ITEM_TOTEM_USE, 0.8f, 1.0f);
            rpg.addPotionEffect(PotionEffectType.REGENERATION, 15*20, 4);
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
    }

    /**
     * Called whenever this DamageModifier is relevant on an offensive front.
     *
     * @param event The AdvancedDamageEvent to consider.
     */
    @Override
    public void attack (AdvancedDamageEvent event) {
        if (getCooldowns()[Abilities.PHOENIX_RENEWAL.ordinal()].getRemaining() >= 165) {
            event.addDamage(12.5, DamageType.FIRE);
            event.getEntity().setFireTicks(5);
        }
    }
}
