package io.github.math0898.rpgframework.particles;

import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

/**
 * AdvancedParticle is a type of particle made from multiple SubParticles.
 *
 * @author Sugaku
 */
public class AdvancedParticle {

    /**
     * A list of SubParticles that should be spawned when this particle is.
     */
    private final List<SubParticle> particles = new ArrayList<>();

    /**
     * Creates a new AdvancedParticle without any sub-particles.
     */
    public AdvancedParticle () {

    }

    /**
     * Adds a SubParticle to this AdvancedParticle.
     *
     * @param s The SubParticle to add.
     */
    public void addSubParticle (SubParticle s) {
        particles.add(s);
    }

    /**
     * Spawns this particle into the world at the given location.
     *
     * @param loc The location to spawn this particle in at.
     */
    public void spawnParticle (Location loc) {
        final Location locale = loc;
        particles.forEach((p) -> p.spawnParticle(locale));
    }
}
