package io.github.math0898.rpgframework.classes.abilities;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

/**
 * The abstract class for an ability. Abilities are active or passive effects that occur on classes.
 *
 * @author Sugaku
 */
public abstract class Ability implements Listener {

    /**
     * The cooldown length.
     */
    private final long cooldown;

    /**
     * The system time that the ability was last cast at.
     */
    private Long castTime = null;

    /**
     * Gets the difference between the current time and the cast time to determine how long since the last cast.
     *
     * @return The time left in seconds until the next cast can occur.
     */
    public int getCooldown () {
        if (castTime == null) return -1;
        return (int) ((cooldown - (System.currentTimeMillis() - castTime) * 1000));
    }

    /**
     * The default constructor for an Ability which for the most part only needs a cooldown length.
     *
     * @param cooldown The duration before this ability can be used a second time.
     */
    public Ability (long cooldown) {
        this.cooldown = cooldown;
    }

    /**
     * Attempts to cast the ability. If the cast turns successful returns true and false if it fails for some reason.
     * Handles setting the latest castTime to the time when this method was called.
     *
     * @return True if and only if the ability can be cast.
     */
    public boolean castAttempt () {
        if (getCooldown() <= 0) {
            castTime = System.currentTimeMillis();
            return true;
        }
        return false;
    }

    /**
     * Sends a message to the player detailing that the ability is still on cooldown as well as how long until it is off
     * cooldown.
     *
     * @param player The player to send the remaining cooldown time to.
     */
    public void onCooldown (Player player) {
        player.sendMessage("That ability is on cooldown for " + (getCooldown()/1000) +"s longer!");
    }
}
