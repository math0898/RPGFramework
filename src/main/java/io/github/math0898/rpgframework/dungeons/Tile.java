package io.github.math0898.rpgframework.dungeons;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * A Tile is a single block or room in a generated dungeon.
 *
 * @author Sugaku
 */
public class Tile {

    /**
     * A list of points and blocks that need to be set for this tile.
     */
    private final List<BlockPlacement> blocks = new ArrayList<>();

    /**
     * Creates a new Tile with the given YamlConfiguration section.
     *
     * @param section The configuration section that defines this tile.
     */
    public Tile (ConfigurationSection section) {
        Set<String> mats = section.getKeys(false);
        for (String m : mats) {
            Material material = Material.valueOf(m);
            Set<String> coordinates = section.getConfigurationSection(m).getKeys(false);
            for (String b : coordinates)
                blocks.add(new BlockPlacement(material, section.getConfigurationSection(m + "." + b)));
        }
    }

    /**
     * Places this tile into the world.
     *
     * @param originPoint The point to place this tile at.
     */
    public void generate (Location originPoint) {
        World world = originPoint.getWorld();
        for (BlockPlacement placement : blocks) {
            Location toUpdate = new Location(originPoint.getWorld(), // We create a new location since it appears Location#add() mutates the original object.
                    originPoint.getX() + placement.dx(),
                    originPoint.getY() + placement.dy(),
                    originPoint.getZ() + placement.dz());
            world.getBlockAt(toUpdate).setType(placement.mat());
        }
    }
}
