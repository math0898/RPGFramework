package io.github.math0898.rpgframework;

import io.github.math0898.rpgframework.enemies.LegacyRarity;
import org.bukkit.ChatColor;

/**
 * Rarity determines the colors and relative difficulty to obtain items, mobs, and bosses.
 *
 * @author Sugaku
 */
public enum Rarity {

    /**
     * The various rarities available.
     */
    COMMON, UNCOMMON, RARE, LEGENDARY, HEROIC, MYTHIC;

    /**
     * Converts the rarity into an int value, ascending from common at 1 to mythic at 6. Used to help determine xp drops
     * from bosses.
     *
     * @param r The rarity to convert.
     * @return The integer value of the given rarity.
     */
    public int toInt (Rarity r) {
        switch(r) {
            case COMMON: return 1;
            case UNCOMMON: return 2;
            case RARE: return 3;
            case LEGENDARY: return 4;
            case HEROIC: return 5;
            case MYTHIC: return 6;
        }
        return 0;
    }

    /**
     * Converts the rarity into a ChatColor.
     *
     * @param r The rarity to convert.
     * @return the ChatColor of the given rarity.
     */
    public static ChatColor toColor(Rarity r) {
        switch (r) {
            case COMMON: return ChatColor.WHITE;
            case UNCOMMON: return ChatColor.GREEN;
            case RARE: return ChatColor.BLUE;
            case LEGENDARY: return ChatColor.GOLD;
            case HEROIC: return ChatColor.LIGHT_PURPLE;
            case MYTHIC: return ChatColor.RED;
        }
        return ChatColor.BLACK;
    }
}
