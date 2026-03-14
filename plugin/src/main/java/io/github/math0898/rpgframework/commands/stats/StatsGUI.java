package io.github.math0898.rpgframework.commands.stats;

import io.github.math0898.rpgframework.PlayerManager;
import io.github.math0898.rpgframework.RpgPlayer;
import io.github.math0898.utils.gui.AbstractGUI;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

/**
 * The StatsGUI shows information about a player.
 *
 * @author Sugaku
 */
public class StatsGUI extends AbstractGUI {

    /**
     * Opens this GUI to the given player.
     *
     * @param player The player to open the GUI to.
     */
    @Override
    public void openInventory (Player player) {
        openInventory(player, player.getName());
    }

    /**
     * Opens this GUI to the given player.
     *
     * @param player The player to open the GUI to.
     * @param params Any parameters to add to the inventory when to open.
     */
    @Override
    public void openInventory (Player player, String... params) {
        if (params.length < 1) return; // No player provided.
        openInventory(player, PlayerManager.getPlayer(params[0]));
    }

    /**
     * Opens this GUI to the given player.
     *
     * @param player The player to open the GUI to.
     * @param rpgPlayer The RpgPlayer to open stats on and about.
     */
    public void openInventory (Player player, RpgPlayer rpgPlayer) {
        if (rpgPlayer == null) return;
        // todo: implement.
    }

    /**
     * Called whenever this GUI is clicked.
     *
     * @param event The inventory click event.
     */
    @Override
    public void onClick (InventoryClickEvent event) {
        event.setCancelled(true);
    }

    /**
     * Called whenever this GUI is closed.
     *
     * @param event The inventory close event.
     */
    @Override
    public void onClose (InventoryCloseEvent event) {

    }

    /**
     * Gets the title of this GUI. Used by the GUIManager to route InventoryClickEvents.
     *
     * @return The title of this GUI.
     */
    @Override
    public String getTitle () {
        return "Player Stats";
    }
}
