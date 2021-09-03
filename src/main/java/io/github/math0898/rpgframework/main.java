package io.github.math0898.rpgframework;

import io.github.math0898.rpgframework.damage.AdvancedDamageHandler;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * The main class for the RPG Framework. A few methods and what not will be declared here for calling from other plugins
 * that are a part of the RPG suite.
 *
 * @author Sugaku
 */
public final class main extends JavaPlugin implements Listener {

    /**
     * Called on enable. Just the normal things such as loading the config, registering listeners, initializing methods.
     */
    @Override
    public void onEnable() {
        //Register damage listeners
        Bukkit.getPluginManager().registerEvents(new AdvancedDamageHandler(), this);
    }

    /**
     * Not much here either. If something needs to be here it will be.
     */
    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
