package io.github.math0898.rpgframework.damage;

/**
 * This enum describes all the damage types which is used in advanced damage calculations.
 *
 * @author Sugaku
 */
public enum DamageType {

    /**
     * Damage from an unspecified source. For the purposes of calculations its considered physical.
     */
    UNSPECIFIED(DamageArchetype.PHYSICAL),

    // ---- Physical Damages ----

    /**
     * Slashing damage like from a sword. Physical.
     */
    SLASH(DamageArchetype.PHYSICAL),

    /**
     * Puncture damage like from an arrow or trident. Physical.
     */
    PUNCTURE(DamageArchetype.PHYSICAL),

    /**
     * Impact damage like from any kind of strong blunt force. Physical.
     */
    IMPACT(DamageArchetype.PHYSICAL),

    // ---- Elemental Magic ----

    /**
     * Fire damage, like from lava, fire, burns, ya know... fire. Magic.
     */
    FIRE(DamageArchetype.MAGIC),

    /**
     * Air walks the line of impact to some extent however with an emphasis on it being from a magic source. Magic.
     */
    AIR(DamageArchetype.MAGIC),

    /**
     * Damage from water magic. Magic.
     */
    WATER(DamageArchetype.MAGIC),

    /**
     * Earth magic related damage... from well earth magic. Magic.
     */
    EARTH(DamageArchetype.MAGIC),

    /**
     * Lighting electricity you know it. Magic.
     */
    ELECTRIC(DamageArchetype.MAGIC),

    /**
     * Likely caused by Dryads and thorns. Magic.
     */
    NATURE(DamageArchetype.MAGIC),

    /**
     * Ice magic, also related to freezing. Magic.
     */
    ICE(DamageArchetype.MAGIC),

    // ---- Advanced and Primal Magic ----

    /**
     * Abyss... pure primal abyssal magic. Magic.
     */
    ABYSS(DamageArchetype.MAGIC),

    /**
     * Ender magic related to the endermen and end. Magic.
     */
    ENDER(DamageArchetype.MAGIC),

    /**
     * In the absence of everything the only thing that remains... pure void magic.
     */
    VOID(DamageArchetype.MAGIC),

    /**
     * Holy magic is well... holy in nature. Magic.
     */
    HOLY(DamageArchetype.MAGIC);

    private final DamageArchetype archetype;

    DamageType(DamageArchetype archetype) {
        this.archetype = archetype;
    }

    /**
     * Checks if the damage type is magic or physical.
     *
     * @param type The damage type being checked.
     * @return Returns the archetype of damage.
     */
    public static String archetype (DamageType type) {
        return type.archetype.name();
    }

    /**
     * Strongly typed archetype accessor.
     *
     * @return The damage archetype for this type.
     */
    public DamageArchetype getArchetype() {
        return archetype;
    }

    /**
     * Returns true if this is a type of physical attack.
     *
     * @return True if this is a physical attack.
     */
    public boolean isPhysical () {
        return archetype == DamageArchetype.PHYSICAL;
    }
}
