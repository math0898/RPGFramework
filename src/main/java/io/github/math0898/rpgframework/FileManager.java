package io.github.math0898.rpgframework;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import sugaku.rpg.framework.players.PlayerManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

public class FileManager {

    /**
     * Prints the given string into console as ChatColor.GRAY.
     * @param message The message to be sent to console
     */
    private static void console(String message) {
        console(message, ChatColor.GRAY);
    }

    /**
     * Prints the given string into console with the given coloring.
     * @param message The message be the sent to console
     * @param color The color the message should be in
     */
    private static void console(String message, ChatColor color) {
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_GREEN + "RPG" + ChatColor.DARK_GRAY + "] [" + ChatColor.LIGHT_PURPLE + "IO" + ChatColor.DARK_GRAY + "] " + color + message);
    }

    /**
     * Checks if the needed folders exist for RPG to function properly. If they do not exist they are created. Called
     * during enable.
     */
    public static void setup() {
        console("Checking for required directories.");

        File container = new File("./plugins/RPG/");
        if (!Files.exists(Paths.get(container.getPath()))) {
            if (container.mkdir()) console("Top level directory created.");
            else { console("Could not create top level directory.", ChatColor.RED); return; }
        }

        File playerData = new File("./plugins/RPG/PlayerData");
        if (!Files.exists(Paths.get(playerData.getPath()))) {
            if(playerData.mkdir()) console("Directory for storing player data created.");
            else { console("Could not create directory to hold player data.", ChatColor.RED); return; }
        }

        console("Required directories found or created.", ChatColor.GREEN);
    }

    /**
     * Initializes and creates a player data folder for the given player.
     * @param p The player a file should be made for.
     */
    public static void init(Player p) {
        console("Creating player data for " + p.getName() + ".");

        File file = new File("./plugins/RPG/PlayerData/" + p.getUniqueId());

        try {
            file.delete();
            if(file.createNewFile()) {
                PlayerManager.addUserData(new UserData(p.getUniqueId()));
                save(p);
                console("Created new file for " + p.getName() + "!", ChatColor.GREEN);
            }
        } catch (IOException e) { console("Could not create file for " + p.getName() + ": " + e.getMessage(), ChatColor.RED); }
    }

    /**
     * Unloads the data on the given player.
     * @param p The player who's data should be unloaded.
     */
    public static void unload (Player p) {
        console("Unloading data on " + p.getName());

        save(p);

        PlayerManager.removeUserData(p.getUniqueId());

        console("Data unloaded.", ChatColor.GREEN);
    }

    /**
     * Reads the associated file for the given player. If no file is found it calls init(Player) to create one.
     * @param p The player's file which should be read.
     */
    public static void load (Player p) {
        console("Loading player data for " + p.getName() + ".");

        File file = new File("./plugins/RPG/PlayerData/" + p.getUniqueId());
        UserData data = new UserData(p.getUniqueId());
        try {
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
            ConfigurationSection collections = config.getConfigurationSection("collections");
            if (collections != null)
                for (String s : collections.getKeys(false))
                    data.addCollection(s, new ItemCollection(collections.getConfigurationSection(s))); // TODO: should this be part of UserData?
            PlayerManager.addUserData(data);
            console("Loaded player data for " + p.getName() + ".", ChatColor.GREEN);
        } catch (Exception e) {
            console("File not found for " + p.getName() + ".", ChatColor.YELLOW);
            init(p);
        }
    }

    /**
     * Saves the player's data into their file.
     * @param p The player who's data we're saving.
     */
    public static void save(Player p) { save(p.getName(), p.getUniqueId()); }

    /**
     * Saves the player's data with the associated UUID.
     * @param u The uuid of the player's data we're saving.
     */
    private static void save(UUID u) { save("Unknown", u); }

    /**
     * Saves the player's data into their file. Such as their current class, standing with factions, and levels of all
     * classes.
     *
     * @param name The name of the player who's data we're saving.
     * @param uuid The uuid of the player who's data we're saving.
     */
    private static void save(String name, UUID uuid) {
        console("Saving data for " + name + ".");

        try {
            UserData data = PlayerManager.getUserData(uuid);
            if (data == null) throw new IOException("Player does not have data!");
            YamlConfiguration config = new YamlConfiguration();
            data.toConfigurationSection(config);
            config.save("./plugins/RPG/PlayerData/" + uuid);
            console("Saved data for " + name + "!", ChatColor.GREEN);

        } catch (IOException e) {
            console("Unable to save data for " + name + ": " + e.getMessage(), ChatColor.RED);
        }
    }

    /**
     * Saves all loaded player files.
     */
    public static void saveAll() {
        console("Saving loaded player data.");

        for (UserData d: PlayerManager.getUserData()) save(d.getUuid());

        console("Player data has been saved.", ChatColor.GREEN);
    }
}
