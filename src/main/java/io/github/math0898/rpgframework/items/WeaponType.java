package io.github.math0898.rpgframework.items;

/**
 * There a couple of different weapon types that exist in the RPG project.
 *
 * @author Suagku
 */
public enum WeaponType {

    /**
     * Bows allow players to cast attacks from far away.
     */
    BOW("#23A5DBBow"),

    /**
     * Daggers are swords which have an increased attack speed.
     */
    DAGGER("#F454DADagger"),

    /**
     * Swords are the generic melee option.
     */
    SWORD("#53C975Sword"),

    /**
     * Axes are an alternative melee option that deals increased damage at the cost of attack speed.
     */
    AXE("#D93747Axe");

    /**
     * The formatted string that is part of this WeaponType.
     */
    private final String formattedName;

    /**
     * Creates a new WeaponType with the given formatted display name.
     *
     * @param formatted The formatted display name.
     */
    WeaponType (String formatted) {
        formattedName = formatted;
    }

    /**
     * An accessor method to the formatted display.
     *
     * @return The formatted display.
     */
    public String getFormattedName () {
        return formattedName;
    }
}
