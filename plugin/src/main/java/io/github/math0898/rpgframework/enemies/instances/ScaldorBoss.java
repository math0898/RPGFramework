package io.github.math0898.rpgframework.enemies.instances;

import io.github.math0898.rpgframework.damage.events.AdvancedDamageEvent;
import io.github.math0898.rpgframework.enemies.ActiveCustomMob;
import io.github.math0898.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitTask;

/**
 * A specific case of the CustomMob that is for Scaldor.
 *
 * @author Sugaku
 */
public class ScaldorBoss extends ActiveCustomMob {

    /**
     * The AI task when the boss is on the field.
     */
    private final BukkitTask aiTask;

    /**
     * The last player to attack Scaldor.
     */
    private Player lastAttacker = null;

    /**
     * Creates a new ActiveCustomMob with the given entity instance.
     *
     * @param entity       The entity to attach to this specific ActiveCustomMob instance.
     * @param namespaceKey The key for the CustomMobEntry inside the MobManager.
     */
    public ScaldorBoss (LivingEntity entity, String namespaceKey) {
        super(entity, namespaceKey);
        aiTask = Bukkit.getScheduler().runTaskTimer(Utils.getPlugin(), this::runAi, 20L, 20L);
    }

    /**
     * Runs the Ai for Scaldor including decision-making.
     */
    public void runAi () { // todo: Make this managed by ActiveCustomMob, then overriden here.
        if (entity.isDead()) {
            aiTask.cancel();
            return;
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
        if (event.getBasicEvent() instanceof EntityDamageByEntityEvent evp) {
            if (evp.getDamager() instanceof Player player)
                lastAttacker = player;
        }
    }
}
