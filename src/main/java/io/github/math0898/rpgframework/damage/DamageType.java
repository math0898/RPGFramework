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
    UNSPECIFIED,

    // ---- Physical Damages ----

    /**
     * Slashing damage like from a sword. Physical.
     */
    SLASH,

    /**
     * Puncture damage like from an arrow or trident. Physical.
     */
    PUNCTURE,

    /**
     * Impact damage like from any kind of strong blunt force. Physical.
     */
    IMPACT,

    // ---- Elemental Magic ----

    /**
     * Fire damage, like from lava, fire, burns, ya know... fire. Magic.
     */
    FIRE,

    /**
     * Air walks the line of impact to some extent however with an emphasis on it being from a magic source. Magic.
     */
    AIR,

    /**
     * Damage from water magic. Magic.
     */
    WATER,

    /**
     * Earth magic related damage... from well earth magic. Magic.
     */
    EARTH,

    /**
     * Lighting electricity you know it. Magic.
     */
    ELECTRIC,

    /**
     * Likely caused by Dryads and thorns. Magic.
     */
    NATURE,

    /**
     * Ice magic, also related to freezing. Magic.
     */
    ICE,

    // ---- Advanced and Primal Magic ----

    /**
     * Abyss... pure primal abyssal magic. Magic.
     */
    ABYSS,

    /**
     * Ender magic related to the endermen and end. Magic.
     */
    ENDER,

    /**
     * In the absence of everything the only thing that remains... pure void magic.
     */
    VOID;

    /**
     * Checks if the damage type is magic or physical.
     *
     * @param type The damage type being checked.
     * @return Returns the archetype of damage.
     */
    public static String archetype (DamageType type) {
        return switch (type) {
            case VOID, ENDER, ABYSS, ICE, NATURE, ELECTRIC, EARTH, WATER, AIR, FIRE -> "MAGIC";
            case IMPACT, PUNCTURE, SLASH, UNSPECIFIED -> "PHYSICAL";
        };
    }
}
