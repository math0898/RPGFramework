package io.github.math0898.rpgframework.dungeons;

import io.github.math0898.rpgframework.RPGFramework;
import org.bukkit.ChatColor;
import org.bukkit.Location;

/**
 * A DungeonTileset is a collection of tiles that can be used to generate a dungeon.
 *
 * @author Sugaku
 */
public class DungeonTileset {

    /**
     * Creates a new DungeonTileset by reading from the given file.
     *
     * @param file The filepath to pull this Tileset data from.
     */
    public DungeonTileset (String file) {
        // todo: Implement.
    }

    /**
     * Creates the starting room for this tileset at the given location.
     *
     * @param originPoint The starting point for this tileset.
     */
    public void createStartingRoom (Location originPoint) {
        RPGFramework.console("Created Starting Room", ChatColor.AQUA);
        // todo: Implement.
    }

    /**
     * Generates a dungeon from this tileset with the given generation values.
     */
    public void generate (Location originPoint, int lengthToEnd, double deadEndProb, int maxRooms) {
        RPGFramework.console("Dungeon Generated", ChatColor.GREEN);
        // todo: Implement.
    }
}
