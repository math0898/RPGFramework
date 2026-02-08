package io.github.math0898.rpgframework.systems;

import io.github.math0898.rpgframework.PlayerManager;
import io.github.math0898.rpgframework.RpgPlayer;
import io.github.math0898.rpgframework.items.ItemManager;
import io.github.math0898.utils.gui.PersonalizedPageableGUI;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

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
    public void openInventory (Player player) {
        ItemManager itemManager = ItemManager.getInstance();
        RpgPlayer rpgPlayer = PlayerManager.getPlayer(player.getUniqueId());
        if (rpgPlayer == null) return;
        List<String> collection = rpgPlayer.getArtifactCollection();
        if (collection == null) collection = List.of();
        ItemStack[] items = new ItemStack[collection.size()];
        for (int i = 0; i < items.length; i++)
            items[i] = itemManager.getItem(collection.get(i));
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
