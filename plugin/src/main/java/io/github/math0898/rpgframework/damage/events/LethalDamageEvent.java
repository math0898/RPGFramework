package io.github.math0898.rpgframework.damage.events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;

/**
 * This is an event that is fired when an entity takes lethal damage as determined by the RPG plugin.
 *
 * @author Sugaku
 */
public class LethalDamageEvent extends EntityEvent implements Cancellable { // todo: Refactor Classes and PlayerManager to use

    /**
     * The AdvancedDamageEvent that caused this LethalDamageEvent.
     */
    private final AdvancedDamageEvent advancedDamageEvent;

    /**
     * The canceled boolean. True if and only if the event has been cancelled otherwise false.
     */
    private boolean cancelled = false;

    /**
     * The list of handlers for this event.
     */
    private static final HandlerList HANDLERS = new HandlerList();

    /**
     * Creates a non-degenerate LethalDamageEvent.
     *
     * @param advancedDamageEvent The AdvancedDamageEvent that is causing this LethalDamageEvent to fire.
     */
    public LethalDamageEvent (AdvancedDamageEvent advancedDamageEvent) {
        super(advancedDamageEvent.getEntity());
        this.advancedDamageEvent = advancedDamageEvent;
    }

    /**
     * An accessor method for the underlying AdvancedDamageEvent that triggered this.
     *
     * @return The AdvancedDamageEvent that triggered this LethalDamageEvent.
     */
    public AdvancedDamageEvent getAdvancedDamageEvent () {
        return advancedDamageEvent;
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
