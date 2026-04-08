package io.github.math0898.junitTests;

import io.github.math0898.rpgframework.damage.DamageCalculator;
import io.github.math0898.rpgframework.damage.DamageProfile;
import io.github.math0898.rpgframework.damage.DamageResistance;
import io.github.math0898.rpgframework.damage.DamageType;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DamageCalculatorTest {

    private final DamageCalculator calculator = new DamageCalculator();

    @Test
    void calculateAppliesArchetypeResistanceAndTypeResistance() {
        Map<DamageType, Double> damages = new EnumMap<>(DamageType.class);
        damages.put(DamageType.SLASH, 30.0);
        damages.put(DamageType.ELECTRIC, 60.0);

        Map<DamageType, DamageResistance> typeResistances = new EnumMap<>(DamageType.class);
        typeResistances.put(DamageType.SLASH, DamageResistance.VULNERABILITY);
        typeResistances.put(DamageType.ELECTRIC, DamageResistance.RESISTANCE);

        DamageProfile profile = new DamageProfile(0.50, 0.33, damages, typeResistances);

        assertEquals(50.1, calculator.calculate(profile), 0.01);
    }

    @Test
    void calculateDefaultsMissingValuesToSafeDefaults() {
        DamageProfile profile = new DamageProfile(0.10, 0.20, Map.of(), Map.of());
        assertEquals(0.0, calculator.calculate(profile));
    }
}
