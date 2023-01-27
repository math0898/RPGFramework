package io.github.math0898.rpgframework.particles;

import org.bukkit.Location;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * ParticleEvents are events where any kind of particle needs to be played.
 *
 * @author Sugaku
 */
public class ParticleEvent extends Event implements Cancellable {

    /**
     * The canceled boolean. True if and only if the event has been cancelled otherwise false.
     */
    private boolean cancelled = false;

    /**
     * The list of handlers for this event.
     */
    private static final HandlerList HANDLERS = new HandlerList();

    /**
     * The String name of the particle to spawn.
     */
    private String particle;

    /**
     * The Location to spawn the particle at.
     */
    private Location locale;

    /**
     * Creates a new ParticleEvent.
     *
     * @param particle The name of the particle the spawn.
     * @param locale   The location to spawn the particle at.
     */
    public ParticleEvent (String particle, Location locale) {
        this.particle = particle;
        this.locale = locale;
    }

    /**
     *
     */

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
    public @NotNull HandlerList getHandlers () {
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
