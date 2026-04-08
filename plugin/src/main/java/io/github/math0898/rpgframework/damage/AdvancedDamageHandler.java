package io.github.math0898.rpgframework.damage;

import io.github.math0898.rpgframework.RPGFramework;
import io.github.math0898.rpgframework.damage.events.AdvancedDamageEvent;
import io.github.math0898.rpgframework.damage.events.LethalDamageEvent;
import io.github.math0898.rpgframework.damage.events.VerifiedDeathEvent;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;

/**
 * This is the end all be all for the AdvancedDamageEvent and where the actual damage happens. It also has the side job
 * of changing normal damage events into advanced ones.
 *
 * @author Sugaku
 */
public class AdvancedDamageHandler implements Listener {
    private static final DamageCalculator DAMAGE_CALCULATOR = new DamageCalculator();

    /**
     * This is an internal counter to prevent hologram name collisions.
     */
    private final AtomicLong hologramCount = new AtomicLong();

    /**
     * Where the conversion from DamageEvent to AdvancedDamageEvent occurs. Any handlers after this point will be
     * ignored since the values are already grabbed.
     *
     * @param event The damage event.
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onDamage (EntityDamageEvent event) {
        AdvancedDamageEvent advancedDamageEvent = new AdvancedDamageEvent(event);
        Bukkit.getPluginManager().callEvent(advancedDamageEvent); //Call the event

        if (advancedDamageEvent.isCancelled()) {
            event.setCancelled(true);
            return;
        }

        double damage = damageCalculation(advancedDamageEvent);
        event.setDamage(damage / 5.00);

        if (RPGFramework.useHolographicDisplays || RPGFramework.useDecentHolograms) {
            displayDamage(damage, event.getEntity().getLocation());
        }

        if (!(event.getEntity() instanceof LivingEntity living)) {
            return;
        }

        if (living.getHealth() > event.getFinalDamage()) {
            return;
        }

        LethalDamageEvent lethalDamageEvent = new LethalDamageEvent(advancedDamageEvent);
        Bukkit.getPluginManager().callEvent(lethalDamageEvent);
        if (lethalDamageEvent.isCancelled()) {
            event.setCancelled(true);
            return;
        }

        VerifiedDeathEvent verifiedDeathEvent = new VerifiedDeathEvent(advancedDamageEvent);
        Bukkit.getPluginManager().callEvent(verifiedDeathEvent);
    }

    /**
     * Determines the amount of damage that needs to be applied to the player based on resistances and types of armor.
     *
     * @param advancedDamageEvent The advancedDamageEvent we're calculating the result of.
     * @return The damage that should be dealt to the victim.
     */
    public static double damageCalculation (AdvancedDamageEvent advancedDamageEvent) {
        return DAMAGE_CALCULATOR.calculate(advancedDamageEvent);
    }

    /**
     * Displays the damage dealt to the entity nearby so that players can see big numbers.
     *
     * @param damage The amount of damage being displayed.
     * @param location The location of the entity that was hit.
     */
    public void displayDamage (Double damage, Location location) {
        Random random = new Random();
        Location locale = new Location(location.getWorld(), location.getX() + random.nextDouble() - 0.50,
                location.getY() + 1.50,
                location.getZ() + random.nextDouble() - 0.50);
        String text = ChatColor.RED + "☆" + ChatColor.YELLOW + String.format("%.1f", damage) + ChatColor.RED + "☆";
        try {
            if (RPGFramework.useHolographicDisplays) {
                spawnHolographicDisplaysHologram(locale, text);
            }
            if (RPGFramework.useDecentHolograms) {
                String name = "rpgframeworkdamage" + hologramCount.getAndAdd(1L);
                spawnDecentHologramsHologram(name, locale, text);
            }
        } catch (ReflectiveOperationException | NoClassDefFoundError error) {
            RPGFramework.getInstance().getLogger().log(Level.WARNING, error.getMessage());
        }
    }

    private void spawnHolographicDisplaysHologram(Location location, String text) throws ReflectiveOperationException {
        Class<?> apiClass = Class.forName("com.gmail.filoghost.holographicdisplays.api.HologramsAPI");
        Object hologram = apiClass
                .getMethod("createHologram", org.bukkit.plugin.Plugin.class, Location.class)
                .invoke(null, RPGFramework.plugin, location);
        hologram.getClass().getMethod("appendTextLine", String.class).invoke(hologram, text);
        Bukkit.getScheduler().runTaskLater(RPGFramework.plugin, () -> {
            try {
                hologram.getClass().getMethod("delete").invoke(hologram);
            } catch (ReflectiveOperationException ignored) {
            }
        }, 5 * 10L);
    }

    private void spawnDecentHologramsHologram(String name, Location location, String text) throws ReflectiveOperationException {
        Class<?> dhApiClass = Class.forName("eu.decentsoftware.holograms.api.DHAPI");
        dhApiClass
                .getMethod("createHologram", String.class, Location.class, java.util.List.class)
                .invoke(null, name, location, java.util.List.of(text));
        Bukkit.getScheduler().runTaskLater(RPGFramework.plugin, () -> {
            try {
                dhApiClass.getMethod("removeHologram", String.class).invoke(null, name);
            } catch (ReflectiveOperationException ignored) {
            }
        }, 5 * 10L);
    }
}
