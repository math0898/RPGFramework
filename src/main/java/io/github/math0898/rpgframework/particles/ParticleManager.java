package io.github.math0898.rpgframework.particles;

import org.bukkit.Location;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
     * The master map of particles present in this ParticleManager.
     */
    private final Map<String, AdvancedParticle> particles;

    /**
     * Creates a new ParticleManager. This will result in reading the local particles saved.
     */
    private ParticleManager () {
        particles = new HashMap<>();
        try {
            loadParticles();
        } catch (IOException exception) {

        }
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
     * A utility function for loading all the particle files on the disk.
     */
    private void loadParticles () throws IOException {
        File dir = new File("./plugins/RPGFramework/particles");
        if (!dir.exists()) if (!dir.mkdirs()) throw new IOException("Failed to create containing dir.");
        File[] candidates = dir.listFiles();
        if (candidates == null) return;
        for (File f : candidates) {

        }
    }

    /**
     * Spawns the given particle at the given location if it's present in the manager. Otherwise, does nothing.
     *
     * @param particle The name of the particle to spawn.
     * @param locale   The location to spawn the particle at.
     */
    public void spawnParticle (String particle, Location locale) {
        if (locale.getWorld() == null) return;
        AdvancedParticle p = particles.get(particle);
        if (p != null) p.spawnParticle(locale);
    }
}
