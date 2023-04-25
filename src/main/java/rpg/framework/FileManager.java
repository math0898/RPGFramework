package sugaku.rpg.framework;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import sugaku.rpg.factions.Faction;
import sugaku.rpg.factions.FactionData;
import sugaku.rpg.framework.classes.Classes;
import sugaku.rpg.framework.players.PlayerManager;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;
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
                FactionData[] norm = {new FactionData(0, Faction.ABYSS), new FactionData(0, Faction.ELEMENTAL)};
                PlayerManager.addUserData(new UserData(norm, p.getUniqueId()));
                save(p);
                console("Created new file for " + p.getName() + "!", ChatColor.GREEN);
            }
        } catch (IOException e) { console("Could not create file for " + p.getName() + ": " + e.getMessage(), ChatColor.RED); }
    }

    /**
     * Unloads the data on the given player.
     * @param p The player who's data should be unloaded.
     */
    public static void unload(Player p) {
        console("Unloading data on " + p.getName());

        save(p);

        PlayerManager.removeUserData(p.getUniqueId());

        console("Data unloaded.", ChatColor.GREEN);
    }

    /**
     * Reads the associated file for the given player. If no file is found it calls init(Player) to create one.
     * @param p The player's file which should be read.
     */
    public static void load(Player p) {
        console("Loading player data for " + p.getName() + ".");

        File file = new File("./plugins/RPG/PlayerData/" + p.getUniqueId());
        try {
            Scanner scanner = new Scanner(file);

            switch (scanner.nextLine()) {
                case "1.0": reader1_0(p, scanner);
                case "1.1": reader1_1(p, scanner);
                case "1.2": reader1_2(p, scanner);
            }

            scanner.close();

            console("Loaded player data for " + p.getName() + ".", ChatColor.GREEN);
        } catch (Exception e) {
            console("File not found for " + p.getName() + ".", ChatColor.YELLOW);
            init(p);
        }
    }

    /**
     * Translates the data in the scanner into a UserData object for Player p.
     * @param p The player who we're making the UserData for
     * @param s The scanner which holds the data dump
     */
    private static void reader1_0 (Player p, Scanner s) {
        ArrayList<FactionData> data = new ArrayList<>();

        while(s.hasNextLine()) {
            String faction = s.nextLine();
            int rep = s.nextInt();
            s.nextLine();
            if (faction.equals("Abyss")) data.add(new FactionData(rep, Faction.ABYSS));
            else if (faction.equals("Elemental")) data.add(new FactionData(rep, Faction.ELEMENTAL));
        }

        PlayerManager.addUserData(new UserData(data.toArray(new FactionData[0]), p.getUniqueId()));
    }

    /**
     * Translates the data in the scanner into a UserData object for Player p.
     * @param p The player who we're making the UserData for.
     * @param s The scanner which holds the data dump.
     */
    private static void reader1_1 (Player p, Scanner s) {
        ArrayList<FactionData> data = new ArrayList<>();

        s.nextLine();
        data.add(new FactionData(Integer.parseInt(s.nextLine()), Faction.ABYSS));
        s.nextLine();
        data.add(new FactionData(Integer.parseInt(s.nextLine()), Faction.ELEMENTAL));
        Objects.requireNonNull(PlayerManager.getPlayer(p.getUniqueId())).joinClass(Classes.fromString(s.nextLine()));

        PlayerManager.addUserData(new UserData(data.toArray(new FactionData[0]), p.getUniqueId()));
    }

    /**
     * Translate the data in the scanner into a UserData object for Player p. Also loads data like class levels.
     *
     * @param p The player who we're making the UserData for.
     * @param s The scanner which holds the data dump.
     */
    private static void reader1_2 (Player p, Scanner s) {
        ArrayList<FactionData> data = new ArrayList<>();

        s.nextLine();
        data.add(new FactionData(Integer.parseInt(s.nextLine()), Faction.ABYSS));
        s.nextLine();
        data.add(new FactionData(Integer.parseInt(s.nextLine()), Faction.ELEMENTAL));
        Objects.requireNonNull(PlayerManager.getPlayer(p.getUniqueId())).joinClass(Classes.fromString(s.nextLine()));

        int[] a = new int[]{Integer.parseInt(s.nextLine()),
                Integer.parseInt(s.nextLine()),
                Integer.parseInt(s.nextLine()),
                Integer.parseInt(s.nextLine()),
                Integer.parseInt(s.nextLine()),
                Integer.parseInt(s.nextLine()),
                Integer.parseInt(s.nextLine())};

        PlayerManager.addUserData(new UserData(data.toArray(new FactionData[0]), p.getUniqueId(), a));
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
            FileWriter writer = new FileWriter("./plugins/RPG/PlayerData/" + uuid);
            UserData save = null;
            for (UserData d: PlayerManager.getUserData()) if (d.getUuid() == uuid) { save = d; break; }
            if (save == null) { console("Unable to find " + name + "'s data among loaded data.", ChatColor.RED); return; }

            writer.write("1.1\n");
            writer.write("Abyss\n");
            writer.write(save.getAbyssData().getReputation() + "\n");
            writer.write("Elemental\n");
            writer.write(save.getElementalData().getReputation() + "\n");
            writer.write(Objects.requireNonNull(PlayerManager.getPlayer(uuid)).getCombatClassString() + "\n");

//            int[] a = Objects.requireNonNull(PlayerManager.getUserData(uuid)).getXp();
//            for (int i = 0; i < a.length; i++) writer.write(a[i]);
            writer.close();

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
