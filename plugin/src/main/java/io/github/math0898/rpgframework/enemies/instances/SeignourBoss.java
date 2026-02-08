package io.github.math0898.rpgframework.enemies.instances;

import io.github.math0898.rpgframework.damage.events.AdvancedDamageEvent;
import io.github.math0898.rpgframework.damage.events.LethalDamageEvent;
import io.github.math0898.rpgframework.enemies.ActiveCustomMob;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;

/**
 * A specific case of CustomMob that applies to the SeignourBoss mob.
 *
 * @author Suagku
 */
public class SeignourBoss extends ActiveCustomMob { // todo: Charge Counter attack with an AOE smash, break blocks.
 // todo: Charge heal interrupted by attacks.
    /** // todo: Pull players towards boss and give slowness 3 for a few seconds.
     * Creates a new ActiveCustomMob with the given entity instance. // todo: When breaking blocks, simply drop while in town. Regenerate when in wilds.
     *
     * @param entity       The entity to attach to this specific ActiveCustomMob instance.
     * @param namespaceKey The key for the CustomMobEntry inside the MobManager.
     */
    public SeignourBoss (LivingEntity entity, String namespaceKey) {
        super(entity, namespaceKey);
    }

    /**
     * Called whenever this DamageModifier is relevant on a defensive front.
     *
     * @param event The AdvancedDamageEvent to consider.
     */
    @Override
    public void damaged (AdvancedDamageEvent event) {
        super.damaged(event);
        System.out.println("Seignour Attacked!");
    }

    /**
     * Called whenever this DamageModifier is relevant on an offensive front.
     *
     * @param event The AdvancedDamageEvent to consider.
     */
    @Override
    public void attack (AdvancedDamageEvent event) {
        super.attack(event);
        System.out.println("Seignour Attacking!");
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
            System.out.println("Seignour took lethal damage! Option to cancel.");
    }
}
