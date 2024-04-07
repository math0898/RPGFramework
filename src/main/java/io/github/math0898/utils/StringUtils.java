package io.github.math0898.utils;

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
     *
     * @param string The string to convert from hex codes.
     * @return The string using Minecraft's legacy color formatting instead of hex colors.
     */
    public static String convertHexCodes (String string) {
        Pattern pattern = Pattern.compile("#(\\d)(\\d)(\\d)(\\d)(\\d)(\\d)");
        Matcher matcher = pattern.matcher(string);
        return matcher.replaceAll("§x§$1§$2§$3§$4§$5§$6");
    }
}
