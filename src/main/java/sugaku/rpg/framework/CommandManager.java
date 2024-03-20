package sugaku.rpg.framework;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import sugaku.rpg.commands.*;

import java.util.Objects;

public class CommandManager {

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
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_GREEN + "RPG" + ChatColor.DARK_GRAY + "] [" + ChatColor.LIGHT_PURPLE + "CMD" + ChatColor.DARK_GRAY + "] " + color + message);
    }

    /**
     * Sets up commands for use. Called on enabled.
     */
    public static void setup(JavaPlugin m){
        console("Setting up command executors.");
        Objects.requireNonNull(m.getCommand("irpg")).setExecutor(new Commandirpg("irpg"));
        console("Command executors setup.", ChatColor.GREEN);

        console("Setting up tab completion,");
        Objects.requireNonNull(m.getCommand("irpg")).setTabCompleter(new Commandirpg("irpg").autocomplete);
        console("Tab completion setup.", ChatColor.GREEN);
    }
}
