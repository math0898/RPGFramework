package io.github.math0898.junitTests;

import io.github.math0898.rpgframework.damage.DamageArchetype;
import io.github.math0898.rpgframework.damage.DamageType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DamageTypeTest {

    @Test
    void archetypeAccessorsRemainConsistent() {
        assertEquals("PHYSICAL", DamageType.archetype(DamageType.IMPACT));
        assertEquals("MAGIC", DamageType.archetype(DamageType.FIRE));
        assertEquals(DamageArchetype.PHYSICAL, DamageType.SLASH.getArchetype());
        assertEquals(DamageArchetype.MAGIC, DamageType.HOLY.getArchetype());
    }

    @Test
    void isPhysicalReflectsTypeArchetype() {
        assertTrue(DamageType.UNSPECIFIED.isPhysical());
        assertFalse(DamageType.ICE.isPhysical());
    }
}
