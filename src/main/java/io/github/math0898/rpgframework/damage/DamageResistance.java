package io.github.math0898.rpgframework.damage;

/**
 * This enum describes the levels of resistance to a particular kind of damage.
 *
 * @author Sugaku
 */
public enum DamageResistance {

    /**
     * Nullifies all damage.
     */
    IMMUNITY,

    /**
     * Halves the damage taken.
     */
    RESISTANCE,

    /**
     * Applies no modifier to damage taken.
     */
    NORMAL,

    /**
     * Increases the damage taken by 50% for a total of 150%.
     */
    SUSCEPTIBILITY,

    /**
     * Doubles the damage taken.
     */
    VULNERABILITY
}
