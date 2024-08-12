package io.github.math0898.rpgframework.enemies;

import io.github.math0898.rpgframework.RPGFramework;
import io.github.math0898.rpgframework.damage.events.AdvancedDamageEvent;
import io.github.math0898.rpgframework.damage.DamageModifier;
import io.github.math0898.rpgframework.damage.events.VerifiedDeathEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

/**
 * This is an active instance of the CustomMobEntry.
 *
 * @author Sugaku
 */
public class ActiveCustomMob implements DamageModifier, Listener {

    /**
     * The LivingEntity attached to this ActiveCustomMob.
     */
    protected final LivingEntity entity;

    /**
     * This is the namespaceKey of the mob in CustomMobEntry.
     */
    protected final String namespaceKey;

    /**
     * Creates a new ActiveCustomMob with the given entity instance.
     *
     * @param entity       The entity to attach to this specific ActiveCustomMob instance.
     * @param namespaceKey The key for the CustomMobEntry inside the MobManager.
     */
    public ActiveCustomMob (LivingEntity entity, String namespaceKey) {
        this.entity = entity;
        Bukkit.getPluginManager().registerEvents(this, RPGFramework.getInstance());
        this.namespaceKey = namespaceKey;
    }

    /**
     * Called whenever an AdvancedDamageEvent is fired by RPGFramework.
     *
     * @param event The AdvancedDamageEvent we may be interested in.
     */
    @EventHandler
    public void onAdvancedDamage (AdvancedDamageEvent event) {
        if (entity == null || entity.isDead()) {
            HandlerList.unregisterAll(this);
            return;
        }
        int id = event.getEntity().getEntityId();
        if (id == entity.getEntityId())
            damaged(event);
        else if (event.getBasicEvent() instanceof EntityDamageByEntityEvent attackingEvent)
            if (attackingEvent.getDamager().getEntityId() == entity.getEntityId())
                attack(event);
    }

    /**
     * Called whenever a death is verified by RPGFramework.
     *
     * @param event The VerifiedDeathEvent we may be interested in.
     */
    @EventHandler
    public void onVerifiedDeath (VerifiedDeathEvent event) {
        if (entity == null) {
            HandlerList.unregisterAll(this);
            return;
        }
        int id = event.getEntity().getEntityId();
        if (id == entity.getEntityId()) {
            System.out.println("Reporting death of " + namespaceKey + "!");
            MobManager.getInstance().reportCustomMobDeath(event.getEntity().getLocation(), namespaceKey);
            HandlerList.unregisterAll();
        }
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
}
