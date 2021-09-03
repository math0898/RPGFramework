package io.github.math0898.rpgframework.damage;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

/**
 * This is the end all be all for the AdvancedDamageEvent and where the actual damage happens. It also has the side job
 * of changing normal damage events into advanced ones.
 *
 * @author Sugaku
 */
public class AdvancedDamageHandler implements Listener {

    /**
     * Where the conversion from DamageEvent to AdvancedDamageEvent occurs. Any handlers after this point will be
     * ignored since the values are already grabbed.
     *
     * @param event The damage event.
     */
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onDamage (EntityDamageEvent event) {
        Bukkit.getConsoleSender().sendMessage("Started damage.");
        //todo real creation of a new AdvancedDamageEvent
        AdvancedDamageEvent advancedDamageEvent = new AdvancedDamageEvent(event.getEntity());
        Bukkit.getPluginManager().callEvent(advancedDamageEvent);
        //todo: do damage calculations here?
        Bukkit.getConsoleSender().sendMessage("Finished damage.");
    }

    /**
     * The lowest priority event handler for the AdvancedDamageEvent. Any handlers which end up after this are ignored.
     *
     * @param event The advanced damage event.
     */
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onAdvancedDamage (AdvancedDamageEvent event) {
        //todo implement
        Bukkit.getConsoleSender().sendMessage("Finished Advanced Damage.");
    }
}
