package io.github.math0898.rpgframework.enemies.bosses;

import org.bukkit.event.Listener;

/**
 * The BossManager handles bosses. That is reading them from files, special events, boss drops, and summoning.
 *
 * @author Sugaku
 */
public class BossManager implements Listener {

    /**
     * The BossManager instance to be used by everything.
     */
    private static final BossManager bossManager = new BossManager();

    /**
     * Default constructor that is private to prevent the creation of other BossManager instances.
     */
    private BossManager () {

    }

    /**
     * Gets the single BossManager to be used.
     *
     * @return The single BossManager instance.
     */
    public static BossManager getInstance () {
        return bossManager;
    }
}
