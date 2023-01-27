package io.github.math0898.rpgframework.particles;

import org.bukkit.Location;

/**
 * The ParticleManager is used to help manage and wrangle all the different particle types that may be present at
 * runtime. Its responsibility is to serve as a database of particles.
 *
 * @author Sugaku
 */
public class ParticleManager {

    /**
     * The single instance of the ParticleManager.
     */
    private static ParticleManager instance = null;

    /**
     * Creates a new ParticleManager. This will result in reading the local particles saved.
     */
    private ParticleManager () {
        // TODO: Implement me!!
    }

    /**
     * Accessor method for the single instance of the ParticleManager.
     *
     * @return The ParticleManager instance being used at runtime.
     */
    public static ParticleManager getInstance () {
        if (instance == null) instance = new ParticleManager();
        return instance;
    }

    /**
     * Spawns the given particle at the given location if it's present in the manager. Otherwise, does nothing.
     *
     * @param particle The name of the particle to spawn.
     * @param locale   The location to spawn the particle at.
     */
    public void spawnParticle (String particle, Location locale) {
        if (locale.getWorld() == null) return;
        // TODO: Implement me!!!
    }
}
