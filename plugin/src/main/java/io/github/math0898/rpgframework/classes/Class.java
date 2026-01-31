package io.github.math0898.rpgframework.classes;

import io.github.math0898.rpgframework.damage.DamageModifier;
import org.bukkit.event.player.PlayerInteractEvent;
import io.github.math0898.rpgframework.RpgPlayer;
import org.bukkit.inventory.EquipmentSlot;

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
     * @return Whether a death should be respected or not.
     */
    boolean onDeath ();

    /**
     * Checks whether this player is wearing the armor type for their class or not.
     */
    boolean correctArmor ();

    /**
     * Checks a specific slot to see if this player is wearing their class armor or not.
     *
     * @param slot The slot to check to see if it is the correct armor type.
     */
    boolean correctArmor (EquipmentSlot slot);
}
