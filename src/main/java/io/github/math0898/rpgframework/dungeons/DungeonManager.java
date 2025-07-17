package io.github.math0898.rpgframework.dungeons;

import io.github.math0898.rpgframework.RpgPlayer;

/**
 * The dungeon manager handles moving players to and from their dungeons. This includes win detection and dungeon
 * generation among many other things.
 *
 * @author Sugaku
 */
public class DungeonManager {

    /**
     * The active DungeonManager instance.
     */
    private static DungeonManager dungeonManager;

    /**
     * Creates a new DungeonManager object.
     */
    private DungeonManager () {
        // todo: Load tile sets from drive.
    }

    /**
     * Accessor method for the DungeonManager singleton instance.
     */
    public static DungeonManager getInstance () {
        if (dungeonManager == null) dungeonManager = new DungeonManager();
        return dungeonManager;
    }

    /**
     * Sends the given player, and their party, to a new dungeon that is in the process of being built.
     */
    public void joinDungeon (RpgPlayer rpgPlayer) {
        // todo: Implement.
    }
}
