package io.github.math0898.rpgframework.forge;

import io.github.math0898.rpgframework.main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.util.HashMap;
import java.util.Map;

/**
 * The Forge Manager wrangles and makes all ForgeMenus accessible to the rest of the plugin.
 *
 * @author Sugaku
 */
public class ForgeManager {

    /**
     * A map of the registered ForgeMenus.
     */
    private final Map<String, ForgeMenu> menus = new HashMap<>();

    /**
     * The static reference to the single instance of the ForgeManager at runtime.
     */
    private static ForgeManager forgeManager = null;

    /**
     * Creates a new forge manager by registering the event listener and menus.
     */
    private ForgeManager () {
        Bukkit.getPluginManager().registerEvents(new ForgeEventListener(), main.plugin);
        for (ForgeMenu m : new ForgeMenu[]{ new HoningMenu() }) menus.put(m.getMenuTitle(), m);
    }

    /**
     * Accessor method for the single instance of the ForgeManager.
     *
     * @return The ForgeManager instance in use.
     */
    public static ForgeManager getInstance () {
        if (forgeManager == null) forgeManager = new ForgeManager();
        return forgeManager;
    }

    /**
     * Called whenever a ForgeMenu is requested to be opened. We then pass this onto the appropriate menu.
     *
     * @param player The player that should have the forge opened to.
     */
    public void openMenu (Player player) {
        // todo: Create and implement a forge main menu.
        ForgeMenu m = menus.get(ForgeMenu.FORGE_MENUS_PREFIX + ChatColor.DARK_GRAY + ": Honing");
        if (m != null) m.openMenu(player);
    }

    /**
     * Called whenever a ForgeMenu is clicked. This is then passed onto the appropriate menu.
     *
     * @param event The inventory click event.
     */
    public void onClick (InventoryClickEvent event) {
        ForgeMenu m = menus.get(event.getView().getTitle());
        if (m != null) m.onClick(event);
    }

    /**
     * Called whenever a ForgeMenu is closed. We then pass this onto the menu which needs this information.
     *
     * @param event The inventory close event.
     */
    public void onClose (InventoryCloseEvent event) {
        ForgeMenu m = menus.get(event.getView().getTitle());
        if (m != null) m.onClose(event);
    }
}
