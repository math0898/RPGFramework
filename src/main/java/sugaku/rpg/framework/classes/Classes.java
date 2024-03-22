package sugaku.rpg.framework.classes;

import org.bukkit.ChatColor;

public enum Classes {

    ASSASSIN, BARD, BERSERKER, PALADIN, PYROMANCER, MARKSMEN, GLADIATOR, NONE;

    public static ChatColor classColor(Classes c) {
        switch(c) {
            case PYROMANCER: return ChatColor.GOLD;
            case BARD: return ChatColor.LIGHT_PURPLE;
            case PALADIN: return ChatColor.YELLOW;
            case ASSASSIN:  return ChatColor.BLUE;
            case MARKSMEN: return ChatColor.DARK_GREEN;
            case BERSERKER: return ChatColor.RED;
            case GLADIATOR: return ChatColor.DARK_AQUA;
            case NONE: return ChatColor.WHITE;
        }
        return ChatColor.WHITE;
    }

    public static Classes fromString (String c) {
        String str = c.toLowerCase();
        return switch (str) {
            case "assassin" -> Classes.ASSASSIN;
            case "bard" -> Classes.BARD;
            case "berserker" -> Classes.BERSERKER;
            case "paladin" -> Classes.PALADIN;
            case "pyromancer" -> Classes.PYROMANCER;
            default -> Classes.NONE;
        };
    }

    /**
     * Converts the given class into an integer. Used in RpgPlayer to index the classes levels array.
     *
     * @param c The class to convert.
     * @return The integer of the class.
     */
    public static int toInt(Classes c) {
        switch (c) {
            case ASSASSIN: return 0;
            case BARD: return 1;
            case BERSERKER: return 2;
            case PALADIN: return 3;
            case PYROMANCER: return 4;
            case MARKSMEN: return 5;
            case GLADIATOR: return 6;
        }
        return -1;
    }
}