package io.github.math0898.rpgframework;

import io.github.math0898.rpgframework.damage.AdvancedDamageHandler;
import io.github.math0898.rpgframework.forge.ForgeManager;
import io.github.math0898.rpgframework.items.GiveCommand;
import io.github.math0898.rpgframework.items.ItemManager;
import io.github.math0898.rpgframework.parties.PartyCommand;
import io.github.math0898.rpgframework.parties.PartyManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;
import java.util.logging.Level;

/**
 * The main class for the RPG Framework. A few methods and what not will be declared here for calling from other plugins
 * that are a part of the RPG suite.
 *
 * @author Sugaku
 */
public final class main extends JavaPlugin implements Listener {

    /**
     * A pointer to the plugin instance.
     */
    public static JavaPlugin plugin = null;

    /**
     * Is holographic displays enabled on the server?
     */
    public static boolean useHolographicDisplays = false;

    /**
     * The ItemManager being used with this RPGFramework instance.
     */
    public static ItemManager itemManager;

    /**
     * This method sends a message to the console and infers the level it should be sent at.
     *
     * @param message The message to send to the console.
     * @param color The main color of the message being sent.
     */
    public static void console (String message, ChatColor color) {
        switch (color) {
            case RED -> console(message, color, Level.SEVERE);
            case YELLOW -> console(message, color, Level.WARNING);
            default -> console(message, color, Level.INFO);
        }
    }

    /**
     * This method sends a message to the console.
     *
     * @param message The message to send to the console.
     * @param color The main color of the message being sent.
     * @param lvl The level that the message should be sent at.
     */
    public static void console (String message, ChatColor color, Level lvl) {
        plugin.getLogger().log(lvl, color + message);
    }

    /**
     * Called on enable. Just the normal things such as loading the config, registering listeners, initializing methods.
     */
    @Override
    public void onEnable () {
        long startTime = System.currentTimeMillis();
        plugin = this;

        //Register damage listeners
        Bukkit.getPluginManager().registerEvents(new AdvancedDamageHandler(), this);
        PartyManager.init();
        PlayerManager.init();
        ForgeManager.getInstance();
        Objects.requireNonNull(Bukkit.getPluginCommand("party")).setExecutor(new PartyCommand());
        Objects.requireNonNull(Bukkit.getPluginCommand("party")).setTabCompleter(PartyCommand.autocomplete);

        //Establish hooks
        useHolographicDisplays = Bukkit.getPluginManager().isPluginEnabled("HolographicDisplays");
        if (!useHolographicDisplays) {
            console("Holographic displays was not found.", ChatColor.YELLOW);
            console("This is non fatal error however you will not see damage numbers when you hit mobs.", ChatColor.YELLOW);
        }

        itemManager = new ItemManager();
        Objects.requireNonNull(Bukkit.getPluginCommand("rpg-give")).setExecutor(new GiveCommand());
        Objects.requireNonNull(Bukkit.getPluginCommand("rpg-give")).setTabCompleter(GiveCommand.autocomplete);

        sugaku.rpg.main.onEnable();

        console("Plugin enabled! " + ChatColor.DARK_GRAY + "Took: " + (System.currentTimeMillis() - startTime) + "ms", ChatColor.GREEN);
    }

    /**
     * Not much here either. If something needs to be here it will be.
     */
    @Override
    public void onDisable () {
        // Plugin shutdown logic
    }
}
