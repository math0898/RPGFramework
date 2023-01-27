package io.github.math0898.rpgframework.particles;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

/**
 * The primary handler of particle events.
 *
 * @author Sugaku
 */
public class ParticleHandler implements Listener {

    /**
     * Executes the given ParticleEvent.
     *
     * @param event The ParticleEvent to execute.
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onParticleRequest (ParticleEvent event) {
        ParticleManager.getInstance().spawnParticle(event.getParticle(), event.getLocale());
    }
}
