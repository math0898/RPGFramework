package io.github.math0898.rpgframework.systems;

import io.github.math0898.utils.gui.PageableGUI;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * The ArtifactMenu displays all artifacts to players. In the case where a player has obtained a specific artifact we
 * change the appearance.
 *
 * @author Sugaku
 */
public class ArtifactMenu extends PageableGUI { // todo: PageableGUI is currently implemented to have 1 list of items per instance.
                                                //       For this case we need it to be per player.
    /**
     * Creates a new PageableGUI with the given name.
     */
    public ArtifactMenu () {
        super("Artifacts");
        setItems(); // todo: Implement here.
    }

    /**
     * Opens this GUI to the given player.
     *
     * @param player The player to open the GUI to.
     */
    @Override
    public void openInventory (Player player) {
        super.openInventory(player);
    }

    /**
     * Called whenever this GUI is clicked.
     *
     * @param event The inventory click event.
     */
    @Override
    public void onClick (InventoryClickEvent event) {
        super.onClick(event);
    }
}
