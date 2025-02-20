package io.github.math0898.rpgframework.systems;

import io.github.math0898.rpgframework.items.ItemManager;
import io.github.math0898.utils.gui.PersonalizedPageableGUI;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/**
 * The ArtifactMenu displays all artifacts to players. In the case where a player has obtained a specific artifact we
 * change the appearance.
 *
 * @author Sugaku
 */
public class ArtifactMenu extends PersonalizedPageableGUI {

    /**
     * Creates a new PageableGUI with the given name.
     */
    public ArtifactMenu () {
        super("Artifacts");
    }

    /**
     * Opens this GUI to the given player.
     *
     * @param player The player to open the GUI to.
     */
    @Override
    public void openInventory (Player player) { // todo: Query ItemManager and RPGPlayer.
        ItemManager itemManager = ItemManager.getInstance();
        ItemStack[] items = new ItemStack[]{ itemManager.getItem("gods:InosAspectI") };
        super.openInventory(player, items);
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
