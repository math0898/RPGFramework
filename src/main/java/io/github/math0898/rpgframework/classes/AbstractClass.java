package io.github.math0898.rpgframework.classes;

import io.github.math0898.rpgframework.damage.AdvancedDamageEvent;
import io.github.math0898.rpgframework.damage.AdvancedDamageHandler;
import org.bukkit.Material;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import io.github.math0898.rpgframework.Cooldown;
import io.github.math0898.rpgframework.RpgPlayer;
import org.bukkit.inventory.EquipmentSlot;

import java.util.ArrayList;
import java.util.List;

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
     * A list of items that are considered class items.
     */
    private final List<Material> classItems = new ArrayList<>();

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
        if (cooldowns[i].isComplete())
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

    /**
     * Sets the class items for this class so that {@link #isClassItem(Material)} can be used.
     *
     * @param mats The materials to add as class items.
     */
    protected void setClassItems (Material... mats) {
        if (mats != null)
            for (Material m : mats)
                if (m != null)
                    classItems.add(m);
    }

    /**
     * Checks whether the given item is a class item or not.
     *
     * @param mat The material to check whether it is a class item.
     * @return The index of the class item in the ClassItems array. -1 If it is not a class item.
     */
    protected int isClassItem (Material mat) {
        return classItems.indexOf(mat);
    }

    @Override
    @Deprecated
    public void damaged (EntityDamageEvent event) {
        AdvancedDamageEvent advancedDamageEvent = new AdvancedDamageEvent(event);
        damaged(new AdvancedDamageEvent(event));
        if (advancedDamageEvent.isCancelled()) event.setCancelled(true);
        event.setDamage(AdvancedDamageHandler.damageCalculation(advancedDamageEvent) / 5.0);
    }

    @Override
    @Deprecated
    public void attack (EntityDamageByEntityEvent event) {
        AdvancedDamageEvent advancedDamageEvent = new AdvancedDamageEvent(event);
        attack(new AdvancedDamageEvent(event));
        if (advancedDamageEvent.isCancelled()) event.setCancelled(true);
        event.setDamage(AdvancedDamageHandler.damageCalculation(advancedDamageEvent)  / 5.0);
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
     * @return Whether a death should be respected or not.
     */
    @Override
    public boolean onDeath () {
        return true;
    }

    /**
     * Checks whether this player is wearing the armor type for their class or not.
     *
     * @return True if the player is wearing an entire suit of the right armor.
     */
    @Override
    public boolean correctArmor () {
        return correctArmor(EquipmentSlot.HEAD)
                && correctArmor(EquipmentSlot.CHEST)
                && correctArmor(EquipmentSlot.LEGS)
                && correctArmor(EquipmentSlot.FEET);
    }

    /**
     * Checks a specific slot to see if this player is wearing their class armor or not.
     *
     * @param slot The slot to check to see if it is the correct armor type.
     * @return True if the player is wearing the correct armor for that slot.
     */
    @Override
    public boolean correctArmor (EquipmentSlot slot) {
        return true;
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
