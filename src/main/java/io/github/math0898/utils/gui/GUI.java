package io.github.math0898.utils.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

/**
 * A GUI is an inventory that has been opened to a player. It should also consider what happens when a player clicks.
 *
 * @author Sugaku
 */
public interface GUI {

    /**
     * Opens this GUI to the given player.
     *
     * @param player The player to open the GUI to.
     */
    void openInventory (Player player);

    /**
     * Called whenever this GUI is clicked.
     *
     * @param event The inventory click event.
     */
    void onClick (InventoryClickEvent event);

    /**
     * Called whenever this GUI is closed.
     *
     * @param event The inventory close event.
     */
    void onClose (InventoryCloseEvent event);

    /**
     * Gets the title of this GUI. Used by the GUIManager to route InventoryClickEvents.
     *
     * @return The title of this GUI.
     */
    String getTitle ();
}