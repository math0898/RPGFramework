package io.github.math0898.rpgframework.dungeons;

import io.github.math0898.rpgframework.RPGFramework;
import io.github.math0898.rpgframework.RpgPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
     * A list of DungeonTilesets that are known to this DungeonMaster.
     */
    private final List<DungeonTileset> dungeonTilesets;

    /**
     * A list of active dungeons.
     */
    private final List<Dungeon> dungeons;

    /**
     * Creates a new DungeonManager object.
     */
    private DungeonManager () {
        dungeonTilesets = new ArrayList<>();
        dungeonTilesets.add(new DungeonTileset("BLANK_TILESET"));
        dungeons = new ArrayList<>();
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
     *
     * @param rpgPlayer The player starting a dungeon to play.
     */
    public void joinDungeon (RpgPlayer rpgPlayer) {
        List<RpgPlayer> players = new ArrayList<>();
        if (rpgPlayer.getParty() != null)
            players.addAll(rpgPlayer.getParty().getRpgPlayers()); // The passed player should be part of this collection.
        else players.add(rpgPlayer); // todo: Check that none of these players are currently in an existing dungeon.

        DungeonTileset randomTileset = dungeonTilesets.get(new Random().nextInt(0, dungeonTilesets.size()));
        Dungeon dungeon = new Dungeon(players, new Location(Bukkit.getWorld("world"), -574, 65, -485), randomTileset); // todo: Create and algorithm to actually determine starting location.
        dungeons.add(dungeon);
        RPGFramework.console("Created Dungeon", ChatColor.AQUA);
    }
}
