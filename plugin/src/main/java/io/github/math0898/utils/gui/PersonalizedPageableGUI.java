package io.github.math0898.utils.gui;

import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * The PersonalizedPageableGUI is very similar in many ways to the PageableGUI. The major exception being that the
 * PersonalizedPageableGUI is for UI and menus that are unique on a per player.
 *
 * @author Sugaku
 */
public abstract class PersonalizedPageableGUI extends PageableGUI {

    /**
     * A map to serve as a lookup table for players
     */
    protected final Map<UUID, ItemStack[]> itemDatabase = new HashMap<>();

    /**
     * Creates a new PageableGUI with the given name.
     *
     * @param title The title for this PageableGUI.
     */
    public PersonalizedPageableGUI (String title) {
        super(title);
    }

    /**
     * Assigns the given items to the given Player.
     *
     * @param player The player these items are specific to.
     * @param items  The items to be contained within the Player's GUI.
     */
    public void assignItems (Player player, ItemStack... items) {
        UUID uuid = player.getUniqueId();
        if (items == null) items = new ItemStack[]{};
        itemDatabase.remove(uuid);
        itemDatabase.put(uuid, items);
    }

    /**
     * Opens this GUI to the given player.
     *
     * @param player The player to open the GUI to.
     */
    @Override
    public void openInventory (Player player) {
        openInventory(player, new ItemStack[]{});
    }

    /**
     * Opens this GUI to the given player.
     *
     * @param player The player to open the GUI to.
     * @param items  The items to assign to the player's GUI. This will override any existing assignments.
     */
    public void openInventory (Player player, ItemStack... items) {
        assignItems(player, items);
        super.openInventory(player); // TODO: The implementation provided by PageableGUI should be sufficient. => Testing Required.
    }
// // TODO: The implementation provided by PageableGUI should be sufficient. => Testing Required.
//    /**
//     * Called whenever this GUI is clicked.
//     *
//     * @param event The inventory click event.
//     */
//    @Override
//    public void onClick (InventoryClickEvent event) {
//        super.onClick(event);
//    }

    /**
     * Called whenever this GUI is closed.
     *
     * @param event The inventory close event.
     */
    @Override
    public void onClose (InventoryCloseEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        itemDatabase.remove(uuid);
    }

    /**
     * Finds the page that is open in the given inventory and returns it as an int.
     *
     * @param inv The opened inventory to determine the page number of.
     * @return The page number of the opened inventory.
     */
    @Override
    public int findPage (Inventory inv) {
        List<HumanEntity> viewers = inv.getViewers();
        if (viewers.isEmpty()) return 0;
        UUID uuid = viewers.get(0).getUniqueId();
        if (!itemDatabase.containsKey(uuid)) return 0;
        ItemStack[] items = itemDatabase.get(uuid);

        ItemStack item = inv.getItem(0);
        if (item == null) return 0;
        for (int i = 0; i <= items.length / 27; i++) // This seems to iterate the number of pages there would be with items.length items.
            if (item.isSimilar(items[i * 27]))
                return i;
        return 0;
    }

    /**
     * Loads the given page index. Contains items (n * 27) -> (n * 27) + 26. Does do bounds checking!
     *
     * @param inv The inventory to fill with this page.
     * @param n   The page index to load.
     */
    @Override
    public void loadPage (Inventory inv, int n) {
        List<HumanEntity> viewers = inv.getViewers();
        if (viewers.isEmpty()) return;
        UUID uuid = viewers.get(0).getUniqueId();
        if (!itemDatabase.containsKey(uuid)) return;
        ItemStack[] items = itemDatabase.get(uuid);

        if (items == null) return;
        if (n < 0) n = 0;
        else if (n > items.length / 27) n = items.length / 27;
        for (int i = 0; i < 27; i++) {
            if (((n * 27) + i) >= items.length) {
                for (int j = i; j < 27; j++)
                    inv.setItem(j, new ItemStack(Material.AIR, 1));
                break;
            }
            inv.setItem(i, items[(n * 27) + i]);
        }
    }
}
