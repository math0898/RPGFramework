package io.github.math0898.rpgframework;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import io.github.math0898.rpgframework.classes.Classes;
import sugaku.rpg.framework.players.RpgPlayer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * The DataManager wrangles, loads, saves, and manages player data on the disk. This includes but is not limited to exp,
 * faction rating, and current class.
 *
 * @author Sugaku
 */
public class DataManager {

    /**
     * Prints the given string into console as ChatColor.GRAY.
     *
     * @param message The message to be sent to console
     */
    @Deprecated
    private void console (String message) {
        console(message, ChatColor.GRAY);
    }

    /**
     * Prints the given string into console with the given coloring.
     * @param message The message be the sent to console
     * @param color The color the message should be in
     */
    @Deprecated
    private void console (String message, ChatColor color) {
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_GREEN + "RPG" + ChatColor.DARK_GRAY + "] [" + ChatColor.LIGHT_PURPLE + "IO" + ChatColor.DARK_GRAY + "] " + color + message);
    }

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
                long experiencePoints = yaml.getLong("experience", 0);
                console("File version: " + version);
                console("Class: " + classString);
                console("Experience: " + experiencePoints);
                // todo: This should use the new RpgPlayer objects.
                sugaku.rpg.framework.players.RpgPlayer rpgPlayer = sugaku.rpg.framework.players.PlayerManager.getPlayer(player.getUuid());
                rpgPlayer.joinClass(Classes.fromString(classString));
                rpgPlayer.setExperience(experiencePoints);
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
        console("Version: 2.0");
        toSave.set("version", "2.0");
        // todo: Use new RpgPlayer object.
        String classString = sugaku.rpg.framework.players.PlayerManager.getPlayer(player.getUuid()).getCombatClass().toString();
        console("Class: " + classString);
        toSave.set("class", classString);
        long xp = sugaku.rpg.framework.players.PlayerManager.getPlayer(player.getUuid()).getExperience();
        console("Experience: " + xp);
        toSave.set("experience", xp);
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
