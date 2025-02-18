package io.github.math0898.utils.gui;

import io.github.math0898.utils.Utils;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

import java.util.HashMap;
import java.util.logging.Level;

/**
 * The GUIManager is used to register GUIs, open them by string name, and to forward click events from one event
 * listener.
 *
 * @author Sugaku
 */
public class GUIManager implements Listener {

    /**
     * A map of guis by their String ids. This is used to make interacting with a specific inventory O(log n).
     */
    private final HashMap<String, GUI> guisByID = new HashMap<>();

    /**
     * A map of guis by their title. This is used to improve the performance of {@link #onInventoryClick(InventoryClickEvent)}
     * significantly.
     */
    private final HashMap<String, GUI> guisByTitle = new HashMap<>();

    /**
     * The GUI Manager follows the singleton design pattern.
     * -- GETTER --
     * Static accessor for the active GUIManager instance.
     */
    @Getter
    private static final GUIManager instance = new GUIManager();

    /**
     * Creates a GUIManager.
     */
    private GUIManager () {
        Bukkit.getPluginManager().registerEvents(this, Utils.getPlugin());
    }

    /**
     * Adds a GUI to the GUI manager.
     *
     * @param id  This is the string id the GUI will go by.
     * @param gui The GUI to add.
     */
    public void addGUI (String id, GUI gui) {
        guisByID.put(id, gui);
        guisByTitle.put(gui.getTitle(), gui);
    }

    /**
     * Removes a GUI from the GUI manager.
     *
     * @param id  This is the string id of the GUI to remove.
     */
    public void removeGUI (String id) {
        GUI gui = guisByID.remove(id);
        guisByTitle.remove(gui.getTitle());
    }

    /**
     * Opens the GUI by the given id to the given player.
     *
     * @param id     The id of the GUI to open.
     * @param player The player to open the GUI to.
     */
    public void openGUI (String id, Player player) {
        GUI gui = guisByID.get(id);
        if (gui == null) Utils.console("Tried to open: " + id + " but GUI not found!", Level.WARNING);
        else gui.openInventory(player);
    }

    /**
     * Called whenever an inventory is clicked.
     *
     * @param event The inventory click event to consider.
     */
    @EventHandler
    public void onInventoryClick (InventoryClickEvent event) {
        Inventory clicked = event.getClickedInventory();
        if (clicked == null) return;
        InventoryView view = event.getView();
        if (clicked.equals(view.getTopInventory())) {
            String title = view.getTitle();
            GUI gui = guisByTitle.get(title);
            if (gui == null) return;
            event.setCancelled(true);
            gui.onClick(event);
        }
    }

    /**
     * Called whenever an inventory is closed.
     *
     * @param event The inventory close event.
     */
    @EventHandler
    public void onInventoryClose (InventoryCloseEvent event) {
        Inventory clicked = event.getInventory();
        InventoryView view = event.getView();
        if (clicked.equals(view.getTopInventory())) {
            String title = view.getTitle();
            GUI gui = guisByTitle.get(title);
            if (gui == null) return;
            gui.onClose(event);
        }
    }
}