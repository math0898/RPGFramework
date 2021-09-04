package io.github.math0898.rpgframework.damage;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityEvent;

import java.util.EnumMap;
import java.util.Map;

/**
 * This method is used to overhaul damage in vanilla minecraft. It is called as an event at the lowest priority of
 * any EntityDamage event that has not yet been canceled. After all handlers have handled this event the damage of the
 * EntityDamage event is adjusted.
 *
 * For the sake of this event health is scaled x 5.0 so the base max health of a player along with most mobs is 100.00.
 *
 * @author Sugaku
 */
public class AdvancedDamageEvent extends EntityEvent implements Cancellable {

    /**
     * A map holding all the damage values for this event.
     */
    private Map<DamageType, Double> damages = new EnumMap<>(DamageType.class);

    /**
     * A map holding all the damage resistance values for this event.
     */
    private Map<DamageType, DamageResistance> resistances = new EnumMap<>(DamageType.class);

    /**
     * The victim's resistance to magic damage.
     */
    private double magicResistance = 0;

    /**
     * The victim's resistance to physical damage.
     */
    private double physicalResistance = 0;

    /**
     * The entity damaged event. DO NOT MODIFY.
     */
    private final EntityDamageEvent basic;

    /**
     * The canceled boolean. True if and only if the event has been cancelled otherwise false.
     */
    private boolean cancelled = false;

    /**
     * The list of handlers for this event.
     */
    private static final HandlerList HANDLERS = new HandlerList();

    /**
     * Creates a non-degenerate AdvancedDamageEvent.
     *
     * @param basic The entity damaged event.
     */
    public AdvancedDamageEvent (EntityDamageEvent basic) {
        super(basic.getEntity());
        this.basic = basic;
        for (DamageType t: DamageType.values()) {
            damages.put(t, 0.00);
            resistances.put(t, DamageResistance.NORMAL);
        }
    }

    /**
     * Gets the EntityDamageEvent. This should not be modified. Damage scaling is kind of weird and the framework
     * handles it.
     *
     * @return The EntityDamageEvent this event is expanding on.
     */
    public EntityDamageEvent getBasicEvent () {
        return basic;
    }

    /**
     * Gets the Map of current damage values. The Map includes the values of damage indexed by their type of damage. You
     * should see {@link DamageType} for a list of damage types.
     *
     * @return The damages Map associated with this AdvancedDamageEvent.
     */
    public Map<DamageType, Double> getDamages () {
        return damages;
    }

    /**
     * Sets the Map of current damage values. Any damage value information currently stored in the event will be
     * overridden. Read the current values using {@link #getDamages()}. Here's the damage types: {@link DamageType}.
     *
     * @param damages The new map of damages.
     */
    public void setDamages (Map<DamageType, Double> damages) {
        this.damages = damages;
    }

    /**
     * Gets the Map of current damage resistances. The Map includes the level of resistance indexed by their damage
     * type. To use this you may want to see {@link DamageType} and {@link DamageResistance}.
     *
     * @return The resistance Map associated with this AdvancedDamageEvent.
     */
    public Map<DamageType, DamageResistance> getResistances () {
        return resistances;
    }

    /**
     * Sets the Map of current damage resistances. Any resistance level information currently stored in the event will
     * be overridden. Read the current values using {@link #getResistances()}. It might be worth checking out
     * {@link DamageType} along with {@link DamageResistance}.
     *
     * @param resistances The new map of resistances.
     */
    public void setResistances (Map<DamageType, DamageResistance> resistances) {
        this.resistances = resistances;
    }

    /**
     * Gets the currently stored magic resistance.
     *
     * @return The current magic resistance of the victim in this event.
     */
    public double getMagicResistance () {
        return magicResistance;
    }

    /**
     * Sets the currently stored magic resistance for the victim in this event.
     *
     * @param resistance The new magic resistance to be used.
     */
    public void setMagicResistance (double resistance) {
        this.magicResistance = resistance;
    }

    /**
     * Gets the currently stored physical resistance.
     *
     * @return The current physical resistance of the victim in this event.
     */
    public double getPhysicalResistance () {
        return physicalResistance;
    }

    /**
     * Sets the currently stored physical resistance for the victim in this event.
     *
     * @param resistance The new physical resistance to be used.
     */
    public void setPhysicalResistance (double resistance) {
        this.physicalResistance = resistance;
    }

    /**
     * Gets the cancellation state of this event. A cancelled event will not
     * be executed in the server, but will still pass to other plugins
     *
     * @return true if this event is cancelled
     */
    @Override
    public boolean isCancelled () {
        return cancelled;
    }

    /**
     * Sets the cancellation state of this event. A cancelled event will not
     * be executed in the server, but will still pass to other plugins.
     *
     * @param cancel true if you wish to cancel this event
     */
    @Override
    public void setCancelled (boolean cancel) {
        cancelled = cancel;
    }

    /**
     * Inherited from event. Simply returns handlers.
     *
     * @return The list of handlers for this event.
     */
    @Override
    public HandlerList getHandlers () {
        return HANDLERS;
    }

    /**
     * To make Spigot happy I think.
     *
     * @return The list of handlers for this event.
     */
    @SuppressWarnings("All")
    public static HandlerList getHandlerList () {
        return HANDLERS;
    }
}
