package io.github.math0898.rpgframework.particles;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import org.bukkit.Particle;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * The ParticleBuilder is a builder which should make the building of Particles significantly easier, and easier to
 * change in the future.
 *
 * @author Sugkua
 */
public class ParticleBuilder { // TODO: Add support for particle data (colors)

    /**
     * The file to read from to create a particle.
     */
    private final File file;

    /**
     * Creates a new ParticleBuilder with the given file to pull data from.
     *
     * @param file The file to pull data from to make this particle.
     */
    public ParticleBuilder (File file) {
        this.file = file;
    }

    /**
     * Creates an AdvancedParticle using the data stored in the file given at construction.
     *
     * @return A fully created AdvancedParticle.
     */
    public AdvancedParticle build () throws IOException {
        try (JsonReader reader = new JsonReader(new FileReader(file))){
            JsonParser parser = new JsonParser();
            JsonObject root = parser.parse(reader).getAsJsonObject();
            AdvancedParticle p = new AdvancedParticle();
            JsonArray subs = root.getAsJsonArray("sub");
            for (JsonElement je : subs) {
                JsonObject obj = je.getAsJsonObject();
                Particle particle = Particle.valueOf(obj.get("type").getAsString());
                p.addSubParticle(new SubParticle(
                        obj.get("dx").getAsDouble(),
                        obj.get("dy").getAsDouble(),
                        obj.get("dz").getAsDouble(),
                        particle,
                        obj.get("delay").getAsInt()
                ));
            }
        }
        return null;
    }
}
