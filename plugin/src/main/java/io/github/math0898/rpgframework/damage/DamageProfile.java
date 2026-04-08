package io.github.math0898.rpgframework.damage;

import java.util.EnumMap;
import java.util.Map;

/**
 * Immutable input object used by damage calculation logic.
 */
public record DamageProfile(
        double physicalResistance,
        double magicResistance,
        Map<DamageType, Double> damages,
        Map<DamageType, DamageResistance> resistances
) {

    public DamageProfile {
        damages = copyDamages(damages);
        resistances = copyResistances(resistances);
    }

    private static Map<DamageType, Double> copyDamages(Map<DamageType, Double> input) {
        Map<DamageType, Double> copy = new EnumMap<>(DamageType.class);
        for (DamageType type : DamageType.values()) {
            copy.put(type, input.getOrDefault(type, 0.0));
        }
        return Map.copyOf(copy);
    }

    private static Map<DamageType, DamageResistance> copyResistances(Map<DamageType, DamageResistance> input) {
        Map<DamageType, DamageResistance> copy = new EnumMap<>(DamageType.class);
        for (DamageType type : DamageType.values()) {
            copy.put(type, input.getOrDefault(type, DamageResistance.NORMAL));
        }
        return Map.copyOf(copy);
    }
}
