package io.github.math0898.rpgframework.particles;

import io.github.math0898.rpgframework.PlayerManager;
import io.github.math0898.rpgframework.RpgPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

/**
 * This is the main class for the custom hit and kill particle feature.
 *
 * @author Sugaku
 */
public class CustomParticles implements Listener {

    /**
     * Called whenever damage is dealt so the CustomParticles class can add the preferred particles.
     *
     * @param event Any kind of player attacking entity event.
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onHit (EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player player) {
            RpgPlayer rpgPlayer = PlayerManager.getPlayer(player.getUniqueId());
            assert rpgPlayer != null;
            Bukkit.getPluginManager().callEvent(new ParticleEvent(rpgPlayer.getOnHitParticle(), event.getEntity().getLocation()));
            // TODO: Check for critical hits
            // TODO: Check for kills.
        }
    }
}
