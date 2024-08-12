package io.github.math0898.rpgframework.enemies;

import io.github.math0898.rpgframework.damage.events.AdvancedDamageEvent;
import io.github.math0898.rpgframework.damage.events.LethalDamageEvent;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;

/**
 * A specific case of CustomMob that applies to the SeignourBoss mob.
 *
 * @author Suagku
 */
public class SeignourBoss extends ActiveCustomMob {

    /**
     * Creates a new ActiveCustomMob with the given entity instance.
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
