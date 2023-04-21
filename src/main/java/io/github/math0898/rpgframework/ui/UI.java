package io.github.math0898.rpgframework.ui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

/**
 * A UI defines and handles events relating to a specific UI.
 *
 * @author Sugaku
 */
public class UI {

    /**
     * Opens this Inventory to the player.
     *
     * @param player The player to open a view to.
     */
    public void open (Player player) {
        Inventory inv = Bukkit.createInventory(player, 1, "title"); // TODO
        open(player.openInventory(inv));
    }

    /**
     * Opens this Inventory in the given InventoryView by replacing elements.
     *
     * @param view The inventory view to override.
     */
    public void open (InventoryView view) {
        view.close();

        // todo implement
    }
}
