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
    IMMUNITY(-2),

    /**
     * Halves the damage taken.
     */
    RESISTANCE(-1),

    /**
     * Applies no modifier to damage taken.
     */
    NORMAL(0),

    /**
     * Increases the damage taken by 50% for a total of 150%.
     */
    SUSCEPTIBILITY(1),

    /**
     * Doubles the damage taken.
     */
    VULNERABILITY(2);

    private final int severity;

    DamageResistance (int severity) {
        this.severity = severity;
    }

    /**
     * Merges two damage resistance levels into one damage resistance level.
     *
     * @param resistance1 The first resistance.
     * @param resistance2 The second resistance.
     * @return The merged resistance.
     */
    public static DamageResistance mergeResistances (DamageResistance resistance1, DamageResistance resistance2) {
        return fromSeverity(resistance1.severity + resistance2.severity);
    }

    /**
     * Returns the int value of the enum entry.
     *
     * @param resistance The resistance level being converted.
     * @return The int value of the resistance.
     */
    public static int getInt (DamageResistance resistance) {
        return resistance.severity;
    }

    /**
     * Returns the enum value of the int.
     *
     * @param integer The integer being converted to a damage resistance.
     * @return The enum value of the integer.
     */
    public static DamageResistance getResistance (int integer) {
        return fromSeverity(integer);
    }

    private static DamageResistance fromSeverity (int severity) {
        if (severity <= IMMUNITY.severity) return IMMUNITY;
        if (severity >= VULNERABILITY.severity) return VULNERABILITY;

        return switch (severity) {
            case -1 -> RESISTANCE;
            case 0 -> NORMAL;
            case 1 -> SUSCEPTIBILITY;
            default -> throw new IllegalStateException("Unhandled resistance severity: " + severity);
        };
    }
}
