package io.github.math0898.rpgframework.classes;

import io.github.math0898.rpgframework.damage.AdvancedDamageEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import io.github.math0898.rpgframework.Cooldown;
import io.github.math0898.rpgframework.RpgPlayer;

/**
 * The AbstractClass implements the basics of what makes up a class, and some protected utility methods, to make
 * specific implementations easier.
 *
 * @author Sugaku
 */
public abstract class AbstractClass implements Class, sugaku.rpg.framework.classes.Class {

    /**
     * The RpgPlayer that this AbstractClass is referencing.
     */
    private final RpgPlayer player;

    /**
     * An array of cooldowns.
     */
    private Cooldown[] cooldowns; // todo: Each implementing class could use a self-defined enum with ability names to index.

    /**
     * Creates a new AbstractClass object which is specific to the given player.
     *
     * @param p The player this class is specific to.
     */
    public AbstractClass (RpgPlayer p) {
        player = p;
    }

    /**
     * An accessor method to get the RpgPlayer that this class references.
     *
     * @return The RpgPlayer that this class references.
     */
    public RpgPlayer getPlayer() {
        return player;
    }

    /**
     * Sets all the cooldowns that will be part of this AbstractClass.
     *
     * @param cooldowns The array of cooldowns that are part of this class.
     */
    protected void setCooldowns (Cooldown[] cooldowns) {
        this.cooldowns = cooldowns;
    }

    /**
     * An accessor method for the entire cooldown array.
     *
     * @return The cooldown array that is a member of this class.
     */
    protected Cooldown[] getCooldowns () {
        return cooldowns;
    }

    /**
     * A helpful utility method that checks if a cooldown is finished and sends a message if it still is on cooldown.
     *
     * @param i The index of the cooldown we're interested in.
     * @return True if the ability is off cooldown.
     */
    protected boolean offCooldown (int i) {
        if (player == null) return true; // Not sure how we got here.
        if (getCooldowns()[i].getRemaining() <= 0)
            return true;
        send("That ability is on cooldown for another " +  getCooldowns()[i].getRemaining() + "s.");
        return false;
    }

    /**
     * A helpful utility method to send a message within the AbstractClass instead of on the RpgPlayer object.
     *
     * @param message The message to send to the player.
     */
    protected void send (String message) {
        if (player == null) return;
        player.sendMessage(message);
    }

    @Override
    public void damaged (EntityDamageEvent event) {
        damaged(new AdvancedDamageEvent(event));
    }

    @Override
    public void attack (EntityDamageByEntityEvent event) {
        attack(new AdvancedDamageEvent(event));
    }

    /**
     * Called once every 20 seconds to apply a passive effect to the player.
     */
    @Override
    public void passive () {

    }

    /**
     * Called whenever the player attached to this class object interacts with the world.
     *
     * @param event The player interact event.
     */
    @Override
    public void onInteract (PlayerInteractEvent event) {

    }

    /**
     * Called when the class user has 'died'.
     *
     * @return Whether an effect nullifies the death or not.
     */
    @Override
    public boolean onDeath () {
        return false;
    }

    /**
     * Called whenever this DamageModifier is relevant on a defensive front.
     *
     * @param event The AdvancedDamageEvent to consider.
     */
    @Override
    public void damaged (AdvancedDamageEvent event) {

    }

    /**
     * Called whenever this DamageModifier is relevant on an offensive front.
     *
     * @param event The AdvancedDamageEvent to consider.
     */
    @Override
    public void attack (AdvancedDamageEvent event) {

    }
}
