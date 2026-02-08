package io.github.math0898.studio;

/**
 * Rarity determines the colors and relative difficulty to obtain items, mobs, and bosses.
 *
 * @author Sugaku
 */
public enum Rarities {

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
    Rarities (String color) {
        this.color = color;
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
