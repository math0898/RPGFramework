package io.github.math0898.rpgframework.particles;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;

import java.util.Random;

import static io.github.math0898.rpgframework.main.plugin;

/**
 * The SubParticle is an individual particle that is spawned when a more advanced effect is played. This class notably
 * uses classes instead of primitive types. This is to allow an additional piece of information to be passed for each
 * value. Namely, null or not null. Null values will be randomized in a short range upon spawn.
 * TODO: Make this range configurable.
 *
 * @author Sugaku
 */
public class SubParticle {

    /**
     * The offset distance of this particle spawn in the x direction.
     */
    private final Double offsetX;

    /**
     * The offset distance of this particle spawn in the y direction.
     */
    private final Double offsetY;

    /**
     * The offset distance of this particle spawn in the z direction.
     */
    private final Double offsetZ;

    /**
     * The type of particle to spawn.
     */
    private final Particle type;

    /**
     * How long to wait before spawning this particle.
     */
    private final Integer delay;

    /**
     * Creates a new SubParticle with the given values. Unassigned values will be randomized within a small range at
     * each spawn.
     *
     * @param offsetX The amount of distance to offset this particle in the x direction.
     * @param offsetY The amount of distance to offset this particle in the y direction.
     * @param offsetZ The amount of distance to offset this particle in the z direction.
     * @param type    The type of particle that should be spawned.
     * @param delay   How long to wait, in ticks, before spawning this particle.
     */
    public SubParticle (Double offsetX, Double offsetY, Double offsetZ, Particle type, Integer delay) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.offsetZ = offsetZ;
        this.type = type;
        this.delay = delay;
    }

    /**
     * Spawns this particle at the given location.
     *
     * @param loc The location to spawn this particle at.
     */
    public void spawnParticle (Location loc) {
        final World w = loc.getWorld();
        if (w == null) return;

        Random rand = new Random();

        /*
         * TODO: These should at some point be made configurable.
         */
        final double X_Y_RAND_OFFSET = 1.0;
        final double Z_RAND_OFFSET = 1.0;
        final int TIME_RAND_OFFSET = 20;

        final double x = loc.getX() + (offsetX == null ? rand.nextDouble() * X_Y_RAND_OFFSET : offsetX);
        final double y = loc.getY() + (offsetY == null ? rand.nextDouble() * X_Y_RAND_OFFSET : offsetY);
        final double z = loc.getZ() + (offsetZ == null ? rand.nextDouble() * Z_RAND_OFFSET : offsetZ);
        final int dt = delay == null ? rand.nextInt(TIME_RAND_OFFSET + 1) : delay;

        Bukkit.getScheduler().runTaskLater(plugin, () -> w.spawnParticle(type, x, y, z, 1), dt);
    }
}
