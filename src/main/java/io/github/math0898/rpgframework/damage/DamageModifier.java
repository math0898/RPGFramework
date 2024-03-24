package io.github.math0898.rpgframework.damage;

import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

/**
 * DamageModifiers are used to cause special effects whenever an advanced damage event occurs.
 *
 * @author Sugaku
 */
public interface DamageModifier {

    /**
     * Called whenever this DamageModifier is relevant on a defensive front.
     *
     * @param event The AdvancedDamageEvent to consider.
     */
    void damaged (AdvancedDamageEvent event);

    /**
     * Called whenever this DamageModifier is relevant on an offensive front.
     *
     * @param event The AdvancedDamageEvent to consider.
     */
    void attack (AdvancedDamageEvent event);

    @Deprecated
    void damaged (EntityDamageEvent event);

    @Deprecated
    void attack (EntityDamageByEntityEvent event);
}
