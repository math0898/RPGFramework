package io.github.math0898.rpgframework;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import io.github.math0898.rpgframework.classes.Classes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import static io.github.math0898.utils.Utils.*;

/**
 * The DataManager wrangles, loads, saves, and manages player data on the disk. This includes but is not limited to exp,
 * faction rating, and current class.
 *
 * @author Sugaku
 */
public class DataManager {

    /**
     * The active DataManager instance.
     */
    private static DataManager instance = null;

    /**
     * Creates a new DataManager.
     */
    private DataManager () {
        console("Checking for required directories.");
        File container = new File("./plugins/RPG/");
        if (!Files.exists(Paths.get(container.getPath()))) {
            if (container.mkdir()) console("Top level directory created.");
            else {
                console("Could not create top level directory.", ChatColor.RED);
                return;
            }
        }
        File playerData = new File("./plugins/RPG/PlayerData");
        if (!Files.exists(Paths.get(playerData.getPath()))) {
            if(playerData.mkdir()) console("Directory for storing player data created.");
            else {
                console("Could not create directory to hold player data.", ChatColor.RED);
                return;
            }
        }
        console("Required directories found or created.", ChatColor.GREEN);
    }

    /**
     * Accessor method for the active DataManager instance.
     *
     * @return The active DataManager instance.
     */
    public static DataManager getInstance () {
        if (instance == null) instance = new DataManager();
        return instance;
    }

    /**
     * Reads the associated file for the given player, if present.
     *
     * @param player The player's file which should be read.
     */
    @Deprecated
    public void load (Player player) {
        PlayerManager.getPlayer(player.getUniqueId());
    }

    /**
     * Reads the associated file for the given player, if present.
     *
     * @param player The player's file which should be read.
     */
    public void load (RpgPlayer player) {
        console("Loading player data for " + player.getName() + ".");
        File file = new File("./plugins/RPG/PlayerData/" + player.getUuid());
        if (file.exists()) {
            try {
                YamlConfiguration yaml = new YamlConfiguration();
                yaml.load(file);
                String version = yaml.getString("version");
                String classString = yaml.getString("class", "NONE");
                console("File version: " + version);
                console("Class: " + classString);
                // todo: This should use the new RpgPlayer objects.
                sugaku.rpg.framework.players.PlayerManager.getPlayer(player.getUuid()).joinClass(Classes.fromString(classString));
                List<String> collectedArtifacts = yaml.getStringList("artifacts");
                console("Artifacts: ");
                for (String s : collectedArtifacts)
                    console(" > " + s);
                player.addCollectedArtifacts(collectedArtifacts);
                console("Loaded.", ChatColor.GREEN);
            } catch (Exception exception) {
                console("Failed to load data for " + player.getName() + ": " + exception.getMessage());
            }
        } else console("Data not found.", ChatColor.YELLOW);
    }

    /**
     * Saves the player's data into their file.
     *
     * @param p The player who's data we're saving.
     */
    @Deprecated
    public void save (Player p) {
        save(PlayerManager.getPlayer(p.getUniqueId()));
    }

    /**
     * Saves the player's data with the associated UUID.
     *
     * @param u The uuid of the player's data we're saving.
     */
    @Deprecated
    public void save (UUID u) {
        save(PlayerManager.getPlayer(u));
    }

    /**
     * Saves the player's data into their file. Such as their current class, exp, faction data, etc.
     *
     * @param player The player to save.
     */
    public void save (RpgPlayer player) {
        if (player == null) return;
        console("Saving data for " + player.getName() + ".");
        YamlConfiguration toSave = new YamlConfiguration();
        console("Version: 2.1");
        toSave.set("version", "2.1");
        // todo: Use new RpgPlayer object.
        String classString = sugaku.rpg.framework.players.PlayerManager.getPlayer(player.getUuid()).getCombatClass().toString();
        console("Class: " + classString);
        toSave.set("class", classString);
        List<String> collectedArtifacts = player.getArtifactCollection();
        console("Artifacts: ");
        for (String s : collectedArtifacts)
            console(" > " + s);
        toSave.set("artifacts", collectedArtifacts);
        try {
            File file = new File("./plugins/RPG/PlayerData/" + player.getUuid());
            if (file.exists()) file.delete();
            toSave.save(file);
        } catch (IOException exception) {
            console("Failed to save data for " + player.getName() + ": " + exception.getMessage(), ChatColor.RED);
        }
        console("Data saved!", ChatColor.GREEN);
    }
}
