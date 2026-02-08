package io.github.math0898.utils;

import org.bukkit.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * String utils is used to helpfully modify strings to conform to Minecraft's weird standards.
 *
 * @author Sugaku
 */
public class StringUtils { // todo: Builder Pattern and Allows Placeholders.

    /**
     * Converts hex codes into Minecraft's legacy color formatting.
     * Works for item display, lore, and mob names.
     *
     * @param string The string to convert from hex codes.
     * @return The string using Minecraft's legacy color formatting instead of hex colors.
     */
    public static String convertHexCodes (String string) {
        Pattern pattern = Pattern.compile("#([0-9a-fA-F])([0-9a-fA-F])([0-9a-fA-F])([0-9a-fA-F])([0-9a-fA-F])([0-9a-fA-F])");
        Matcher matcher = pattern.matcher(string);
        return matcher.replaceAll("§x§$1§$2§$3§$4§$5§$6");
    }

    /**
     * Converts the given String into a properly capitalized one.
     *
     * @param string The string to capitalize.
     * @return The capitalize string.
     */
    public static String capitalize (String string) {
        char[] tmp = string.toLowerCase().replace("_", " ").replace("-", " ").toCharArray();
        tmp[0] = Character.toUpperCase(tmp[0]);
        for (int i = 1; i < tmp.length; i++)
            if (tmp[i] == ' ')
                if (i < tmp.length - 1)
                    tmp[i + 1] = Character.toUpperCase(tmp[i + 1]);
        return new String(tmp);
    }

    /**
     * Returns the given string in dark gray brackets. Lots of formatting use this.
     *
     * @param s The string to be enclosed.
     * @return The given string enclosed in brackets.
     */
    public static String brackets (String s) {
        return ChatColor.DARK_GRAY + "[" + s + ChatColor.DARK_GRAY + "]";
    }
}
