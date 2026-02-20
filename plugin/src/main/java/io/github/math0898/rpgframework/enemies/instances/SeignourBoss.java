package io.github.math0898.rpgframework.enemies.instances;

import io.github.math0898.rpgframework.Cooldown;
import io.github.math0898.rpgframework.damage.events.AdvancedDamageEvent;
import io.github.math0898.rpgframework.damage.events.LethalDamageEvent;
import io.github.math0898.rpgframework.enemies.ActiveCustomMob;
import io.github.math0898.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.scheduler.BukkitTask;

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
     * The AI task when the boss is on the field.
     */
    private final BukkitTask aiTask;

    /**
     * Array of cooldowns for managing Seignour's abilities.
     */
    private final Cooldown[] cds;

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
    public void runAi () {
        if (entity.isDead()) {
            Bukkit.getPlayer("math0898").sendMessage("Mob Died, unregistering.");
            aiTask.cancel();
            return;
        }

        if (cds[POWER_LIGHT.ordinal()].isComplete()) {
            // do power_light things
            cds[POWER_LIGHT.ordinal()].restart();
            Bukkit.getPlayer("math0898").sendMessage("Power of light");
        } else if (cds[COUNTER_SLAM.ordinal()].isComplete()) {
            // do counter_Slam things
            cds[COUNTER_SLAM.ordinal()].restart();
            Bukkit.getPlayer("math0898").sendMessage("Counter Slam");
        } else if (cds[GET_OVER_HERE.ordinal()].isComplete()) {
            // do GET_OVER_HERE things
            cds[GET_OVER_HERE.ordinal()].restart();
            Bukkit.getPlayer("math0898").sendMessage("Get Over Here");
        }

    }

    /**
     * Called whenever this DamageModifier is relevant on a defensive front.
     *
     * @param event The AdvancedDamageEvent to consider.
     */
    @Override
    public void damaged (AdvancedDamageEvent event) {
        super.damaged(event);
        Bukkit.getPlayer("math0898").sendMessage(ChatColor.GRAY + " > " + event.getPrimaryDamage() + " : " + event.getDamages().get(event.getPrimaryDamage()));
    }

    /**
     * Called whenever this DamageModifier is relevant on an offensive front.
     *
     * @param event The AdvancedDamageEvent to consider.
     */
    @Override
    public void attack (AdvancedDamageEvent event) {
        super.attack(event);
        Bukkit.getPlayer("math0898").sendMessage(ChatColor.GRAY + " > " + event.getPrimaryDamage() + " : " + event.getDamages().get(event.getPrimaryDamage()));
    }

    /**
     * Called whenever an AdvancedDamageEvent would lead to lethal damage on a target.
     *
     * @param event The LethalDamageEvent to consider.
     */
    @EventHandler
    public void onLethalDamage (LethalDamageEvent event) {
        if (entity == null) {
            HandlerList.unregisterAll(this);
            return;
        }
        int id = event.getEntity().getEntityId();
        if (id == entity.getEntityId())
            Bukkit.getPlayer("math0898").sendMessage(ChatColor.GRAY + "Seignour took lethal damage! Option to cancel.");
    }
}
