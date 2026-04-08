package io.github.math0898.rpgframework.damage;

import io.github.math0898.rpgframework.damage.events.AdvancedDamageEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Pure damage calculation service.
 */
public final class DamageCalculator {

    public double calculate(AdvancedDamageEvent event) {
        return calculate(new DamageProfile(
                event.getPhysicalResistance(),
                event.getMagicResistance(),
                event.getDamages(),
                event.getResistances()
        ));
    }

    public double calculate(DamageProfile profile) {
        double totalDamage = 0.0;

        for (DamageType type : DamageType.values()) {
            double scaledDamage = applyBaseResistance(profile, type);
            DamageResistance typeResistance = profile.resistances().getOrDefault(type, DamageResistance.NORMAL);
            totalDamage += applyTypeResistance(scaledDamage, typeResistance);
        }

        return totalDamage;
    }

    private double applyBaseResistance(DamageProfile profile, DamageType type) {
        double rawDamage = profile.damages().getOrDefault(type, 0.0);
        return switch (type.getArchetype()) {
            case MAGIC -> rawDamage * (1.0 - profile.magicResistance());
            case PHYSICAL -> rawDamage * (1.0 - profile.physicalResistance());
        };
    }

    private static double applyTypeResistance(double damage, @NotNull DamageResistance resistance) {
        return switch (resistance) {
            case IMMUNITY -> 0.00;
            case RESISTANCE -> damage * 0.50;
            case NORMAL -> damage;
            case SUSCEPTIBILITY -> damage * 1.50;
            case VULNERABILITY -> damage * 2.00;
        };
    }
}
