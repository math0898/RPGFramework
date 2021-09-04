package io.github.math0898.rpgframework.damage;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.Map;

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
        event.setDamage(event.getDamage() * 5.0); //Scale damage for the Advanced Damage Calculations
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

        event.setDamage(damageCalculation(advancedDamageEvent));
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
}
