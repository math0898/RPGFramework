package io.github.math0898.utils.gui;

import io.github.math0898.utils.items.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * A pageable GUI is a GUI used to display a long list of items.
 *
 * @author Sugaku
 */
public abstract class PageableGUI implements GUI {

    /**
     * An array of items that are in the pages of this PageableGUI.
     */
    private ItemStack[] items;

    /**
     * The title of this GUI.
     */
    private final String title;

    /**
     * Creates a new PageableGUI with the given name.
     *
     * @param title The title for this PageableGUI.
     */
    public PageableGUI (String title) {
        this.title = title;
    }

    /**
     * Sets the contents of this PageableGUI.
     *
     * @param items The items that should be contained within this PageableGUI.
     */
    protected void setItems (ItemStack... items) {
        this.items = items;
    }

    /**
     * Opens this GUI to the given player.
     *
     * @param player The player to open the GUI to.
     */
    @Override
    public void openInventory (Player player) {
        Inventory inv = Bukkit.createInventory(player, 45, getTitle());
        inv.setItem(36, new ItemBuilder(Material.ARROW).setDisplayName(ChatColor.GRAY + "Previous Page").build());
        inv.setItem(44, new ItemBuilder(Material.ARROW).setDisplayName(ChatColor.GRAY + "Next Page").build());
        player.openInventory(inv);
        loadPage(inv, 0);
    }

    /**
     * Called whenever this GUI is clicked.
     *
     * @param event The inventory click event.
     */
    @Override
    public void onClick (InventoryClickEvent event) {
        Inventory inv = event.getClickedInventory();
        assert inv != null; // See GUIManager#onInventoryClick(event):
        if (event.getSlot() == 44) loadPage(inv, findPage(inv) + 1);
        else if (event.getSlot() == 36) loadPage(inv, findPage(inv) - 1);
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
     * Loads the given page index. Contains items (n * 27) -> (n * 27) + 26. Does do bounds checking!
     *
     * @param inv The inventory to fill with this page.
     * @param n   The page index to load.
     */
    public void loadPage (Inventory inv, int n) {
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

    /**
     * Finds the page that is open in the given inventory and returns it as an int.
     *
     * @param inv The opened inventory to determine the page number of.
     * @return The page number of the opened inventory.
     */
    public int findPage (Inventory inv) {
        ItemStack item = inv.getItem(0);
        if (item == null) return 0;
        for (int i = 0; i <= items.length / 27; i++) // This seems to iterate the number of pages there would be with items.length items.
            if (item.isSimilar(items[i * 27]))
                return i;
        return 0;
    }

    /**
     * Gets the title of this GUI. Used by the GUIManager to route InventoryClickEvents.
     *
     * @return The title of this GUI.
     */
    @Override
    public String getTitle () {
        return title;
    }
}