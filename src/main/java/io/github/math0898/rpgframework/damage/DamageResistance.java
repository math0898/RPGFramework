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
    VULNERABILITY;

    /**
     * Merges two damage resistance levels into one damage resistance level.
     *
     * @param resistance1 The first resistance.
     * @param resistance2 The second resistance.
     * @return The merged resistance.
     */
    public static DamageResistance mergeResistances (DamageResistance resistance1, DamageResistance resistance2) {
        int i = getInt(resistance1);
        int j = getInt(resistance2);
        return getResistance(i + j);
    }

    /**
     * Returns the int value of the enum entry.
     *
     * @param resistance The resistance level being converted.
     * @return The int value of the resistance.
     */
    public static int getInt (DamageResistance resistance) {
        return switch (resistance) {
            case IMMUNITY -> -2;
            case RESISTANCE -> -1;
            case NORMAL -> 0;
            case SUSCEPTIBILITY -> 1;
            case VULNERABILITY -> 2;
        };
    }

    /**
     * Returns the enum value of the int.
     *
     * @param integer The integer being converted to a damage resistance.
     * @return The enum value of the integer.
     */
    public static DamageResistance getResistance (int integer) {
        if (integer <= -2) return DamageResistance.IMMUNITY;
        else if (integer >= 2) return DamageResistance.VULNERABILITY;
        return switch (integer) {
            case -1 -> DamageResistance.RESISTANCE;
            case 0 -> DamageResistance.NORMAL;
            case 1 -> DamageResistance.SUSCEPTIBILITY;
            default ->
                    //There's nothing to do. We've handled (-\infinity, \infinity)
                    null;
        };
    }
}
