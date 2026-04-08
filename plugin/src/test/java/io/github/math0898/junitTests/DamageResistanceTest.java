package io.github.math0898.junitTests;

import io.github.math0898.rpgframework.damage.DamageResistance;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DamageResistanceTest {

    @Test
    void mergeResistancesAddsAndClampsSeverity () {
        assertEquals(DamageResistance.IMMUNITY,
                DamageResistance.mergeResistances(DamageResistance.IMMUNITY, DamageResistance.RESISTANCE));
        assertEquals(DamageResistance.RESISTANCE,
                DamageResistance.mergeResistances(DamageResistance.RESISTANCE, DamageResistance.NORMAL));
        assertEquals(DamageResistance.NORMAL,
                DamageResistance.mergeResistances(DamageResistance.RESISTANCE, DamageResistance.SUSCEPTIBILITY));
        assertEquals(DamageResistance.SUSCEPTIBILITY,
                DamageResistance.mergeResistances(DamageResistance.NORMAL, DamageResistance.SUSCEPTIBILITY));
        assertEquals(DamageResistance.VULNERABILITY,
                DamageResistance.mergeResistances(DamageResistance.SUSCEPTIBILITY, DamageResistance.VULNERABILITY));
    }

    @Test
    void getResistanceMapsAndClampsIntegers () {
        assertEquals(DamageResistance.IMMUNITY, DamageResistance.getResistance(-999));
        assertEquals(DamageResistance.IMMUNITY, DamageResistance.getResistance(-2));
        assertEquals(DamageResistance.RESISTANCE, DamageResistance.getResistance(-1));
        assertEquals(DamageResistance.NORMAL, DamageResistance.getResistance(0));
        assertEquals(DamageResistance.SUSCEPTIBILITY, DamageResistance.getResistance(1));
        assertEquals(DamageResistance.VULNERABILITY, DamageResistance.getResistance(2));
        assertEquals(DamageResistance.VULNERABILITY, DamageResistance.getResistance(999));
    }
}
