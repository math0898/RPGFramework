package io.github.math0898.rpgframework.classes;

import org.bukkit.ChatColor;

/**
 * The Classes class is an enum list of the classes currently implemented, and planned.
 *
 * @author Sugaku
 */
public enum Classes { // TODO: Plan GLADIATOR,

    /**
     * The assassin is a mobile melee dps.
     */
    ASSASSIN(ChatColor.BLUE + "Assassin"),

    /**
     * The bard is an aura casting support class.
     */
    BARD(ChatColor.LIGHT_PURPLE + "Bard"),

    /**
     * The berserker class is a melee dps tank hybrid.
     */
    BERSERKER(ChatColor.RED + "Berserker"),

    /**
     * A healer, and a tank, the paladin is a powerful front-liner.
     */
    PALADIN(ChatColor.YELLOW + "Paladin"),

    /**
     * The pyromancer is a master of fire magics.
     */
    PYROMANCER(ChatColor.GOLD + "Pyromancer"),

    /**
     * The marksmen class is a class of powerful archers.
     */
    //MARKSMEN(ChatColor.DARK_GREEN + "Marksmen"), // todo: Requires an implementing class.

    /**
     * The none class represents both 'class missing' and 'no class assigned'.
     */
    NONE(ChatColor.WHITE + "None");

    /**
     * The formatted string version of this enum value.
     */
    private final String formatted;

    /**
     * Creates a new Classes enum entry.
     *
     * @param formatted The formatted string version of this enum value.
     */
    Classes (String formatted) {
        this.formatted = formatted;
    }

    /**
     * An accessor method to the formatted text version of this class.
     *
     * @return The formatted name of this class.
     */
    public String getFormattedName () {
        return  formatted;
    }

    /**
     * An accessor method for a text version of this class.
     *
     * @return The name of this class without any color formatting.
     */
    public String getName () {
        char[] str = this.toString().toLowerCase().toCharArray();
        str[0] = Character.toUpperCase(str[0]);
        return new String(str);
    }

    /**
     * Checks for the correct Classes enum value based on the given string.
     *
     * @param string The string to convert to a Classes enum value.
     * @return Either the class named in the string, or {@link Classes#NONE}.
     */
    public static Classes fromString (String string) {
        Classes[] values = Classes.values();
        for (Classes c : values)
            if (string.equalsIgnoreCase(c.toString()))
                return c;
        return Classes.NONE;
    }
}
