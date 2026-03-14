package io.github.math0898.utils.gui;

import org.bukkit.entity.Player;

/**
 * Provides a baseline GUI with some dummy implementations to make extensions easier.
 *
 * @author Sugaku
 */
public abstract class AbstractGUI implements GUI {

    /**
     * Opens this GUI to the given player. {@link AbstractGUI} provides a dummy implementation that calls
     * {@link #openInventory(Player)}.
     *
     * @param player The player to open the GUI to.
     * @param params Any parameters to pass onto the GUI when opening.
     */
    @Override
    public void openInventory (Player player, String... params) {
        openInventory(player);
    }
}
