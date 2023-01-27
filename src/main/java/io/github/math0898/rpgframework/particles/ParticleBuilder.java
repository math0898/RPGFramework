package io.github.math0898.rpgframework.particles;

import java.io.File;
import java.io.IOException;

/**
 * The ParticleBuilder is a builder which should make the building of Particles significantly easier, and easier to
 * change in the future.
 *
 * @author Sugkua
 */
public class ParticleBuilder {

    /**
     * Creates a new ParticleBuilder with the given file to pull data from.
     *
     * @param file The file to pull data from to make this particle.
     */
    public ParticleBuilder (File file) {
        // TODO: Implement me!!!
    }

    /**
     * Creates an AdvancedParticle using the data stored in the file given at construction.
     *
     * @return A fully created AdvancedParticle.
     */
    public AdvancedParticle build () throws IOException {
        // TODO: Implement me!!
        return new AdvancedParticle();
    }
}
