package io.github.math0898.rpgframework;

import com.google.common.primitives.UnsignedLong;
import org.bukkit.configuration.ConfigurationSection;

/**
 * An ItemCollection contains the score that a given player has for a specific resource.
 *
 * @author Sugaku
 */
public class ItemCollection {

    /**
     * The value of this collection.
     */
    private UnsignedLong value;

    /**
     * Creates a new ItemCollection with the given value.
     *
     * @param val The value to assign to this ItemCollection.
     */
    public ItemCollection (long val) {
        value = UnsignedLong.valueOf(val);
    }

    /**
     * Creates a new ItemCollection from the given configuration section.
     *
     * @param sec The configuration section to create this ItemCollection from.
     */
    public ItemCollection (ConfigurationSection sec) {
        if (sec != null)
            if (sec.contains("value"))  {
                value = UnsignedLong.valueOf(sec.getLong("value", 0));
                return;
            }
        value = UnsignedLong.ZERO;
    }

    /**
     * Assigns the necessary information to the given ConfigurationSection to reconstruct this object.
     *
     * @param sec The configuration section to assign data to.
     */
    public void toConfigurationSection (ConfigurationSection sec) {
        sec.set("value", value.longValue());
    }

    /**
     * Adds the given value to the ItemCollection.
     *
     * @param val The value to add onto this ItemCollection.
     */
    public void add (long val) {
        value = value.plus(UnsignedLong.valueOf(val));
    }

    /**
     * Returns the value stored in this ItemCollection.
     *
     * @return The value stored in this ItemCollection.
     */
    public long get () {
        return value.longValue();
    }
}
