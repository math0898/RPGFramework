package io.github.math0898.rpgframework.damage;

import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;

import java.util.EnumMap;
import java.util.Map;

/**
 * This method is used to overhaul damage in vanilla minecraft. It is handled often by other plugins in the RPG suite
 * and then executed here.
 *
 * @author Sugaku
 */
public class AdvancedDamageEvent extends EntityEvent implements Cancellable {

    /**
     * A map holding all the damage values for this event.
     */
    private Map<DamageType, Double> damages = new EnumMap<>(DamageType.class); //todo allow manipulation

    /**
     * The canceled boolean. True if and only if the event has been cancelled otherwise false.
     */
    private boolean cancelled = false;

    /**
     * The list of handlers for this event.
     */
    private static final HandlerList HANDLERS = new HandlerList();

    //todo real constructors that'll let me do what I want

    /**
     * Degenerate constructor. Exists to make IDE's happy.
     *
     * @param what The entity in the event.
     */
    public AdvancedDamageEvent (Entity what) {
        super(what);
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
    public static HandlerList getHandlerList () {
        return HANDLERS;
    }
}
