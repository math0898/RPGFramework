package io.github.math0898.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The LoreMaster helps to correctly colorize proper nouns in the universe. This is particularly important for item
 * descriptions.
 *
 * @author Sugaku
 */
public class LoreMaster {

    /**
     * A list of all the proper nouns known to the LoreMaster.
     */
    private final List<ProperNoun> nouns = new ArrayList<>();

    /**
     * The active LoreMaster instance.
     */
    private static LoreMaster instance;

    /**
     * Creates a new LoreMaster instance.
     */
    private LoreMaster () {
        // todo: Implement.
    }

    /**
     * Replaces the proper nouns in the given string.
     *
     * @param string       The string to replace proper nouns in.
     * @param returnColor  The hex color to return to after an insertion.
     */
    public void apply (String string, String returnColor) {

    }
}
