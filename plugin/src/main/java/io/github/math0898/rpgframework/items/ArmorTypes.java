package io.github.math0898.rpgframework.items;

/**
 * The types of armor that exists in RPG.
 *
 * @author Sugaku
 */
public enum ArmorTypes {

    /**
     * Light armor is for nimble classes such as Berserker and Assassin.
     */
    LIGHT("#3FB74ALight Armor"),

    /**
     * Medium armor is for general classes that don't mind much of anything for armor.
     */
    MEDIUM("#3AE5E5Medium Armor"),

    /**
     * Heavy armor is used for heavy, strong bruiser classes.
     */
    HEAVY("#EC5254Heavy Armor");

    /**
     * The formatted name of this Armor type.
     */
    private final String formattedName;

    /**
     * Creates a new ArmorType with the given formatted name.
     *
     * @param formatted The name of this ArmorType when it is formatted.
     */
    ArmorTypes (String formatted) {
        formattedName = formatted;
    }

    /**
     * An accessor method to the formatted name of this armor type.
     *
     * @return The formatted name of this armor type.
     */
    public String getFormattedName () {
        return formattedName;
    }
}
