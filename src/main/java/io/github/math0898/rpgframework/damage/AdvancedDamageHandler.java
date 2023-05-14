package io.github.math0898.rpgframework.damage;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import io.github.math0898.rpgframework.main;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.Map;
import java.util.Random;

/**
 * This is the end all be all for the AdvancedDamageEvent and where the actual damage happens. It also has the side job
 * of changing normal damage events into advanced ones.
 *
 * @author Sugaku
 */
public class AdvancedDamageHandler implements Listener {

    /**
     * Called at the very end of DamageEvents to display the damage dealt.
     *
     * @param event The event to display.
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onDamageDisplay (EntityDamageEvent event) {
        if (main.useHolographicDisplays) displayDamage(event.getDamage() * 5.00, event.getEntity().getLocation());
    }

    /**
     * Where the conversion from DamageEvent to AdvancedDamageEvent occurs. Any handlers after this point will be
     * ignored since the values are already grabbed.
     *
     * @param event The damage event.
     */
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onDamage (EntityDamageEvent event) {
        event.setDamage(event.getDamage() * 5.00); //Scale damage for the Advanced Damage Calculations
        AdvancedDamageEvent advancedDamageEvent = new AdvancedDamageEvent(event);

        Map<DamageType, Double> damages = advancedDamageEvent.getDamages(); //Setting default damage distributions
        switch (event.getCause()) {
            case LIGHTNING -> damages.replace(DamageType.ELECTRIC, event.getDamage());
            case FIRE, LAVA, FIRE_TICK, HOT_FLOOR -> damages.replace(DamageType.FIRE, event.getDamage());
            case FALL, CONTACT, FALLING_BLOCK, FLY_INTO_WALL, ENTITY_EXPLOSION, BLOCK_EXPLOSION
                    -> damages.replace(DamageType.IMPACT, event.getDamage());
            case FREEZE -> damages.replace(DamageType.ICE, event.getDamage());
            case PROJECTILE -> damages.replace(DamageType.PUNCTURE, event.getDamage());
            case THORNS -> damages.replace(DamageType.NATURE, event.getDamage());
            case VOID -> damages.replace(DamageType.VOID, event.getDamage());
            default -> damages.replace(DamageType.UNSPECIFIED, event.getDamage());
        }
        advancedDamageEvent.setDamages(damages);

        Bukkit.getPluginManager().callEvent(advancedDamageEvent); //Call the event

        double damage = damageCalculation(advancedDamageEvent);
        event.setDamage(damage/5.00);
    }

    /**
     * Determines the amount of damage that needs to be applied to the player based on resistances and types of armor.
     *
     * @param advancedDamageEvent The advancedDamageEvent we're calculating the result of.
     * @return The damage that should be dealt to the victim.
     */
    public static double damageCalculation (AdvancedDamageEvent advancedDamageEvent) {
        double damage = 0.00;
        Map<DamageType, Double> damages = advancedDamageEvent.getDamages();
        Map<DamageType, DamageResistance> resistance = advancedDamageEvent.getResistances();
        for (DamageType type: damages.keySet()) {
            double dmg = damages.get(type);
            if (DamageType.archetype(type).equalsIgnoreCase("MAGIC")) dmg = dmg * (1.00 - advancedDamageEvent.getMagicResistance());
            else if (DamageType.archetype(type).equalsIgnoreCase("PHYSICAL")) dmg = dmg * (1.00 - advancedDamageEvent.getPhysicalResistance());
            switch (resistance.get(type)) {
                case IMMUNITY -> dmg = 0.00;
                case RESISTANCE -> dmg = dmg * 0.50;
                case SUSCEPTIBILITY -> dmg = dmg * 1.50;
                case VULNERABILITY -> dmg = dmg * 2.00;
            }
            damage += dmg;
        }
        return damage;
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
        Hologram hologram = HologramsAPI.createHologram(main.plugin, locale);
        hologram.appendTextLine(ChatColor.RED + "☆" + ChatColor.YELLOW + String.format("%.1f", damage) + ChatColor.RED + "☆");
        Bukkit.getScheduler().runTaskLater(main.plugin, hologram::delete, 5*10);
    }
}
