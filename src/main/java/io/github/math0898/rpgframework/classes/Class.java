package io.github.math0898.rpgframework.classes;

import io.github.math0898.rpgframework.damage.DamageModifier;
import org.bukkit.event.player.PlayerInteractEvent;
import io.github.math0898.rpgframework.RpgPlayer;

/**
 * The Class interfaces all the required components of a class.
 *
 * @author Sugaku
 */
public interface Class extends DamageModifier {

    /**
     * A public reference to the player that this class is encapsulating.
     */
    RpgPlayer player = null;

    /**
     * Called once every 20 seconds to apply a passive effect to the player.
     */
    void passive ();

    /**
     * Called whenever the player attached to this class object interacts with the world.
     *
     * @param event The player interact event.
     */
    void onInteract (PlayerInteractEvent event);

    /**
     * Called when the class user has 'died'.
     *
     * @return Whether the death should be respected or not.
     */
    boolean onDeath ();
}
