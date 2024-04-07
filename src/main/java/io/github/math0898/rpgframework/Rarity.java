package io.github.math0898.rpgframework;

import io.github.math0898.utils.StringUtils;
import org.bukkit.ChatColor;

/**
 * Rarity determines the colors and relative difficulty to obtain items, mobs, and bosses.
 *
 * @author Sugaku
 */
public enum Rarity {

    /**
     * Vanilla items are considered common.
     */
    COMMON("#DDDDDD"),

    /**
     * Uncommon items are the introductory level items to RPG.
     */
    UNCOMMON("#53C975"),

    /**
     * Rare items have more powerful bonuses than their uncommon counter-parts.
     */
    RARE("#23A5DB"),

    /**
     * Legendary items have unique effects when stacked together and collected.
     */
    LEGENDARY("#F3B36B"),

    /**
     * Heroic items provide unique effects by themselves.
     */
    HEROIC("#F454DA"),

    /**
     * Mythic items come from gods. They are either entirely cosmetic or their power is unmatched.
     */
    MYTHIC("#D93747");

    /**
     * The hex color contained with in this Rarity.
     */
    private final String color;

    /**
     * Creates a new Rarity with the given hex color.
     *
     * @param color The hex color to assign to this Rarity.
     */
    Rarity (String color) {
        this.color = color;
    }

    /**
     * Converts the rarity into a ChatColor.
     *
     * @param r The rarity to convert.
     * @return the ChatColor of the given rarity.
     */
    @Deprecated
    public static ChatColor toColor(Rarity r) {
        return switch (r) {
            case COMMON -> ChatColor.WHITE;
            case UNCOMMON -> ChatColor.GREEN;
            case RARE -> ChatColor.BLUE;
            case LEGENDARY -> ChatColor.GOLD;
            case HEROIC -> ChatColor.LIGHT_PURPLE;
            case MYTHIC -> ChatColor.RED;
        };
    }

    /**
     * Modifies the given string to include this Rarity's hex color. This also converts the hex code to Minecraft's
     * format.
     *
     * @param str The string to update using this Rarity's hex color.
     * @return The updated string.
     */
    public String modify (String str) {
        return StringUtils.convertHexCodes(color + str);
    }

    /**
     * An accessor method to the hex color contained in this Rarity.
     *
     * @return The hex color contained in this Rarity.
     */
    public String getHexColor () {
        return color;
    }
}
