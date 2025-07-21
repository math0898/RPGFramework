package io.github.math0898.rpgframework.dungeons;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

/**
 * A BlockPlacement is used to store the relative coordinates along with block material in a single object.
 *
 * @param mat The material that should be placed at this location.
 * @param dx  The x offset of this placement from the start location.
 * @param dy  The y offset of this placement from the start location.
 * @param dz  The z offset of this placement from the start location.
 * @author Sugaku
 */
public record BlockPlacement (Material mat, double dx, double dy, double dz) {

    /**
     * Creates a BlockPlacement with the given material and configuration section.
     *
     * @param mat     The material for this BlockPlacement.
     * @param section The section to pull location data from.
     */
    public BlockPlacement (Material mat, ConfigurationSection section) {
        this(mat, section.getInt("x"), section.getInt("y"), section.getInt("z"));
    }
}
