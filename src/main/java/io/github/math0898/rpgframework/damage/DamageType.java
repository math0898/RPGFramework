package io.github.math0898.rpgframework.damage;

/**
 * This enum describes all the damage types which is used in advanced damage calculations.
 *
 * @author Sugaku
 */
public enum DamageType {
    UNSPECIFIED, // Damage from an unspecified source

    SLASH, PUNCTURE, IMPACT, // The three physical types

    FIRE, AIR, WATER, EARTH, ELECTRIC, NATURE, // Basic elements

    ABYSS, ENDER, VOID // Advanced elements
}
