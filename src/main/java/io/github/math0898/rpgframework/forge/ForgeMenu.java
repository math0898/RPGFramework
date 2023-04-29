package io.github.math0898.rpgframework.forge;

import io.github.math0898.rpgframework.items.ItemUtility;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import static org.bukkit.Material.*;

/**
 * The ForgeMenu is a general definition of menus used throughout the Forge system.
 *
 * @author Sugaku
 */
public abstract class ForgeMenu {

    /**
     * A constant value which prefixes any menu involving the forge.
     */
    public static final String FORGE_MENUS_PREFIX = ChatColor.DARK_GREEN.toString() + ChatColor.BOLD + "Forge";

    /**
     * The title of this specific ForgeMenu.
     */
    private final String menuTitle;

    /**
     * Creates a ForgeMenu using the given subtitle.
     *
     * @param subtitle The title to append to the default title.
     */
    public ForgeMenu (String subtitle) {
        menuTitle = FORGE_MENUS_PREFIX + ChatColor.DARK_GRAY + ": " + subtitle;
    }

    /**
     * Accessor method for the title of this menu.
     *
     * @return The title of this menu.
     */
    public String getMenuTitle () {
        return menuTitle;
    }

    /**
     * Builds the items that are placed into the ForgeMenu.
     *
     * @param inv The inventory to place the built items into.
     */
    protected void buildForgeMenu (Inventory inv) {
        ItemStack fill = ItemUtility.createItem(BLACK_STAINED_GLASS_PANE, 1, " ", false);
        for (int i = 0; i < 54; i++) inv.setItem(i, fill);
    }

    /**
     * Opens this menu to the given player.
     *
     * @param p The player whom should have this menu opened to.
     */
    public void openMenu (Player p) {
        Inventory i = Bukkit.getServer().createInventory(p.getPlayer(), 54, menuTitle);
        buildForgeMenu(i);
        p.openInventory(i);
    }

    /**
     * Called when a player closes this menu.
     *
     * @param event The relevant inventory close event.
     */
    public abstract void onClose (InventoryCloseEvent event);

    /**
     * Called whenever a player clicks on an inventory slot with this menu open.
     *
     * @param event The relevant inventory click event.
     */
    public abstract void onClick (InventoryClickEvent event);
}
