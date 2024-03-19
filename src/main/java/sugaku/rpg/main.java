package sugaku.rpg;

import io.github.math0898.rpgframework.RPGFramework;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import sugaku.rpg.framework.CommandManager;
import sugaku.rpg.framework.FileManager;
import sugaku.rpg.framework.classes.implementations.Pyromancer;
import sugaku.rpg.framework.items.ItemsManager;
import sugaku.rpg.framework.RPGEventListener;
import sugaku.rpg.mobs.teir1.eiryeras.EiryerasBoss;
import sugaku.rpg.mobs.teir1.feyrith.FeyrithBoss;

/**
 * The main class which describes the RPG plugin.
 */
public final class main {

    /**
     * A pointer to the plugin during runtime. Used for scheduling and a few other things.
     */
    public static JavaPlugin plugin;

    /**
     * Prints the given string into console with gray coloring and the [RPG] prefix.
     * @param message The message to be sent to console.
     */
    public static void console(String message) { console(message, ChatColor.GRAY); }

    /**
     * Prints the given string into console with the given color and the [RPG] prefix.
     * @param message The message to be sent to console.
     * @param color The color the message should be.
     */
    public static void console(String message, ChatColor color) { Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_GREEN + "RPG" + ChatColor.DARK_GRAY + "] " + color + message); }

    /**
     * Returns the given string in dark gray brackets. Lots of formatting use this.
     * @param s The string to be enclosed.
     */
    public static String brackets(String s) { return ChatColor.DARK_GRAY + "[" + s + ChatColor.DARK_GRAY + "]"; }

    /**
     * onEnable() is called when the server initializes the RPG plugin. I've further broken it down into a few steps
     * which each have the appropriate managers called.
     */
    public static void onEnable() {
        plugin = RPGFramework.plugin;
        //Loading
        console("Loading Sugaku's RPG Plugin...");

        //Files
        FileManager.setup();

        //Commands
        CommandManager.setup(plugin);

        //Registering events TODO: Move this somewhere?
        Bukkit.getPluginManager().registerEvents(new RPGEventListener(), plugin);
        Bukkit.getPluginManager().registerEvents(new EiryerasBoss(), plugin);
        Bukkit.getPluginManager().registerEvents(new Pyromancer(), plugin);
        Bukkit.getPluginManager().registerEvents(new FeyrithBoss(), plugin);

        //Other TODO: Add console to Item Manager
        ItemsManager.init();


        //Loaded
        console("RPG loaded successfully!", ChatColor.GREEN);
    }

    /**
     * onDisable() is called when the server starts shutting down and tells the RPG plugin its time to clean up. As a
     * mostly runtime plugin there isn't a ton to do here other than save player data.
     */
    public void onDisable() {
        console("Tearing down...");

        //Files
        FileManager.saveAll();

        console("Tear down successful!", ChatColor.GREEN);
    }

    /**
     * This is the standard disclaimer for players who are using beta features. Includes a message to console to log
     * that beta actions are being taken place.
     * @param p The player who is being sent the warning.
     */
    public static void beta(Player p) {
        p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_GREEN + "RPG" + ChatColor.DARK_GRAY + "] " + ChatColor.RED + "This system is currently in development. We are not responsible for lost items or future buffs/nerfs.");
        console("Player: " + p.getName() + ". Is using beta things.", ChatColor.YELLOW);
    }
}
